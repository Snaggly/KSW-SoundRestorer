package com.wits.pms.statuscontrol;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wits.pms.ICmdListener;
import com.wits.pms.IContentObserver;
import com.wits.pms.IPowerManagerAppService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PowerManagerApp {
    /* access modifiers changed from: private */
    public static ICmdListener cmdListener;
    private static Context context;
    /* access modifiers changed from: private */
    public static final HashMap<String, IContentObserver> maps = new HashMap<>();

    public static void init(Context context2) {
        context = context2;
        context2.getContentResolver().registerContentObserver(Settings.System.getUriFor("bootTimes"), true, new ContentObserver(new Handler(context2.getMainLooper())) {
            public void onChange(boolean z) {
                super.onChange(z);
                if (PowerManagerApp.cmdListener != null) {
                    PowerManagerApp.registerICmdListener(PowerManagerApp.cmdListener);
                }
                for (String str : PowerManagerApp.maps.keySet()) {
                    PowerManagerApp.registerIContentObserver(str, (IContentObserver) PowerManagerApp.maps.get(str));
                }
            }
        });
    }

    public static IPowerManagerAppService getManager() {
        return IPowerManagerAppService.Stub.asInterface(getService("wits_pms"));
    }

    public static void registerICmdListener(ICmdListener iCmdListener) {
        try {
            cmdListener = iCmdListener;
            if (getManager() != null) {
                getManager().registerCmdListener(iCmdListener);
            }
        } catch (RemoteException unused) {
        }
    }

    public static void registerIContentObserver(String str, IContentObserver iContentObserver) {
        try {
            maps.put(str, iContentObserver);
            if (getManager() != null) {
                getManager().registerObserver(str, iContentObserver);
            }
        } catch (RemoteException unused) {
        }
    }

    public static void unRegisterIContentObserver(IContentObserver iContentObserver) {
        try {
            for (String next : maps.keySet()) {
                if (maps.get(next) == iContentObserver) {
                    maps.remove(next, iContentObserver);
                }
            }
            if (getManager() != null) {
                getManager().unregisterObserver(iContentObserver);
            }
        } catch (RemoteException unused) {
        }
    }

    @SuppressLint({"PrivateApi"})
    public static IBinder getService(String str) {
        try {
            Class<?> cls = Class.forName("android.os.ServiceManager");
            return (IBinder) cls.getMethod("getService", new Class[]{String.class}).invoke(cls, new Object[]{str});
        } catch (Exception e) {
            String name = PowerManagerApp.class.getName();
            Log.e(name, "error service init - " + str, e);
            return null;
        }
    }

    public static boolean sendCommand(String str) {
        try {
            return getManager().sendCommand(str);
        } catch (RemoteException e) {
            Log.i(getManager().getClass().getName(), "error sendCommand", e);
            return false;
        }
    }

    public static void sendStatus(WitsStatus witsStatus) {
        try {
            getManager().sendStatus(new Gson().toJson((Object) witsStatus));
        } catch (RemoteException unused) {
        }
    }

    public static List<String> getDataListFromJsonKey(String str) {
        return (List) new Gson().fromJson(Settings.System.getString(context.getContentResolver(), str), new TypeToken<ArrayList<String>>() {
        }.getType());
    }

    public static boolean getStatusBoolean(String str) throws RemoteException {
        return getManager().getStatusBoolean(str);
    }

    public static String getStatusString(String str) throws RemoteException {
        return getManager().getStatusString(str);
    }

    public static int getStatusInt(String str) throws RemoteException {
        return getManager().getStatusInt(str);
    }

    public static int getSettingsInt(String str) throws RemoteException {
        return getManager().getSettingsInt(str);
    }

    public static String getSettingsString(String str) throws RemoteException {
        return getManager().getSettingsString(str);
    }

    public static void setSettingsInt(String str, int i) throws RemoteException {
        getManager().setSettingsInt(str, i);
    }

    public static void setSettingsString(String str, String str2) throws RemoteException {
        getManager().setSettingsString(str, str2);
    }
}
