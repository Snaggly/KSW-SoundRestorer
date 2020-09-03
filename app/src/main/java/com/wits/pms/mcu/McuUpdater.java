package com.wits.pms.mcu;

import android.os.RemoteException;
import com.wits.pms.ksw.IMcuUpdate;
import com.wits.pms.ksw.OnMcuUpdateProgressListener;
import com.wits.pms.statuscontrol.PowerManagerApp;

public class McuUpdater {
    private static IMcuUpdate getMcuUpdater() {
        return IMcuUpdate.Stub.asInterface(PowerManagerApp.getService("mcu_update"));
    }

    public static void registerMcuUpdateListener(OnMcuUpdateProgressListener onMcuUpdateProgressListener) throws RemoteException {
        getMcuUpdater().setOnMcuUpdateProgressListener(onMcuUpdateProgressListener);
    }

    public static void mcuUpdate(String str) throws RemoteException {
        getMcuUpdater().mcuUpdate(str);
    }
}
