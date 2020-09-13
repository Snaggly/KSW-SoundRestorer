package com.snaggly.ksw_soundrestorer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Process;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    final public static String TAG = "KSW-SoundRestorer";
    final public String reqPermission = "android.permission.READ_LOGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!checkPermission()){
            if (!attemptRoot()){
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Failed to get permissions!\nIf your device is not rooted, manually permit READ_LOGS permission via adb!");
                dlgAlert.setTitle(TAG);
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.setPositiveButton("Ok", (dialog, which) -> { startTest(); });
                dlgAlert.create().show();
            }
            else {
                startTest();
            }
        }
        else startTest();
    }

    private void startTest(){
        try{
            startActivity(new Intent(this, TestActivity.class));
        }
        catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public boolean checkPermission() {
        return this.checkPermission(reqPermission, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean attemptRoot() {
        try {
            java.lang.Process su = Runtime.getRuntime().exec("su");
            DataOutputStream dosSU = new DataOutputStream(su.getOutputStream());
            dosSU.writeBytes("pm grant " + BuildConfig.APPLICATION_ID + " android.permission.READ_LOGS\n");
            dosSU.flush();
            dosSU.writeBytes("exit\n");
            dosSU.close();
            su.waitFor();
            return checkPermission();
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}