package com.scubzik.kodiautostart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        WakeMonitorService.ensureRunning(context);
        if (!Prefs.enabled(context)) return;
        KodiController.launchKodi(context.getApplicationContext(), Prefs.bootDelay(context));
    }
}
