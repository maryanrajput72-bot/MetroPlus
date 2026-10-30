# Metro+ Native App — Handoff (v1.38 / versionCode 39) — DEBUG build

> **PRIVATE:** `keystore/debug.keystore` is the app's signing key. Keep this zip private.
> Without this exact key, new builds will NOT install over the app on the phone.
> Store password `android`, alias `androiddebugkey`, key password `android`.
> Cert SHA-256: `b13501ef91dd30d621c645ea596010d57446a26e2aa5ce26215cc7dc28019302`.

## Build rule change (30 Oct 2026, user's order)
- Iteration builds are now DEBUG (`assembleDebug`, no R8) — R8 compression runs
  ONLY at the final public stage. Debug and release share the SAME project keystore
  (`keystore/debug.keystore`), so debug builds update in place over release builds
  and vice versa (same cert, versionCode keeps increasing). Updater manifest chain
  stays common for both.

## v1.38 change (30 Oct 2026) — DEBUG build, no R8
- TOM Data Register cards: card-to-card gaps 28→14dp (trial opening-view gap),
  card radius 24→22dp, hero→switch gap 16→12dp (trial switch margin-top 12).
  Card padding/inputs unchanged (already trial-close).
- Mode colors (user's rule: Closing = red, Opening = blue, in both places):
  switch active Opening gradient cyan→pure blue (#4CB4FF→#1A6FD8), Closing stays
  red (#E6353E→#A30D14); hero Opening light blue (#DCECFF→#B9D4FB), hero Closing
  light red/pink (#F2BDC0→#E69A9E, trial easypaisa hero tones) with matching
  shadow/border tints. Dark hero text kept (readable on light gradients).
- Closing screen: all 3 green WhatsApp buttons now show the real WhatsApp logo
  (`img_icon_whatsapp.webp`, 30dp on the 48dp squares, 22dp on the wide Share
  button) instead of the message-bubble icon. GUI phase only — taps still show
  the GUI-phase note; no WhatsApp function wired yet.

## What this is
- Native rebuild of the old Metro+ WebView staff app. Package `com.metroplus.app` (never change).
- **Kotlin + Jetpack Compose only. WebView is banned** (user's rule).
- **GUI phase only.** Buttons show a "GUI phase" note; features come later, one by one.
- Design spec = trial **2.1.63** APK on GitHub:
  `https://maryanrajput72-bot.github.io/metro-admin-panel/apk/MetroPlus-v2.1.63-trial.apk`
  Every screen was measured from that APK's own HTML/CSS (1 CSS px = 1 dp) and copied exactly.

## Exceptions the user asked for (NOT trial)
- **Dock:** white pill bar; unselected = colourful 3D icon only; selected = red pill with icon on a
  white circle + full label. Stays on top of every page (hidden only for camera and drawer).
  Icons are hand-built vectors in `ui/DockIcons.kt`.
- **Welcome card:** trial card + the user's mascot (`drawable-nodpi/metro_plus_welcome.webp`) on the right.
- **Profile drawer:** user's JazzCash style (black→red→pink header), enhanced with cards and gradient icon tiles.

## Screens done (all trial copies)
| Screen | File |
|---|---|
| Home (header, banners auto-slide, minis, hubs) | `ui/HomeScreen.kt` |
| Settings & Control | `ui/SettingsScreen.kt` |
| Opening (menu, Data, Staff cards + status menu, Leave, GPS) | `ui/OpeningScreen.kt` |
| Tom Data Register (Opening / Closing modes) | `ui/TomDataScreen.kt` |
| Money Calculator (Cash Denominations + Today's TOM Closing) | `ui/MoneyCalculatorScreen.kt` |
| Closing Time | `ui/ClosingScreen.kt` |
| GPS Camera (GUI only) | `ui/GpsCameraScreen.kt` |
| Shell: drawer, dock, slide navigation, update pill | `MainActivity.kt` |
| Silent in-app updater | `UpdateCheck.kt` |

Shared helpers (in `HomeScreen.kt`): `cssLinear()` = exact CSS linear-gradient, `Glow()`, `Watermark()`, `cssShadow()`.
Typed values are kept in ViewModels so they survive leaving a page.

## Not built yet
Login / Signup / Forgot ID, Onboarding, TL PIN lock, Calculator, Complaints, Group Chat,
Settings sub-pages, Metro Portal (it is a website in the trial; WebView is banned, so it needs the user's decision).

## Build
- JDK 17+ (built here with JDK 21), Gradle 8.7 wrapper, AGP 8.5.2, Kotlin 1.9.24, Compose BOM 2024.06.00,
  compileSdk 34, minSdk 24.
- Put your Android SDK path in `local.properties` (`sdk.dir=...`).
- `gradlew assembleRelease` → R8-shrunk, signed with the keystore above (~2.3 MB, fast).
  `gradlew assembleDebug` → same key, ~16 MB, slower (testing only).

## Version rule
Bump together: `versionCode`/`versionName` in `app/build.gradle.kts`, `CURRENT_VERSION_CODE` in `UpdateCheck.kt`,
and the "Version X.Y" text in the drawer (`MainActivity.kt`). Only a version the user confirms as final goes
to the updater manifest (`maryanrajput72-bot/MetroPlus/metroplus-app-version.json`).

## Folder
- `app/` source + resources, `gradle/` + `gradlew*` wrapper, `keystore/` signing key,
  `apk/MetroPlus-v1.38-debug.apk` last built (DEBUG) build.
