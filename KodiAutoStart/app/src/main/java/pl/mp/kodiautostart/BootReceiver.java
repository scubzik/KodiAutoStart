package pl.mp.kodiautostart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            return;
        }

        final PendingResult pending = goAsync();
        final Context appContext = context.getApplicationContext();
        final SharedPreferences prefs = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
        final int delayMs = prefs.getInt("delay_ms", 3000);

        new Thread(() -> {
            try {
                if (delayMs > 0) {
                    Thread.sleep(delayMs);
                }
                KodiLauncher.launch(appContext);
            } catch (Throwable ignored) {
                // Intentionally silent: this utility should never block device startup.
            } finally {
                pending.finish();
            }
        }, "KodiAutoStart").start();
    }
}
