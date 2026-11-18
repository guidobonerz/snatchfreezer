//! Serial connection to the controller. It lives in its own thread, so the
//! UI never waits for the port.

use std::io::{self, Read, Write};
use std::sync::mpsc::{self, Receiver, RecvTimeoutError, Sender, TryRecvError};
use std::thread::{self, JoinHandle};
use std::time::{Duration, Instant};

use crate::protocol::*;

/// UI -> serial thread.
pub enum Command {
    Connect(String),
    Disconnect,
    Shot(Program),
    Test(u8),
    Flush(u8, bool),
    Cancel,
}

/// Serial thread -> UI.
pub enum Event {
    /// The available serial ports changed.
    Ports(Vec<String>),
    Connected { port: String },
    /// The connection is closed; the text is empty if that was requested.
    Disconnected(String),
    Log(String),
    /// Upload progress of the program, 0..=1.
    Transfer(f32),
    Cycle { current: u16, total: u16 },
    Finished { canceled: bool },
    /// A command could not be executed.
    Failed(String),
}

const READ_TIMEOUT: Duration = Duration::from_millis(20);
const ACK_TIMEOUT: Duration = Duration::from_millis(300);
const ATTEMPTS: usize = 4;
/// Opening the port may reset the board; the boot loader needs a moment.
const HELLO_TIMEOUT: Duration = Duration::from_secs(5);
const PORT_SCAN: Duration = Duration::from_secs(1);

#[derive(Debug)]
pub enum LinkError {
    Timeout,
    /// The controller rejected the request with this error code.
    Nak(u8),
    Io(io::Error),
}

impl std::fmt::Display for LinkError {
    fn fmt(&self, f: &mut std::fmt::Formatter) -> std::fmt::Result {
        match self {
            LinkError::Timeout => write!(f, "no answer from the controller"),
            LinkError::Nak(code) => write!(f, "{}", error_text(*code)),
            LinkError::Io(e) => write!(f, "{e}"),
        }
    }
}

/// Request / reply layer on top of a byte stream.
pub struct Link<P> {
    port: P,
    parser: Parser,
    seq: u8,
    ack_timeout: Duration,
    /// Frames that were received while waiting for a reply.
    events: Vec<Frame>,
}

impl<P: Read + Write> Link<P> {
    pub fn new(port: P) -> Link<P> {
        Link { port, parser: Parser::default(), seq: 0, ack_timeout: ACK_TIMEOUT, events: Vec::new() }
    }

    /// Reads what is available (waiting at most the read timeout of the port).
    fn read(&mut self) -> io::Result<Vec<Frame>> {
        let mut buf = [0u8; 256];
        let n = match self.port.read(&mut buf) {
            Ok(n) => n,
            Err(e) if matches!(e.kind(), io::ErrorKind::TimedOut | io::ErrorKind::WouldBlock | io::ErrorKind::Interrupted) => 0,
            Err(e) => return Err(e),
        };
        Ok(buf[..n].iter().filter_map(|b| self.parser.push(*b)).collect())
    }

    /// Unsolicited frames (events) received since the last call.
    pub fn poll(&mut self) -> io::Result<Vec<Frame>> {
        let mut frames = std::mem::take(&mut self.events);
        frames.extend(self.read()?);
        Ok(frames)
    }

    /// Sends a request and waits for its reply. Without a reply the request
    /// is repeated with the same sequence number, which the controller
    /// answers again without executing it twice.
    pub fn request(&mut self, cmd: u8, payload: &[u8]) -> Result<Frame, LinkError> {
        self.request_n(cmd, payload, ATTEMPTS)
    }

    fn request_n(&mut self, cmd: u8, payload: &[u8], attempts: usize) -> Result<Frame, LinkError> {
        self.seq = if self.seq == 255 { 1 } else { self.seq + 1 };
        let bytes = encode(cmd, self.seq, payload);
        for _ in 0..attempts {
            self.port.write_all(&bytes).and_then(|_| self.port.flush()).map_err(LinkError::Io)?;
            let deadline = Instant::now() + self.ack_timeout;
            'wait: while Instant::now() < deadline {
                for f in self.read().map_err(LinkError::Io)? {
                    match f.cmd {
                        // the request arrived damaged: repeat it right away
                        CMD_NAK if f.payload.first() == Some(&ERR_CRC) => break 'wait,
                        CMD_NAK if f.seq == self.seq => return Err(LinkError::Nak(f.payload.first().copied().unwrap_or(0))),
                        CMD_ACK | CMD_HELLO_REPLY if f.seq == self.seq => return Ok(f),
                        // late reply to an earlier request
                        CMD_ACK | CMD_NAK => {}
                        _ => self.events.push(f),
                    }
                }
            }
        }
        Err(LinkError::Timeout)
    }
}

struct Connection {
    link: Link<Box<dyn serialport::SerialPort>>,
    /// A program is being executed.
    running: bool,
}

/// Starts the serial thread. It ends, switching all outputs off, when the
/// command sender is dropped.
pub fn spawn() -> (Sender<Command>, Receiver<Event>, JoinHandle<()>) {
    let (cmd_tx, cmd_rx) = mpsc::channel();
    let (ev_tx, ev_rx) = mpsc::channel();
    let handle = thread::spawn(move || run(cmd_rx, ev_tx));
    (cmd_tx, ev_rx, handle)
}

fn port_names() -> Vec<String> {
    let mut names: Vec<String> = serialport::available_ports().unwrap_or_default().into_iter().map(|p| p.port_name).collect();
    names.sort();
    names
}

fn connect(port: &str, tx: &Sender<Event>) -> Result<(Connection, String), String> {
    let _ = tx.send(Event::Log(format!("Connecting to {port} ...")));
    let serial = serialport::new(port, BAUD_RATE).timeout(READ_TIMEOUT).open().map_err(|e| format!("{port}: {e}"))?;
    let mut link = Link::new(serial);
    let deadline = Instant::now() + HELLO_TIMEOUT;
    let hello = loop {
        match link.request_n(CMD_HELLO, &[], 1) {
            Ok(f) => break f,
            Err(LinkError::Timeout) if Instant::now() < deadline => {}
            Err(LinkError::Timeout) => return Err(format!("{port}: no answer. Is the Snatchfreezer firmware 2.x installed?")),
            Err(e) => return Err(format!("{port}: {e}")),
        }
    };
    // the greeting sent by the controller after a reset is not needed
    link.events.clear();
    let &[protocol, major, minor] = &hello.payload[..] else {
        return Err(format!("{port}: unexpected answer"));
    };
    if protocol != PROTOCOL_VERSION {
        return Err(format!("{port}: firmware {major}.{minor} uses protocol {protocol}, this frontend needs {PROTOCOL_VERSION}"));
    }
    Ok((Connection { link, running: false }, format!("{major}.{minor}")))
}

/// Uploads and starts a program.
fn shot(c: &mut Connection, program: &Program, tx: &Sender<Event>) -> Result<(), LinkError> {
    let requests = program.requests();
    let _ = tx.send(Event::Log(format!(
        "Sending {} action(s), {} cycle(s), cycle delay {} ms",
        program.actions.len(),
        program.cycles,
        program.cycle_delay_ms
    )));
    for (i, (cmd, payload)) in requests.iter().enumerate() {
        c.link.request(*cmd, payload)?;
        let _ = tx.send(Event::Transfer((i + 1) as f32 / requests.len() as f32));
    }
    Ok(())
}

fn run(rx: Receiver<Command>, tx: Sender<Event>) {
    let send = |e: Event| {
        let _ = tx.send(e);
    };
    let log = |s: String| send(Event::Log(s));
    let mut conn: Option<Connection> = None;
    let mut ports: Option<Vec<String>> = None;
    let mut last_scan = Instant::now();

    loop {
        if ports.is_none() || last_scan.elapsed() >= PORT_SCAN {
            last_scan = Instant::now();
            let now = port_names();
            if ports.as_ref() != Some(&now) {
                send(Event::Ports(now.clone()));
                ports = Some(now);
            }
        }

        // While connected, reading the port below is what paces the loop.
        let cmd = if conn.is_some() {
            match rx.try_recv() {
                Ok(c) => Some(c),
                Err(TryRecvError::Empty) => None,
                Err(TryRecvError::Disconnected) => break,
            }
        } else {
            match rx.recv_timeout(Duration::from_millis(200)) {
                Ok(c) => Some(c),
                Err(RecvTimeoutError::Timeout) => None,
                Err(RecvTimeoutError::Disconnected) => break,
            }
        };

        // (text for the log, result of the request)
        let mut done: Option<(String, Result<Frame, LinkError>)> = None;
        match (cmd, &mut conn) {
            (None, _) => {}
            (Some(Command::Connect(port)), _) => {
                // switch everything off before the previous port is closed
                if let Some(mut old) = conn.take() {
                    let _ = old.link.request(CMD_CANCEL, &[]);
                }
                match connect(&port, &tx) {
                    Ok((c, firmware)) => {
                        log(format!("Connected to {port}, firmware {firmware}"));
                        send(Event::Connected { port });
                        conn = Some(c);
                    }
                    Err(e) => {
                        log(e.clone());
                        send(Event::Disconnected(e));
                    }
                }
            }
            (Some(Command::Disconnect), c) => {
                if let Some(c) = c {
                    let _ = c.link.request(CMD_CANCEL, &[]);
                    log("Disconnected".into());
                }
                conn = None;
                send(Event::Disconnected(String::new()));
            }
            (Some(_), None) => send(Event::Failed("Serial communication not possible".into())),
            (Some(Command::Shot(program)), Some(c)) => match shot(c, &program, &tx) {
                Ok(()) => {
                    c.running = true;
                    log("Started".into());
                }
                Err(e) => {
                    // whatever arrived of the program must not stay armed
                    let _ = c.link.request(CMD_CANCEL, &[]);
                    done = Some(("Transfer".into(), Err(e)));
                }
            },
            (Some(Command::Test(pin)), Some(c)) => done = Some((format!("Test pin {pin}"), c.link.request(CMD_TEST, &[pin]))),
            (Some(Command::Flush(pin, on)), Some(c)) => {
                let text = format!("Flush pin {pin} {}", if on { "on" } else { "off" });
                done = Some((text, c.link.request(CMD_FLUSH, &[pin, on as u8])));
            }
            (Some(Command::Cancel), Some(c)) => done = Some(("Cancel".into(), c.link.request(CMD_CANCEL, &[]))),
        }
        match done {
            Some((text, Ok(_))) => log(text),
            Some((text, Err(LinkError::Io(e)))) => {
                conn = None;
                log(format!("{text}: {e}"));
                send(Event::Disconnected(e.to_string()));
            }
            Some((text, Err(e))) => {
                log(format!("{text} failed: {e}"));
                send(Event::Failed(format!("{text} failed: {e}")));
            }
            None => {}
        }

        let Some(c) = &mut conn else { continue };
        match c.link.poll() {
            Ok(frames) => {
                for f in frames {
                    match (f.cmd, &f.payload[..]) {
                        (CMD_CYCLE, &[c0, c1, t0, t1]) => {
                            let (current, total) = (u16::from_le_bytes([c0, c1]), u16::from_le_bytes([t0, t1]));
                            log(format!("Cycle {current} of {total}"));
                            send(Event::Cycle { current, total });
                        }
                        (CMD_FINISHED, &[status]) => {
                            c.running = false;
                            let canceled = status == FINISHED_CANCELED;
                            log(if canceled { "Canceled" } else { "Finished" }.into());
                            send(Event::Finished { canceled });
                        }
                        // sent by the controller when it starts
                        (CMD_HELLO_REPLY, _) => {
                            log("The controller was restarted".into());
                            if std::mem::take(&mut c.running) {
                                send(Event::Failed("The controller was restarted while running".into()));
                            }
                        }
                        _ => {}
                    }
                }
            }
            Err(e) => {
                conn = None;
                log(format!("Connection lost: {e}"));
                send(Event::Disconnected(format!("Connection lost: {e}")));
            }
        }
    }

    // The application is closing: do not leave a valve open.
    if let Some(c) = &mut conn {
        let _ = c.link.request_n(CMD_CANCEL, &[], 2);
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use std::collections::VecDeque;

    /// A controller that acknowledges every valid frame, like the firmware.
    #[derive(Default)]
    struct Fake {
        parser: Parser,
        rx: VecDeque<u8>,
        /// Requests that were executed (repeated ones are not).
        executed: Vec<Frame>,
        last: Option<(u8, u8)>,
        /// Number of frames to swallow without an answer.
        drop_requests: usize,
        /// Number of answers to lose.
        drop_replies: usize,
        nak: Option<u8>,
    }

    impl Write for Fake {
        fn write(&mut self, buf: &[u8]) -> io::Result<usize> {
            for b in buf {
                let Some(f) = self.parser.push(*b) else { continue };
                if self.drop_requests > 0 {
                    self.drop_requests -= 1;
                    continue;
                }
                if self.last != Some((f.seq, f.cmd)) {
                    self.last = Some((f.seq, f.cmd));
                    self.executed.push(f.clone());
                }
                if self.drop_replies > 0 {
                    self.drop_replies -= 1;
                    continue;
                }
                let reply = match self.nak {
                    Some(code) => encode(CMD_NAK, f.seq, &[code]),
                    None => encode(CMD_ACK, f.seq, &[]),
                };
                // an event in between must not disturb the request
                self.rx.extend(encode(CMD_CYCLE, 0, &[1, 0, 2, 0]));
                self.rx.extend(reply);
            }
            Ok(buf.len())
        }
        fn flush(&mut self) -> io::Result<()> {
            Ok(())
        }
    }

    impl Read for Fake {
        fn read(&mut self, buf: &mut [u8]) -> io::Result<usize> {
            if self.rx.is_empty() {
                return Err(io::ErrorKind::TimedOut.into());
            }
            let n = buf.len().min(self.rx.len());
            for b in &mut buf[..n] {
                *b = self.rx.pop_front().unwrap();
            }
            Ok(n)
        }
    }

    fn link(fake: Fake) -> Link<Fake> {
        let mut link = Link::new(fake);
        link.ack_timeout = Duration::from_millis(5);
        link
    }

    #[test]
    fn request_is_acknowledged_and_events_are_kept() {
        let mut l = link(Fake::default());
        assert_eq!(l.request(CMD_TEST, &[3]).unwrap().cmd, CMD_ACK);
        assert_eq!(l.port.executed, [Frame { cmd: CMD_TEST, seq: 1, payload: vec![3] }]);
        let events = l.poll().unwrap();
        assert_eq!(events.len(), 1);
        assert_eq!(events[0].cmd, CMD_CYCLE);
    }

    #[test]
    fn lost_request_and_lost_reply_are_repeated_once_executed() {
        let mut l = link(Fake { drop_requests: 1, drop_replies: 1, ..Fake::default() });
        l.request(CMD_ACTION, &[2]).unwrap();
        assert_eq!(l.port.executed.len(), 1);
        l.request(CMD_ACTION, &[3]).unwrap();
        assert_eq!(l.port.executed.len(), 2);
    }

    #[test]
    fn nak_and_timeout_are_reported() {
        let mut l = link(Fake { nak: Some(7), ..Fake::default() });
        assert!(matches!(l.request(CMD_RUN, &[]), Err(LinkError::Nak(7))));
        let mut l = link(Fake { drop_requests: 100, ..Fake::default() });
        assert!(matches!(l.request(CMD_RUN, &[]), Err(LinkError::Timeout)));
    }

    #[test]
    fn sequence_number_skips_zero() {
        let mut l = link(Fake::default());
        l.seq = 255;
        assert_eq!(l.request(CMD_RUN, &[]).unwrap().seq, 1);
    }
}
