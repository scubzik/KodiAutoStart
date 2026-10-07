package com.scubzik.kodiautostart;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
    private final long[] bootDelays = {0, 3000, 5000, 10000};
    private final long[] wakeDelays = {0, 500, 1000, 2000, 3000};

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        WakeMonitorService.ensureRunning(this);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(36, 28, 36, 28);
        root.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText("KodiAutoStart v2");
        title.setTextSize(26f);
        root.addView(title);

        TextView sub = new TextView(this);
        sub.setText("Ferguson / Android TV\nBoot + restart Kodi po wybudzeniu");
        sub.setTextSize(16f);
        sub.setPadding(0, 8, 0, 24);
        root.addView(sub);

        CheckBox enabled = new CheckBox(this);
        enabled.setText("Włącz autostart Kodi");
        enabled.setChecked(Prefs.enabled(this));
        enabled.setTextSize(18f);
        root.addView(enabled);

        CheckBox wake = new CheckBox(this);
        wake.setText("Restartuj Kodi po wybudzeniu");
        wake.setChecked(Prefs.wakeRestart(this));
        wake.setTextSize(18f);
        root.addView(wake);

        TextView bootLabel = label("Opóźnienie po uruchomieniu boxa:");
        root.addView(bootLabel);
        Spinner bootSpin = spinner(new String[]{"0 s", "3 s", "5 s", "10 s"}, indexOf(bootDelays, Prefs.bootDelay(this)));
        root.addView(bootSpin);

        TextView wakeLabel = label("Opóźnienie po wybudzeniu:");
        root.addView(wakeLabel);
        Spinner wakeSpin = spinner(new String[]{"0 s", "0,5 s", "1 s", "2 s", "3 s"}, indexOf(wakeDelays, Prefs.wakeDelay(this)));
        root.addView(wakeSpin);

        Button save = new Button(this);
        save.setText("ZAPISZ USTAWIENIA");
        save.setOnClickListener(v -> {
            Prefs.setEnabled(this, enabled.isChecked());
            Prefs.setWakeRestart(this, wake.isChecked());
            Prefs.setBootDelay(this, bootDelays[bootSpin.getSelectedItemPosition()]);
            Prefs.setWakeDelay(this, wakeDelays[wakeSpin.getSelectedItemPosition()]);
            if (enabled.isChecked()) WakeMonitorService.ensureRunning(this);
            toast(root, "Zapisano");
        });
        root.addView(save);

        Button start = new Button(this);
        start.setText("URUCHOM KODI TERAZ");
        start.setOnClickListener(v -> KodiController.launchKodi(this, 0));
        root.addView(start);

        Button testWake = new Button(this);
        testWake.setText("TEST: RESTART KODI");
        testWake.setOnClickListener(v -> KodiController.restartKodi(this, Prefs.wakeDelay(this)));
        root.addView(testWake);

        TextView note = new TextView(this);
        note.setText("Restart nie czyści cache, baz ani ustawień Kodi.\nNajpierw próba force-stop przez su; bez su używany jest bezpieczny fallback Androida.");
        note.setTextSize(14f);
        note.setPadding(0, 22, 0, 0);
        root.addView(note);

        setContentView(root);
    }

    private TextView label(String s) {
        TextView v = new TextView(this);
        v.setText(s);
        v.setTextSize(16f);
        v.setPadding(0, 18, 0, 6);
        return v;
    }

    private Spinner spinner(String[] values, int selected) {
        Spinner s = new Spinner(this);
        ArrayAdapter<String> a = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, values);
        s.setAdapter(a);
        s.setSelection(selected);
        return s;
    }

    private int indexOf(long[] arr, long v) {
        for (int i = 0; i < arr.length; i++) if (arr[i] == v) return i;
        return 0;
    }

    private void toast(View anchor, String msg) {
        android.widget.Toast.makeText(this, msg, android.widget.Toast.LENGTH_SHORT).show();
    }
}
