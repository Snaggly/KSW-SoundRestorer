package com.snaggly.ksw_soundrestorer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    final public static String TAG = "KSW-SoundRestorer";
    final public String reqPermission = "android.permission.READ_LOGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())), 5469);
        }

        else if(!checkPermission() && !attemptAdb()){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Failed to get permissions!\nYou will have to manually grant READ_LOGS permission to this app!");
            dlgAlert.setTitle(TAG);
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.setPositiveButton("Ok", (dialog, which) -> startTest());
            dlgAlert.create().show();
        }
        else startTest();
    }

    private void startTest(){
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startService(new Intent(this, ActivityService.class));
            }
            startActivity(new Intent(this, TestActivity.class));
            Intent testingActivity = new Intent();
            testingActivity.setComponent(new ComponentName(BuildConfig.APPLICATION_ID, TestActivity.class.getName()));
            testingActivity.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            this.startActivity(testingActivity);

            Thread.sleep(600);

            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            home.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            this.startActivity(home);
        }
        catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public boolean checkPermission() {
        return this.checkPermission(reqPermission, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean attemptAdb() {
        try {
            PmAdbManager.tryGrantingPermissionOverAdb(getFilesDir(), "READ_LOGS");
            return checkPermission();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
            return false;
        }
    }
}