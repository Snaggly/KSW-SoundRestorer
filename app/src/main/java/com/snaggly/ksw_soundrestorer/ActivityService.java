package com.snaggly.ksw_soundrestorer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ActivityService extends Service implements McuAction {
    public static boolean isRunning = false;
    private AudioManager am;
    private KeyEvent playEvent;
    private KeyEvent pauseEvent;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID;
        String channelName = "McuListenerService";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLACK);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Service running in background...")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public void onCreate() {
        try{
            am = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
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
            Log.d(BuildConfig.TAG, "Started Service...");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startMyOwnForeground();
            }
        }
        catch (Exception e){
            Log.d(BuildConfig.TAG, e.getMessage());
        }

        am.dispatchMediaKeyEvent(pauseEvent);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        am.dispatchMediaKeyEvent(playEvent);

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (McuCommunicator.getInstance()!=null)
            McuCommunicator.getInstance().killCommunicator();
        Log.d(BuildConfig.TAG, "Stopped Service...");
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
            if (am.isMusicActive()){
                am.dispatchMediaKeyEvent(pauseEvent);
                am.dispatchMediaKeyEvent(playEvent);
            }

        } else if (McuEvent.SWITCHED_TO_OEM.equals(logcatMessage)){
            McuCommunicator.getInstance().sendCommand(McuCommands.SET_TO_MUSIC_SOURCE);
            if (am.isMusicActive()){
                am.dispatchMediaKeyEvent(pauseEvent);
                am.dispatchMediaKeyEvent(playEvent);
            }
        }
    }
}
