package com.snaggly.ksw_soundrestorer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class ActivityService extends Service implements McuAction {
    public static boolean isRunning = false;
    private AudioManager am;
    private KeyEvent playEvent;
    private KeyEvent pauseEvent;

    @Override
    public void onCreate() {
        try{
            am = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            playEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY);
            pauseEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE);

            try{
                if ((McuCommunicator.makeAndGetInstance()).startReading(this) != null)
                    isRunning = true;
                //McuCommunicator.getInstance().sendCommand(McuCommands.SET_TO_MUSIC_SOURCE);
            }
            catch (Exception e){
                Toast.makeText(this, "Failed to set up Serial connection to MCU!", Toast.LENGTH_LONG).show();
                stopSelf();
            }
            Log.d("Ksw-SoundRestorer", "Started Service...");
        }
        catch (Exception e){
            Log.d("Ksw-SoundRestorer", e.getMessage());
        }

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (McuCommunicator.getInstance()!=null)
            McuCommunicator.getInstance().killCommunicator();
        Log.d("Ksw-SoundRestorer", "Stopped Service...");
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
        if (TestActivity.instance != null)
            TestActivity.instance.addNewItemToList(logcatMessage);

        if (McuEvent.SWITCHED_TO_OEM.equals(logcatMessage)){
            McuCommunicator.getInstance().sendCommand(McuCommands.SET_TO_ATSL_AIRCONSOLE);
            am.dispatchMediaKeyEvent(pauseEvent);
            am.dispatchMediaKeyEvent(playEvent);

        } else if (McuEvent.SWITCHED_TO_OEM.equals(logcatMessage)){
            McuCommunicator.getInstance().sendCommand(McuCommands.SET_TO_MUSIC_SOURCE);
            am.dispatchMediaKeyEvent(pauseEvent);
            am.dispatchMediaKeyEvent(playEvent);
        }
    }
}
