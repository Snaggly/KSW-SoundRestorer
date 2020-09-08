# KSW-SoundRestorer

To restore Ksw's sounddriver without having to launch the preinstalled media apps. The driver doesn't start at cold boot, also get suspended when you exit the Android side back to your HU. Going back to Android without restarting driver can have the effect that Navi' commands get rerouted to the addittional speaker. 

The idea is to have a service that restarts the driver when needed.

Due to limitations, it can not detect if the driver switched back to Android. Therefore the user has to manually restart the app.
