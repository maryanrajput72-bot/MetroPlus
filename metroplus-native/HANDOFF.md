# Metro+ Native App — Handoff (v1.53 / versionCode 54) — DEBUG build

> **PRIVATE:** `keystore/debug.keystore` is the app's signing key. Keep this zip private.
> Without this exact key, new builds will NOT install over the app on the phone.
> Store password `android`, alias `androiddebugkey`, key password `android`.
> Cert SHA-256: `b13501ef91dd30d621c645ea596010d57446a26e2aa5ce26215cc7dc28019302`.

## v1.53 change (30 Oct 2026) — 63 ke picker poori app me, DEBUG build, no R8
- Uska order: station selection wali har jagah pe 63 APK jaisa interface
  lagao — date pe click karo to date aayen, cards aayen, station card me
  aaye, operator ke naam pyare cards me aayen, aur jahan dropdown hai wo
  bhi usi me aaye; poori app me dekho aur 63 jaisa same lagao.
- Tehqiq (63, 96-profile-v2.js pv-picker + 98-opening-staff.js loDateSheet
  + lo staff cards): station picker = centered sheet, "Select Station" +
  24 pastel cards (naam + 201-224 number chip), chuna hua laal border +
  ring; date picker = month grid sheet (Su-Sa header, day cards, aaj laal
  border, chuna hua purple gradient, Today/Close buttons, DD-MM-YYYY);
  staff cards = pv-st-card, 6 pastel gradients, naam center + ID chip,
  lamba naam (12+) poori line; koi native dropdown nahi (pv-picker ne
  sab <select> replace kiye).
- Nayi shared file ui/Pickers.kt: StationPickerSheet (63 ke 24 stations,
  tarteeb + number wahi; pastel cycle %4 pink/yellow/green/blue),
  DatePickerSheet (63 ka month grid wahi: Sunday-start, aaj laal, select
  purple, Today/Close), OptionPickerSheet (kisi bhi dropdown ki jagah).
  Helpers: formatDmy + dmyToDisplay.
- Wiring (saare "Station Picker"/"Date Picker" GUI notes khatam, koi
  DropdownMenu nahi bacha): Edit Profile station row, Staff Edit station
  card, Leave Form date + station, Overtime Detail date + station,
  Closing REPORT DATE card, Tom Data hero date chip — sab apni sheet
  kholte hain aur chuni hui value wahi dikhate hain (GUI phase: save
  wiring baad me). Staff status menu (pehle DropdownMenu) ab
  OptionPickerSheet se khulta hai (5 options, rang/label wahi).
  StaffPicker: 6th pastel (orange) add + lamba naam poori line (63 ka
  lo-wide); chuna hua naam gayab rehne wala Item 5 rule wahi hai.
- Verify: vc54/1.53, DEBUG, cert b13501ef, SHA a2a55848, 17,612,776
  bytes; dex me "Select Station", "Select Status", "Pak Secretariat",
  "Version 1.53" maujood; "Station Picker"/"Date Picker" notes ghaib.
  GitHub code push + manifest live verify; KOI LINK NAHI BHEJA.


## v1.52 change (30 Oct 2026) — Portal screen (63 se) + drawer/settings safai, DEBUG build, no R8
- Uska order: (1) Portal ka interface 63 APK ko dekh ke banao, uske andar
  har cheez ke sath; (2) drawer (profile pe click) ki Tl/Rp access setting
  me sab se upar Gmail + password login rakho, aur jinke paas Gmail/password
  nahi unke liye sab se upar "Contact to admin for access" likho, beech se
  Password Change option khatam karo (access sirf admin de sakta hai);
  (3) GPS setting se calculator wali setting khatam karo; (4) Money
  Calculation page se upar wale Settings + Refresh options khatam karo;
  (5) is update me KOI logo nahi — logo wo baad me karega.
- Nayi file ui/PortalScreen.kt (63 ke 50-portal.js + portalScreen markup se):
  header (back | Online Portal | Fill + Key + Reload buttons), GCS Portal
  card (63 me yahan iframe tha — native me WebView SAKHT MANA hai, is liye
  "Open in Browser" button site https://shababulmohsinapp.ai.studio/ ko
  bahar browser me kholta hai; Reload bhi browser dobara kholta hai),
  QUICK FILL card (63 ka FAB menu: Auto Fill, TOM A, TOM B, TKM, ASKARI —
  sab GUI note), SAVED LOGIN card (Key button se sheet: Gmail + Username +
  Password form + Save, GUI note), Data-Fill sheet (aaj ki tareekh + TOM A/B,
  TKM, BOP/POS rows, value "—" — asli values Tom Data Save banne pe
  wire hongi; Auto-Fill/Copy GUI note). Dock Portal + banner/hub "Online
  Portal" ab ye page kholte hain (pehle notYet tha).
- DrawerPages.kt: TlRpSettingsScreen se TL PIN CHANGE card (Current/New PIN
  + Change PIN) POORA hata diya; sab se upar "Contact to admin for access"
  note card + TL LOGIN card (Gmail field + password field, masked + Login
  button, GUI note); ROLE ACCESS + TL ACCESS cards wahi. GpsSettingsScreen
  se "Money Calculator Setting" row hata di (onOpenMoney param bhi gaya;
  denomination setting sirf drawer ki "Calculator setting" se khulti hai).
- MoneyCalculatorScreen.kt: header se Tune "Manage Denominations" circle
  aur Refresh "Reset All" circle dono hata diye (neeche wale Clear All +
  Copy + WhatsApp buttons wahi hain).
- Verify: vc53/1.52, DEBUG, cert b13501ef, SHA 2c97d5a5, 17,576,048
  bytes; dex me naye strings maujood ("Contact to admin for access",
  "Open in Browser", "TL LOGIN", "QUICK FILL", "Version 1.52") aur hataye
  hue strings ghaib ("TL PIN CHANGE", "Change PIN", "Money Calculator
  Setting", "Manage Denominations", "Reset All"). GitHub code push +
  manifest live verify; KOI LINK NAHI BHEJA.


## Build rule (30 Oct 2026, user's order)
- Iteration builds are DEBUG (`assembleDebug`, no R8) — R8 compression runs
  ONLY at the final public stage. Debug and release share the SAME project keystore
  (`keystore/debug.keystore`), so debug builds update in place over release builds
  and vice versa (same cert, versionCode keeps increasing). Updater manifest chain
  stays common for both.

## v1.50 change (30 Oct 2026) — Tom Data Shift History screen (63 se), DEBUG build, no R8
- Uska order: Tom Data Register ke upar wale History button ka UI banao —
  Opening + Closing, 63 app se dekh ke. ("JOI" ko UI parha — delivery me
  ye reading saaf likhi hai taake ghalat ho to ek lafz me correct ho.)
- FIX (nayi file ui/HistoryScreen.kt): 63 ke tomHistoryModal ka native
  full-page version — amber tile + "Shift History" + sub (N saved days ·
  last 7 days · station), Opening (sun) / Closing (moon) segment, pichhle
  7 din naye se purane; har row: date box (din + mahina), weekday +
  shift chip, Opening ✓ / Closing ✓ chips, Sale Rs. + slips line (sirf
  sale > 0 ho to), View (laal) + Delete (halka laal) buttons; aaj ka din
  laal border + OPEN NOW chip. Tom Data header ka History button ab
  page kholta hai (slide, StaffEdit wali pattern); back → Tom Data.
  Page poora opaque + click-consumer (v1.42 rule). SIRF GUI: demo
  records hain; asli saved records Save banne pe wire honge; View/Delete
  abhi GUI note.
- Verify: vc51/1.50, DEBUG, cert b13501ef, SHA b3ba99a0, 17,533,652
  bytes; dex scan: "Shift History" + "OPEN NOW" maujood; GitHub code
  push + manifest live verify (vc51/1.50); KOI LINK NAHI BHEJA (updater
  URL sirf manifest me, 1 Nov 2026 ~20:50 PKT ko expire hogi).

- Uska correction (theek tha): 19:28 order me jagah clear nahi thi —
  maine pooche baghair drawer samajh liya aur drawer ki "app Setting"
  row delete kar di, jabke uska matlab Hubs ka Settings card tha. Usne
  saaf kaha: pehle poochna chahiye tha ke kis me se delete karni hai.
  Lesson lock: jagah ambiguous ho to code se pehle EK sawal poocho,
  apni taraf se andaza nahi lagana.
- FIX: drawer ke SETTINGS section me "app Setting" row dobara laga di
  (wahi purana pink gradient #FF7EB0→#E91E63, wahi onMenu branch) —
  drawer me phir 4 rows hain. Hubs wala Settings card hata hua hi hai
  (v1.48 wala sahi kaam).
- Verify: vc50/1.49, DEBUG, cert b13501ef, SHA 6c1a2f22, 17,508,944
  bytes; dex scan: "app Setting" dobara maujood; GitHub code push +
  manifest live verify (vc50/1.49); KOI LINK NAHI BHEJA (updater URL
  sirf manifest me, 1 Nov 2026 ~20:37 PKT ko expire hogi).

- Uska correction (theek tha): 19:28 wale order me "neeche setting wala"
  ka matlab HUBS section ka Settings hub tha, drawer ki row nahi — maine
  ghalat samajh ke drawer row hata di thi. Hubs wala kaam ab hua hai.
- FIX: Hubs section se Settings hub bilkul khatam — sirf 3 rahenge
  (Opening, Closing, Online Portal); Settings ab sirf dock tab se
  khulengi. Baaki 3 hubs ke colors dark: Opening #FFF4E3→#FFD29E se
  #FFEFC9→#FFBE78, Closing #F4F0FF→#D5C8FF se #E9DEFF→#AC8FFF, Portal
  #EAF6FF→#BFE0FF se #D6EBFF→#7FB9FF. More with Metro tiles ko haath
  nahi lagaya (usne kaha ho gaya hai, theek hai).
- Red-marked strips (uska naya order, 63 se dekh ke): app edge-to-edge
  thi aur system bars be-rang thin — ab upar status bar + neeche nav
  bar 63 ke theme-color #B02A31 (gehra laal) me hain, icons safed.
  (63 ka meta theme-color #B02A31 hai; 63 ka header safed + dock
  safed hai, is liye unko nahi chheda.)
- Drawer ki "app Setting" row ghalat samajh ke hati hui abhi bhi hati
  hui hai — wapas chahiye to uska ek lafz kafi hai.
- Verify: vc49/1.48, DEBUG, cert b13501ef, SHA 4c886de9, 17,590,354
  bytes; dex scan: naye hub colors maujood + "Preferences & profile"
  (hate hue hub ka sub) gayab; GitHub code push + manifest live verify
  (vc49/1.48); KOI LINK NAHI BHEJA (updater URL sirf manifest me,
  1 Nov 2026 ~20:29 PKT ko expire hogi).

- Uska gussa ("fake update"): uska home screenshot 20:13 ka tha, jab
  v1.46 abhi live hi nahi hua tha (manifest 20:14 pe live hua) — is liye
  usme v1.45 wala halka color tha. Aur v1.46 APK ka dex byte-scan kiya:
  naye dark colors maujood (MiniPurple 9D7BFF, MiniPeach FF9A52, drawer
  TlRp 5B21B6), purane v1.45 colors gayab — matlab build me changes the,
  fake update nahi tha. Uske apne drawer screenshot (20:08) me bhi saboot
  hai: Version 1.45 + SETTINGS me sirf 3 rows, "app Setting" row gayab.
  Asal ghalti meri ye thi ke v1.44/45 ka dark itna halka tha ke nazar
  nahi aata tha.
- FIX: is dafa waazeh gehra step — tiles: Blue #B7D2FF→#6B9BF5, Yellow
  #FFE9A3→#FFB020, Purple #CDB4FF→#8B5CF6, Peach #FFD2A8→#FB923C;
  drawer icons: Calculator #D97706→#92400E, Gps #2563EB→#1E3A8A,
  Tl/Rp #6D28D9→#4C1D95 (icons safed hain, gehre pe theek dikhenge).
  Banners phir bhi untouched.
- Verify: vc48/1.47, DEBUG, cert b13501ef, SHA 3f6f3278, 17,508,404 bytes;
  dex scan se naye colors confirm + purane v1.46 colors absent; GitHub
  code push + manifest live verify (vc48/1.47); KOI LINK NAHI BHEJA
  (updater URL sirf manifest me, 1 Nov 2026 ~20:18 PKT ko expire hogi).

## v1.46 change (30 Oct 2026) — dark step 1, DEBUG build, no R8
- Drawer 3 rows + 4 tiles ka pehla waazeh-gehera step (tiles: Blue
  #C9DFFF→#7FA8FF, Yellow #FFE9A3→#FFB830, Purple #DDC9FF→#9D7BFF,
  Peach #FFD9B8→#FF9A52; icons: Calculator #E89B0C→#B45309, Gps
  #3B82F6→#1E40AF, TlRp #7C3AED→#5B21B6). Verify: vc47/1.46, DEBUG,
  cert b13501ef, SHA 6db0176d, dex scan se naye colors confirm;
  GitHub code push + manifest live verify (vc47/1.46); KOI LINK NAHI
  BHEJA. (Iska delivery message uske agle gusse wale message se pehle
  nahi ja saka; user ka 20:13 wala screenshot v1.46 live hone se pehle
  ka tha.)

- Uska report: update ke baad bhi kuch change nahi hua. TAHQEEQ: v1.44 APK
  ko khud khol ke check kiya — dex files me "app Setting" kahin nahi
  (row waqai khatam), "Version 1.44" maujood — matlab BUILD theek tha,
  uske phone tak v1.44 install hi nahi hua tha (purani APK ya atka hua
  update cycle). Server file ka poora download is VM ke proxy se stall
  hua (curl exit 52), is liye byte-compare nahi ho saka — upload tool ne
  bytes=17508448 (local size ke barabar) + ok=true diya tha.
- FIX (uska "again karo"): wahi v1.44 wala code, naya version 1.45/vc46,
  nayi upload URL, manifest update — taake phone ko naya version nazar
  aaye aur update cycle dobara chale. Code me v1.44 ke muqable sirf
  version number badla hai.
- Verify: vc46/1.45, DEBUG, cert b13501ef, SHA 74428d29, 17,508,436 bytes;
  GitHub code push (sirf version files) + manifest live verify (vc46/1.45);
  KOI LINK NAHI BHEJA (updater URL sirf manifest me, 1 Nov 2026 ~20:05
  PKT ko expire hogi).

- Uska order: drawer me sab se neeche wali "app Setting" row khatam; baaki
  3 settings rows ke colors thore dark; More with Metro ke 4 tiles bhi
  thore dark (banners untouched).
- FIX: Drawer SETTINGS section se "app Setting" row + uska divider hata
  diya (dead onMenu branch + unused Settings import bhi saaf); Settings
  ab sirf neeche dock ke Settings tab se khulengi. 3 rows ke icon
  gradients thore dark: Calculator #FFC04D→#F08C00 se #F5B03D→#DE7E00,
  Gps #5AB0FF→#1A6FD8 se #4A9CF0→#1663C4, Tl/Rp #A78BFA→#5B3FD0 se
  #9670F0→#4E34C2. More with Metro tiles ke liye naye Mini*Stops (banners
  purane stops pe hi hain): Blue #EAF3FF→#B0CDFF se #DCE9FF→#9CBCFF,
  Yellow #FFF8D9→#FFD666 se #FFF0B8→#FFCC4D, Purple #F4EDFF→#CBB2FF se
  #EADFFF→#B898FF, Peach #FFF3E8→#FFC59B se #FFE7D0→#FFB07C.
- HANG SAWAL (uska): Tom Data screen ka poora code check kiya — koi loop,
  main-thread IO, ya per-frame bitmap kaam nahi mila. Wajah combination:
  DEBUG build (R8 band, no optimization) + poora page aik verticalScroll
  me (Lazy nahi) + mode switch pe saare cards dobara bante hain. Jawab
  diya: release se kafi behtar hone ki umeed hai, poori guarantee nahi;
  agar release me bhi atka to asal fix LazyColumn hoga. Release test
  build ka faisla uska pending hai (options diye hue hain).
- Verify: vc45/1.44, DEBUG, cert b13501ef, SHA 5de9612f, 17,508,448 bytes;
  GitHub code push + manifest live verify (vc45/1.44); KOI LINK NAHI
  BHEJA (updater URL sirf manifest me, 1 Nov 2026 ~19:36 PKT ko expire
  hogi).

- Uska order (screenshot ke sath): Closing Data ka "Enter Data (Closing Shift)"
  laal button khatam karo; pehle wala card (Daily Shift Register hero) ka
  color dark karo — DONO modes me.
- FIX: ClosingMode se Enter Data Row + uska Spacer hata diya (ye button sirf
  Closing mode me tha; Opening mode me kabhi tha hi nahi) + unused EditNote
  import saaf. StationHero gradient dono modes me dark: Closing halka pink
  #F2BDC0→#E69A9E se gehra gulabi #E88F97→#D46970; Opening halka neela
  #DCECFF→#B9D4FB se gehra neela #A9C7F7→#86A9EF. Baaki hero (station naam,
  shift/date chips, revenue, save/WhatsApp) untouched; BOP Roll card untouched
  (uska order sirf pehle wale card ka tha).
- Verify: vc44/1.43, DEBUG, cert b13501ef, SHA f827e12a, 17,508,504 bytes;
  GitHub code push + manifest live verify (vc44/1.43); KOI LINK NAHI
  BHEJA (updater URL sirf manifest me, 1 Nov 2026 ~18:59 PKT ko expire
  hogi). NOTE: upload step pe ek exec session beech me gayab hua tha —
  verify + copy pehle hi ho chuke the, upload dobara chalaya to kamyab hua;
  manifest me wahi nayi URL hai.


> **PRIVATE:** `keystore/debug.keystore` is the app's signing key. Keep this zip private.
> Without this exact key, new builds will NOT install over the app on the phone.
> Store password `android`, alias `androiddebugkey`, key password `android`.
> Cert SHA-256: `b13501ef91dd30d621c645ea596010d57446a26e2aa5ce26215cc7dc28019302`.

## Build rule (30 Oct 2026, user's order)
- Iteration builds are DEBUG (`assembleDebug`, no R8) — R8 compression runs
  ONLY at the final public stage. Debug and release share the SAME project keystore
  (`keystore/debug.keystore`), so debug builds update in place over release builds
  and vice versa (same cert, versionCode keeps increasing). Updater manifest chain
  stays common for both.

## v1.42 change (30 Oct 2026) — page layering fix, DEBUG build, no R8
- WAJAH: Complaint form ka apna background nahi tha (list peeche se nazar
  aati thi); aur kisi bhi khule page ki khaali jagah tap nahi rokti thi,
  is liye tap neeche wale home tiles tak pahunch jata tha aur ghalat page
  khul jata tha.
- FIX (sab pages): ComplaintFormScreen ko poora opaque background
  (#F3EDED) + click-consumer; StaffEditScreen ko click-consumer; main
  sub-screen overlay (MainActivity) ko click-consumer — ab khule page ka
  har tap wahin rukta hai, neeche kuch nahi dabta. Back tarteeb pehle se
  theek thi (form→list→home, edit→staff tab→menu→home) aur ab opaque
  pages se saaf nazar aati hai.
- Verify: vc43/1.42, DEBUG, cert b13501ef, SHA f964d777, 17,510,012 bytes;
  GitHub code push + manifest live verify (vc43/1.42); KOI LINK NAHI
  BHEJA (updater URL sirf manifest me, 1 Nov 2026 ~17:31 PKT ko expire
  hogi).

## Dictation series (30 Oct 2026, user's order)
- Zabii ne 7 items dictate kiye (PENDING-ITEMS.md me poori tafseel);
  15:55 PKT allow mila: har item DETAIL se, EK EK KARKE banana hai.
  16:01 PKT correction: banana ek ek karke hai lekin delivery EK hi
  update me hogi — items 2–7 ek hi version (v1.41) me updater pe gaye.

## v1.41 change (30 Oct 2026) — Items 2–7, EK hi update, DEBUG build, no R8
- Item 2 (Money Calculator): TotalCard chhota (Glow/Watermark matchParentSize
  me, content height ~124dp); TomClosingCard se watermark hata (content
  ~157dp); Clear All + Copy buttons ke kone 18dp gol (pehle square the).
- Item 3 (Complaints): All Complaints page (63 se: TOM/AGM/TKM/Other 2x2
  grid, trial rang) + har type ka form (Report Info + type card + Generate
  Letter + Letter Preview + WhatsApp share); PMBS variant bhi andar.
  More with Metro ke 4 dabbe barabar (fixed 184dp).
- Item 4 (Opening): Data tab — Save Opening pehle apni line me, neeche
  WhatsApp + Copy ek line me (sirf yehi labels); Staff Edit page (63
  staffEditScreen: hero, station card, list + delete, Add Member, Save).
- Item 5 (Leave): chuna hua naam chip list se gayab (clear karne par
  wapas); Overtime me Leave Form wala station + wahi chips; Generate
  Note dabane par Generated Detail neeche dikhta hai.
- Item 6 (Closing + updater): END OF SHIFT hero 63 jaisa (gradient start
  #211647, halki glow; logo watermark jo card lamba karta tha, hata);
  PMBS Complaint card Item-3 form (PMBS) se wire; har page right→left
  slide; updater fix: progress line + % ab sab se upar render (pehle
  sub-screen overlay isay chhupa deta tha) — 100% par install khud.
  System installer ka "Update" button Android ka usool hai, wo rahega.
- Item 7 (Drawer pages): Edit Profile (drawer top + My Account, ek hi
  page; trial profileEditScreen: Personal + Work Info, Shift/Role toggle,
  Save Profile); Calculator setting (63 Manage Denominations: 11 toggles
  5000–1, 75 samet); Gps setting (Station on Photo toggle, GPS Accuracy,
  Map Settings Satellite↔Streets, Money Calculator Setting); Tl/Rp access
  setting (TL PIN Change + Role Access + TL Access note); app Setting
  pehle se wired (main Settings screen).
- Verify: vc42/1.41, DEBUG, cert b13501ef, SHA 7f901e8d, 17,508,928 bytes;
  GitHub code push (5 batches) + manifest live verify (vc42/1.41);
  KOI LINK NAHI BHEJA (updater URL sirf manifest me, 1 Nov 2026 ~16:37
  PKT ko expire hogi).

## v1.40 change (30 Oct 2026) — Item 1 ONLY, DEBUG build, no R8
- Home header greeting "Hey There 👋" → **"Hi Aryan 👋"** (demo naam Aryan;
  signup se naam aana future kaam hai, signup banega tab).
- Welcome card: date chip ab **shift chip ke NEECHE apni row me** (pehle
  dono ek row me barabar me the); date format poora: **"Friday,
  30 October"** ("EEEE, d MMMM"; pehle "Fri, Oct 30" tha). Card height
  182dp me naya layout fit hai (content ~163dp).
- DEAR Aryan / Shaheed-e-Millat / Welcome to the Metro + app / Operator
  badge — pehle se theek the, inko haath nahi lagaya.

## v1.39 change (30 Oct 2026) — DEBUG build, no R8
- TOM Data Register cards, REAL fix: TomCard's 219dp Watermark image forced
  every card to at least 219dp tall (Box wraps to its largest child; offset
  does not shrink layout size) — far above content (~128dp for a 1-row card).
  Watermark REMOVED from TomCard (trial .tom-card has none), so cards are now
  exactly content-fit in BOTH Opening and Closing modes. Colors NOT touched.
- Closing WhatsApp buttons: old `img_icon_whatsapp.webp` is opaque RGB
  (light-grey square background, zero transparent pixels). New asset
  `img_icon_whatsapp_clean.webp`: background + soft shadow removed by edge
  flood-fill through low-saturation pixels (white phone survives: enclosed
  by green). Verified on green-gradient preview, no white. Sizes up:
  30→36dp on the 48dp squares, 22→26dp on the wide Share button. GUI phase
  only — taps still show the GUI-phase note.
- NOTE on the clean logo asset: GitHub MCP tools are text-only, so the binary
  `img_icon_whatsapp_clean.webp` is stored in the repo as base64 text at
  `app/src/main/res/drawable-nodpi/img_icon_whatsapp_clean.webp.b64`
  (restore with `base64 -d`). The real .webp lives in the workspace
  drawable-nodpi folder alongside the other image assets.

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
Settings sub-pages. Metro Portal GUI v1.52 me ban gaya (site browser me khulti hai —
WebView banned hai); uski Save/fill wiring baaki hai.

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
  `apk/MetroPlus-v1.40-debug.apk` last built (DEBUG) build.
