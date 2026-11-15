# Ball Battle Studio: Android APK project

Needs: Node 18+, JDK 17, and the Android SDK (install Android Studio, which includes it).

## Build a debug APK
    npm install
    npx cap add android            # first time only
    node add-gallery-plugin.js     # adds the save-to-gallery plugin (safe to re-run)
    npx capacitor-assets generate --android
    npx cap sync android
    cd android
    ./gradlew assembleDebug        # Windows: gradlew.bat assembleDebug

APK: android/app/build/outputs/apk/debug/app-debug.apk
Copy it to your phone, open it, and allow "Install unknown apps" when asked.

## Updating an existing project
Replace www/index.html, then run:
    node add-gallery-plugin.js
    npx cap sync android
and rebuild the APK.

## Notes
- The app is in www/index.html.
- When a battle ends, the recording is saved automatically to the gallery (Movies/BallBattle). Needs Android 10+. On older Android it opens the share menu instead.
- Fonts load from Google Fonts when online and fall back to the system font offline.
