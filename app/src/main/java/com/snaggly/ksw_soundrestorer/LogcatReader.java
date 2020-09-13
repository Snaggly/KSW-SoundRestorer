package com.snaggly.ksw_soundrestorer;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;

public class LogcatReader {
    private Thread readerThread;
    private Process logProc;
    private McuAction callback;
    private boolean isReading = false;

    public LogcatReader(McuAction callbackEvent) {
        this.callback = callbackEvent;
    }

    public void startReading(){
        if (isReading)
            return;
        readerThread = new Thread(() -> {
            try {
                Runtime.getRuntime().exec("logcat -c");
                logProc = Runtime.getRuntime().exec("logcat KswMcuListener:I *:S");
                isReading = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader bufRead = new BufferedReader(new InputStreamReader(logProc.getInputStream()));
            String line = "";
            try{
                while (isReading){
                    try{
                        while (bufRead.ready()){
                            line = bufRead.readLine();
                            if (line.contains("--Mcu toString-----")) {
                                line = line.substring(line.lastIndexOf('[') + 2, line.lastIndexOf(']') - 1);
                                callback.update(line);
                            }
                        }
                        Log.i("KswMcuListener", "End-Of-Line");
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    catch (InterruptedIOException e){
                        break;
                    }
                }

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
        readerThread.start();
    }

    public void stopReading(){
        logProc.destroy();
        isReading = false;
    }
}
