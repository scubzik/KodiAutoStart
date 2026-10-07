package pl.mp.kodiautostart;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
final class KodiController {
    static final String KODI="org.xbmc.kodi";
    private static final String TAG="KodiAutoStart";
    private static final ExecutorService EXEC=Executors.newSingleThreadExecutor();
    private static final Handler MAIN=new Handler(Looper.getMainLooper());
    static void launchKodi(Context c,long delay){ MAIN.postDelayed(() -> {
        try { PackageManager pm=c.getPackageManager(); Intent i=pm.getLaunchIntentForPackage(KODI); if(i==null){Log.e(TAG,"Kodi launch intent not found"); return;} i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED); c.startActivity(i); Log.i(TAG,"Kodi launched"); }
        catch(Throwable t){Log.e(TAG,"Kodi launch failed",t);} }, Math.max(0L,delay)); }
    static void restartKodi(Context c,long delay){ EXEC.execute(() -> {
        boolean hard=tryRootForceStop();
        if(!hard){ try { ActivityManager am=(ActivityManager)c.getSystemService(Context.ACTIVITY_SERVICE); if(am!=null) am.killBackgroundProcesses(KODI); Log.i(TAG,"fallback kill issued"); } catch(Throwable t){Log.w(TAG,"fallback kill failed",t);} }
        launchKodi(c.getApplicationContext(),delay);
    }); }
    private static boolean tryRootForceStop(){ try { Process p=new ProcessBuilder("su","-c","am force-stop "+KODI).redirectErrorStream(true).start(); int rc=p.waitFor(); p.destroy(); if(rc==0){Log.i(TAG,"root force-stop OK"); return true;} } catch(Throwable t){Log.i(TAG,"su unavailable");} return false; }
}
