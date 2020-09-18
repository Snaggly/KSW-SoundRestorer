package com.snaggly.ksw_soundrestorer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    final public static String TAG = "KSW-SoundRestorer";
    final public String reqPermission = "android.permission.READ_LOGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            McuCommunicator.getInstance().sendCommand(McuCommands.SET_TO_ATSL_AIRCONSOLE);
        } catch (Exception e) {
            Toast.makeText(this, "MainActivity\nFailed to send command\n" + e.getMessage(), Toast.LENGTH_LONG);
        }
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())), 5469);
        }
        else if(!checkPermission()){
            if (!attemptAdb()){
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Failed to get permissions!\nYou will have to manually grant READ_LOGS permission to this app!");
                dlgAlert.setTitle(TAG);
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.setPositiveButton("Ok", (dialog, which) -> finish());
                dlgAlert.create().show();
            }
            else {
                Toast.makeText(this, "Acquired READ_LOGS permission..", Toast.LENGTH_SHORT);
                finish();
            }
        } else if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("startService"))
            startService();
        else
            startTest();
    }

    private void startService(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, ActivityService.class));
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        home.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        this.startActivity(home);
    }

    private void startTest(){
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(this, ActivityService.class));
            }
            startActivity(new Intent(this, TestActivity.class));
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
            PmAdbManager.tryGrantingPermissionOverAdb(getFilesDir(), reqPermission);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}