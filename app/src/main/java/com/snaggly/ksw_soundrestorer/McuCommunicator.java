package com.snaggly.ksw_soundrestorer;

import android.annotation.SuppressLint;

/*import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import dalvik.system.DexClassLoader;

public class McuCommunicator /*implements SerialPortDataListener*/{
    private static McuCommunicator instance;
    private McuAction handler;
    //private SerialPort serial;
    private LogcatReader readerThread;
    private byte[] frame;

    private McuCommunicator() /*throws SerialPortInvalidPortException*/{
        /*try{
            serial = SerialPort.getCommPorts()[0];
        }
        catch (Exception e){
            throw new SerialPortInvalidPortException("No serial ports!");
        }
        serial.setBaudRate(115200);
        serial.addDataListener(this);
        serial.openPort();*/
        //hijackKSW();
    }

    public static McuCommunicator getInstance() {
        if (instance == null)
            instance = new McuCommunicator();

        return instance;
    }

    public McuCommunicator startReading(McuAction handler){
        if (handler != null){
            this.handler = handler;
            readerThread = new LogcatReader(handler);
            readerThread.startReading();
        }
        return this;
    }

    public McuCommunicator stopReading(){
        if (handler != null && readerThread != null){
            readerThread.stopReading();
        }
        return this;
    }

    public void sendCommand(int cmdType, byte[] data, boolean update) throws /*SerialPortInvalidPortException,*/ IOException, InvocationTargetException, IllegalAccessException {
        /*if (serial.isOpen()){
            byte[] frame = KSWobtain(cmdType, data, false);
            serial.writeBytes(frame, frame.length);
        } else throw new SerialPortInvalidPortException("Port not open!");*/
        sendCommandViaShell(KSWobtain(cmdType, data, update));
        //hijackedKSWMcuSender.invoke(null, cmdType, data);
    }

    public void sendCommand(McuCommands mcuCommands) throws /*SerialPortInvalidPortException,*/ IOException, InvocationTargetException, IllegalAccessException {
        sendCommand(mcuCommands.getCommand(), mcuCommands.getData(), mcuCommands.getUpdate());
    }

    public void killCommunicator(){
        /*try{
            serial.removeDataListener();
            serial.closePort();
        }
        catch(Exception e){
            e.printStackTrace();
        }*/
        stopReading();
        instance = null;
    }

    private int checkSum(){
        int sum = 0;
        for (byte b = 1; b<frame.length-1; b++)
            sum += frame[b];

        return 0xFF - sum;
    }

    public byte[] KSWobtain(int cmdType, byte[] data, boolean update) {
        frame = new byte[(data.length + 5)];
        frame[0] = (byte) 242;
        frame[1] = update ? (byte)160 : 0;
        frame[2] = (byte) cmdType;
        frame[3] = (byte) data.length;
        System.arraycopy(data, 0, frame, 4, data.length);
        frame[frame.length-1] = (byte)checkSum();

        return frame;
    }

    private void sendCommandViaShell(byte[] frame) throws IOException {
        FileOutputStream fos = new FileOutputStream("/dev/ttyMSM1");
        fos.write(frame);
        fos.close();
    }

    /*private void readMcuViaShell() {
        FileInputStream fis = new FileInputStream("/dev/ttyMSM1");
        new Thread(() -> {
            while(isReading.get()){
                ArrayList<Byte> bytes = new ArrayList<>();
                while(fis.available()>0){
                    bytes.add(fis.read());
                }
                notifyObservers(bytes.toArray());
            }
        }).start();
    }*/

    /*@Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        return;
    }*/

    private Method hijackedKSWMcuSender;
    private boolean hijackKSW() {
        try {
            @SuppressLint("PrivateApi")
            final Class<?> cls = new DexClassLoader (
                    "/system/priv-app/CenterService/CenterService.apk",
                    "/data/tmp/",
                    "/data/tmp/",
                    ClassLoader.getSystemClassLoader()).loadClass("com.wits.pms.mcu.custom.KswMcuSender");
            hijackedKSWMcuSender = cls.getDeclaredMethod("sendMessage", int.class, byte[].class);
            return true;
        } catch (final Exception e) {
            return false;
        }
    }
}
