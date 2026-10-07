package pl.mp.kodiautostart;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;
public class MainActivity extends Activity {
    final long[] boot={0,3000,5000,10000}, wake={0,500,1000,2000,3000};
    public void onCreate(Bundle b){ super.onCreate(b); LinearLayout r=new LinearLayout(this); r.setOrientation(LinearLayout.VERTICAL); r.setPadding(36,28,36,28); r.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView t=new TextView(this); t.setText("KodiAutoStart v3 – Ferguson"); t.setTextSize(25); r.addView(t);
        TextView s=new TextView(this); s.setText("HiSilicon smart-suspend + boot"); s.setTextSize(15); s.setPadding(0,8,0,20); r.addView(s);
        CheckBox e=new CheckBox(this); e.setText("Włącz autostart Kodi"); e.setTextSize(18); e.setChecked(Prefs.enabled(this)); r.addView(e);
        CheckBox w=new CheckBox(this); w.setText("Restartuj Kodi po wybudzeniu"); w.setTextSize(18); w.setChecked(Prefs.wakeRestart(this)); r.addView(w);
        TextView bl=new TextView(this); bl.setText("Opóźnienie po uruchomieniu boxa:"); r.addView(bl); Spinner bs=spin(new String[]{"0 s","3 s","5 s","10 s"}, idx(boot,Prefs.bootDelay(this))); r.addView(bs);
        TextView wl=new TextView(this); wl.setText("Opóźnienie po wybudzeniu:"); wl.setPadding(0,15,0,0); r.addView(wl); Spinner ws=spin(new String[]{"0 s","0,5 s","1 s","2 s","3 s"}, idx(wake,Prefs.wakeDelay(this))); r.addView(ws);
        Button save=new Button(this); save.setText("ZAPISZ USTAWIENIA"); save.setOnClickListener(v->{Prefs.setEnabled(this,e.isChecked());Prefs.setWakeRestart(this,w.isChecked());Prefs.setBootDelay(this,boot[bs.getSelectedItemPosition()]);Prefs.setWakeDelay(this,wake[ws.getSelectedItemPosition()]);Toast.makeText(this,"Zapisano",Toast.LENGTH_SHORT).show();}); r.addView(save);
        Button start=new Button(this); start.setText("URUCHOM KODI TERAZ"); start.setOnClickListener(v->KodiController.launchKodi(this,0)); r.addView(start);
        Button test=new Button(this); test.setText("TEST: RESTART KODI"); test.setOnClickListener(v->KodiController.restartKodi(this,Prefs.wakeDelay(this))); r.addView(test);
        TextView n=new TextView(this); n.setText("v3 nasłuchuje: smart_suspend_broadcast_quit / screen oraz standardowych SCREEN_ON / USER_PRESENT / DREAMING_STOPPED.\nNie czyści cache Kodi."); n.setPadding(0,18,0,0); r.addView(n);
        setContentView(r); }
    Spinner spin(String[] a,int p){Spinner s=new Spinner(this);ArrayAdapter<String> ad=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,a);s.setAdapter(ad);s.setSelection(p);return s;}
    int idx(long[] a,long v){for(int i=0;i<a.length;i++)if(a[i]==v)return i;return 0;}
}
