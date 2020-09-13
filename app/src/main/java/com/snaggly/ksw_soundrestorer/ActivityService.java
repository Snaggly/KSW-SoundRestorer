package com.snaggly.ksw_soundrestorer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ActivityService extends Service implements McuAction {
    public static boolean isRunning = false;

    @Override
    public void onCreate() {
        try{
            if ((McuCommunicator.makeAndGetInstance()).startReading(this) != null)
                isRunning = true;
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Failed to set up Serial connection to MCU!", Toast.LENGTH_LONG).show();
            stopSelf();
        }

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (McuCommunicator.getInstance()!=null)
            McuCommunicator.getInstance().killCommunicator();
        isRunning = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
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
