# Kodi AutoStart

Minimalna aplikacja Android/Android TV uruchamiająca Kodi (`org.xbmc.kodi`) po zakończeniu startu systemu.

## Założenia
- brak usług działających stale w tle,
- brak Internetu i zbędnych uprawnień,
- odbiornik `BOOT_COMPLETED` jest fizycznie wyłączony, gdy autostart jest wyłączony,
- domyślne opóźnienie: 3 s; z interfejsu można przełączać 0 / 3 / 5 / 10 s,
- przycisk testowy „Uruchom Kodi teraz”.

## Pierwsze uruchomienie
1. Zainstaluj APK.
2. Uruchom **Kodi AutoStart** przynajmniej raz.
3. Naciśnij **WŁĄCZ AUTOSTART**.
4. Przycisk „URUCHOM KODI TERAZ” pozwala sprawdzić, czy pakiet Kodi jest prawidłowo wykrywany.
5. Zrestartuj TV Box i sprawdź działanie.

## Wyłączenie
Otwórz **Kodi AutoStart** i naciśnij **WYŁĄCZ AUTOSTART**. Odbiornik rozruchu zostanie całkowicie wyłączony i aplikacja nie wykonuje niczego w tle.

## Budowanie
Projekt jest zwykłym projektem Android Studio bez bibliotek zewnętrznych. Otwórz katalog w Android Studio i wybierz **Build > Build APK(s)**.

## Uwaga o firmware TV Box
Niektóre wersje Android TV/firmware producentów mogą blokować otwieranie aplikacji z tła po `BOOT_COMPLETED`. Na typowych starszych boxach Android TV rozwiązanie działa bez stałej usługi. Jeśli firmware Fergusona blokuje takie uruchomienie, potrzebny będzie wariant zgodny z jego wersją Androida/ograniczeniami producenta.
