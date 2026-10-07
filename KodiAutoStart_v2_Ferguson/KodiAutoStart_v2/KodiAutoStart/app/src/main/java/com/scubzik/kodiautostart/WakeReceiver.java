package com.scubzik.kodiautostart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WakeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        WakeMonitorService.ensureRunning(context);
        WakeMonitorService.handleWake(context, intent != null ? intent.getAction() : "manifest");
    }
}
