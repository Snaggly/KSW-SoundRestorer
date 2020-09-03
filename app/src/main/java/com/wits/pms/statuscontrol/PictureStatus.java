package com.wits.pms.statuscontrol;

public class PictureStatus {
    public static final int TYPE_PIC_STATUS = 23;
    private boolean mask;
    private String path;
    private boolean play;

    public PictureStatus(boolean z, String str, boolean z2) {
        this.play = z;
        this.path = str;
        this.mask = z2;
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

    public boolean isMask() {
        return this.mask;
    }

    public void setMask(boolean z) {
        this.mask = z;
    }
}
