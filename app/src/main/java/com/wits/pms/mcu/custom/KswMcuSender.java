package com.wits.pms.mcu.custom;

import com.wits.pms.mcu.McuMessage;
import com.wits.pms.mcu.McuSender;

public class KswMcuSender {
    private static KswMcuSender kswMcuSender;
    private final McuSender mcuSender;

    public KswMcuSender(McuSender sender) {
        this.mcuSender = sender;
    }

    public void sendMessage(int cmdType, byte[] data) {
        sendMessage(KswMessage.obtain(cmdType, data));
    }

    public void sendMessage(KswMessage msg) {
        if (this.mcuSender != null && KswMcuLogic.handleSendMsg(msg)) {
            this.mcuSender.send((McuMessage) msg);
        }
    }

    public void sendUpdateMessage(KswMessage msg) {
        if (this.mcuSender != null && KswMcuLogic.handleSendMsg(msg)) {
            this.mcuSender.send((McuMessage) msg);
        }
    }

    public static void init(McuSender sender) {
        kswMcuSender = new KswMcuSender(sender);
    }

    public static KswMcuSender getSender() {
        if (kswMcuSender == null) {
            kswMcuSender = new KswMcuSender((McuSender) null);
        }
        return kswMcuSender;
    }
}
