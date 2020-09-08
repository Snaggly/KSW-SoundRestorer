package com.wits.pms.custom;

import android.os.RemoteException;
import android.util.Log;
import com.wits.pms.ksw.IMcuUpdate;
import com.wits.pms.ksw.OnMcuUpdateProgressListener;
import com.wits.pms.mcu.custom.utils.UpdateHelper;
import java.io.File;

public class McuUpdateService extends IMcuUpdate.Stub {
    public void mcuUpdate(String path) throws RemoteException {
        try {
            UpdateHelper.getInstance().sendUpdateMessage(new File(path));
        } catch (Exception e) {
            Log.e(McuUpdateService.class.getName(), "Mcu Update Error", e);
        }
    }

    public void setOnMcuUpdateProgressListener(final OnMcuUpdateProgressListener listener) throws RemoteException {
        UpdateHelper.getInstance().setListener(new UpdateHelper.McuUpdateListener() {
            public void success() {
                try {
                    listener.success();
                } catch (RemoteException e) {
                }
            }

            public void failed(int errorCode) {
                try {
                    listener.failed(errorCode);
                } catch (RemoteException e) {
                }
            }

            public void progress(float pg) {
                try {
                    listener.progress((int) pg);
                } catch (RemoteException e) {
                }
            }
        });
    }
}
