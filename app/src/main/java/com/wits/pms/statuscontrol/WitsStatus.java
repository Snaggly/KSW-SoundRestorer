package com.wits.pms.statuscontrol;

import com.google.gson.Gson;

public class WitsStatus {
    public String jsonArg;
    public int type;

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public String getJsonArg() {
        return this.jsonArg;
    }

    public void setJsonArg(String str) {
        this.jsonArg = str;
    }

    public static WitsStatus getWitsStatusFormJson(String str) {
        return (WitsStatus) new Gson().fromJson(str, WitsStatus.class);
    }

    public WitsStatus(int i, String str) {
        this.type = i;
        this.jsonArg = str;
    }

    public static void sendOutBtStatus(BtPhoneStatus btPhoneStatus) {
        PowerManagerApp.sendStatus(new WitsStatus(3, new Gson().toJson((Object) btPhoneStatus)));
    }

    public static void sendOutMusicStatus(MusicStatus musicStatus) {
        PowerManagerApp.sendStatus(new WitsStatus(21, new Gson().toJson((Object) musicStatus)));
    }

    public static void sendOutVideoStatus(VideoStatus videoStatus) {
        PowerManagerApp.sendStatus(new WitsStatus(22, new Gson().toJson((Object) videoStatus)));
    }

    public static void sendOutPictureStatus(PictureStatus pictureStatus) {
        PowerManagerApp.sendStatus(new WitsStatus(23, new Gson().toJson((Object) pictureStatus)));
    }

    public static void sendOutSystemStatus(SystemStatus systemStatus) {
        PowerManagerApp.sendStatus(new WitsStatus(1, new Gson().toJson((Object) systemStatus)));
    }
}
