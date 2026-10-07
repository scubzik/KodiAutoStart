package pl.mp.kodiautostart;
import android.content.Context;
import android.content.SharedPreferences;
final class Prefs {
    private static SharedPreferences p(Context c){ return c.getSharedPreferences("cfg", Context.MODE_PRIVATE); }
    static boolean enabled(Context c){ return p(c).getBoolean("enabled", true); }
    static boolean wakeRestart(Context c){ return p(c).getBoolean("wake", true); }
    static long bootDelay(Context c){ return p(c).getLong("bootDelay", 3000L); }
    static long wakeDelay(Context c){ return p(c).getLong("wakeDelay", 1000L); }
    static void setEnabled(Context c, boolean v){ p(c).edit().putBoolean("enabled",v).apply(); }
    static void setWakeRestart(Context c, boolean v){ p(c).edit().putBoolean("wake",v).apply(); }
    static void setBootDelay(Context c,long v){ p(c).edit().putLong("bootDelay",v).apply(); }
    static void setWakeDelay(Context c,long v){ p(c).edit().putLong("wakeDelay",v).apply(); }
}
