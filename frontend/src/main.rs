//! Snatchfreezer frontend: edits the timing tables and sends them to the
//! Arduino controller.

#![cfg_attr(not(debug_assertions), windows_subsystem = "windows")]

mod app;
mod clipboard;
mod config;
mod icons;
mod protocol;
mod serial;
mod settings;

use std::sync::mpsc;
use std::time::Instant;

use drazui::soft::Canvas;
use drazui::{vec2, Key, Ui};

/// Display scaling factor (1.5 at 150 %). Declaring the process DPI-aware
/// keeps Windows from stretching (and blurring) the window.
#[cfg(windows)]
fn dpi_scale() -> f32 {
    #[link(name = "user32")]
    extern "system" {
        fn SetProcessDPIAware() -> i32;
        fn GetDpiForSystem() -> u32;
    }
    // SAFETY: plain Win32 calls without arguments.
    unsafe {
        SetProcessDPIAware();
        GetDpiForSystem() as f32 / 96.0
    }
}

#[cfg(not(windows))]
fn dpi_scale() -> f32 {
    1.0
}

struct Chars(mpsc::Sender<u32>);

impl minifb::InputCallback for Chars {
    fn add_char(&mut self, c: u32) {
        let _ = self.0.send(c);
    }
}

fn main() {
    let (commands, events, serial_thread) = serial::spawn();
    let mut app = app::App::new(commands, events);
    let config = config::Config::load();
    app.restore(&config);

    let scale = dpi_scale();
    let mut ui = Ui::new();
    ui.theme = app::theme(scale);
    ui.toast_secs = 5.0;

    // the configuration holds the window size without the display scaling
    let size = config.window.map_or(app::WINDOW, |(w, h)| (w as f32, h as f32));
    let (mut w, mut h) = ((size.0 * scale) as usize, (size.1 * scale) as usize);
    let mut window_size = config.window;
    let mut canvas = Canvas::new(w, h);
    let mut title = app.title();
    let options = minifb::WindowOptions { resize: true, ..Default::default() };
    let mut window = minifb::Window::new(&title, w, h, options).expect("cannot open a window");
    window.set_target_fps(60);
    let (char_tx, chars) = mpsc::channel();
    window.set_input_callback(Box::new(Chars(char_tx)));

    let mut last_frame = Instant::now();
    while window.is_open() && !app.quit {
        (w, h) = window.get_size();
        if w == 0 || h == 0 {
            // minimized
            window.update();
            continue;
        }
        canvas.resize(w, h);
        window_size = Some(((w as f32 / scale).round() as u32, (h as f32 / scale).round() as u32));

        if let Some((x, y)) = window.get_mouse_pos(minifb::MouseMode::Pass) {
            ui.input.mouse = vec2(x, y);
        }
        ui.input.down = window.get_mouse_down(minifb::MouseButton::Left);
        ui.input.right = window.get_mouse_down(minifb::MouseButton::Right);
        ui.input.dt = last_frame.elapsed().as_secs_f32();
        last_frame = Instant::now();
        if let Some((x, y)) = window.get_scroll_wheel() {
            // wheel units differ per platform, so only the direction is used
            let dir = |v: f32| if v == 0.0 { 0.0 } else { v.signum() };
            ui.input.scroll = vec2(dir(x), dir(y));
        }
        use minifb::Key as K;
        let ctrl = window.is_key_down(K::LeftCtrl) || window.is_key_down(K::RightCtrl);
        ui.input.shift = window.is_key_down(K::LeftShift) || window.is_key_down(K::RightShift);
        for key in window.get_keys_pressed(minifb::KeyRepeat::Yes) {
            ui.input.keys.push(match key {
                K::A if ctrl => Key::SelectAll,
                K::C if ctrl => Key::Copy,
                K::X if ctrl => Key::Cut,
                K::V if ctrl => {
                    if let Some(text) = clipboard::get() {
                        ui.clipboard = text;
                    }
                    Key::Paste
                }
                K::Left => Key::Left,
                K::Right => Key::Right,
                K::Up => Key::Up,
                K::Down => Key::Down,
                K::PageUp => Key::PageUp,
                K::PageDown => Key::PageDown,
                K::Home => Key::Home,
                K::End => Key::End,
                K::Backspace => Key::Backspace,
                K::Delete => Key::Delete,
                K::Enter | K::NumPadEnter => Key::Enter,
                K::Escape => Key::Escape,
                _ => continue,
            });
        }
        // with Ctrl held the characters belong to a shortcut
        ui.input.text.extend(chars.try_iter().filter_map(char::from_u32).filter(|c| !ctrl && !c.is_control()));

        app.update();
        ui.begin(w as f32, h as f32);
        app.build(&mut ui);
        ui.end();
        if let Some(text) = ui.take_copied() {
            clipboard::set(text);
        }
        canvas.render(&ui);
        window.update_with_buffer(&canvas.buf, w, h).expect("cannot present the frame");

        app.after_frame();
        if app.title() != title {
            title = app.title();
            window.set_title(&title);
        }
    }

    // Not being able to write the configuration must not keep the app from closing.
    let _ = config::Config { window: window_size, ..app.config() }.save();

    // Ends the serial thread, which switches all outputs off first.
    drop(app);
    let _ = serial_thread.join();
}
