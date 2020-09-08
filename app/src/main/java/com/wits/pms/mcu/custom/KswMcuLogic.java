package com.wits.pms.mcu.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.wits.pms.mcu.McuService;
import com.wits.pms.utils.SystemProperties;

public class KswMcuLogic {
    private static final byte ANDROID_MODE = 1;
    private static final byte AUX = 6;
    private static final byte CAR_MODE = 2;
    private static final byte DTV = 9;
    private static final byte DVD = 8;
    private static final byte DVD_YUV = 12;
    private static final byte DVR = 5;
    private static final byte DV_TYPE_MODE = 0;
    private static final int EVENT_KILL_PROCESS = 5;
    private static final int EVENT_OPEN_LOGCAT = 4;
    private static final int EVENT_SHOW_A_SHORT_TOAST = 1;
    private static final int EVENT_START_LOGCAT = 2;
    private static final int EVENT_STOP_LOGCAT = 3;
    public static final String IAP_ERROR = "iaperror";
    private static final String TAG = "KswMcuLogic";
    /* access modifiers changed from: private */
    public static boolean callingStopHeartBeat;
    private static KswMcuLogic kswMcuLogic;
    /* access modifiers changed from: private */
    public static boolean stopHeartBeat;
    private static boolean willCloseScreen;
    private boolean DEBUG = true;
    private boolean isReversing;
    private boolean isTurnOnBrightness;
    private boolean isUpdating;
    private boolean mCloseScreen;
    /* access modifiers changed from: private */
    public Context mContext = null;
    private byte mCurrentStatus = 0;
    private int mCurrentTouchX;
    private int mCurrentTouchY;
    private Handler mHandler;
    private View mInterceptView;
    private final WindowManager mWindowManager;
    private boolean wasAdded;

    public static class McuToArm {
        public static final int CMD_ATMOSPHERE_LIGHT_CONTROL = 25;
        public static final int CMD_CAN_MSG_RECV = 161;
        public static final int CMD_CAR_CONTROL_STATUS_INFO = 17;
        public static final int CMD_CAR_SYSTEM_SETTINGS = 19;
        public static final int CMD_DVD_SOURCE_CONTROL = 22;
        public static final int CMD_FACTORY_SETTINGS = 23;
        public static final int CMD_MCU_SERIAL_NUMBER = 20;
        public static final int CMD_MCU_VERSION_INFO = 18;
        public static final int CMD_MEDIA_CONTROL = 21;
        public static final int CMD_POWER_STATUS = 16;
        public static final int CMD_TOUCH_SYSTEM_SWITCH = 26;
        public static final int CMD_VIDEO_STATUS = 28;
    }

    public KswMcuLogic(Context context) {
        this.mContext = context;
        this.mHandler = new Handler(context.getMainLooper());
        this.mWindowManager = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("isCalling"), true, new ContentObserver(this.mHandler) {
            public void onChange(boolean selfChange) {
                boolean z = true;
                if (Settings.System.getInt(KswMcuLogic.this.mContext.getContentResolver(), "isCalling", 0) != 1) {
                    z = false;
                }
                boolean unused = KswMcuLogic.callingStopHeartBeat = z;
            }
        });
        heartBeatData();
    }

    private void heartBeatData() {
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        if (!KswMcuLogic.stopHeartBeat && !KswMcuLogic.callingStopHeartBeat) {
                            KswMcuLogic.this.send(KswMessage.obtain(104, new byte[]{8, 0}));
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }.start();
    }

    public void testMcuMessage(int cmdType, byte[] data) {
        handleMessage(KswMessage.obtain(cmdType, data));
    }

    private void setScreenLightOn(boolean on) {
        try {
            this.mCloseScreen = false;
            KswMcuSender.getSender().sendMessage(108, new byte[]{2, on ? (byte) 1 : 0});
        } catch (Exception e) {
        }
    }

    public void powerOff() {
    }

    public void updateVideoStatus(byte status) {
        byte b = 1;
        if (status != 1) {
            b = 2;
        }
        this.mCurrentStatus = b;
        updateInterceptView();
    }

    public void updateStatus(byte status) {
        this.mCurrentStatus = status;
        updateInterceptView();
    }

    private void updateInterceptView() {
        if (this.isReversing) {
            opInterceptView(true, false);
            return;
        }
        switch (this.mCurrentStatus) {
            case 1:
                opInterceptView(false, false);
                return;
            case 2:
                opInterceptView(true, false);
                return;
            default:
                opInterceptView(false, false);
                return;
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private synchronized void opInterceptView(boolean intercept, boolean closeScreen) {
        if (!this.mCloseScreen) {
            this.mCloseScreen = closeScreen;
            this.mHandler.post(() -> KswMcuLogic.lambda$opInterceptView$3(KswMcuLogic.this, intercept));
        }
    }

    public static /* synthetic */ void lambda$opInterceptView$3(KswMcuLogic kswMcuLogic2, boolean intercept) {
        if (!intercept) {
            if (kswMcuLogic2.mInterceptView != null && kswMcuLogic2.wasAdded) {
                try {
                    kswMcuLogic2.mWindowManager.removeViewImmediate(kswMcuLogic2.mInterceptView);
                    kswMcuLogic2.wasAdded = false;
                } catch (Exception e) {
                    Log.w(TAG, "remove interceptView failed.");
                }
            }
        } else if (!kswMcuLogic2.wasAdded) {
            if (kswMcuLogic2.DEBUG) {
                Log.v(TAG, "opInterceptView to op other View");
            }
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.type = 2010;
            lp.flags = lp.flags | 1024 | 262144 | 524288;
            lp.height = -1;
            lp.width = -1;
            lp.format = 1;
            lp.alpha = 1.0f;
            lp.x = 0;
            lp.y = 0;
            kswMcuLogic2.mInterceptView = new View(kswMcuLogic2.mContext);
            kswMcuLogic2.mInterceptView.setClickable(true);
            kswMcuLogic2.mInterceptView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            kswMcuLogic2.mInterceptView.setOnLongClickListener(new View.OnLongClickListener() {
                public final boolean onLongClick(View view) {
                    return false;
                }
            });
            kswMcuLogic2.mInterceptView.setOnTouchListener(new View.OnTouchListener() {
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
            try {
                if (kswMcuLogic2.wasAdded) {
                    kswMcuLogic2.mWindowManager.removeViewImmediate(kswMcuLogic2.mInterceptView);
                }
            } catch (Exception e2) {
                Log.w(TAG, "remove interceptView failed.");
            }
            kswMcuLogic2.wasAdded = true;
            kswMcuLogic2.mWindowManager.addView(kswMcuLogic2.mInterceptView, lp);
        }
    }

    static /* synthetic */ void lambda$null$0(View v) {
    }

    public static /* synthetic */ boolean lambda$null$2(KswMcuLogic kswMcuLogic2, View v, MotionEvent event) {
        if (kswMcuLogic2.mCloseScreen) {
            kswMcuLogic2.wasAdded = false;
            try {
                kswMcuLogic2.mWindowManager.removeViewImmediate(kswMcuLogic2.mInterceptView);
            } catch (Exception e) {
                Log.w(TAG, "remove interceptView failed.");
            }
            kswMcuLogic2.setScreenLightOn(true);
        } else if (kswMcuLogic2.needSendTouchData()) {
            kswMcuLogic2.mCurrentTouchX = (int) event.getX();
            kswMcuLogic2.mCurrentTouchY = kswMcuLogic2.getTouchY((int) event.getY());
            if (event.getAction() == 1) {
                kswMcuLogic2.sendTouchData(false);
            }
        }
        return false;
    }

    private int getTouchY(int currentY) {
        return (SystemProperties.get("app.carMode.control").equals("0") ? 62 : 0) + currentY;
    }

    /* access modifiers changed from: private */
    public void sendTouchData(boolean isLongClicked) {
        int x = this.mCurrentTouchX;
        int y = this.mCurrentTouchY;
        Log.i(TAG, "send Touch X - " + this.mCurrentTouchX + " ; Y - " + this.mCurrentTouchY);
        byte[] touchData = {this.mCurrentStatus, (byte) (x >> 8), (byte) x, (byte) (y >> 8), (byte) y, isLongClicked ^ true ? (byte) 1 : 0};
        if (!this.isUpdating) {
            send(KswMessage.obtain(107, touchData));
        }
    }

    private boolean needSendTouchData() {
        return this.isReversing || this.mCurrentStatus == 2;
    }

    private void handleUpdateMessage(KswMessage message) {
        if (message != null) {
            Log.i("handleUpdateMessage", message.toString());
        }
        if (message.getCmdType() == 224) {
            if (message.getData()[0] == 1) {
                if (!this.isUpdating) {
                    opInterceptView(true, false);
                }
                this.isUpdating = true;
            } else if (message.getData()[0] == 0) {
                opInterceptView(false, false);
                this.isUpdating = false;
                stopHeartBeat = false;
            } else if (message.getData()[0] != 2 && message.getData()[0] != 3 && message.getData()[0] != 4 && (message.getData()[0] & 255) >= 241) {
                opInterceptView(false, false);
                this.isUpdating = false;
                stopHeartBeat = false;
            }
        } else if (message.getCmdType() != 225) {
            message.getCmdType();
        }
    }

    private boolean handleSendMessage(KswMessage msg) {
        boolean intercept = false;
        if (msg == null) {
            return false;
        }
        Log.i("McuSendMessage", msg.toString());
        if (this.isUpdating && msg.getDataType() != 160) {
            return false;
        }
        if (msg.getDataType() == 160) {
            Log.e(TAG, "sendMessage::update type");
            if (msg.getCmdType() == 232) {
                stopHeartBeat = true;
            }
            return true;
        } else if (msg.getCmdType() == 0) {
            Log.e(TAG, "sendMessage::cmdType error!");
            return false;
        } else {
            byte data = msg.getData()[0];
            if (msg.getData() != null && msg.getData().length < 1) {
                return false;
            }
            if (msg.getCmdType() == 108) {
                if (data == 2 && msg.getData()[1] == 0) {
                    if (!this.mCloseScreen) {
                        opInterceptView(true, true);
                    }
                } else if (this.mCloseScreen) {
                    this.mCloseScreen = false;
                    opInterceptView(false, false);
                }
            }
            if (msg.getCmdType() == 99) {
                if (data == 1 && msg.getData()[1] > 0) {
                    updateStatus((byte) 1);
                }
            }
            if (msg.getCmdType() == 103) {
                if (data == 0 || data == 8 || data == 12 || data == 5 || data == 6 || data == 11) {
                    updateStatus((byte) 2);
                } else {
                    updateStatus((byte) 1);
                }
            }
            if (msg.getCmdType() == 104) {
                if (data == 4) {
                    updateStatus((byte) 1);
                }
            }
            if (msg.getCmdType() == 105) {
                if (data == 18 && msg.getData()[1] == 2) {
                    intercept = true;
                }
                if (intercept) {
                    updateStatus((byte) 2);
                }
            }
            return true;
        }
    }

    private void handleMessage(KswMessage message) {
        if (message.getDataType() == 160) {
            handleUpdateMessage(message);
            return;
        }
        int cmdType = message.getCmdType();
        boolean z = false;
        if (cmdType != 28) {
            if (cmdType != 161) {
                switch (cmdType) {
                    case 16:
                    case 18:
                    case 19:
                    case 20:
                    case 22:
                    case 23:
                        return;
                    case 17:
                        if (message.getData()[0] <= 3) {
                            if (message.getData()[1] == 1) {
                                z = true;
                            }
                            this.isReversing = z;
                            updateInterceptView();
                            return;
                        } else if (message.getData()[0] == 4) {
                            if (message.getData()[1] == 1) {
                                z = true;
                            }
                            this.isTurnOnBrightness = z;
                            return;
                        } else {
                            return;
                        }
                    case 21:
                        this.mCurrentStatus = message.getData()[0];
                        updateStatus(this.mCurrentStatus);
                        return;
                    default:
                        switch (cmdType) {
                            case 26:
                                if (message.getData()[0] == 1) {
                                    updateStatus((byte) 2);
                                    return;
                                } else {
                                    updateStatus((byte) 1);
                                    return;
                                }
                            default:
                                return;
                        }
                }
            } else {
                if (message.getData()[0] == 26) {
                    updateStatus(message.getData()[1]);
                }
            }
        } else if (!callingStopHeartBeat) {
            updateVideoStatus(message.getData()[0]);
        }
    }

    public void send(KswMessage message) {
        KswMcuSender.getSender().sendMessage(message);
    }

    public static boolean handleSendMsg(KswMessage message) {
        if (kswMcuLogic != null) {
            return kswMcuLogic.handleSendMessage(message);
        }
        return false;
    }

    public static void handleMsg(KswMessage message) {
        if (kswMcuLogic != null) {
            kswMcuLogic.handleMessage(message);
        }
    }

    public static void init(Context context) {
        kswMcuLogic = new KswMcuLogic(context);
    }

    public static KswMcuLogic getTestMcu() {
        return kswMcuLogic;
    }
}
