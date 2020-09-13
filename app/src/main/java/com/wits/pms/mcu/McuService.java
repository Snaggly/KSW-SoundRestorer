package com.wits.pms.mcu;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.wits.pms.mcu.custom.KswMcuSender;
import com.wits.pms.mcu.custom.KswMessage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class McuService extends Service implements McuSender {
    private static final boolean Debug = false;
    private static final String TAG = "McuService-SnagglySR";
    private static OnReceiveData listener;
    public static Context mContext;
    private static ReadThread mReadThread;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public InputStream mInputStream;
    protected OutputStream mOutputStream;
    SerialPort mSerialPort;
    private byte[] openSourceMode3 = {-14, 0, 103, 1, 3, -108};

    public interface OnReceiveData {
        void onReceiveMcu(byte[] bArr);

        void reset();
    }

    /* access modifiers changed from: protected */
    public void onDataReceived(byte[] buffer) {
        if (listener != null) {
            listener.onReceiveMcu(buffer);
        }
    }

    public void onCreate() {
        try {
            this.mHandler = new Handler(getMainLooper());
            this.mSerialPort = new SerialPort(new File("/dev/ttyMSM1"), 115200, 0);
            //Log.d(TAG, "open port\t" + this.mSerialPort);
            this.mOutputStream = this.mSerialPort.getOutputStream();
            this.mInputStream = this.mSerialPort.getInputStream();
            new Thread(() -> {
                while(true){
                    try {
                        Thread.sleep(500);
                        if (McuService.this.mOutputStream != null) {
                            try {
                                McuService.this.mOutputStream.write(openSourceMode3);
                            } catch (IOException e) {
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            /*mReadThread = new ReadThread();
            mReadThread.start();*/
        }
        catch (SecurityException e){
            Toast.makeText(this, "Exception in McuService onCreate\nSecurityException\n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch (IOException e){
            Toast.makeText(this, "Exception in McuService onCreate\nIOException\n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(this, "Exception in McuService onCreate\n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this, "Created MCUService", Toast.LENGTH_LONG).show();
        //KswMcuSender.init(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;
        return super.onStartCommand(intent, flags, startId);
        //return Service.START_NOT_STICKY;
    }

    private class ReadThread extends Thread {
        public final Object portLock;

        private ReadThread() {
            this.portLock = new Object();
        }

        public void write(final byte[] buf) {
            new Thread() {
                public void run() {
                    if (McuService.this.mOutputStream != null) {
                        try {
                            McuService.this.mOutputStream.write(buf);
                        } catch (IOException e) {
                        }
                    }
                }
            }.start();
        }

        public void run() {
            while (McuService.this.mInputStream != null) {
                try {
                    byte[] buffer = new byte[128];
                    int size = McuService.this.mInputStream.read(buffer);
                    byte[] data = new byte[size];
                    if (size > 0) {
                        System.arraycopy(buffer, 0, data, 0, size);
                        synchronized (this.portLock) {
                            McuService.this.onDataReceived(data);
                        }
                    }
                } catch (IOException e) {
                    return;
                }
            }
        }
    }

    public void send(McuMessage msg) {
        if (mReadThread != null && msg != null) {
            mReadThread.write(msg.outData);
        }
    }

    public void send(byte[] pack) {
    }

    public static void printHex(String method, byte[] data) {
        StringBuilder sb = new StringBuilder();
        sb.append(method);
        sb.append("-----[");
        for (byte b : data) {
            String hex = Integer.toHexString(b & 255);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append("0x");
            sb.append(hex.toUpperCase());
            //sb.append(SmsManager.REGEX_PREFIX_DELIMITER);
        }
        sb.replace(sb.length() - 1, sb.length(), "");
        sb.append("]\n");
        Log.v(TAG, sb.toString());
    }

    public static void printHex(String method, McuMessage msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(method);
        sb.append("-----[");
        for (byte b : msg.getData()) {
            String hex = Integer.toHexString(b & 255);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append("0x");
            sb.append(hex.toUpperCase());
            //sb.append(SmsManager.REGEX_PREFIX_DELIMITER);
        }
        sb.replace(sb.length() - 1, sb.length(), "");
        sb.append("]\n");
        Log.v(TAG, sb.toString());
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "close port");
        if (mReadThread != null) {
            mReadThread.interrupt();
        }
        if (this.mSerialPort != null) {
            this.mSerialPort.close();
            this.mSerialPort = null;
        }
        this.mSerialPort = null;
        Toast.makeText(this, "Destroyed MCUService", Toast.LENGTH_LONG).show();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
