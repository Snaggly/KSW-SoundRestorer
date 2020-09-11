package com.snaggly.ksw_soundrestorer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.IOException;

public class ActivityService extends Service {
    public static boolean isRunning = false;
    private McuAction action = new McuAction();
    public ActivityService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            McuCommunicator.makeAndGetInstance(action);
        } catch (Exception e) {
            e.printStackTrace();
        }
        isRunning = true;
    }

    @Override
    public void onDestroy() {
        McuCommunicator.getInstance().killCommunicator();
        super.onDestroy();
        isRunning = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class McuAction implements IMcuAction{

        @Override
        public void update(int cmdType, byte[] data) {
            if (TestActivity.instance !=null){
                String dataStr = "";
                for (byte b = 4; b<data.length-1; b++)
                    dataStr+=Integer.toHexString(b) +" ";
                TestActivity.instance.addNewItemToList("Command: " + cmdType + "\n Data: { " + dataStr +"}");
            }
        }
    }
}
