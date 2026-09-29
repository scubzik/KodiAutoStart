package pl.mp.kodiautostart;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

final class KodiLauncher {
    static final String KODI_PACKAGE = "org.xbmc.kodi";

    private KodiLauncher() {}

    static boolean launch(Context context) {
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLeanbackLaunchIntentForPackage(KODI_PACKAGE);
        if (intent == null) {
            intent = pm.getLaunchIntentForPackage(KODI_PACKAGE);
        }
        if (intent == null) {
            return false;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        return true;
    }
}
