//! System clipboard for the text fields. minifb has no clipboard API, so on
//! Windows the Win32 one is used; elsewhere copy and paste stay inside the
//! application.

#[cfg(windows)]
mod imp {
    use std::ffi::c_void;
    use std::ptr::null_mut;

    #[link(name = "user32")]
    extern "system" {
        fn OpenClipboard(owner: *mut c_void) -> i32;
        fn CloseClipboard() -> i32;
        fn EmptyClipboard() -> i32;
        fn GetClipboardData(format: u32) -> *mut c_void;
        fn SetClipboardData(format: u32, mem: *mut c_void) -> *mut c_void;
    }
    #[link(name = "kernel32")]
    extern "system" {
        fn GlobalAlloc(flags: u32, bytes: usize) -> *mut c_void;
        fn GlobalLock(mem: *mut c_void) -> *mut c_void;
        fn GlobalUnlock(mem: *mut c_void) -> i32;
        fn GlobalFree(mem: *mut c_void) -> *mut c_void;
    }
    const CF_UNICODETEXT: u32 = 13;
    const GMEM_MOVEABLE: u32 = 2;

    pub fn get() -> Option<String> {
        // SAFETY: the handle is only read while locked and the clipboard is
        // open; CF_UNICODETEXT data is a NUL-terminated UTF-16 string.
        unsafe {
            if OpenClipboard(null_mut()) == 0 {
                return None;
            }
            let mut text = None;
            let mem = GetClipboardData(CF_UNICODETEXT);
            if !mem.is_null() {
                let p = GlobalLock(mem) as *const u16;
                if !p.is_null() {
                    let len = (0..).take_while(|&i| *p.add(i) != 0).count();
                    text = Some(String::from_utf16_lossy(std::slice::from_raw_parts(p, len)));
                    GlobalUnlock(mem);
                }
            }
            CloseClipboard();
            text
        }
    }

    pub fn set(text: &str) {
        let wide: Vec<u16> = text.replace('\n', "\r\n").encode_utf16().chain([0]).collect();
        // SAFETY: the block is large enough for `wide`; the clipboard owns it
        // once SetClipboardData succeeds, otherwise it is freed here.
        unsafe {
            if OpenClipboard(null_mut()) == 0 {
                return;
            }
            EmptyClipboard();
            let mem = GlobalAlloc(GMEM_MOVEABLE, wide.len() * 2);
            if !mem.is_null() {
                let p = GlobalLock(mem) as *mut u16;
                if p.is_null() {
                    GlobalFree(mem);
                } else {
                    std::ptr::copy_nonoverlapping(wide.as_ptr(), p, wide.len());
                    GlobalUnlock(mem);
                    if SetClipboardData(CF_UNICODETEXT, mem).is_null() {
                        GlobalFree(mem);
                    }
                }
            }
            CloseClipboard();
        }
    }
}

#[cfg(not(windows))]
mod imp {
    pub fn get() -> Option<String> {
        None
    }
    pub fn set(_: &str) {}
}

pub use imp::{get, set};
