package com.snaggly.ksw_soundrestorer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ActivityService extends Service implements McuAction {
    public static boolean isRunning = false;
    public ActivityService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            McuCommunicator.makeAndGetInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        isRunning = true;
    }

    @Override
    public void onDestroy() {
        try {
            McuCommunicator.getInstance().killCommunicator();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
        isRunning = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void update(int cmdType, byte[] data) {
        if (TestActivity.instance !=null){
            String dataStr = "";
            for (byte b = 4; b<data.length-1; b++)
                dataStr+=Integer.toHexString(b) +" ";
            TestActivity.instance.addNewItemToList("Command: " + cmdType + "\n Data: { " + dataStr +"}");
        }
    }

    @Override
    public void update(String logcatMessage) {
        TestActivity.instance.addNewItemToList(logcatMessage);
    }
}
