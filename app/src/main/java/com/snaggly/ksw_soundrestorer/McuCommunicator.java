package com.snaggly.ksw_soundrestorer;

import java.io.IOException;

public class McuCommunicator {
    private static McuCommunicator instance;

    private void executeShell(String cmd) throws IOException {
        Runtime.getRuntime().exec(cmd);
    }

    private McuCommunicator() throws IOException {
        executeShell("stty -F /dev/ttyMSM1 speed 115200");
    }

    public static McuCommunicator getInstance() throws IOException {
        if (instance == null){
            instance = new McuCommunicator();
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

    private byte[] KSWobtain(int cmdType, byte[] data, boolean update) {
        byte[] bytes = new byte[(data.length + 5)];
        bytes[0] = (byte) 242;
        bytes[1] = update ? (byte)160 : 0;
        bytes[2] = (byte) cmdType;
        bytes[3] = (byte) data.length;
        System.arraycopy(data, 0, bytes, 4, data.length);
        int sum = 0;
        for (int i = 1; i < bytes.length - 1; i++) {
            sum += bytes[i] & 255;
        }
        bytes[bytes.length-1] = (byte)~sum;

        return bytes;
    }
}
