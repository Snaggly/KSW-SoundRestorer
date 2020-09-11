package com.snaggly.ksw_soundrestorer;

public interface McuAction {
    void update(int cmdType, byte[] data);
    void update(String logcatMessage);
}
