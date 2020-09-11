package com.snaggly.ksw_soundrestorer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver implements McuAction {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            McuCommunicator.makeAndGetInstance(this).sendCommand(103, new byte[]{1}, false);
        }
    }

    @Override
    public void update(int cmdType, byte[] data) {

    }

    @Override
    public void update(String logcatMessage) {

    }
}