package com.snaggly.ksw_soundrestorer;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private static Preferences instance;
    private SharedPreferences pref;

    private static Preferences makeInstance(Context context){
        instance = new Preferences();
        instance.pref = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        return instance;
    }


    private static final String SET_ON_BOOT_SWITCH_ID = BuildConfig.APPLICATION_ID + ".StartOnBoot";
    public final static void SET_START_ON_BOOT (boolean b, Context context){
        if (instance == null)
            makeInstance(context);

        instance.pref.edit().putBoolean(SET_ON_BOOT_SWITCH_ID, b).apply();
    }
    public final static boolean GET_START_ON_BOOT (Context context){
        if (instance == null)
            makeInstance(context);

        return instance.pref.getBoolean(SET_ON_BOOT_SWITCH_ID, true);
    }
}
