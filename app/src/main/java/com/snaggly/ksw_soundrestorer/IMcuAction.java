package com.snaggly.ksw_soundrestorer;

public interface IMcuAction {
    void update(int cmdType, byte[] data);
}
