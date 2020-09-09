package com.snaggly.ksw_soundrestorer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Interpolator;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wits.pms.mcu.McuService;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    final public static String targetPackage = "com.wits.ksw.bt";
    final public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())), ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
        }

        try{
           Thread.sleep(500);
           startService(new Intent(this, ActivityService.class));
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        //Toast.makeText(getApplicationContext(), "Executed!\n" + getPackageName(), Toast.LENGTH_LONG).show();
        //finish();
    }
}