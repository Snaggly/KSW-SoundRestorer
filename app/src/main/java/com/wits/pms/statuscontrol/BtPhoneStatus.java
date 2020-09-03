package com.wits.pms.statuscontrol;

import com.google.gson.Gson;
import com.wits.ksw.media.utlis.Constants;
import java.util.ArrayList;
import java.util.List;

public class BtPhoneStatus {
    public static final int CALL_CALLOUT = 4;
    public static final int CALL_HANDUP = 7;
    public static final int CALL_INCOMING = 5;
    public static final int CALL_TALKING = 6;
    public static final int TYPE_BT_STATUS = 3;
    public static final int VOICE_CAR_SYSTEM_VOICE = 0;
    public static final int VOICE_CUSTOM_PHONE_VOICE = 1;
    public boolean btSwitch;
    public int callStatus;
    public String devAddr;
    public boolean isConnected;
    public boolean isPlayingMusic;
    public int voiceStatus;

    public boolean isConnected() {
        return this.isConnected;
    }

    public void setConnected(boolean z) {
        this.isConnected = z;
    }

    public void setBtSwitch(boolean z) {
        this.btSwitch = z;
    }

    public boolean isBtSwitch() {
        return this.btSwitch;
    }

    public boolean isPlayingMusic() {
        return this.isPlayingMusic;
    }

    public void setPlayingMusic(boolean z) {
        this.isPlayingMusic = z;
    }

    public String getDevAddr() {
        return this.devAddr;
    }

    public void setDevAddr(String str) {
        this.devAddr = str;
    }

    public int getCallStatus() {
        return this.callStatus;
    }

    public void setCallStatus(int i) {
        this.callStatus = i;
    }

    public int getVoiceStatus() {
        return this.voiceStatus;
    }

    public void setVoiceStatus(int i) {
        this.voiceStatus = i;
    }

    public BtPhoneStatus(boolean z, boolean z2, String str, int i, int i2, boolean z3) {
        this.isConnected = z;
        this.isPlayingMusic = z2;
        this.devAddr = str;
        this.callStatus = i;
        this.voiceStatus = i2;
        this.btSwitch = z3;
    }

    public static BtPhoneStatus getStatusForJson(String str) {
        return (BtPhoneStatus) new Gson().fromJson(str, BtPhoneStatus.class);
    }

    public List<String> compare(BtPhoneStatus btPhoneStatus) {
        ArrayList arrayList = new ArrayList();
        if (this.btSwitch != btPhoneStatus.btSwitch) {
            arrayList.add("btSwitch");
        }
        if (this.isConnected != btPhoneStatus.isConnected) {
            arrayList.add("isConnected");
        }
        if (this.isPlayingMusic != btPhoneStatus.isPlayingMusic) {
            arrayList.add("isPlayingMusic");
        }
        if (this.callStatus != btPhoneStatus.callStatus) {
            arrayList.add(Constants.BT_STATUS_CALLSTATUS);
        }
        String str = this.devAddr;
        if (str != null && !str.equals(btPhoneStatus.devAddr)) {
            arrayList.add("devAddr");
        }
        if (this.voiceStatus != btPhoneStatus.voiceStatus) {
            arrayList.add("voiceStatus");
        }
        return arrayList;
    }
}
