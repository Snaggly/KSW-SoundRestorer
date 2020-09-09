package com.snaggly.ksw_soundrestorer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.IOException;

public class ActivityService extends Service {
    public ActivityService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        try{
            McuCommandsStore.SetToMusicSource setToMusic = new McuCommandsStore.SetToMusicSource();
            McuCommunicator.getInstance().sendCommand(setToMusic.command, setToMusic.data, setToMusic.loop);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
