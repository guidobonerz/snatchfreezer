//! Project state and its `.sf` file (JSON, same layout as the Java frontend
//! wrote, so old projects still load).

use std::path::Path;

use serde::{Deserialize, Serialize};

use crate::protocol::{Action, Program, Timing, MAX_TIMINGS};

pub const VALVES: usize = 6;
/// Output pins in table order: valves 1-6, then cameras / flashes 1-4.
pub const PINS: [u8; 10] = [2, 3, 4, 5, 6, 7, 8, 9, 10, 11];

pub const MAX_CYCLES: u16 = 999;
pub const MAX_CYCLE_DELAY_MS: u32 = 60_000;

#[derive(Clone, Copy, Debug, Default, PartialEq, Eq)]
pub struct Row {
    pub timing: Timing,
    /// Kept from the file: an ignored row is not sent.
    pub ignore: bool,
}

impl Row {
    fn is_used(&self) -> bool {
        !self.ignore && self.timing != Timing::default()
    }
}

/// One table.
#[derive(Clone, Debug, PartialEq, Eq)]
pub struct Channel {
    pub pin: u8,
    pub enabled: bool,
    pub description: String,
    pub rows: Vec<Row>,
}

impl Channel {
    pub fn is_valve(&self) -> bool {
        self.pin <= PINS[VALVES - 1]
    }
}

#[derive(Clone, Debug, PartialEq, Eq)]
pub struct Project {
    pub description: String,
    pub cycles: u16,
    pub cycle_delay_ms: u32,
    pub channels: Vec<Channel>,
}

impl Default for Project {
    fn default() -> Self {
        let channels = PINS
            .iter()
            .enumerate()
            .map(|(i, &pin)| Channel {
                pin,
                enabled: true,
                description: String::new(),
                rows: vec![Row::default(); if i < VALVES { MAX_TIMINGS } else { 1 }],
            })
            .collect();
        Project { description: String::new(), cycles: 1, cycle_delay_ms: 0, channels }
    }
}

impl Project {
    /// The program for the controller: one action per enabled table that has
    /// at least one used row.
    pub fn program(&self) -> Program {
        let actions = self
            .channels
            .iter()
            .filter(|c| c.enabled)
            .map(|c| Action { pin: c.pin, timings: c.rows.iter().filter(|r| r.is_used()).map(|r| r.timing).collect() })
            .filter(|a: &Action| !a.timings.is_empty())
            .collect();
        Program { cycles: self.cycles, cycle_delay_ms: self.cycle_delay_ms, actions }
    }

    pub fn load(path: &Path) -> Result<Project, String> {
        let text = std::fs::read_to_string(path).map_err(|e| e.to_string())?;
        let file: FileSettings = serde_json::from_str(&text).map_err(|e| e.to_string())?;
        Ok(file.into_project())
    }

    pub fn save(&self, path: &Path) -> Result<(), String> {
        let text = serde_json::to_string_pretty(&FileSettings::from_project(self)).map_err(|e| e.to_string())?;
        std::fs::write(path, text).map_err(|e| e.to_string())
    }
}

// ---- file format -------------------------------------------------------------

#[derive(Serialize, Deserialize, Default)]
#[serde(rename_all = "camelCase", default)]
struct FileSettings {
    description: Option<String>,
    cycles: i64,
    cycle_delay: i64,
    action_bean_list: Vec<FileAction>,
}

#[derive(Serialize, Deserialize, Default)]
#[serde(rename_all = "camelCase", default)]
struct FileAction {
    pin_index: i64,
    activ: Option<bool>,
    description: Option<String>,
    action_item_list: Vec<FileItem>,
}

#[derive(Serialize, Deserialize, Default)]
#[serde(rename_all = "camelCase", default)]
struct FileItem {
    id: i64,
    delay: i64,
    release: i64,
    delay_increment: i64,
    release_increment: i64,
    ignore: bool,
}

fn us(v: i64) -> u32 {
    v.clamp(0, u32::MAX as i64) as u32
}

impl FileSettings {
    fn into_project(self) -> Project {
        let mut p = Project {
            description: self.description.unwrap_or_default(),
            cycles: self.cycles.clamp(1, MAX_CYCLES as i64) as u16,
            cycle_delay_ms: self.cycle_delay.clamp(0, MAX_CYCLE_DELAY_MS as i64) as u32,
            ..Project::default()
        };
        // tables are stored in order; anything beyond the known ones is dropped
        for (c, a) in p.channels.iter_mut().zip(self.action_bean_list) {
            c.enabled = a.activ.unwrap_or(true);
            c.description = a.description.unwrap_or_default();
            for (row, item) in c.rows.iter_mut().zip(a.action_item_list) {
                row.timing = Timing {
                    delay: us(item.delay),
                    release: us(item.release),
                    delay_increment: us(item.delay_increment),
                    release_increment: us(item.release_increment),
                };
                row.ignore = item.ignore;
            }
        }
        p
    }

    fn from_project(p: &Project) -> FileSettings {
        let action = |(i, c): (usize, &Channel)| FileAction {
            pin_index: i as i64 + 1,
            activ: Some(c.enabled),
            description: Some(c.description.clone()),
            action_item_list: c
                .rows
                .iter()
                .enumerate()
                .map(|(j, r)| FileItem {
                    id: j as i64 + 1,
                    delay: r.timing.delay as i64,
                    release: r.timing.release as i64,
                    delay_increment: r.timing.delay_increment as i64,
                    release_increment: r.timing.release_increment as i64,
                    ignore: r.ignore,
                })
                .collect(),
        };
        FileSettings {
            description: Some(p.description.clone()),
            cycles: p.cycles as i64,
            cycle_delay: p.cycle_delay_ms as i64,
            action_bean_list: p.channels.iter().enumerate().map(action).collect(),
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    fn timing(delay: u32, release: u32) -> Timing {
        Timing { delay, release, ..Timing::default() }
    }

    #[test]
    fn only_enabled_tables_with_used_rows_become_actions() {
        let mut p = Project::default();
        p.channels[0].rows[0].timing = timing(1_000_000, 100_000);
        p.channels[0].rows[2].timing = timing(5, 6);
        p.channels[0].rows[3] = Row { timing: timing(7, 8), ignore: true };
        p.channels[1].rows[0].timing = timing(1, 2);
        p.channels[1].enabled = false;
        p.channels[8].rows[0].timing = timing(3, 4);
        // channel 2 is enabled but empty
        let program = p.program();
        assert_eq!(
            program.actions,
            [
                Action { pin: 2, timings: vec![timing(1_000_000, 100_000), timing(5, 6)] },
                Action { pin: 10, timings: vec![timing(3, 4)] },
            ]
        );
    }

    #[test]
    fn reads_a_file_of_the_java_frontend() {
        let text = r#"{
          "description" : null,
          "cycles" : 10,
          "cycleDelay" : 500,
          "actionBeanList" : [ {
            "pinIndex" : 1,
            "activ" : true,
            "description" : "",
            "actionItemList" : [ { "id" : 1, "delay" : 1000000, "release" : 100000, "delayIncrement" : 0, "releaseIncrement" : 250, "ignore" : false } ]
          } ]
        }"#;
        let p = serde_json::from_str::<FileSettings>(text).unwrap().into_project();
        assert_eq!((p.cycles, p.cycle_delay_ms, p.channels.len()), (10, 500, 10));
        assert_eq!(p.channels[0].rows.len(), 5);
        assert_eq!(p.channels[0].rows[0].timing, Timing { delay: 1_000_000, release: 100_000, delay_increment: 0, release_increment: 250 });
    }

    #[test]
    fn save_and_load_roundtrip() {
        let mut p = Project { description: "two\nlines".into(), cycles: 7, cycle_delay_ms: 1500, ..Project::default() };
        p.channels[3].enabled = false;
        p.channels[3].description = "red".into();
        p.channels[9].rows[0] = Row { timing: timing(u32::MAX, 1), ignore: true };
        let text = serde_json::to_string(&FileSettings::from_project(&p)).unwrap();
        assert_eq!(serde_json::from_str::<FileSettings>(&text).unwrap().into_project(), p);
    }
}
