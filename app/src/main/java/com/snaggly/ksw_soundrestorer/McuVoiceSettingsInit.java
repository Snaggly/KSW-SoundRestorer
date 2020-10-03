package com.snaggly.ksw_soundrestorer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

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

    @SuppressLint("PrivateApi")
    private static IBinder getService(String serviceName) {
        try {
            Class<?> serviceManager = Class.forName("android.os.ServiceManager");
            return (IBinder) serviceManager.getMethod("getService", new Class[]{String.class}).invoke(serviceManager, new Object[]{serviceName});
        } catch (Exception e) {
            return null;
        }
    }
    public static int getSettingsInt(String key) throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken("com.wits.pms.IPowerManagerAppService");
            _data.writeString(key);
            getService("wits_pms").transact(10, _data, _reply, 0);
            _reply.readException();
            return _reply.readInt();
        }
        finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public static void setSettingsInt(String key, int value) throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken("com.wits.pms.IPowerManagerAppService");
            _data.writeString(key);
            _data.writeInt(value);
            getService("wits_pms").transact(12, _data, _reply, 0);
            _reply.readException();
        }
        finally {
            _reply.recycle();
            _data.recycle();
        }
    }

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
