//! Application configuration: what the frontend remembers between two runs.
//! Loaded on start and saved on exit as `~/.snatchfreezer/config.json`.

use std::path::{Path, PathBuf};

use serde::{Deserialize, Serialize};

/// Smallest / largest window size that is restored, in unscaled pixels.
const WINDOW_MIN: (u32, u32) = (320, 240);
const WINDOW_MAX: u32 = 16_384;

#[derive(Clone, Debug, Default, PartialEq, Eq, Serialize, Deserialize)]
#[serde(rename_all = "camelCase", default)]
pub struct Config {
    /// The project file that was open.
    pub project: Option<PathBuf>,
    /// The serial port of the last connection.
    pub port: Option<String>,
    /// Window size in unscaled pixels (as at 100 % display scaling).
    pub window: Option<(u32, u32)>,
}

/// The directory of the configuration; also the default one for projects.
pub fn dir() -> PathBuf {
    let home = std::env::var_os("USERPROFILE").or_else(|| std::env::var_os("HOME")).unwrap_or_default();
    let dir = PathBuf::from(home).join(".snatchfreezer");
    let _ = std::fs::create_dir_all(&dir);
    dir
}

impl Config {
    /// A missing or unreadable file gives the defaults.
    pub fn load() -> Config {
        Config::load_from(&dir().join("config.json"))
    }

    pub fn save(&self) -> Result<(), String> {
        self.save_to(&dir().join("config.json"))
    }

    fn load_from(path: &Path) -> Config {
        let config: Config =
            std::fs::read_to_string(path).ok().and_then(|text| serde_json::from_str(&text).ok()).unwrap_or_default();
        Config {
            window: config
                .window
                .map(|(w, h)| (w.clamp(WINDOW_MIN.0, WINDOW_MAX), h.clamp(WINDOW_MIN.1, WINDOW_MAX))),
            ..config
        }
    }

    fn save_to(&self, path: &Path) -> Result<(), String> {
        let text = serde_json::to_string_pretty(self).map_err(|e| e.to_string())?;
        std::fs::write(path, text).map_err(|e| e.to_string())
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    fn temp_file(name: &str) -> PathBuf {
        std::env::temp_dir().join(format!("snatchfreezer-{}-{name}", std::process::id()))
    }

    #[test]
    fn save_and_load_roundtrip() {
        let path = temp_file("roundtrip.json");
        let config =
            Config { project: Some(PathBuf::from("drops.sf")), port: Some("COM3".into()), window: Some((1500, 900)) };
        config.save_to(&path).unwrap();
        assert_eq!(Config::load_from(&path), config);
        let _ = std::fs::remove_file(&path);
    }

    #[test]
    fn missing_or_broken_file_gives_the_defaults() {
        assert_eq!(Config::load_from(&temp_file("missing.json")), Config::default());
        let path = temp_file("broken.json");
        std::fs::write(&path, "{ not json").unwrap();
        assert_eq!(Config::load_from(&path), Config::default());
        let _ = std::fs::remove_file(&path);
    }

    #[test]
    fn unknown_and_missing_fields_are_tolerated_and_the_window_is_clamped() {
        let path = temp_file("partial.json");
        std::fs::write(&path, r#"{ "port": "/dev/ttyUSB0", "window": [1, 99999], "later": true }"#).unwrap();
        let config = Config::load_from(&path);
        assert_eq!(config, Config { project: None, port: Some("/dev/ttyUSB0".into()), window: Some((320, 16_384)) });
        let _ = std::fs::remove_file(&path);
    }
}
