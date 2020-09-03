package com.snaggly.ksw_soundrestorer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Interpolator;
import android.media.AudioManager;
import android.media.Session2Command;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    final private String targetPackage = "com.wits.ksw.bt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bigButt = findViewById(R.id.button);
        bigButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread t1 = new Thread(() -> {
                        try {
                            Thread.sleep(600);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                    t1.start();

                    Intent kswBtIntent = getPackageManager().getLaunchIntentForPackage(targetPackage);
                    kswBtIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(kswBtIntent);
                    t1.join();

                    Intent home = new Intent(Intent.ACTION_MAIN);
                    home.addCategory(Intent.CATEGORY_HOME);
                    startActivity(home);

                    finishAndRemoveTask();
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), targetPackage + " could not be started.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}