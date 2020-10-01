package com.snaggly.ksw_soundrestorer;

import android.annotation.SuppressLint;
import android.content.Context;

import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class McuVoiceSettingsInit {
    @SuppressLint("PrivateApi")
    private final Class<?> PowerManagerApp = new DexClassLoader(
            "/system/app/KswPLauncher/KswPLauncher.apk",
            "/data/tmp/",
            "/data/tmp/",
            ClassLoader.getSystemClassLoader()).loadClass("com.wits.pms.statuscontrol.PowerManagerApp");

    private Method getSettingsInt = PowerManagerApp.getDeclaredMethod("getSettingsInt", String.class);
    private Method setSettingsInt = PowerManagerApp.getDeclaredMethod("setSettingsInt", String.class, int.class);

    private static McuVoiceSettingsInit instance;

    private McuVoiceSettingsInit() throws ClassNotFoundException, NoSuchMethodException {}


    public static int getWitsCommand(String name) {
        try {
            if (instance==null)
                instance = new McuVoiceSettingsInit();

            Object object = instance.getSettingsInt.invoke(null, name);
            return (int)object;
        } catch (final Exception e) {
            return -1;
        }
    }

    public static void setWitsCommand(String name, int value) {
        try {
            if (instance==null)
                instance = new McuVoiceSettingsInit();

            instance.setSettingsInt.invoke(null, name, value);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }


    public static int getANDROID_MEDIA_VOL() {
        return getWitsCommand("Android_media_vol");
    }

    public static int getANDROID_PHONE_VOL() {
        return getWitsCommand("Android_phone_vol");
    }

    public static int getCAR_PHONE_VOL() {
        return getWitsCommand("Car_phone_vol");
    }

    public static int getCAR_NAVI_VOL() {
        return getWitsCommand("Car_navi_vol");
    }

    public static void setANDROID_MEDIA_VOL(int value) {
        setWitsCommand("Android_media_vol", value);
    }

    public static void setANDROID_PHONE_VOL(int value) {
        setWitsCommand("Android_phone_vol", value);
    }

    public static void setCAR_PHONE_VOL(int value) {
        setWitsCommand("Car_phone_vol", value);
    }

    public static void setCAR_NAVI_VOL(int value) {
        setWitsCommand("Car_navi_vol", value);
    }

    public static void reinitAllVol() {
        setANDROID_MEDIA_VOL(getANDROID_MEDIA_VOL());
        setANDROID_PHONE_VOL(getANDROID_PHONE_VOL());
        setCAR_NAVI_VOL(getCAR_NAVI_VOL());
        setCAR_PHONE_VOL(getCAR_PHONE_VOL());
    }

}
