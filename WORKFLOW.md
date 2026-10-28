# Workflow (Hinglish)

## 1. GitHub par
1. Ye repo ready hai — project files root mein hain (`app/`, `build.gradle`, `settings.gradle`).
2. **Actions** tab > **Build APK and icon pack** > **Run workflow** dabao (ya koi bhi push karo).
3. Run complete hone par **Artifacts** mein:
   - `DailyCalendarIcon-apk` : andar `DailyCalendar-debug.apk`
   - `LiquidGlass-icon-pack` : saare liquid glass icons

## 2. Phone par
1. `DailyCalendar-debug.apk` install karo (unknown sources allow karna padega) aur app ek baar kholo.
2. Settings > Apps > Daily Calendar > Battery > **Unrestricted**.
3. App mein **Turn on daily app icon** ya **Add changing shortcut to Home screen** dabao.
   Icon home screen se hat jaye to dusra tareeka (changing shortcut) use karo.

## 3. Icon pack
`icon-pack/icons/` ke icons apne icon pack project ya Theme Park mein use karo.
Date wale icons `calendar_1`..`calendar_31` hain. One UI Home ka Theme Park inhe roz nahi badalta,
isliye roz badalne ka kaam `app/` wala Daily Calendar app karta hai.

Kisi app ka icon nahi hai to:
`python icon-pack/make_icon.py "App Name" out.png`
