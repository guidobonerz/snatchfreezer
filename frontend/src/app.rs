//! Application state and user interface.

use std::ops::Range;
use std::path::PathBuf;
use std::sync::mpsc::{Receiver, Sender};

use drazui::*;

use crate::icons;
use crate::protocol::Timing;
use crate::serial::{Command, Event};
use crate::config::Config;
use crate::settings::*;

pub const WINDOW: (f32, f32) = (1220.0, 768.0);

const VALVE_HEAD: Color = Color::hex(0x375473);
const CAMERA_HEAD: Color = Color::hex(0x387261);
const CONTROL_HEAD: Color = Color::hex(0x5a4a8a);
const TABLE_HEAD: Color = Color::hex(0x353434);
const TABLE_ROW: Color = Color::hex(0x494949);
const TABLE_STRIPE: Color = Color::hex(0x5f5f5f);
const HOVER: Color = Color::hex(0x5f5c5c);
const ORANGE: Color = Color::hex(0xff8040);
const ORANGE_LIGHT: Color = Color::hex(0xffb18c);
const BLUE: Color = Color::hex(0x16afe9);
const BLUE_LIGHT: Color = Color::hex(0x7cd2f3);
const YELLOW: Color = Color::hex(0xd5d500);
const YELLOW_LIGHT: Color = Color::hex(0xffff4f);
const GREEN: Color = Color::hex(0x00b700);
const RED: Color = Color::hex(0xc73f42);
const GREY: Color = Color::hex(0x8a8888);
const DISABLED: Color = Color::hex(0x7d7b7b);

/// Width of the valve pane and spacing inside the panes.
const LEFT_W: f32 = 592.0;
const MARGIN: f32 = 8.0;
const GAP: f32 = 6.0;
const HEAD_H: f32 = 40.0;
const NUMBER_W: f32 = 20.0;
const TOGGLE_W: f32 = 32.0;
const ICON_W: f32 = 26.0;
const COLUMNS: [(&str, f32); 5] = [("#", 28.0), ("Delay", 64.0), ("Cycle+", 60.0), ("Release", 64.0), ("Cycle+", 60.0)];
const LOG_LINES: usize = 300;
/// Edge length of the corner that marks a changed field.
const DIRTY: f32 = 6.0;

pub fn theme(scale: f32) -> Theme {
    let mut t = Theme::palette(Color::hex(0x4f4d4d), Color::hex(0x403f3e), ORANGE, ORANGE);
    t.scale = scale;
    t.font_size = 13.0;
    t.pad = vec2(5.0, 4.0);
    t.gap = 4.0;
    t.select_text = Color::hex(0x000000);
    t
}

#[derive(Clone, Copy, PartialEq, Eq)]
enum Link {
    Offline,
    Connecting,
    Online,
}

#[derive(Clone, Copy, PartialEq, Eq)]
enum Run {
    Idle,
    /// The program is being uploaded.
    Transfer,
    Running,
}

enum FileAction {
    Load,
    Save,
    SaveAs,
}

pub struct App {
    project: Project,
    /// The project as it was loaded or saved last: fields that differ from it
    /// are marked as changed.
    saved: Project,
    file: Option<PathBuf>,
    /// Siphons that are being flushed (valve open).
    flushing: [bool; VALVES],
    flush_all: bool,
    ports: Vec<String>,
    port: Option<String>,
    /// The port of the last connection, kept in the configuration.
    last_port: Option<String>,
    link: Link,
    /// Connect on start to the port of the last run, or to the only one.
    auto_connect: bool,
    run: Run,
    transfer: f32,
    progress: f32,
    log: Vec<String>,
    log_changed: bool,
    tab: usize,
    /// File dialogs are opened between two frames.
    pending: Option<FileAction>,
    /// Toasts to show: text, is an error.
    messages: Vec<(String, bool)>,
    /// Quitting was requested with unsaved changes: ask first.
    ask_quit: bool,
    pub quit: bool,
    tx: Sender<Command>,
    rx: Receiver<Event>,
}

/// Draws text in another size than the theme's.
fn text_sized(ui: &mut Ui, r: Rect, text: &str, size: f32, color: Color, align: Align) {
    let normal = std::mem::replace(&mut ui.theme.font_size, size);
    ui.text_in(r, text, color, align);
    ui.theme.font_size = normal;
}

fn header(ui: &mut Ui, title: &str, color: Color) {
    let r = ui.alloc(vec2(0.0, HEAD_H));
    ui.fill(r, color);
    text_sized(ui, r.inset_xy(14.0, 0.0), title, 20.0, Color::hex(0xe8e8e8), Align::Left);
}

/// Icon in a toolbar row. Returns true when clicked; `draw` gets the rect and
/// whether the button is pressed.
fn icon_button(ui: &mut Ui, name: &str, enabled: bool, draw: impl FnOnce(&mut Ui, Rect, bool)) -> bool {
    let id = ui.id(name);
    let r = ui.alloc(vec2(ICON_W, ui.row_h()));
    let resp = if enabled { ui.interact(id, r) } else { Resp::default() };
    if resp.hovered {
        ui.fill(r, HOVER);
    }
    draw(ui, icons::square(r, 17.0), resp.held);
    resp.clicked
}

/// The value of table column `c` (1 to 4).
fn column(t: &Timing, c: usize) -> u32 {
    match c {
        1 => t.delay,
        2 => t.delay_increment,
        3 => t.release,
        _ => t.release_increment,
    }
}

/// Marks the field in `r` as changed: a corner at its top right.
fn dirty_mark(ui: &mut Ui, r: Rect, dirty: bool) {
    if !dirty {
        return;
    }
    let n = DIRTY.min(r.w).min(r.h) as usize;
    for k in 0..n {
        ui.fill(Rect::new(r.right() - (n - k) as f32, r.y + k as f32, (n - k) as f32, 1.0), ORANGE);
    }
}

/// Looks like the table, but is not editable: for disabled tables. `saved`
/// are the rows of the saved project.
fn static_table(ui: &mut Ui, rows: &[Row], saved: &[Row]) {
    let t = ui.theme;
    let rh = t.row_h();
    let r = ui.alloc(vec2(0.0, rh * (rows.len() + 1) as f32 + 2.0));
    let inner = r.inset(1.0);
    ui.fill(r, TABLE_ROW);
    ui.fill(Rect::new(inner.x, inner.y, inner.w, rh), TABLE_HEAD);
    let mut x = inner.x;
    for (c, (title, width)) in COLUMNS.iter().enumerate() {
        let w = if c + 1 == COLUMNS.len() { inner.right() - x } else { *width };
        let cell = |row: usize| Rect::new(x + t.pad.x, inner.y + row as f32 * rh, w - 2.0 * t.pad.x, rh);
        ui.text_in(cell(0), title, DISABLED, Align::Left);
        ui.fill(Rect::new(x + w - 1.0, inner.y, 1.0, rh), t.border);
        for (i, row) in rows.iter().enumerate() {
            if c == 0 && i % 2 == 1 {
                ui.fill(Rect::new(inner.x, inner.y + (i + 1) as f32 * rh, inner.w, rh), TABLE_STRIPE);
            }
            let text = if c == 0 { (i + 1).to_string() } else { column(&row.timing, c).to_string() };
            ui.text_in(cell(i + 1), &text, DISABLED, if c == 0 { Align::Left } else { Align::Right });
            let changed = c > 0 && saved.get(i).is_some_and(|s| column(&s.timing, c) != column(&row.timing, c));
            dirty_mark(ui, Rect::new(x, inner.y + (i + 1) as f32 * rh, w - 1.0, rh), changed);
        }
        x += w;
    }
    ui.border(r, t.border);
}

impl App {
    pub fn new(tx: Sender<Command>, rx: Receiver<Event>) -> App {
        App {
            project: Project::default(),
            saved: Project::default(),
            file: None,
            flushing: [false; VALVES],
            flush_all: false,
            ports: Vec::new(),
            port: None,
            last_port: None,
            link: Link::Offline,
            auto_connect: true,
            run: Run::Idle,
            transfer: 0.0,
            progress: 0.0,
            log: Vec::new(),
            log_changed: false,
            tab: 0,
            pending: None,
            messages: Vec::new(),
            ask_quit: false,
            quit: false,
            tx,
            rx,
        }
    }

    /// Takes over what the last run left: its project file and serial port.
    pub fn restore(&mut self, config: &Config) {
        self.last_port = config.port.clone();
        // a project that was moved or deleted in the meantime is no error
        let Some(path) = config.project.as_ref().filter(|p| p.is_file()) else { return };
        match Project::load(path) {
            Ok(project) => {
                self.saved = project.clone();
                self.project = project;
                self.file = Some(path.clone());
            }
            Err(e) => self.error(format!("{}: {e}", path.display())),
        }
    }

    /// What the next run should start with. The window is added by the caller.
    pub fn config(&self) -> Config {
        Config { project: self.file.clone(), port: self.last_port.clone(), window: None }
    }

    /// True if the project differs from what was loaded or saved last.
    fn is_dirty(&self) -> bool {
        self.project != self.saved
    }

    /// Quits, but asks first if there are unsaved changes.
    fn request_quit(&mut self) {
        if self.is_dirty() {
            self.ask_quit = true;
        } else {
            self.quit = true;
        }
    }

    /// Unsaved changes are shown by a `*`.
    pub fn title(&self) -> String {
        let changed = if self.is_dirty() { " *" } else { "" };
        match self.file.as_ref().and_then(|f| f.file_name()) {
            Some(name) => format!("Snatchfreezer - {}{changed}", name.to_string_lossy()),
            None => format!("Snatchfreezer{changed}"),
        }
    }

    fn send(&self, command: Command) {
        let _ = self.tx.send(command);
    }

    fn info(&mut self, text: impl Into<String>) {
        self.messages.push((text.into(), false));
    }

    fn error(&mut self, text: impl Into<String>) {
        self.messages.push((text.into(), true));
    }

    fn add_log(&mut self, line: String) {
        if self.log.len() >= LOG_LINES {
            self.log.remove(0);
        }
        self.log.push(line);
        self.log_changed = true;
    }

    /// True if manual actions are possible; tells the user why if not.
    fn ready(&mut self) -> bool {
        if self.link != Link::Online {
            self.error("Serial communication not possible");
        } else if self.run != Run::Idle {
            self.error("The controller is busy");
        }
        self.link == Link::Online && self.run == Run::Idle
    }

    fn connect(&mut self, port: String) {
        self.link = Link::Connecting;
        self.port = Some(port.clone());
        self.send(Command::Connect(port));
    }

    /// Everything the controller switches off is off here as well.
    fn outputs_off(&mut self) {
        self.flushing = [false; VALVES];
        self.flush_all = false;
    }

    /// Handles the events of the serial thread. Call once per frame.
    pub fn update(&mut self) {
        while let Ok(event) = self.rx.try_recv() {
            match event {
                Event::Ports(ports) => {
                    self.ports = ports;
                    if std::mem::take(&mut self.auto_connect) {
                        let last = self.last_port.clone().filter(|p| self.ports.contains(p));
                        let only = (self.ports.len() == 1).then(|| self.ports[0].clone());
                        if let Some(port) = last.or(only) {
                            self.connect(port);
                        }
                    }
                }
                Event::Connected { port } => {
                    self.link = Link::Online;
                    self.last_port = Some(port.clone());
                    self.port = Some(port);
                    self.run = Run::Idle;
                    self.outputs_off();
                }
                Event::Disconnected(reason) => {
                    self.link = Link::Offline;
                    self.port = None;
                    self.run = Run::Idle;
                    self.outputs_off();
                    if !reason.is_empty() {
                        self.error(reason);
                    }
                }
                Event::Log(line) => self.add_log(line),
                Event::Transfer(value) => self.transfer = value,
                Event::Cycle { current, total } => {
                    self.run = Run::Running;
                    self.progress = current as f32 / total.max(1) as f32;
                }
                Event::Finished { canceled } => {
                    self.run = Run::Idle;
                    self.outputs_off();
                    if canceled {
                        (self.transfer, self.progress) = (0.0, 0.0);
                    }
                }
                Event::Failed(text) => {
                    if self.run != Run::Idle {
                        self.run = Run::Idle;
                        (self.transfer, self.progress) = (0.0, 0.0);
                    }
                    self.error(text);
                }
            }
        }
    }

    /// Opens the file dialog requested during the frame, if any.
    pub fn after_frame(&mut self) {
        let Some(action) = self.pending.take() else { return };
        let dialog = || rfd::FileDialog::new().add_filter("SnatchFreezer Settings", &["sf"]).set_directory(crate::config::dir());
        if let FileAction::Load = action {
            let Some(path) = dialog().pick_file() else { return };
            match Project::load(&path) {
                Ok(project) => {
                    self.saved = project.clone();
                    self.project = project;
                    self.info(format!("Loaded {}", path.display()));
                    self.file = Some(path);
                }
                Err(e) => self.error(format!("{}: {e}", path.display())),
            }
            return;
        }
        let known = if let FileAction::Save = action { self.file.clone() } else { None };
        let Some(mut path) = known.or_else(|| dialog().set_file_name("project.sf").save_file()) else { return };
        if path.extension().is_none() {
            path.set_extension("sf");
        }
        match self.project.save(&path) {
            Ok(()) => {
                self.saved = self.project.clone();
                self.info(format!("Saved {}", path.display()));
                self.file = Some(path);
            }
            Err(e) => self.error(format!("{}: {e}", path.display())),
        }
    }

    // ---- actions -------------------------------------------------------------

    fn shot(&mut self) {
        if !self.ready() {
            return;
        }
        let program = self.project.program();
        if program.actions.is_empty() {
            return self.error("No actions defined");
        }
        // the controller switches all outputs off before it starts
        self.outputs_off();
        self.run = Run::Transfer;
        (self.transfer, self.progress) = (0.0, 0.0);
        self.send(Command::Shot(program));
    }

    fn set_flush(&mut self, valve: usize, on: bool) {
        self.flushing[valve] = on;
        self.send(Command::Flush(PINS[valve], on));
    }

    fn set_flush_all(&mut self, on: bool) {
        if !self.ready() {
            return;
        }
        self.flush_all = on;
        for valve in 0..VALVES {
            // only enabled valves are opened, but all are closed
            if (self.project.channels[valve].enabled || !on) && self.flushing[valve] != on {
                self.set_flush(valve, on);
            }
        }
    }

    fn test_all(&mut self) {
        if !self.ready() {
            return;
        }
        for c in self.project.channels.iter().filter(|c| c.enabled) {
            self.send(Command::Test(c.pin));
        }
    }

    /// Starting points only: the right timings depend on the setup.
    fn preset(&mut self, drop_on_drop: bool) {
        let row = |delay, release| Row { timing: Timing { delay, release, ..Timing::default() }, ignore: false };
        // nothing of a preset is saved yet: all its fields are marked
        self.project = Project::default();
        self.saved = Project::default();
        self.file = None;
        let c = &mut self.project.channels;
        c[0].description = "Drop".into();
        c[0].rows[0] = row(0, 40_000);
        c[6].description = "Flash".into();
        c[6].rows[0] = row(250_000, 10_000);
        c[8].description = "Camera".into();
        c[8].rows[0] = row(0, 500_000);
        if drop_on_drop {
            c[0].rows[1] = row(80_000, 40_000);
            c[6].rows[0] = row(330_000, 10_000);
        }
        self.project.description = if drop_on_drop { "Drop on Drop" } else { "Crown" }.into();
        self.info("Preset loaded - adjust the timings to your setup");
    }

    // ---- user interface ------------------------------------------------------

    pub fn build(&mut self, ui: &mut Ui) {
        for (text, error) in self.messages.drain(..) {
            ui.toast_colored(&text, if error { RED } else { GREEN });
        }
        self.menus(ui);
        let area = ui.size(Size::Fill, Size::Fill).alloc(Vec2::ZERO);
        let left = Rect::new(area.x, area.y, LEFT_W.min(area.w), area.h);
        let right = Rect::new(left.right(), area.y, area.w - left.w, area.h);
        ui.pane(left, |ui| {
            header(ui, "Valves", VALVE_HEAD);
            self.channels(ui, 0..VALVES);
        });
        ui.pane(right, |ui| {
            header(ui, "Cameras / Flashes", CAMERA_HEAD);
            self.channels(ui, VALVES..PINS.len());
            header(ui, "Control", CONTROL_HEAD);
            self.control(ui);
        });
        if std::mem::take(&mut self.ask_quit) {
            ui.open_dialog("quit");
        }
        let question = "The project has unsaved changes.\nQuit without saving them?";
        if ui.confirm("quit", "Unsaved changes", question, &["Quit", "Cancel"]) == Some(0) {
            self.quit = true;
        }
    }

    fn menus(&mut self, ui: &mut Ui) {
        ui.menubar(|ui| {
            ui.menu("Application", |ui| {
                if ui.menu_item("New") {
                    self.project = Project::default();
                    self.saved = Project::default();
                    self.file = None;
                }
                if ui.menu_item("Load") {
                    self.pending = Some(FileAction::Load);
                }
                if ui.menu_item("Save") {
                    self.pending = Some(FileAction::Save);
                }
                if ui.menu_item("Save As ...") {
                    self.pending = Some(FileAction::SaveAs);
                }
                ui.separator();
                if ui.menu_item("Quit") {
                    self.request_quit();
                }
            });
            ui.menu("Control", |ui| {
                let mut all = self.flush_all;
                if ui.menu_check("Flush all siphons", &mut all) {
                    self.set_flush_all(all);
                }
                if ui.menu_item("Test all channels") {
                    self.test_all();
                }
            });
            ui.menu("Presets", |ui| {
                if ui.menu_item("Crown") {
                    self.preset(false);
                }
                if ui.menu_item("Drop on Drop") {
                    self.preset(true);
                }
            });
            ui.menu("Help", |ui| {
                if ui.menu_item("Who made it...") {
                    self.info(concat!("Snatchfreezer ", env!("CARGO_PKG_VERSION"), " - github.com/guidobonerz/snatchfreezer"));
                }
            });
        });
    }

    /// The tables `range`, two per line.
    fn channels(&mut self, ui: &mut Ui, range: Range<usize>) {
        let rh = ui.row_h();
        let rows = self.project.channels[range.start].rows.len();
        let block_h = rh * (rows + 2) as f32 + 2.0 + ui.theme.gap + 14.0;
        let area = ui.alloc(vec2(0.0, range.len().div_ceil(2) as f32 * block_h + MARGIN));
        let w = ((area.w - 2.0 * MARGIN - GAP) / 2.0).floor();
        for (k, i) in range.enumerate() {
            let x = area.x + MARGIN + (k % 2) as f32 * (w + GAP);
            let y = area.y + MARGIN + (k / 2) as f32 * block_h;
            ui.pane(Rect::new(x, y, w, block_h), |ui| self.channel(ui, i, w));
        }
    }

    /// Table `i` with its toolbar, `width` wide.
    fn channel(&mut self, ui: &mut Ui, i: usize, width: f32) {
        let t = ui.theme;
        let rh = t.row_h();
        let idle = self.link == Link::Online && self.run == Run::Idle;
        let flushing = i < VALVES && self.flushing[i];
        let ch = &mut self.project.channels[i];
        let saved = &self.saved.channels[i];
        let (valve, enabled) = (ch.is_valve(), ch.enabled);
        let number = if valve { i + 1 } else { i + 1 - VALVES };
        let (mut toggled, mut flush, mut test) = (false, false, false);

        ui.push_id(i);
        ui.row(|ui| {
            let r = ui.alloc(vec2(NUMBER_W, rh));
            text_sized(ui, r, &number.to_string(), 19.0, if enabled { t.text } else { DISABLED }, Align::Left);

            let r = ui.alloc(vec2(TOGGLE_W, rh));
            toggled = ui.interact(ui.id("enabled"), r).clicked;
            let pill = Rect::new(r.x + 2.0, r.y + ((rh - 14.0) / 2.0).round(), 28.0, 14.0);
            icons::toggle(ui, pill, enabled, if enabled { BLUE } else { GREY });
            dirty_mark(ui, r, enabled != saved.enabled);

            let icons_n = if valve { 2.0 } else { 1.0 };
            let w = width - NUMBER_W - TOGGLE_W - icons_n * ICON_W - (icons_n + 2.0) * t.gap;
            if enabled {
                ui.w(Size::Px(w)).text_edit("description", &mut ch.description);
            } else {
                let r = ui.alloc(vec2(w, rh));
                ui.fill(r, t.field);
                ui.border(r, t.border);
                ui.text_in(r.inset_xy(t.pad.x, 0.0), &ch.description, DISABLED, Align::Left);
            }
            dirty_mark(ui, ui.last_rect().inset(1.0), ch.description != saved.description);

            let active = enabled && idle;
            if valve {
                flush = icon_button(ui, "flush", active, |ui, r, held| {
                    let color = if !active { DISABLED } else if held || flushing { BLUE_LIGHT } else { BLUE };
                    if flushing {
                        ui.fill(Rect::new(r.x - 3.0, r.bottom() + 1.0, r.w + 6.0, 2.0), BLUE_LIGHT);
                    }
                    icons::drop(ui, r, color);
                });
            }
            test = icon_button(ui, "test", active, |ui, r, held| {
                icons::bolt(ui, r, if !active { DISABLED } else if held { YELLOW_LIGHT } else { YELLOW });
            });
        });
        ui.space(2.0);

        if enabled {
            // table colors differ from the rest of the UI
            let normal = ui.theme;
            ui.theme.field = TABLE_ROW;
            ui.theme.stripe = TABLE_STRIPE;
            ui.theme.widget = TABLE_HEAD;
            ui.theme.text_dim = t.text;
            let rows = &mut ch.rows;
            let height = rh * (rows.len() + 1) as f32 + 2.0;
            // rows are not selectable: the selection is dropped every frame
            ui.h(Size::Px(height)).table("timings", &COLUMNS, rows.len(), &mut None, |ui, r, c| {
                let v = &mut rows[r].timing;
                match c {
                    0 => return ui.label(&(r + 1).to_string()),
                    1 => drop(ui.cell_num(&mut v.delay)),
                    2 => drop(ui.cell_num(&mut v.delay_increment)),
                    3 => drop(ui.cell_num(&mut v.release)),
                    _ => drop(ui.cell_num(&mut v.release_increment)),
                }
                // the clip rect is the cell
                let cell = ui.clip();
                dirty_mark(ui, cell, column(v, c) != column(&saved.rows[r].timing, c));
            });
            ui.theme = normal;
        } else {
            static_table(ui, &ch.rows, &saved.rows);
        }
        ui.pop_id();

        if toggled {
            ch.enabled = !enabled;
            // a disabled valve must not stay open
            if enabled && flushing {
                self.set_flush(i, false);
            }
        }
        if flush {
            self.set_flush(i, !flushing);
        }
        if test {
            self.send(Command::Test(PINS[i]));
        }
    }

    fn control(&mut self, ui: &mut Ui) {
        let t = ui.theme;
        let rh = t.row_h();
        let area = ui.size(Size::Fill, Size::Fill).alloc(Vec2::ZERO).inset(MARGIN);
        const TOP_H: f32 = 84.0;

        // ---- connection and cycles ----
        let ports = self.ports.clone();
        let mut names = vec!["-"];
        names.extend(ports.iter().map(String::as_str));
        let current = self.port.as_ref().and_then(|p| ports.iter().position(|x| x == p)).map_or(0, |i| i + 1);
        let mut selected = current;
        ui.pane(Rect::new(area.x, area.y + 4.0, 290.0, TOP_H), |ui| {
            ui.grid(&[Size::Px(112.0), Size::Px(140.0), Size::Px(rh)], |ui| {
                ui.label("Serial Port");
                ui.select("port", &mut selected, &names);
                let r = ui.alloc(vec2(rh, rh));
                let (mood, color) = match self.link {
                    Link::Online => (1, GREEN),
                    Link::Connecting => (0, YELLOW),
                    Link::Offline => (-1, RED),
                };
                icons::face(ui, icons::square(r, 17.0), mood, color);

                ui.label("Cycles");
                ui.number("cycles", &mut self.project.cycles, 1);
                dirty_mark(ui, ui.last_rect().inset(1.0), self.project.cycles != self.saved.cycles);
                ui.alloc(Vec2::ZERO);

                ui.label("Cycle Delay (ms)");
                ui.number("cycle delay", &mut self.project.cycle_delay_ms, 500);
                dirty_mark(ui, ui.last_rect().inset(1.0), self.project.cycle_delay_ms != self.saved.cycle_delay_ms);
                ui.alloc(Vec2::ZERO);
            });
        });
        self.project.cycles = self.project.cycles.clamp(1, MAX_CYCLES);
        self.project.cycle_delay_ms = self.project.cycle_delay_ms.min(MAX_CYCLE_DELAY_MS);
        if selected != current {
            match selected.checked_sub(1) {
                Some(i) => self.connect(ports[i].clone()),
                None => self.send(Command::Disconnect),
            }
        }

        // ---- snapshot ----
        let shot = Rect::new(area.x + 330.0, area.y + 2.0, 74.0, 62.0);
        let resp = ui.interact(ui.id("shot"), shot);
        if resp.hovered {
            ui.fill(shot, HOVER);
        }
        if self.run == Run::Idle {
            icons::camera(ui, icons::square(shot, 54.0), if resp.held { ORANGE_LIGHT } else { ORANGE });
            if resp.clicked {
                self.shot();
            }
        } else {
            // while a program is uploaded or running the button cancels it
            icons::stop(ui, icons::square(shot, 54.0), RED);
            if resp.clicked {
                self.send(Command::Cancel);
            }
        }
        let percent = |v: f32| format!("{:.0}%", v * 100.0);
        let transfer = if self.transfer >= 1.0 { "Done".to_string() } else { percent(self.transfer) };
        for (k, (value, text)) in [(self.transfer, transfer), (self.progress, percent(self.progress))].into_iter().enumerate() {
            let r = Rect::new(shot.right() + 22.0 + k as f32 * 78.0, area.y + 4.0, 50.0, 50.0);
            icons::progress(ui, r, value, TABLE_HEAD, Color::hex(0x777575), ORANGE);
            ui.text_in(Rect::new(r.x - 10.0, r.bottom() + 2.0, r.w + 20.0, rh), &text, t.text, Align::Center);
        }

        // ---- protocol and description ----
        let y = area.y + TOP_H + 10.0;
        ui.pane(Rect::new(area.x, y, area.w, (area.bottom() - y).max(0.0)), |ui| {
            ui.row(|ui| {
                for (i, name) in ["Protocol", "Description"].into_iter().enumerate() {
                    if ui.tool_toggle(name, &mut (self.tab == i)) {
                        self.tab = i;
                    }
                }
            });
            if self.tab == 1 {
                ui.text_area("description", &mut self.project.description);
                dirty_mark(ui, ui.last_rect().inset(1.0), self.project.description != self.saved.description);
                return;
            }
            let r = ui.size(Size::Fill, Size::Fill).alloc(Vec2::ZERO);
            ui.fill(r, t.field);
            ui.border(r, t.border);
            if std::mem::take(&mut self.log_changed) {
                // follow the newest line (the offset is clamped to the content)
                ui.set_state(ui.id("log"), vec2(0.0, 1e9));
            }
            ui.theme.gap = 0.0;
            ui.theme.pad.y = 1.0;
            ui.pane(r.inset(4.0), |ui| {
                ui.scroll("log", |ui| {
                    for line in &self.log {
                        ui.label(line);
                    }
                })
            });
            ui.theme = t;
        });
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use std::sync::mpsc;

    /// Builds the whole UI in the states it can be in. With the environment
    /// variable SNATCHFREEZER_SHOT set, the last frame is written there as PPM.
    /// SNATCHFREEZER_QUIT adds the dialog that asks before quitting.
    #[test]
    fn user_interface_builds() {
        let (tx, commands) = mpsc::channel();
        let (events, rx) = mpsc::channel();
        let mut app = App::new(tx, rx);
        let mut ui = Ui::new();
        let scale = std::env::var("SNATCHFREEZER_SCALE").ok().and_then(|s| s.parse().ok()).unwrap_or(1.0);
        ui.theme = theme(scale);
        let (w, h) = (WINDOW.0 * scale, WINDOW.1 * scale);
        let frame = |app: &mut App, ui: &mut Ui| {
            app.update();
            ui.begin(w, h);
            app.build(ui);
            ui.end();
            assert!(ui.draws().count() > 100);
        };
        frame(&mut app, &mut ui);

        // a single port is connected automatically
        events.send(Event::Ports(vec!["COM7".into()])).unwrap();
        frame(&mut app, &mut ui);
        assert!(matches!(commands.try_recv(), Ok(Command::Connect(p)) if p == "COM7"));
        events.send(Event::Connected { port: "COM7".into() }).unwrap();
        events.send(Event::Log("Connected to COM7, firmware 2.0".into())).unwrap();

        app.project.channels[0].rows[0].timing = Timing { delay: 1_000_000, release: 100_000, ..Timing::default() };
        app.project.channels[3].enabled = false;
        app.project.channels[3].description = "disabled".into();
        app.project.cycles = 10;
        frame(&mut app, &mut ui);
        app.shot();
        assert!(matches!(commands.try_recv(), Ok(Command::Shot(p)) if p.actions.len() == 1 && p.cycles == 10));
        events.send(Event::Transfer(1.0)).unwrap();
        events.send(Event::Cycle { current: 1, total: 10 }).unwrap();
        frame(&mut app, &mut ui);
        assert!(app.run == Run::Running && app.progress == 0.1);
        if std::env::var_os("SNATCHFREEZER_BUSY").is_none() {
            events.send(Event::Finished { canceled: false }).unwrap();
            frame(&mut app, &mut ui);
            assert!(app.run == Run::Idle);
            app.set_flush(1, true);
        }
        if std::env::var_os("SNATCHFREEZER_QUIT").is_some() {
            // the dialog that asks about the unsaved changes (sized in its first frame)
            app.request_quit();
            frame(&mut app, &mut ui);
        }
        frame(&mut app, &mut ui);

        if let Some(path) = std::env::var_os("SNATCHFREEZER_SHOT") {
            let mut canvas = soft::Canvas::new(w as usize, h as usize);
            canvas.render(&ui);
            let mut out = format!("P6 {} {} 255\n", canvas.w, canvas.h).into_bytes();
            out.extend(canvas.buf.iter().flat_map(|p| [(p >> 16) as u8, (p >> 8) as u8, *p as u8]));
            std::fs::write(path, out).unwrap();
        }
    }

    #[test]
    fn the_last_run_is_restored() {
        let path = std::env::temp_dir().join(format!("snatchfreezer-{}-restore.sf", std::process::id()));
        let saved = Project { description: "last".into(), cycles: 4, ..Project::default() };
        saved.save(&path).unwrap();

        let (tx, commands) = mpsc::channel();
        let (events, rx) = mpsc::channel();
        let mut app = App::new(tx, rx);
        app.restore(&Config { project: Some(path.clone()), port: Some("COM9".into()), window: None });
        assert!(app.project == saved && app.file.as_ref() == Some(&path));
        let _ = std::fs::remove_file(&path);

        // the port of the last run wins over the other ones
        events.send(Event::Ports(vec!["COM7".into(), "COM9".into()])).unwrap();
        app.update();
        assert!(matches!(commands.try_recv(), Ok(Command::Connect(p)) if p == "COM9"));

        // a new connection replaces it in the configuration
        events.send(Event::Connected { port: "COM7".into() }).unwrap();
        events.send(Event::Disconnected(String::new())).unwrap();
        app.update();
        assert_eq!(app.config(), Config { project: Some(path), port: Some("COM7".into()), window: None });
    }

    #[test]
    fn a_missing_project_or_port_of_the_last_run_is_ignored() {
        let (tx, commands) = mpsc::channel();
        let (events, rx) = mpsc::channel();
        let mut app = App::new(tx, rx);
        let gone = std::env::temp_dir().join("snatchfreezer-does-not-exist.sf");
        app.restore(&Config { project: Some(gone), port: Some("COM9".into()), window: None });
        assert!(app.project == Project::default() && app.file.is_none() && app.messages.is_empty());
        events.send(Event::Ports(vec!["COM7".into(), "COM8".into()])).unwrap();
        app.update();
        assert!(commands.try_recv().is_err());
    }

    #[test]
    fn changes_are_dirty_until_they_are_saved_or_undone() {
        let (tx, _commands) = mpsc::channel();
        let (_events, rx) = mpsc::channel();
        let mut app = App::new(tx, rx);
        assert!(!app.is_dirty() && app.title() == "Snatchfreezer");

        app.project.cycles = 5;
        assert!(app.is_dirty() && app.title() == "Snatchfreezer *");
        // back to the saved value is not a change
        app.project.cycles = 1;
        assert!(!app.is_dirty());

        // saving makes the current state the clean one
        app.project.channels[2].rows[1].timing.delay = 7;
        let path = std::env::temp_dir().join(format!("snatchfreezer-{}-dirty.sf", std::process::id()));
        app.file = Some(path.clone());
        app.pending = Some(FileAction::Save);
        app.after_frame();
        let _ = std::fs::remove_file(&path);
        assert!(!app.is_dirty() && !app.title().ends_with('*'));
        app.project.channels[2].enabled = false;
        assert!(app.is_dirty() && app.title().ends_with(".sf *"));

        // a preset is unsaved as a whole
        app.preset(false);
        assert!(app.is_dirty() && app.title() == "Snatchfreezer *");
    }

    #[test]
    fn quitting_with_unsaved_changes_has_to_be_confirmed() {
        let frame = |app: &mut App, ui: &mut Ui, key: Option<Key>| {
            ui.input.keys.extend(key);
            ui.begin(WINDOW.0, WINDOW.1);
            app.build(ui);
            ui.end();
        };
        for (key, quits) in [(Key::Enter, true), (Key::Escape, false)] {
            let (tx, _commands) = mpsc::channel();
            let (_events, rx) = mpsc::channel();
            let mut app = App::new(tx, rx);
            let mut ui = Ui::new();
            app.project.cycles = 3;
            app.request_quit();
            frame(&mut app, &mut ui, None);
            frame(&mut app, &mut ui, None);
            assert!(!app.quit && ui.dialog_open());
            frame(&mut app, &mut ui, Some(key));
            assert!(app.quit == quits && !ui.dialog_open());
        }

        // nothing to ask without changes
        let (tx, _commands) = mpsc::channel();
        let (_events, rx) = mpsc::channel();
        let mut app = App::new(tx, rx);
        app.request_quit();
        assert!(app.quit);
    }

    #[test]
    fn nothing_is_sent_without_a_connection() {
        let (tx, commands) = mpsc::channel();
        let (_events, rx) = mpsc::channel();
        let mut app = App::new(tx, rx);
        app.project.channels[0].rows[0].timing.release = 5;
        app.shot();
        app.set_flush_all(true);
        app.test_all();
        assert!(commands.try_recv().is_err());
        assert!(app.messages.iter().all(|m| m.1) && app.messages.len() == 3);
    }
}
