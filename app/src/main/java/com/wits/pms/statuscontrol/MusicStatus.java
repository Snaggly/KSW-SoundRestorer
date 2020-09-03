package com.wits.pms.statuscontrol;

import android.text.TextUtils;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class MusicStatus {
    public static final int TYPE_MUSIC_STATUS = 21;
    public String mode;
    public String path;
    public boolean play;
    public int position;

    public MusicStatus(String str, String str2, boolean z, int i) {
        this.path = str;
        this.mode = str2;
        this.play = z;
        this.position = i;
    }

    public boolean isPlay() {
        return this.play;
    }

    public void setPlay(boolean z) {
        this.play = z;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String str) {
        this.mode = str;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int i) {
        this.position = i;
    }

    public static MusicStatus getMusicStatusFromJson(String str) {
        return (MusicStatus) new Gson().fromJson(str, MusicStatus.class);
    }

    public List<String> compare(MusicStatus musicStatus) {
        ArrayList arrayList = new ArrayList();
        if (!TextUtils.isEmpty(this.path) && !this.path.equals(musicStatus.path)) {
            arrayList.add("path");
        }
        if (!TextUtils.isEmpty(this.mode) && !this.path.equals(musicStatus.mode)) {
            arrayList.add("mode");
        }
        if (this.play != musicStatus.play) {
            arrayList.add("play");
        }
        if (this.position != musicStatus.position) {
            arrayList.add("position");
        }
        return arrayList;
    }
}
