package com.snaggly.ksw_soundrestorer;

import android.util.Log;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;

public class McuCommunicator implements SerialPortDataListener{
    private static McuCommunicator instance;
    private IMcuAction handler;
    private SerialPort serial;
    private Thread readerThread;
    private InputStream serialReader;
    private OutputStream serialWriter;
    private int cmdType;
    private boolean update;
    private byte[] frame;

    private Process executeShell(String cmd) throws IOException {
        return Runtime.getRuntime().exec(cmd);
    }

    private McuCommunicator(IMcuAction handler) {
        this.handler = handler;
        serial = SerialPort.getCommPorts()[0];
        serial.setBaudRate(115200);
        serial.addDataListener(this);
        serial.openPort();
        readerThread = new Thread(() -> {
           while(true){
               try {
                   int availTest = serial.bytesAvailable();
                   if (availTest > 5){
                       frame = new byte[availTest];
                       serial.readBytes(frame, availTest);
                       if (frame[0] != (byte)0xf2)
                           continue;
                       update  = frame[1] == (byte)160 ? true : false;
                       cmdType = frame[2];
                       if ((byte)checkSum() == frame[frame.length-1]);
                           announceMCUEvent();
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
        });
        //readerThread.start();
    }

    public static McuCommunicator getInstance() {
        return instance;
    }

    public static McuCommunicator makeAndGetInstance(IMcuAction handler) {
        if (instance == null && handler != null){
            instance = new McuCommunicator(handler);
        } else {
            return null;
        }
        return instance;
    }

    public void sendCommand(int cmdType, byte[] data, boolean update) throws SerialPortInvalidPortException {
        if (serial.isOpen()){
            byte[] frame = KSWobtain(cmdType, data, false);
            serial.writeBytes(frame, frame.length);
        } else throw new SerialPortInvalidPortException("Port not open!");
    }

    public void killCommunicator(){
        try{
            serial.removeDataListener();
            readerThread.interrupt();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        instance = null;
    }

    private void announceMCUEvent(){
        handler.update(cmdType, frame);
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

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        return;
    }
}
