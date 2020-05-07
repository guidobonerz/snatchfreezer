# SnatchFreezer (Beta)
SnatchFreezer is a project for Highspeed photography

Runs on Windows, Mac and Linux

This project also contains all required software and PCB layouts including:
- JavaFX - Controller Frontend
- Arduino Sketch
- Eagle PCB

Current Features:
- Connect up to six 12V driven valves or similar devices directly with 5 timing entries each.
- Connect up to two camera and/or flashes.

- Load and Save your settings
- Test all channels separately or altogether for 100ms.
- Toggle flushing all or single siphons from UI.
- Set amount of cycle loops
- Set delay time after each cycle loop.
- Describe each channel and/or the whole project
- Presets for TaT and Crown
- View log protocol
- Minimum valve delay/release time combination: 0,015 s -> 15 ms -> 15000 us / 0,004 s -> 4 ms -> 4000 us

Known Bugs:
- Data transfer sometimes hangs


Future plans:
- WLAN and/or Bluetooth Module
- Rest Api
- Android Client


# Build Java Application
mvn jfx:jar


# Download latest Frontend
[Snathfreezer Frontend](https://github.com/guidobonerz/snatchfreezer/blob/develop/download/snatchfreezer.zip)

## Shot 1
![shot1](https://github.com/guidobonerz/snatchfreezer/blob/develop/docs/DSC0114.png)
## Shot 2
![shot2](https://github.com/guidobonerz/snatchfreezer/blob/develop/docs/DSC0147.png)

## Controller View
![screenshot1](https://github.com/guidobonerz/snatchfreezer/blob/develop/docs/ControllerView.png)

## PCB
![screenshot1](https://github.com/guidobonerz/snatchfreezer/blob/develop/docs/pcb.jpg)

## Frame
![screenshot1](https://github.com/guidobonerz/snatchfreezer/blob/develop/docs/frame.jpg)

## Siphons
![screenshot1](https://github.com/guidobonerz/snatchfreezer/blob/develop/docs/siphons.jpg)

## Valves
![screenshot1](https://github.com/guidobonerz/snatchfreezer/blob/develop/docs/valves.jpg)
