package com.wits.pms.mcu;

//import com.android.internal.content.NativeLibraryHelper;

public class McuMessage {
    public byte[] data;
    public int dataType;
    public int frameHead;
    private int len;
    public byte[] outData;

    public McuMessage(int frameHead2, int dataType2, byte[] data2) {
        this.frameHead = frameHead2;
        this.dataType = dataType2;
        this.data = data2;
        obtain(data2);
    }

    public void obtain(byte[] data2) {
        this.len = data2.length + 2;
        byte[] bytes = new byte[(this.len + 1)];
        bytes[0] = (byte) this.frameHead;
        bytes[1] = (byte) this.dataType;
        System.arraycopy(data2, 0, bytes, 2, data2.length);
        bytes[this.len] = (byte) sumCheck(bytes);
        this.outData = bytes;
    }

    public McuMessage() {
    }

    public static McuMessage parse(byte[] data2) {
        if (!check(data2)) {
            return null;
        }
        McuMessage mcuMessage = new McuMessage();
        mcuMessage.frameHead = data2[0];
        mcuMessage.dataType = data2[1];
        mcuMessage.len = data2[3];
        mcuMessage.outData = data2;
        mcuMessage.data = new byte[(data2[3] + 5)];
        System.arraycopy(data2, 2, mcuMessage.data, 0, mcuMessage.data.length);
        return mcuMessage;
    }

    public static int sumCheck(byte[] b) {
        int sum = 0;
        for (int i = 1; i < b.length - 1; i++) {
            sum += b[i] & 255;
        }
        return ~sum;
    }

    public static boolean check(byte[] b) {
        int sum = 0;
        for (int i = 1; i < b.length - 1; i++) {
            sum += b[i] & 255;
        }
        if (b[b.length - 1] + sum == 255) {
            return true;
        }
        return false;
    }

    public String toString() {
        return printHex("Mcu toString", this);
    }

    public String printHex(String method, McuMessage msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(method);
        sb.append("-----[");
        for (byte b : msg.getData()) {
            String hex = Integer.toHexString(b & 255);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
            //sb.append(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
        }
        sb.replace(sb.length() - 1, sb.length(), "");
        sb.append(" ]\n");
        return sb.toString();
    }

    public int getFrameHead() {
        return this.frameHead;
    }

    public byte[] getRealData() {
        return this.data;
    }

    public byte[] getData() {
        return this.outData;
    }

    public int getDataType() {
        return this.dataType;
    }
}
