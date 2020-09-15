package com.snaggly.ksw_soundrestorer;

import java.io.IOException;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;

public class McuCommunicator implements SerialPortDataListener{
    private static McuCommunicator instance;
    private McuAction handler;
    private SerialPort serial;
    private LogcatReader readerThread;
    private byte[] frame;

    private McuCommunicator() throws SerialPortInvalidPortException{
        try{
            serial = SerialPort.getCommPorts()[0];
        }
        catch (ArrayIndexOutOfBoundsException e){
            throw new SerialPortInvalidPortException("No serial ports!");
        }
        serial.setBaudRate(115200);
        serial.addDataListener(this);
        serial.openPort();
    }

    public static McuCommunicator getInstance() {
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

    public static McuCommunicator makeAndGetInstance() throws SerialPortInvalidPortException {
        if (instance == null)
            instance = new McuCommunicator();

        return instance;
    }

    public void sendCommand(int cmdType, byte[] data, boolean update) throws SerialPortInvalidPortException {
        if (serial.isOpen()){
            byte[] frame = KSWobtain(cmdType, data, false);
            serial.writeBytes(frame, frame.length);
        } else throw new SerialPortInvalidPortException("Port not open!");
    }

    public void sendCommand(McuCommands mcuCommands) throws SerialPortInvalidPortException {
        sendCommand(mcuCommands.getCommand(), mcuCommands.getData(), mcuCommands.getUpdate());
    }

    public void killCommunicator(){
        try{
            serial.removeDataListener();
            serial.closePort();
        }
        catch(Exception e){
            e.printStackTrace();
        }
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

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        return;
    }
}
