package com.snaggly.ksw_soundrestorer;

import java.io.InputStream;
import java.io.IOException;

public class McuCommunicator {
    private static McuCommunicator instance;
    private Thread runningThread;
    private Process catProc;
    private IMcuAction handler;
    private int cmdType;
    private byte[] data;
    private static int interceptInterval = 250;

    private Process executeShell(String cmd) throws IOException {
        return Runtime.getRuntime().exec(cmd);
    }

    private McuCommunicator(IMcuAction handler) throws IOException {
        executeShell("stty -F /dev/ttyMSM1 speed 115200");
        this.handler = handler;
        runningThread = new Thread(()->{
            try{
                while(true) {
                    catProc = executeShell("cat /dev/ttyMSM1");
                    InputStream stream = catProc.getInputStream();
                    if (stream.read() == 0xf2) {
                        stream.read();
                        cmdType = stream.read();
                        data = new byte[stream.read()];
                        for (int i=0; i<data.length; i++)
                            data[i] = (byte)stream.read();
                        if (stream.read() == checkSum(cmdType, data)) {
                            announceMCUEvent();
                        }
                    }
                    stream.close();
                    catProc.destroy();
                    Thread.sleep(interceptInterval);
                }
            }
            catch (IOException | InterruptedException e){
                e.printStackTrace();
            }
        });
        runningThread.start();
    }

    public static McuCommunicator getInstance() {
        return instance;
    }

    public static McuCommunicator makeAndGetInstance(IMcuAction handler) throws IOException {
        if (instance == null && handler != null){
            instance = new McuCommunicator(handler);
        } else {
            return null;
        }
        return instance;
    }

    public void sendCommand(int cmdType, byte[] data, boolean update) throws IOException{
        String cmd = "echo -en \"";
        byte[] sequence = KSWobtain(cmdType, data, update);
        for (byte b : sequence){
            cmd += "\\x" + Integer.toHexString(b & 0xFF);
        }
        cmd += "\" > /dev/ttyMSM1";

        executeShell(cmd);
    }

    public static void setInterval(int interval){
        interceptInterval = interval;
    }

    public void killCommunicator(){
        try{
            catProc.destroy();
            runningThread.interrupt();
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

    private byte checkSum(byte[] frame){
        byte sum = 0;
        for (int i = 1; i < frame.length - 1; i++) {
            sum += frame[i];
        }
        return (byte)(~sum);
    }

    private byte[] KSWobtain(int cmdType, byte[] data, boolean update) {
        byte[] bytes = new byte[(data.length + 5)];
        bytes[0] = (byte) 242;
        bytes[1] = update ? (byte)160 : 0;
        bytes[2] = (byte) cmdType;
        bytes[3] = (byte) data.length;
        System.arraycopy(data, 0, bytes, 4, data.length);

        bytes[bytes.length-1] = checkSum(bytes);

        return bytes;
    }
}
