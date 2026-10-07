package com.scubzik.kodiautostart;

import android.content.Context;
import android.content.SharedPreferences;

final class Prefs {
    private static final String NAME = "kodi_autostart";
    private static final String K_ENABLED = "enabled";
    private static final String K_WAKE = "wake_restart";
    private static final String K_BOOT_DELAY = "boot_delay_ms";
    private static final String K_WAKE_DELAY = "wake_delay_ms";

    private Prefs() {}

    static SharedPreferences sp(Context c) {
        return c.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    static boolean enabled(Context c) { return sp(c).getBoolean(K_ENABLED, true); }
    static void setEnabled(Context c, boolean v) { sp(c).edit().putBoolean(K_ENABLED, v).apply(); }

    static boolean wakeRestart(Context c) { return sp(c).getBoolean(K_WAKE, true); }
    static void setWakeRestart(Context c, boolean v) { sp(c).edit().putBoolean(K_WAKE, v).apply(); }

    static long bootDelay(Context c) { return sp(c).getLong(K_BOOT_DELAY, 3000L); }
    static void setBootDelay(Context c, long v) { sp(c).edit().putLong(K_BOOT_DELAY, v).apply(); }

    static long wakeDelay(Context c) { return sp(c).getLong(K_WAKE_DELAY, 1000L); }
    static void setWakeDelay(Context c, long v) { sp(c).edit().putLong(K_WAKE_DELAY, v).apply(); }
}
