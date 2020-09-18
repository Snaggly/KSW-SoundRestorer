# KSW-SoundRestorer

To restore Mcu's sound output without having to launch the preinstalled media apps. At cold boot the volume is extremely low and has no sound effects. Also whenever you switch to OEM Radio, the sound source get switched to the extra speaker for only Navi-Callouts. When the Navi-Callout occurs it cuts off the sound output entirely, until the next callout!

The idea is to have a service that restores the proper sound whenever needed. The app uses [jSerialComm by Fazecast](https://github.com/Fazecast/jSerialComm) to directly send the required commands to the MCU. This also uses [AdbLib by cgutman](https://github.com/cgutman/AdbLib) to self adquire READ_LOGS permission. The app listens continously to MCU events over Logcat to figure when the user switched sources.

If the SelfADB proceedure fails, you'll have to acquire the permission either via root or manually over adb. Execute the following to grant permission:
> adb pm grant com.snaggly.soundrestorer android.permission.READ_LOGS
