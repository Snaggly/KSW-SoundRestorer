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


    public static int getANDROID_MEDIA_VOL() {
        try {
            if (instance==null)
                instance = new McuVoiceSettingsInit();

            Object object = instance.getSettingsInt.invoke(null, "Android_media_vol");
            return (int)object;
        } catch (final Exception e) {
            return -1;
        }
    }

    public static int getANDROID_PHONE_VOL() {
        try {
            if (instance==null)
                instance = new McuVoiceSettingsInit();

            Object object = instance.getSettingsInt.invoke(null, "Android_phone_vol");
            return (int)object;
        } catch (final Exception e) {
            return -1;
        }
    }

    public static int getCAR_PHONE_VOL() {
        try {
            if (instance==null)
                instance = new McuVoiceSettingsInit();

            Object object = instance.getSettingsInt.invoke(null, "Car_phone_vol");
            return (int)object;
        } catch (final Exception e) {
            return -1;
        }
    }



    public static int getCAR_NAVI_VOL() {
        try {
            if (instance==null)
                instance = new McuVoiceSettingsInit();

            Object object = instance.getSettingsInt.invoke(null, "Car_navi_vol");
            return (int)object;
        } catch (final Exception e) {
            return -1;
        }
    }

    public static void setANDROID_MEDIA_VOL(int value) {
        try {
            if (instance==null)
                instance = new McuVoiceSettingsInit();

            instance.setSettingsInt.invoke(null, "Android_media_vol", value);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void setANDROID_PHONE_VOL(int value) {
        try {
            if (instance==null)
                instance = new McuVoiceSettingsInit();

            instance.setSettingsInt.invoke(null, "Android_phone_vol", value);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void setCAR_PHONE_VOL(int value) {
        try {
            if (instance==null)
                instance = new McuVoiceSettingsInit();

            instance.setSettingsInt.invoke(null, "Car_phone_vol", value);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void setCAR_NAVI_VOL(int value) {
        try {
            if (instance==null)
                instance = new McuVoiceSettingsInit();

            instance.setSettingsInt.invoke(null, "Car_navi_vol", value);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void reinitAllVol() {
        setANDROID_MEDIA_VOL(getANDROID_MEDIA_VOL());
        setANDROID_PHONE_VOL(getANDROID_PHONE_VOL());
        setCAR_NAVI_VOL(getCAR_NAVI_VOL());
        setCAR_PHONE_VOL(getCAR_PHONE_VOL());
    }

}
