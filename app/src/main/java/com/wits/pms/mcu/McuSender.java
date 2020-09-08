package com.wits.pms.mcu;

public interface McuSender {
    void send(McuMessage mcuMessage);

    void send(byte[] bArr);
}
