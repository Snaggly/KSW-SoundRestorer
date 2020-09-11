package com.snaggly.ksw_soundrestorer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
            while (true){
                try {
                    line = bufRead.readLine();
                    if (line.contains("--Mcu toString-----")){
                        line.substring(line.lastIndexOf('[' + 1), line.lastIndexOf(']' - 1));
                        callback.update(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        readerThread.start();
    }

    public void stopReading(){
        logProc.destroy();
        readerThread.interrupt();
        isReading = false;
    }
}
