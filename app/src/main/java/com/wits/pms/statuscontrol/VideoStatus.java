package com.wits.pms.statuscontrol;

import com.google.gson.Gson;

public class VideoStatus {
    public static final int TYPE_VIDEO_STATUS = 22;
    private boolean mask;
    private String mode;
    private String path;
    private boolean play;
    private int position;

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

    public boolean isPlay() {
        return this.play;
    }

    public void setPlay(boolean z) {
        this.play = z;
    }

    public boolean isMask() {
        return this.mask;
    }

    public void setMask(boolean z) {
        this.mask = z;
    }

    public static VideoStatus getVideoStatusFromJson(String str) {
        return (VideoStatus) new Gson().fromJson(str, VideoStatus.class);
    }
}
