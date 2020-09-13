package com.snaggly.ksw_soundrestorer;

public interface McuEvent {
    boolean equals(String logcatMsg);
    boolean equals(int cmdType, byte[] data);

    McuEvent SWITCHED_TO_OEM = new McuEvent() {
        @Override
        public boolean equals(String logcatMsg) {
            return logcatMsg.equals("cmdType:A1 - data:1A-02") || logcatMsg.equals("cmdType:A1 - data:1A-F2");
        }

        @Override
        public boolean equals(int cmdType, byte[] data) {
            return cmdType == 0xA1 && data.length==2 && data[0]==0x1A && data[1]==0x2;
        }
    };

    McuEvent SWITCHED_TO_ARM = new McuEvent() {
        @Override
        public boolean equals(String logcatMsg) {
            return logcatMsg.equals("cmdType:A1 - data:1A-01") || logcatMsg.equals("cmdType:A1 - data:1A-F1");
        }

        @Override
        public boolean equals(int cmdType, byte[] data) {
            return cmdType == 0xA1 && data.length==2 && data[0]==0x1A && data[1]==0x1;
        }
    };
}
