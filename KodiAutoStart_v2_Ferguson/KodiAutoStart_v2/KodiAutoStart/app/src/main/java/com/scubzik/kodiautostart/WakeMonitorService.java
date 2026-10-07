package com.scubzik.kodiautostart;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class WakeMonitorService extends Service {
    private static final String TAG = "KodiAutoStart";
    private static final String CH = "kodi_autostart_monitor";
    private static final long DEBOUNCE_MS = 6000L;
    private static volatile long lastWake = 0L;
    private BroadcastReceiver screenReceiver;

    static void ensureRunning(Context c) {
        try {
            Intent i = new Intent(c, WakeMonitorService.class);
            if (Build.VERSION.SDK_INT >= 26) c.startForegroundService(i);
            else c.startService(i);
        } catch (Throwable t) {
            Log.w(TAG, "Cannot start monitor service", t);
        }
    }

    static synchronized void handleWake(Context c, String source) {
        if (!Prefs.enabled(c) || !Prefs.wakeRestart(c)) return;
        long now = SystemClock.elapsedRealtime();
        if (now - lastWake < DEBOUNCE_MS) {
            Log.i(TAG, "Wake ignored (debounce): " + source);
            return;
        }
        lastWake = now;
        Log.i(TAG, "Wake restart from: " + source);
        KodiController.restartKodi(c.getApplicationContext(), Prefs.wakeDelay(c));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (nm != null) {
                nm.createNotificationChannel(new NotificationChannel(CH, "KodiAutoStart", NotificationManager.IMPORTANCE_MIN));
            }
            Notification n = new Notification.Builder(this, CH)
                    .setContentTitle("KodiAutoStart")
                    .setContentText("Monitor wybudzenia aktywny")
                    .setSmallIcon(android.R.drawable.ic_media_play)
                    .build();
            startForeground(17, n);
        }

        screenReceiver = new BroadcastReceiver() {
            @Override public void onReceive(Context context, Intent intent) {
                String a = intent != null ? intent.getAction() : "dynamic";
                handleWake(context, a);
            }
        };
        IntentFilter f = new IntentFilter();
        f.addAction(Intent.ACTION_SCREEN_ON);
        f.addAction(Intent.ACTION_USER_PRESENT);
        f.addAction("android.intent.action.DREAMING_STOPPED");
        registerReceiver(screenReceiver, f);
        Log.i(TAG, "Wake monitor registered");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (screenReceiver != null) {
            try { unregisterReceiver(screenReceiver); } catch (Throwable ignored) {}
        }
        super.onDestroy();
    }

    @Override public IBinder onBind(Intent intent) { return null; }
}
