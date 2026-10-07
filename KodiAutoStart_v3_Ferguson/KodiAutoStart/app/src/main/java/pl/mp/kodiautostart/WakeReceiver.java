package pl.mp.kodiautostart;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
public class WakeReceiver extends BroadcastReceiver {
    private static long last=0L;
    public void onReceive(Context c, Intent i){
        String a=i!=null?i.getAction():"null";
        Log.i("KodiAutoStart","WAKE broadcast: "+a);
        if(!Prefs.enabled(c)||!Prefs.wakeRestart(c)) return;
        long now=SystemClock.elapsedRealtime();
        if(now-last<6000L){ Log.i("KodiAutoStart","wake debounce: "+a); return; }
        last=now;
        KodiController.restartKodi(c.getApplicationContext(),Prefs.wakeDelay(c));
    }
}
