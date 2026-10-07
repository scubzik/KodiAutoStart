package com.scubzik.kodiautostart;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class KodiController {
    static final String KODI = "org.xbmc.kodi";
    private static final String TAG = "KodiAutoStart";
    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();
    private static final Handler MAIN = new Handler(Looper.getMainLooper());

    private KodiController() {}

    static void launchKodi(Context context, long delayMs) {
        MAIN.postDelayed(() -> {
            try {
                PackageManager pm = context.getPackageManager();
                Intent i = pm.getLaunchIntentForPackage(KODI);
                if (i == null) {
                    Log.e(TAG, "Kodi launch intent not found");
                    return;
                }
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(i);
                Log.i(TAG, "Kodi launched");
            } catch (Throwable t) {
                Log.e(TAG, "Kodi launch failed", t);
            }
        }, Math.max(0L, delayMs));
    }

    static void restartKodi(Context context, long delayMs) {
        EXEC.execute(() -> {
            boolean hardStopped = tryRootForceStop();
            if (!hardStopped) {
                try {
                    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    if (am != null) {
                        am.killBackgroundProcesses(KODI);
                        Log.i(TAG, "Fallback killBackgroundProcesses issued");
                    }
                } catch (Throwable t) {
                    Log.w(TAG, "Fallback kill failed", t);
                }
            }
            launchKodi(context.getApplicationContext(), delayMs);
        });
    }

    private static boolean tryRootForceStop() {
        Process p = null;
        try {
            p = new ProcessBuilder("su", "-c", "am force-stop " + KODI)
                    .redirectErrorStream(true)
                    .start();
            int rc = p.waitFor();
            if (rc == 0) {
                Log.i(TAG, "Root force-stop OK");
                return true;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) sb.append(line).append(' ');
            Log.w(TAG, "Root force-stop rc=" + rc + " " + sb);
        } catch (Throwable t) {
            Log.i(TAG, "No usable su; using fallback");
        } finally {
            if (p != null) p.destroy();
        }
        return false;
    }
}
