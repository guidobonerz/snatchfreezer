//! Icons, drawn as filled shapes: drazui only knows rectangles and text.

use drazui::{Color, Rect, Ui};

/// Fills the pixels of `r` for which `inside(x, y)` holds; x and y run from
/// -1 to 1 across the rect (y down).
pub fn shape(ui: &mut Ui, r: Rect, color: Color, inside: impl Fn(f32, f32) -> bool) {
    // Work on whole device pixels, so that the runs neither overlap nor leave
    // gaps at fractional scales.
    let s = ui.theme.scale.max(0.25);
    let (x0, y0) = ((r.x * s).round(), (r.y * s).round());
    let (nx, ny) = ((r.w * s).round() as usize, (r.h * s).round() as usize);
    for j in 0..ny {
        let y = (j as f32 + 0.5) / ny as f32 * 2.0 - 1.0;
        let mut start = None;
        for i in 0..=nx {
            let on = i < nx && inside((i as f32 + 0.5) / nx as f32 * 2.0 - 1.0, y);
            match (on, start) {
                (true, None) => start = Some(i),
                (false, Some(a)) => {
                    ui.fill(Rect::new((x0 + a as f32) / s, (y0 + j as f32) / s, (i - a) as f32 / s, 1.0 / s), color);
                    start = None;
                }
                _ => {}
            }
        }
    }
}

/// Largest centered square in `r`, shrunk to `size` if that is smaller.
pub fn square(r: Rect, size: f32) -> Rect {
    let s = size.min(r.w).min(r.h);
    Rect::new((r.x + (r.w - s) / 2.0).round(), (r.y + (r.h - s) / 2.0).round(), s, s)
}

fn disc(x: f32, y: f32, cx: f32, cy: f32, radius: f32) -> bool {
    (x - cx) * (x - cx) + (y - cy) * (y - cy) <= radius * radius
}

/// Distance from (x, y) to the segment a-b.
fn segment(x: f32, y: f32, a: (f32, f32), b: (f32, f32)) -> f32 {
    let (dx, dy) = (b.0 - a.0, b.1 - a.1);
    let t = (((x - a.0) * dx + (y - a.1) * dy) / (dx * dx + dy * dy)).clamp(0.0, 1.0);
    ((x - a.0 - t * dx).powi(2) + (y - a.1 - t * dy).powi(2)).sqrt()
}

fn polygon(x: f32, y: f32, points: &[(f32, f32)]) -> bool {
    let mut inside = false;
    let mut a = points[points.len() - 1];
    for &b in points {
        if (a.1 > y) != (b.1 > y) && x < a.0 + (y - a.1) / (b.1 - a.1) * (b.0 - a.0) {
            inside = !inside;
        }
        a = b;
    }
    inside
}

/// Switch: a pill (twice as wide as high) with the knob right when on.
pub fn toggle(ui: &mut Ui, r: Rect, on: bool, color: Color) {
    shape(ui, r, color, |x, y| {
        let x = x * 2.0;
        let pill = disc(x.abs().max(1.0), y, 1.0, 0.0, 1.0);
        pill && !disc(x, y, if on { 1.0 } else { -1.0 }, 0.0, 0.6)
    });
}

pub fn bolt(ui: &mut Ui, r: Rect, color: Color) {
    const P: [(f32, f32); 6] = [(0.3, -1.0), (-0.65, 0.12), (-0.08, 0.12), (-0.32, 1.0), (0.65, -0.22), (0.1, -0.22)];
    shape(ui, r, color, |x, y| polygon(x, y, &P));
}

pub fn drop(ui: &mut Ui, r: Rect, color: Color) {
    shape(ui, r, color, |x, y| disc(x, y, 0.0, 0.32, 0.62) || (y < 0.1 && x.abs() <= (y + 1.0) * 0.5));
}

/// Face: smiling, neutral or sad for `mood` > 0, = 0, < 0.
pub fn face(ui: &mut Ui, r: Rect, mood: i8, color: Color) {
    shape(ui, r, color, |x, y| {
        let eyes = disc(x.abs(), y, 0.36, -0.28, 0.15);
        let mouth = match mood {
            1.. => y > 0.12 && disc(x, y, 0.0, 0.0, 0.7) && !disc(x, y, 0.0, 0.0, 0.52),
            0 => x.abs() < 0.45 && (y - 0.4).abs() < 0.09,
            _ => y < 0.6 && disc(x, y, 0.0, 0.95, 0.7) && !disc(x, y, 0.0, 0.95, 0.52),
        };
        disc(x, y, 0.0, 0.0, 1.0) && !eyes && !mouth
    });
}

pub fn camera(ui: &mut Ui, r: Rect, color: Color) {
    shape(ui, r, color, |x, y| {
        let body = x.abs() <= 1.0 && (-0.5..=0.85).contains(&y);
        let top = (x.abs() <= 0.38 && (-0.8..=-0.5).contains(&y)) || ((-0.9..=-0.6).contains(&x) && (-0.66..=-0.5).contains(&y));
        let lens_ring = disc(x, y, 0.0, 0.2, 0.5) && !disc(x, y, 0.0, 0.2, 0.36);
        let flash = (0.6..=0.86).contains(&x) && (-0.34..=-0.16).contains(&y);
        (body || top) && !lens_ring && !flash
    });
}

pub fn stop(ui: &mut Ui, r: Rect, color: Color) {
    shape(ui, r, color, |x, y| x.abs() <= 0.6 && y.abs() <= 0.6);
}

/// Progress indicator: a pie that fills clockwise from the top, and a check
/// mark when complete.
pub fn progress(ui: &mut Ui, r: Rect, value: f32, ring: Color, base: Color, fill: Color) {
    let value = value.clamp(0.0, 1.0);
    shape(ui, r, ring, |x, y| disc(x, y, 0.0, 0.0, 1.0));
    if value >= 1.0 {
        shape(ui, r, fill, |x, y| disc(x, y, 0.0, 0.0, 0.92));
        shape(ui, r, Color::hex(0xffffff), |x, y| {
            segment(x, y, (-0.45, 0.02), (-0.14, 0.36)).min(segment(x, y, (-0.14, 0.36), (0.46, -0.36))) < 0.13
        });
        return;
    }
    shape(ui, r, base, |x, y| disc(x, y, 0.0, 0.0, 0.92));
    if value > 0.0 {
        shape(ui, r, fill, |x, y| {
            let angle = x.atan2(-y).rem_euclid(std::f32::consts::TAU);
            disc(x, y, 0.0, 0.0, 0.92) && angle <= value * std::f32::consts::TAU
        });
    }
}
