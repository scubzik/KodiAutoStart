KodiAutoStart v3 – Ferguson / HiSilicon

Zmiany:
- BOOT_COMPLETED -> start Kodi
- smart_suspend_broadcast_quit -> restart Kodi po wybudzeniu
- smart_suspend_broadcast_screen -> fallback HiSilicon
- SCREEN_ON / USER_PRESENT / DREAMING_STOPPED -> standardowe fallbacki
- debounce 6 s
- brak czyszczenia cache Kodi

Domyślne ustawienia: boot 3 s, wake 1 s.
