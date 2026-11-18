//! Serial protocol between the frontend and the controller.
//! Keep in sync with `arduino/Snatchfreezer/Protocol.h`, which documents it.

pub const PROTOCOL_VERSION: u8 = 2;
pub const BAUD_RATE: u32 = 19200;

const SYNC1: u8 = 0xaa;
const SYNC2: u8 = 0x55;
const MAX_PAYLOAD: usize = 32;

pub const MAX_TIMINGS: usize = 5;

// frontend -> controller
pub const CMD_HELLO: u8 = 0x01;
pub const CMD_BEGIN: u8 = 0x10;
pub const CMD_ACTION: u8 = 0x11;
pub const CMD_TIMING: u8 = 0x12;
pub const CMD_RUN: u8 = 0x13;
pub const CMD_CANCEL: u8 = 0x14;
pub const CMD_TEST: u8 = 0x20;
pub const CMD_FLUSH: u8 = 0x21;

// controller -> frontend
pub const CMD_ACK: u8 = 0x80;
pub const CMD_NAK: u8 = 0x81;
pub const CMD_HELLO_REPLY: u8 = 0x82;
pub const CMD_CYCLE: u8 = 0x90;
pub const CMD_FINISHED: u8 = 0x91;

pub const ERR_CRC: u8 = 1;

pub const FINISHED_CANCELED: u8 = 1;

/// Text for the error code of a NAK.
pub fn error_text(code: u8) -> &'static str {
    match code {
        1 => "checksum error",
        2 => "unknown command",
        3 => "wrong parameter length",
        4 => "invalid pin",
        5 => "too many actions",
        6 => "too many timings",
        7 => "controller is busy",
        8 => "no actions defined",
        9 => "invalid value",
        _ => "unknown error",
    }
}

/// CRC-8, polynomial 0x07, initial value 0.
pub fn crc8(data: &[u8]) -> u8 {
    data.iter().fold(0u8, |mut crc, b| {
        crc ^= b;
        for _ in 0..8 {
            crc = if crc & 0x80 != 0 { (crc << 1) ^ 0x07 } else { crc << 1 };
        }
        crc
    })
}

#[derive(Clone, Debug, PartialEq, Eq)]
pub struct Frame {
    pub cmd: u8,
    pub seq: u8,
    pub payload: Vec<u8>,
}

/// `SYNC1 SYNC2 CMD SEQ LEN PAYLOAD CRC`
pub fn encode(cmd: u8, seq: u8, payload: &[u8]) -> Vec<u8> {
    assert!(payload.len() <= MAX_PAYLOAD);
    let mut out = vec![SYNC1, SYNC2, cmd, seq, payload.len() as u8];
    out.extend_from_slice(payload);
    out.push(crc8(&out[2..]));
    out
}

#[derive(Clone, Copy, PartialEq, Eq)]
enum State {
    Sync1,
    Sync2,
    Cmd,
    Seq,
    Len,
    Payload,
    Crc,
}

/// Byte-wise frame parser. Anything that is not a valid frame is skipped.
pub struct Parser {
    state: State,
    /// CMD, SEQ, LEN and the payload received so far.
    body: Vec<u8>,
}

impl Default for Parser {
    fn default() -> Self {
        Parser { state: State::Sync1, body: Vec::new() }
    }
}

impl Parser {
    pub fn push(&mut self, b: u8) -> Option<Frame> {
        match self.state {
            State::Sync1 => {
                if b == SYNC1 {
                    self.state = State::Sync2;
                }
            }
            State::Sync2 => {
                if b == SYNC2 {
                    self.body.clear();
                    self.state = State::Cmd;
                } else if b != SYNC1 {
                    self.state = State::Sync1;
                }
            }
            State::Cmd => {
                self.body.push(b);
                self.state = State::Seq;
            }
            State::Seq => {
                self.body.push(b);
                self.state = State::Len;
            }
            State::Len => {
                self.body.push(b);
                self.state = match b as usize {
                    0 => State::Crc,
                    n if n <= MAX_PAYLOAD => State::Payload,
                    _ => State::Sync1,
                };
            }
            State::Payload => {
                self.body.push(b);
                if self.body.len() == 3 + self.body[2] as usize {
                    self.state = State::Crc;
                }
            }
            State::Crc => {
                self.state = State::Sync1;
                if b == crc8(&self.body) {
                    return Some(Frame { cmd: self.body[0], seq: self.body[1], payload: self.body[3..].to_vec() });
                }
            }
        }
        None
    }
}

/// One table row; times in microseconds.
#[derive(Clone, Copy, Debug, Default, PartialEq, Eq)]
pub struct Timing {
    pub delay: u32,
    pub release: u32,
    pub delay_increment: u32,
    pub release_increment: u32,
}

/// One enabled table: the pulses of one output pin.
#[derive(Clone, Debug, PartialEq, Eq)]
pub struct Action {
    pub pin: u8,
    pub timings: Vec<Timing>,
}

#[derive(Clone, Debug, PartialEq, Eq)]
pub struct Program {
    pub cycles: u16,
    pub cycle_delay_ms: u32,
    pub actions: Vec<Action>,
}

impl Program {
    /// The requests that upload and start the program, as `(cmd, payload)`.
    pub fn requests(&self) -> Vec<(u8, Vec<u8>)> {
        let mut begin = self.cycles.to_le_bytes().to_vec();
        begin.extend(self.cycle_delay_ms.to_le_bytes());
        let mut out = vec![(CMD_BEGIN, begin)];
        for a in &self.actions {
            out.push((CMD_ACTION, vec![a.pin]));
            for t in &a.timings {
                let p = [t.delay, t.release, t.delay_increment, t.release_increment];
                out.push((CMD_TIMING, p.iter().flat_map(|v| v.to_le_bytes()).collect()));
            }
        }
        out.push((CMD_RUN, Vec::new()));
        out
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn crc_matches_reference() {
        // CRC-8/SMBUS check value
        assert_eq!(crc8(b"123456789"), 0xf4);
    }

    #[test]
    fn frame_roundtrip() {
        let bytes = encode(CMD_TIMING, 7, &[1, 2, 3, 0xaa, 0x55]);
        let mut p = Parser::default();
        let frames: Vec<Frame> = bytes.iter().filter_map(|b| p.push(*b)).collect();
        assert_eq!(frames, [Frame { cmd: CMD_TIMING, seq: 7, payload: vec![1, 2, 3, 0xaa, 0x55] }]);
    }

    #[test]
    fn parser_skips_garbage_and_damaged_frames() {
        let mut bytes = vec![0x00, 0xaa, 0x13, 0xaa, 0xaa];
        bytes.extend(encode(CMD_ACK, 1, &[]));
        let mut bad = encode(CMD_NAK, 2, &[4]);
        bad[5] ^= 1;
        bytes.extend(bad);
        // length beyond the limit: not a frame
        bytes.extend([0xaa, 0x55, CMD_ACK, 3, 200]);
        bytes.extend(encode(CMD_CYCLE, 0, &[1, 0, 9, 0]));
        let mut p = Parser::default();
        let frames: Vec<Frame> = bytes.iter().filter_map(|b| p.push(*b)).collect();
        assert_eq!(frames.len(), 2);
        assert_eq!((frames[0].cmd, frames[0].seq), (CMD_ACK, 1));
        assert_eq!(frames[1].payload, [1, 0, 9, 0]);
    }

    #[test]
    fn program_requests() {
        let program = Program {
            cycles: 0x0102,
            cycle_delay_ms: 500,
            actions: vec![Action { pin: 2, timings: vec![Timing { delay: 1_000_000, release: 100_000, delay_increment: 1, release_increment: 2 }] }],
        };
        let r = program.requests();
        assert_eq!(r.iter().map(|x| x.0).collect::<Vec<_>>(), [CMD_BEGIN, CMD_ACTION, CMD_TIMING, CMD_RUN]);
        assert_eq!(r[0].1, [0x02, 0x01, 0xf4, 0x01, 0, 0]);
        assert_eq!(r[1].1, [2]);
        assert_eq!(r[2].1, [0x40, 0x42, 0x0f, 0, 0xa0, 0x86, 0x01, 0, 1, 0, 0, 0, 2, 0, 0, 0]);
    }
}
