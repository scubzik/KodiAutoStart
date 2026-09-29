package pl.mp.kodiautostart;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int[] DELAYS = {0, 3000, 5000, 10000};

    private Button toggleButton;
    private Button delayButton;
    private TextView statusText;
    private SharedPreferences prefs;
    private ComponentName receiverComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("settings", Context.MODE_PRIVATE);
        receiverComponent = new ComponentName(this, BootReceiver.class);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(dp(48), dp(36), dp(48), dp(36));
        root.setBackgroundColor(Color.rgb(17, 17, 17));

        TextView title = new TextView(this);
        title.setText("Kodi AutoStart");
        title.setTextColor(Color.WHITE);
        title.setTextSize(28);
        title.setGravity(Gravity.CENTER);
        root.addView(title, params(-1, -2, 0, 0, 0, dp(18)));

        statusText = new TextView(this);
        statusText.setTextColor(Color.LTGRAY);
        statusText.setTextSize(18);
        statusText.setGravity(Gravity.CENTER);
        root.addView(statusText, params(-1, -2, 0, 0, 0, dp(24)));

        toggleButton = new Button(this);
        toggleButton.setTextSize(20);
        toggleButton.setMinHeight(dp(64));
        toggleButton.setOnClickListener(v -> {
            setAutoStartEnabled(!isAutoStartEnabled());
            refreshUi();
        });
        root.addView(toggleButton, params(dp(420), -2, 0, 0, 0, dp(16)));

        delayButton = new Button(this);
        delayButton.setTextSize(18);
        delayButton.setMinHeight(dp(58));
        delayButton.setOnClickListener(v -> {
            int current = prefs.getInt("delay_ms", 3000);
            int next = DELAYS[0];
            for (int i = 0; i < DELAYS.length; i++) {
                if (DELAYS[i] == current) {
                    next = DELAYS[(i + 1) % DELAYS.length];
                    break;
                }
            }
            prefs.edit().putInt("delay_ms", next).apply();
            refreshUi();
        });
        root.addView(delayButton, params(dp(420), -2, 0, 0, 0, dp(16)));

        Button testButton = new Button(this);
        testButton.setText("URUCHOM KODI TERAZ");
        testButton.setTextSize(18);
        testButton.setMinHeight(dp(58));
        testButton.setOnClickListener(v -> {
            try {
                if (!KodiLauncher.launch(this)) {
                    Toast.makeText(this, "Nie znaleziono Kodi (org.xbmc.kodi)", Toast.LENGTH_LONG).show();
                }
            } catch (Throwable e) {
                Toast.makeText(this, "Nie udało się uruchomić Kodi", Toast.LENGTH_LONG).show();
            }
        });
        root.addView(testButton, params(dp(420), -2, 0, 0, 0, dp(18)));

        TextView note = new TextView(this);
        note.setText("Po wyłączeniu autostartu odbiornik BOOT_COMPLETED jest całkowicie wyłączony — aplikacja nie działa w tle.");
        note.setTextColor(Color.GRAY);
        note.setTextSize(14);
        note.setGravity(Gravity.CENTER);
        root.addView(note, params(dp(560), -2, 0, 0, 0, 0));

        setContentView(root);
        refreshUi();
    }

    private void refreshUi() {
        boolean enabled = isAutoStartEnabled();
        statusText.setText(enabled ? "Autostart aktywny" : "Autostart wyłączony");
        statusText.setTextColor(enabled ? Color.rgb(102, 187, 106) : Color.rgb(239, 83, 80));
        toggleButton.setText(enabled ? "WYŁĄCZ AUTOSTART" : "WŁĄCZ AUTOSTART");

        int delay = prefs.getInt("delay_ms", 3000);
        delayButton.setText("OPÓŹNIENIE STARTU: " + (delay / 1000) + " s");
    }

    private boolean isAutoStartEnabled() {
        int state = getPackageManager().getComponentEnabledSetting(receiverComponent);
        return state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
    }

    private void setAutoStartEnabled(boolean enabled) {
        getPackageManager().setComponentEnabledSetting(
                receiverComponent,
                enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
        );
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private LinearLayout.LayoutParams params(int width, int height, int ml, int mt, int mr, int mb) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(width, height);
        p.setMargins(dp(ml), dp(mt), dp(mr), dp(mb));
        return p;
    }
}
