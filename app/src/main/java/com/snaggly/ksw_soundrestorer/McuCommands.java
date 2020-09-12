package com.snaggly.ksw_soundrestorer;

public interface McuCommands {
    int getCommand();
    byte[] getData();
    boolean getUpdate();

    McuCommands SET_TO_MUSIC_SOURCE = new McuCommands() {
        @Override
        public int getCommand() {
            return 103;
        }

        @Override
        public byte[] getData() {
            return new byte[]{1};
        }

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
            return new byte[] {0xd};
        }

        @Override
        public boolean getUpdate() {
            return false;
        }
    };
}
