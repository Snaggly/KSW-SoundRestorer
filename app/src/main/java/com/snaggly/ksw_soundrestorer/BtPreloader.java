package com.snaggly.ksw_soundrestorer;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BtPreloader {
    public static void preload(Context context, String targetPackage, int cacheTime){
        try {
            Intent kswBtIntent = context.getPackageManager().getLaunchIntentForPackage(targetPackage);

            kswBtIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(kswBtIntent);
        }
        catch (Exception e){
            Toast.makeText(context, targetPackage + " could not be started.\n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        try {
            Thread.sleep(cacheTime);

            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            home.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(home);
        }
        catch (Exception e){
            Toast.makeText(context, "Home could not be started.\n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
