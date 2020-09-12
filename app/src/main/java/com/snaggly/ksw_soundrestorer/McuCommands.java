package com.snaggly.ksw_soundrestorer;

public interface McuCommands {
    int getCommand();
    byte[] getData();
    boolean getUpdate();

    McuCommands SET_TO_MUSIC_SOURCE = new McuCommands() {
        @Override
        public int getCommand() { return 103; }

        @Override
        public byte[] getData() { return new byte[]{1}; }

        @Override
        public boolean getUpdate() {
            return false;
        }
    };

    McuCommands SET_TO_ATSL_AIRCONSOLE = new McuCommands() {
        @Override
        public int getCommand() {
            return 103;
        }

        @Override
        public byte[] getData() {
            return new byte[] {13};
        }

        @Override
        public boolean getUpdate() {
            return false;
        }
    };

    McuCommands SWITCH_TO_OEM = new McuCommands() {
        @Override
        public int getCommand() { return 105; }

        @Override
        public byte[] getData() { return new byte[] {18, 2}; }

        @Override
        public boolean getUpdate() { return false; }
    };

    McuCommands SWITCH_TO_ANDROID = new McuCommands() {
        @Override
        public int getCommand() { return 105; }

        @Override
        public byte[] getData() { return new byte[] {18, 1}; }

        @Override
        public boolean getUpdate() { return false; }
    };
}
