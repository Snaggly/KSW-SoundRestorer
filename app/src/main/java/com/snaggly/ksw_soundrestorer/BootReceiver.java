package com.snaggly.ksw_soundrestorer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            try {
                McuCommunicator.getInstance().sendCommand(McuCommands.SET_TO_ATSL_AIRCONSOLE);
            } catch (Exception e) {
                Toast.makeText(context, "BootReceiver\nFailed to send command", Toast.LENGTH_LONG);
            }

            if (Preferences.GET_START_ON_BOOT(context)){
                try {
                    Intent testingActivity = context.getPackageManager().getLaunchIntentForPackage(BuildConfig.APPLICATION_ID);
                    testingActivity.putExtra("startService", true);
                    testingActivity.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    context.startActivity(testingActivity);
                }
                catch (Exception e){
                    Toast.makeText(context, "TestActivity could not be started.\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}