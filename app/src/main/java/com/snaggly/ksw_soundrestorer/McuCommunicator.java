package com.snaggly.ksw_soundrestorer;

import java.io.InputStream;
import java.io.IOException;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;

public class McuCommunicator implements SerialPortDataListener{
    private static McuCommunicator instance;
    private IMcuAction handler;
    private SerialPort serial;
    private int cmdType;
    private byte[] data;

    private Process executeShell(String cmd) throws IOException {
        return Runtime.getRuntime().exec(cmd);
    }

    private McuCommunicator(IMcuAction handler) {
        this.handler = handler;
        serial = SerialPort.getCommPort("/dev/ttyMSM1");
        serial.setBaudRate(115200);
        serial.addDataListener(this);
        serial.openPort();
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
        }
        catch(Exception e){
            e.printStackTrace();
        }
        instance = null;
    }

    private void announceMCUEvent(){
        handler.update(cmdType, data);
    }

    private int checkSum(int cmdType, byte[] data){
        int sum = cmdType + data.length;
        for (byte b: data)
            sum += b;

        return 0xFF - sum;
    }

    private byte[] KSWobtain(int cmdType, byte[] data, boolean update) {
        byte[] bytes = new byte[(data.length + 5)];
        bytes[0] = (byte) 242;
        bytes[1] = update ? (byte)160 : 0;
        bytes[2] = (byte) cmdType;
        bytes[3] = (byte) data.length;
        System.arraycopy(data, 0, bytes, 4, data.length);
        bytes[bytes.length-1] = (byte)checkSum(cmdType, data);

        return bytes;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        byte[] frame = event.getReceivedData();
        if (frame[0] == (byte)242){
            cmdType = frame[2];
            data = new byte[frame[3]];
            System.arraycopy(frame, 4, data, 0, data.length);
            if (checkSum(cmdType, data) == frame[frame.length-1])
                announceMCUEvent();
        }
    }
}
