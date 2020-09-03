package com.wits.pms.statuscontrol;

import android.view.KeyEvent;
import com.google.gson.Gson;
import com.wits.ksw.media.utlis.Constants;
import java.util.ArrayList;
import java.util.List;

public class SystemStatus {
    public static final int TYPE_SYSTEM_STATUS = 1;
    public int acc = 2;
    public int ccd;
    public boolean dormant;
    public int epb;
    public KeyEvent event;
    public int ill;
    public int lastMode;
    public int rlight;
    public int screenSwitch = 2;
    public String topApp;

    public static final class ACC {
        public static final int NORMAL = 2;
        public static final int OFF = 0;
        public static final int ON = 1;
    }

    public static final class CCD {
        public static final int NORMAL = 0;
        public static final int REVER = 1;
    }

    public static final class EPB {
        public static final int TURN_OFF = 0;
        public static final int TURN_ON = 1;
    }

    public static final class ILL {
        public static final int TURN_OFF = 0;
        public static final int TURN_ON = 1;
    }

    public static final class MODE {
        public static final int AUX = 2;
        public static final int FM = 0;
        public static final int MEDIA = 1;
        public static final int NAVI = 3;
        public static final int OTHER = 4;
    }

    public static final class RLIGHT {
        public static final int NORMAL = 0;
        public static final int OPEN = 1;
    }

    public static final class SCREEN {
        public static final int NORMAL = 2;
        public static final int OFF = 0;
        public static final int ON = 1;
    }

    public List<String> compare(SystemStatus systemStatus) {
        ArrayList arrayList = new ArrayList();
        if (this.acc != systemStatus.acc) {
            arrayList.add(Constants.SYSTEM_STATUS_ACC);
        }
        if (this.rlight != systemStatus.rlight) {
            arrayList.add("rlight");
        }
        if (this.screenSwitch != systemStatus.screenSwitch) {
            arrayList.add("screenSwitch");
        }
        if (this.ccd != systemStatus.ccd) {
            arrayList.add(Constants.SYSTEM_STATUS_CCD);
        }
        if (this.ill != systemStatus.ill) {
            arrayList.add(Constants.SYSTEM_STATUS_ILL);
        }
        if (this.epb != systemStatus.epb) {
            arrayList.add(Constants.SYSTEM_STATUS_EPB);
        }
        if (this.dormant != systemStatus.dormant) {
            arrayList.add(Constants.SYSTEM_STATUS_DORMANT);
        }
        if (this.lastMode != systemStatus.lastMode) {
            arrayList.add("lastMode");
        }
        String str = systemStatus.topApp;
        if (str != null && !str.equals(this.topApp)) {
            arrayList.add(Constants.SYSTEM_STATUS_TOPAPP);
        }
        return arrayList;
    }

    public int getAcc() {
        return this.acc;
    }

    public void setAcc(int i) {
        this.acc = i;
    }

    public KeyEvent getEvent() {
        return this.event;
    }

    public void setEvent(KeyEvent keyEvent) {
        this.event = keyEvent;
    }

    public String getTopApp() {
        return this.topApp;
    }

    public void setTopApp(String str) {
        this.topApp = str;
    }

    public boolean isDormant() {
        return this.dormant;
    }

    public void setDormant(boolean z) {
        this.dormant = z;
    }

    public int getLastMode() {
        return this.lastMode;
    }

    public void setLastMode(int i) {
        this.lastMode = i;
    }

    public int getScreenSwitch() {
        return this.screenSwitch;
    }

    public void setScreenSwitch(int i) {
        this.screenSwitch = i;
    }

    public static SystemStatus getSystemStatusFormJson(String str) {
        return (SystemStatus) new Gson().fromJson(str, SystemStatus.class);
    }

    public int getRlight() {
        return this.rlight;
    }

    public void setRlight(int i) {
        this.rlight = i;
    }

    public int getCcd() {
        return this.ccd;
    }

    public void setCcd(int i) {
        this.ccd = i;
    }

    public int getIll() {
        return this.ill;
    }

    public void setIll(int i) {
        this.ill = i;
    }

    public int getEpb() {
        return this.epb;
    }

    public void setEpb(int i) {
        this.epb = i;
    }
}
