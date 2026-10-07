package pl.mp.kodiautostart;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
public class BootReceiver extends BroadcastReceiver {
    public void onReceive(Context c, Intent i){ Log.i("KodiAutoStart","BOOT received"); if(Prefs.enabled(c)) KodiController.launchKodi(c.getApplicationContext(),Prefs.bootDelay(c)); }
}
