package com.wits.ksw.bt.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.widget.Toast;

public class AmsUtil {
    public static void forceStopPackage(Context context, String pkgName) throws Exception{
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", new Class[]{String.class}).invoke(am, new Object[]{pkgName});
    }
}
