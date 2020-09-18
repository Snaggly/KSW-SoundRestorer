package com.snaggly.ksw_soundrestorer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {
    private ListView listMcu;
    private ArrayAdapter<String> mcuEventAdapter;
    private ArrayList<String> mcuEvents = new ArrayList<String>();
    private TextInputLayout textCmd;
    private TextInputLayout textBytes;
    public static TestActivity instance;
    private Intent activityService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        instance = this;

        SwitchCompat startOnBootSwitch = findViewById(R.id.startOnBootSw);
        startOnBootSwitch.setChecked(Preferences.GET_START_ON_BOOT(this));
        startOnBootSwitch.setOnCheckedChangeListener((compoundButton, b) -> Preferences.SET_START_ON_BOOT(b, this));

        TextView runningLabel = findViewById(R.id.isServiceRunningText);
        if (!ActivityService.isRunning)
            runningLabel.setText(R.string.service_stopped);
        textCmd = findViewById(R.id.texfieldCmd);
        textBytes = findViewById(R.id.texfieldBytes);
        textCmd.getEditText().setText("103");
        textBytes.getEditText().setText("13");
        activityService = new Intent(this, ActivityService.class);

        listMcu = findViewById(R.id.mcuListView);
        mcuEventAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, mcuEvents);
        listMcu.setAdapter(mcuEventAdapter);

        findViewById(R.id.serviceStartButton).setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(activityService);
            }
            Toast.makeText(view.getContext(), "Service starting...", Toast.LENGTH_SHORT).show();
            runningLabel.setText(R.string.service_running);

        });
        findViewById(R.id.stopServiceBtn).setOnClickListener(view -> {
            stopService(activityService);
            Toast.makeText(view.getContext(), "Service stopping...", Toast.LENGTH_SHORT).show();
            runningLabel.setText(R.string.service_stopped);

        });

        findViewById(R.id.sendCommandBtn).setOnClickListener(view -> {
            if (McuCommunicator.getInstance() != null) {
                String[] bytesStr = textBytes.getEditText().getText().toString().split(",");
                byte[] data = new byte[bytesStr.length];
                for (int i=0; i<data.length; i++){
                    data[i] = Byte.parseByte(bytesStr[i]);
                }
                try {
                    McuCommunicator.getInstance().sendCommand(Integer.parseInt(textCmd.getEditText().getText().toString()), data, false);
                    Toast.makeText(view.getContext(), "Sent command.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), "Error in sending!\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(view.getContext(), "Service not running.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addNewItemToList(String item){
        mcuEvents.add(0, item);
        runOnUiThread(() -> mcuEventAdapter.notifyDataSetChanged());
    }
}