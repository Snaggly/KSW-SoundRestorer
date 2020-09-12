package com.snaggly.ksw_soundrestorer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver implements McuAction {
    McuCommunicator communicator;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            communicator = McuCommunicator.makeAndGetInstance(this);
            communicator.sendCommand(McuCommands.SET_TO_MUSIC_SOURCE);
        }
    }

    @Override
    public void update(int cmdType, byte[] data) {

    }

    @Override
    public void update(String logcatMessage) {

    }
}