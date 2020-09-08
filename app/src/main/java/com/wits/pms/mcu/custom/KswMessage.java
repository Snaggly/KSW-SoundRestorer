package com.wits.pms.mcu.custom;

//import com.android.internal.content.NativeLibraryHelper;
import com.wits.pms.mcu.McuMessage;

public class KswMessage extends McuMessage {
    public static final int FRAMEHEAD = 242;
    public static final int NORMAL_DATATYPE = 0;
    public static final int UPDATE_DATATYPE = 160;
    private int cmdType;

    public KswMessage(int cmdType2, byte[] data) {
        this(cmdType2, data, false);
    }

    public KswMessage(int cmdType2, byte[] data, boolean update) {
        this.cmdType = cmdType2;
        this.data = data;
        byte[] bytes = new byte[(data.length + 2)];
        System.arraycopy(data, 0, bytes, 2, data.length);
        this.frameHead = 242;
        this.dataType = update ? 160 : 0;
        bytes[0] = (byte) cmdType2;
        bytes[1] = (byte) data.length;
        obtain(bytes);
    }

    public static KswMessage obtain(int cmdType2, byte[] data) {
        return new KswMessage(cmdType2, data);
    }

    public static KswMessage parse(byte[] data) {
        byte[] realData = new byte[data[3]];
        boolean z = false;
        System.arraycopy(data, 4, realData, 0, realData.length);
        byte b = (byte)(data[2] & 255);
        if ((data[1] & 255) != 0) {
            z = true;
        }
        return new KswMessage(b, realData, z);
    }

    public String printHex(String method, McuMessage msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(method);
        sb.append("-----[ cmdType:");
        sb.append(Integer.toHexString(getCmdType() & 255).toUpperCase());
        sb.append(" - data:");
        for (byte b : msg.getRealData()) {
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

    public int getCmdType() {
        return this.cmdType;
    }

    public static KswMessage obtain(int cmdType2, byte[] datas, boolean b) {
        return new KswMessage(cmdType2, datas, b);
    }

    public byte[] getData() {
        return getRealData();
    }

    public byte[] getSourceData() {
        return this.outData;
    }
}
