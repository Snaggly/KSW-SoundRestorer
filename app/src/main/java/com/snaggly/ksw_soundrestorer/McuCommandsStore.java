package com.snaggly.ksw_soundrestorer;

public class McuCommandsStore {
    public static class SetToMusicSource {
        public static final int command = 103;
        public static final byte[] data = {1};
        public static final boolean loop = false;
    }
}
