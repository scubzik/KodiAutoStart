KodiAutoStart v2 - Ferguson / Android TV 7

Zmiany:
- zachowany autostart Kodi po BOOT_COMPLETED,
- monitor SCREEN_ON / USER_PRESENT / DREAMING_STOPPED,
- po wybudzeniu: próba zatrzymania Kodi -> krótka pauza -> ponowne uruchomienie,
- debounce 6 s, aby kilka eventów wybudzenia nie uruchamiało restartu wielokrotnie,
- brak czyszczenia cache, baz, userdata i addon_data,
- ustawienia: autostart ON/OFF, restart po wake ON/OFF, boot delay 0/3/5/10 s, wake delay 0/0.5/1/2/3 s,
- przycisk TEST: RESTART KODI.

WAŻNE:
Zwykła aplikacja Android nie może wykonać 'am force-stop' innej aplikacji bez uprawnień systemowych/root.
Ta wersja najpierw próbuje:
  su -c "am force-stop org.xbmc.kodi"
Jeśli 'su' nie jest dostępne dla aplikacji, używa:
  ActivityManager.killBackgroundProcesses("org.xbmc.kodi")
po czym uruchamia Kodi.

Rekomendowane ustawienie dla Fergusona:
- Włącz autostart Kodi: TAK
- Restartuj Kodi po wybudzeniu: TAK
- Boot delay: 3 s
- Wake delay: 1 s

Budowanie:
Repo ma strukturę zgodną z wcześniejszym workflow:
  KodiAutoStart/app/...
GitHub Actions -> Build APK -> artifact KodiAutoStart-v2-apk

APK po buildzie:
  KodiAutoStart/app/build/outputs/apk/debug/app-debug.apk

Instalacja ADB:
  adb install -r app-debug.apk

Jeżeli Android zgłosi niezgodny podpis starej aplikacji, odinstaluj poprzedni KodiAutoStart i zainstaluj nową wersję.
Kodi i jego dane nie są przez to ruszane.

Log diagnostyczny:
  adb logcat -s KodiAutoStart
