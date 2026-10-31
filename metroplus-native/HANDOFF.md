# Metro+ Native App — Handoff (v1.76 / versionCode 77) — DEBUG build (non-debuggable)

## v1.76 change (31 Oct 2026) — 63 wala SILENT install + GitHub APK host (uska order, DEBUG)
- CORRECTION (pehle wale jawab ki): maine kaha tha aakhri Update wali
  screen koi app khatam nahi kar sakta — ye GHALAT tha. 63 ki shell APK
  (trial263.apk) ka dex khol ke verify kiya: WebAppBridge.
  downloadAndInstallSilentUpdate -> UpdateManager.startSilent ->
  DownloadManager download + PackageInstaller session (SessionParams,
  setRequireUserAction, createSession/openSession) + manifest me
  UPDATE_PACKAGES_WITHOUT_USER_ACTION + REQUEST_INSTALL_PACKAGES.
  Android ka official rule: self-update + REQUEST_INSTALL_PACKAGES +
  ye permission declare + USER_ACTION_NOT_REQUIRED = bina popup install
  (target API floor: Android 14 par 31+, native target 34 — poora hota hai).
- Native me wahi rasta lagaya: UpdateCheck.installUpdateSilent()
  (PackageInstaller session, MODE_FULL_INSTALL, setAppPackageName,
  USER_ACTION_NOT_REQUIRED; API <31 par false -> purana rasta) +
  UpdateInstallReceiver (SUCCESS pe pending saaf; PENDING_USER_ACTION pe
  system ki installer screen kholo; failure pe ACTION_VIEW fallback) +
  installInFlight guard (dobara session na bane). Manifest me
  UPDATE_PACKAGES_WITHOUT_USER_ACTION + receiver entry.
  MainActivity ke dono install points (100% ke baad + onResume pending)
  pehle silent, na chale to ACTION_VIEW.
- GitHub APK host: push-files sirf TEXT leta hai (binary pe "not UTF-8"
  error) — is liye v1.75 APK browser se GitHub web upload karwayi ja
  rahi hai (task browser-task:97895f3b); v1.76 ki APK bhi usi raste
  GitHub par jayegi, manifest apkUrl = GitHub raw (size milne se %
  dikhega). Manifest vc77 us upload ke verify ke BAAD update hogi.
- Shart saaf: silent sirf SELF-update pe chalta hai; "Install unknown
  apps" ijazat ek dafa on hona lazmi hai, warna system phir bhi
  installer screen dikha dega (receiver fallback).
- Verify: TODO build ke baad.

## v1.75 change (31 Oct 2026) — square staff card naya design (uska order, DEBUG)
- Accent line side ki bajaye har card ke UPAR (top stripe, 5dp, accent rang).
- Line 1: ID badge (wahi 46x30) left + duty pill RIGHT (dropdown trigger,
  label + neeche-wala arrow; pill thodi bari 34dp/10sp). Card ka rang =
  uska accent: ID badge, time box border, +/- buttons sab accent family
  ke (halke amber par gehra text). Duty pill ka rang STATUS ka hi hai
  (on duty/off pehchana jaye) — ye judgment call report me bataya gaya.
- Line 2: naam poori chaurai me (ID ke start se card ke aakhir tak), 16sp.
- Line 3 (sirf ON DUTY): time + pyare chhote +/- — teeno barabar height
  34dp, halka shade (buttons accent gradient + safed nishan).
- Card thoda chaura: aspectRatio 1.12 (pehle 1.0 square), height taqreeban wahi.
- Status options ab sheet ki bajaye pill ke NEECHE dropdown (DropdownMenu)
  me khulte hain — har option apne pyare card me (Leave/Absent/... apne
  status rang me). Purana OperatorCardRow + OptionPickerSheet file me
  mehfooz (OperatorCardRow ab bhi TimeStep/OptionPickerSheet use karta hai).
- Updater me KOI change nahi — uska sawaal sirf jawab se hal hua (neeche note).
- Verify (2026-10-31 ~20:57 PKT): APK 12,549,007 bytes, SHA ea1866d9;
  cert apksigner se b13501ef MATCH; dex "Version 1.75", 1.74 gayab.
  GitHub code push ok, MANIFEST LIVE vc76 JSON verify (RAW JSON).

## Updater note (31 Oct 2026, uske sawaal ke jawab ke liye verify kiya gaya)
- Native me update ka KOI dialog/button nahi: har foreground +5s check,
  update mile to BINA poochhe download (startDownload seedha), upar
  "Updating… X%" strip (size na mile to MB), 100% pe confetti + toast,
  2.7s baad FileProvider install screen. 63 ka JS bhi yahi karta tha
  ("auto popup BILKUL BAND", % bar, 100% pe bar band) — farq sirf ye
  ke 63 ka download+install shell ke native Java (downloadAndInstall-
  SilentUpdate) me tha, jiska source workspace me NAHI hai (sirf trial
  APK hai). Aakhri "Update" wali system screen Android har normal app
  ko dikhata hai — host (GitHub vs remote-storage) se silent ka koi
  taalluq nahi. Nayi package (com.metroplus.app) ko "Install unknown
  apps" ki ijazat EK dafa chahiye — purani app ki ijazat nayi par nahi
  chalti; yehi sab se mumkin wajah hai ke usko ab popup mehsoos hota hai.

## v1.74 change (31 Oct 2026) — hub-style cards + square staff trial (uska order, DEBUG)
- Opening menu ke 4 cards (Opening Data, Staff Attendance, Leave Form,
  GPS Picture) ab hub style: poora 2-rang gradient + safed border
  (pehle safed card + halki tint). Rang: Data yellow, Staff rose, Leave
  teal, GPS sky. Hero NAHI chheda.
- Closing cards bhi hub style (Closing me koi tab nahi hai — cards hi
  hain): TOM·TKM·POS rose, RMBS yellow, Card Claim mint-green, PMBS
  purple; ClCard ko colors param mila, shadow/border card ke rang se.
- Staff Attendance TRIAL IDEA: har banda apne SQUARE card me (2-column
  grid, aspectRatio 1). Upar ID badge + naam; neeche ek line me duty
  pill + time changer (sirf ON DUTY par; chhota stepper 24dp + time box).
  Duty pill par tap = wahi status options (arrow jagah bachane ke liye
  hataya). State wiring wahi (vm.status/dutyMinutes). PURANA design
  OperatorCardRow ke naam se file me MEHFOOZ hai — restore ke liye
  sirf grid ki jagah wapas uska forEach lagana hai.
- Verify (2026-10-31 ~20:28 PKT): APK 12,547,099 bytes, SHA df38f641;
  cert apksigner se b13501ef MATCH; dex "Version 1.74", 1.73 gayab.
  (Pehli koshish aspectRatio import ki wajah se fail hui thi — theek
  karke dobara build chalayi.) GitHub code head 33db3125, MANIFEST
  LIVE vc75 JSON verify (RAW JSON).

## v1.73 change (31 Oct 2026) — minis tartib (uska order, DEBUG)
- More with Metro ke 4 cards ki tartib: pehla Tom Data, doosra
  Complaints (upar wali row), neeche GPS Camera, chautha Money Calc.
  Sirf list ki tartib badli — har card ka rang/icon/kaam wahi.
- OPEN (uske sawaal ka jawab diya, build ka order NAHI mila):
  notification panel me quick access — mumkin hai: ongoing notification
  (3 action buttons) ya Quick Settings tile. Buttons se WhatsApp redirect
  (chat khul ke prefilled text, user khud Send dabayega — khamoshi se
  auto-send official tareeqe se NAHI hota), ya app ke screens ke deep
  links. Reliable rakhne ke liye foreground service + BOOT receiver
  chahiye; Android 13+ par notification permission ek dafa leni hogi.
  Buttons ki list uske batane par banegi.
- Verify (2026-10-31 ~20:09 PKT): APK 12,541,199 bytes, SHA d63ef28a;
  cert apksigner se b13501ef MATCH; dex "Version 1.73", 1.72 gayab.
  GitHub code head e708827d, MANIFEST LIVE vc74 JSON verify (RAW JSON).

## v1.72 change (31 Oct 2026) — Staff Edit theek + week strip naya (uska order, DEBUG)
- Staff Edit: hero status bar ke upar/andar ja raha tha (statusBarsPadding
  nahi tha) — ab page status bar ke neeche se shuru + neeche gesture bar
  ke upar khatam hota hai. Hero thora chhota (title 20sp, pen tile 44dp).
  Staff rows ab asli cards: naam ka pehla harf jamni gradient tile me,
  ID pill wahi, delete ab asli button (halki laal gol tile 34dp, poori
  tile tap hoti hai — pehle nanga 20dp icon tha). Overlay pehle se Box
  ka full-screen child tha (line 180-189 check kiya) — andar jaane ki
  wajah status-bar overlap thi, nesting nahi.
- Week strip: dots + date numbers KHATAM. Ab har din: upar weekday naam
  (Mon/Tue... 3 harf — 7 poore naam ek line me nahi aate), neeche us din
  hazir bande ki ginti (person icon + number; 5 sabz, 4 peela, 3 ya kam
  laal; demo counts, aaj = 5). Aaj ka din halki laal pill me.
- Verify (2026-10-31 ~19:49 PKT): APK 12,541,183 bytes, SHA a08a0db3;
  cert apksigner se b13501ef MATCH; dex "Version 1.72", 1.71 gayab.
  (Pehli build runtime restart se mari thi — stale v1.71 APK check karke
  dobara build chalayi.) GitHub code head 422f75f1, MANIFEST LIVE vc73
  JSON verify (RAW JSON). Updater se update aani chahiye.

## v1.71 change (31 Oct 2026) — supervisor card + TL login ek + 2-rang cards (uska order, DEBUG)
- Supervisor: Settings ACCOUNT me "Supervisor Name" row + pencil ab chhota
  pyara popup card kholte hain (63 ka tlNameModal, usse behtar UI: safed
  gol card, teal tile, gol text field, teal Save + Cancel). Naam Save karo
  to row me aa jata hai; pageState("supervisor_name") is liye page band
  karke dobara kholo to naam wahi rahega. 63 me do tareeqe the (inline
  input + modal) — native me sirf modal card.
- TL/RP Login ek: drawer se Tl/Rp access setting me "TL LOGIN" ka Login
  button pehle sirf note dikhata tha; ab WAHI TlLockScreen kholta hai jo
  Settings ki "TL / RP Login" row kholti hai (5-digit PIN lock). Dono
  jagah ek hi screen. (Settings wali row pehle se TL_LOCK par thi.)
- More with Metro (4 minis) + Hubs ab SIRF 2-rang gradient (3-rang "three
  in one" khatam, pretty2()): Money yellow #FFF3B0→#FFC93C, Complaints
  purple #E9D5FF→#A855F7 (uske bataye), GPS teal #CCFBF1→#2DD4BF, Tom
  rose #FFE4E6→#FB7185 (meri pasand); hubs Opening sabz #DCFCE7→#4ADE80,
  Closing indigo #E0E7FF→#818CF8, Portal sky #E0F2FE→#38BDF8. Halka →
  darmiyana taake kaali likhai parhi jaye. Banners wahi 3-rang (vivid).
  Minis ke accent/shadow + hubs ke tag rang naye gradients se milaye.
- OPEN: uska "create new" wala jumla poora samajh nahi aaya (usne kaha wo
  aur check karke batayega) — is liye us par kuch nahi banaya; uske
  agle message ka intezar hai.
- Verify (2026-10-31 ~19:08 PKT): APK 12,539,463 bytes, SHA b4f69ccd;
  cert apksigner se b13501ef MATCH; dex "Version 1.71" + "supervisor_name"
  key maujood, 1.70 gayab. GitHub code head 04bd4073, MANIFEST LIVE
  vc72 JSON verify (RAW JSON). Updater se update aani chahiye.

## v1.70 change (31 Oct 2026) — icon M yellow + drawer reset + splash 6.5s, no white (uska order, DEBUG)
- Icon: M ka rang YELLOW kar diya (dark + light yellow) — HSV hue shift
  (S,V wahi, is liye 3D shading/gloss wahi raha; halka gloss safed hi
  raha). Background red-black gradient: badge wahi original (dark red),
  crop ke baad corners red-black vertical gradient (#4A0D16 → #120507)
  par composite — poora opaque, full-bleed (v1.69 wale sabak barkarar).
  Gold fringe + glow wahi. logo_hd NAHI chheda (sirf mipmap icons).
  Backup: icon-backup-v169/ (project root).
- Drawer: band hote hi scroll TOP par reset — ProfileDrawer ka scroll
  state hoist kiya (drawerScroll), MetroPlusApp me drawerState.currentValue
  par LaunchedEffect: Closed hote hi drawerReset++ aur ProfileDrawer me
  scrollTo(0). Dobara khulne par profile upar se poori nazar aayegi;
  swipe se upar karna ab bhi usi waqt hoga jab user khud swipe kare.
- Splash: SPLASH_MS 5000 → 6500 (takreeban 6-7 second). White flash ki
  wajah: themes.xml me windowBackground SAFED tha + system splash
  (Android 12+) launcher icon safed par dikhata tha. Fix: windowBackground
  #B02A31, windowSplashScreenBackground #B02A31, animatedIcon =
  splash_red.xml (solid red — red par red = nazar nahi aata),
  iconBackgroundColor #B02A31. Ab cold open se intro tak sirf red.
- Verify (2026-10-31 ~18:52 PKT): APK 12,536,683 bytes, SHA 88b53eef;
  cert apksigner se b13501ef MATCH (Python v2 parse ne ghalat hash diya
  tha — aage se apksigner use karo: build-tools/34.0.0/apksigner + JDK17);
  dex "Version 1.70", naya icon APK me byte-identical (res/o-.png).
  GitHub code head bc2865f2, MANIFEST LIVE vc71 JSON commit e8005667
  (blob e2ecf864, RAW JSON verify). Updater se update aani chahiye.

## v1.69 change (31 Oct 2026) — icon dobara: full-bleed + opaque + wazeh yellow (uska order, DEBUG)
- Uska verdict v1.68 par: koi color change nazar nahi aaya, icon aur
  chhota lag raha hai, background white ho gaya hai. Wajah samjhi: v1.68
  ka gold halo safed background par nazar hi nahi aata (gold on white =
  white) is liye badge ka kinara ghum ho gaya aur badge chhota + background
  safed laga; M ka golden top bhi halka tha (0.42 mix) is liye change
  nazar nahi aaya. (Purana icon bhi transparent tha — farq sirf halo ka
  rang tha: black fringe safed par dikhta tha, gold nahi.)
- Fix: (1) M gradient BOLD — 0.65 mix upar, dheere khatam, taake chhoti
  nazar me bhi gold-red farq saaf dikhe. (2) Badge ko frame bhar me
  crop kiya (bbox + 2% padding, 512 resize) taake bara lage. (3) Poora
  canvas solid dark red (#5E0B16) par composite kiya — alpha sirf 255,
  ZERO transparency: ab koi launcher/chat background (safed ya kuch bhi)
  kabhi nahi dikhega. White-bg simulation par bhi check kiya: icon wahi
  dikhta hai, koi safed nahi. (4) Round icons wahi opaque master ka
  circular mask (round ke liye transparent corners normal hain).
- Purane v1.68 icons ka backup: icon-backup-v168/ (project root).
- Verify (build 18:23 PKT): APK 12,535,199 bytes, SHA 4a451ff1…, cert
  b13501ef EXACT match, dex me "Version 1.69", naya icon APK me
  byte-identical (res/o-.png), disk icon alpha sirf 255 (poora opaque).
- GitHub code head: d637592e (HANDOFF, build.gradle, UpdateCheck,
  MainActivity — icons binary, push nahi hui). Manifest LIVE: vc70/1.69
  JSON, commit e265d815, get_file_contents se verify.

## v1.68 change (31 Oct 2026) — launcher icon: red-yellow gradient + yellow glow (uska order, DEBUG)
- Shikayat: phone par app ka icon blur type lagta tha, jabke splash/main
  logo pyara lagta hai. Wajah: launcher PNGs purane render the (192 max),
  logo_hd 512 ka sharp source hai. Fix: naya icon logo_hd (512) se banaya
  — design wahi, sirf rang badle (uska wazeh order: logo ko kuch nahi
  karna, sirf colour change).
- Rang: M par upar se golden-yellow gradient (neeche pure red — zyada
  red, yellow accent); badge ka dark maroon wahi; bahar ka black shadow
  hata ke golden-yellow glow halo lagaya (fringe = semi-transparent dark
  pixels jo asal transparency ke 10px andar hon; badge interior ko haath
  nahi lagaya). Pehli 2 koshishein zyada peeli ho gayi thi (badge bhi
  gold ho gaya tha) — teesri me fringe detection theek ki to badge dark
  raha, M red-yellow raha.
- Sharpness: 512 master se LANCZOS downscale + halka UnsharpMask, exact
  densities 48/72/96/144/192; round icons wahi artwork circular mask ke
  sath. Purane icons ka backup: icon-backup-v167/ (project root, res se
  bahar — APK me package nahi hota).
- logo_hd.webp NAHI chheda (splash/main logo wahi pyara wala rahega).
- NOTE: mipmap PNGs binary hain — GitHub text-only push me NAHI jati
  (pehle wali binaries ki tarah); naye icons workspace + APK me hain.
  Agli zip sync me res/mipmap-* zaroor shamil karna.
- Verify (build 18:12 PKT): APK 12,537,063 bytes, SHA 1656a1a1…, cert
  b13501ef EXACT match, dex me "Version 1.68". APK ke res naam obfuscate
  hain (aapt2) — naya icon byte-identical mila: res/o-.png (square) aur
  res/Gc.png (round) = nayi mipmap-xxxhdpi files.
- GitHub code head: fc55b68f (HANDOFF, build.gradle, UpdateCheck,
  MainActivity — icons binary hain is liye push nahi hui). Manifest LIVE:
  vc69/1.68 JSON, commit 22eb6296, get_file_contents se verify.

## v1.67 change (31 Oct 2026) — Login/SignUp form update (uska order, DEBUG)
- Login: Employee ID/Gmail field + Password field hata diye. Ab do fields:
  Number (mobile, Phone icon, placeholder 03xx xxxxxxx) + Your ID (saaf
  nazar aane wala text, Badge icon, placeholder e.g. 2022 — password jaisa
  chhupa nahi). Subtitle: "Log in with your number and ID to start your
  shift." Remember me / Forgot ID? / Create new account wahi hain.
- Sign Up: subtitle sirf "Fill in your details" (pehle wala "— they
  appear on all your reports" wala hissa hata diya). Full name ke neeche
  TL Name ka naya section. Password + Confirm password dono hata diye
  (mismatch note bhi unke sath gaya); Role Operator/TL/RP wahi; Shift
  Morning/Evening wahi; Mobile number / Gmail / Station wahi. Checkbox ka
  text ab "I confirm this detail". Employee ID field rakha hai (login ki
  Your ID isi se banti hai — user ne hatane ko nahi kaha tha).
- Validation function (donon pages, asli login baad me): koi field khaali
  ho (Sign Up me confirm checkbox bhi) to button dabane par laal note
  "Please fill in all fields" aur login/sign up NAHI hoga; sab bhara ho to
  pehle wala GUI note. Shift/Role ke default (Morning/Operator) hain is
  liye wo hamesha selected rehte hain.
- Verify (build 16:33 PKT): APK 12,492,195 bytes, SHA 20a1efbb…, cert
  b13501ef EXACT match, dex me "Version 1.67" + "Your ID" + "TL Name" +
  "I confirm this detail" + "Fill in your details" + "Please fill in all
  fields" maujood; "Employee ID / Gmail" + "Create a password" + "Type it
  again" + "Passwords do not match" gayab.
- GitHub code head: 38f40c7e (SplashLogin, build.gradle, UpdateCheck,
  MainActivity, HANDOFF). Manifest LIVE (16:36 PKT): vc68/1.67 JSON,
  commit 5d9708cb, get_file_contents se verify.

## v1.66 push record (31 Oct 2026, is session) — Muse check + GitHub + manifest
- User ki bheji zip (MetroPlus-v1.66-source_3_kg6y.zip) check ki: build.gradle
  vc67/1.66, CURRENT_VERSION_CODE 67, drawer "Version 1.66" — teeno ek.
  Keystore hash eed11f7e project keystore jaisa (same key). Bundled APK
  12,492,043 bytes, SHA 03b65907…, cert b13501ef EXACT match, dex me
  "Version 1.66" + "Create new account" + "Installing update" + "MB".
  v1.63 cheezein maujood: dock me SETTINGS entry nahi (4 tabs), updater MB
  code maujood. Res me koi nayi binary nahi — 10 text files push hui.
- GitHub code head: e12841d7 (HANDOFF, build.gradle, MainActivity,
  UpdateCheck, DrawerPages, ExtraGuiScreens, HomeScreen, MoneyCalculator,
  Settings, SplashLogin naya). ZIP-README.txt / local.properties /
  keystore / apk GitHub par NAHI bheje (repo abhi public hai).
- Manifest note: push se pehle manifest par vc65/1.64 live tha (doosri
  taraf se update hua tha, apkUrl v1.64-debug wali) — uske upar vc67/1.66
  likha (RAW JSON, base64 wali ghalti nahi), get_file_contents se live
  verify: {"versionCode": 67, "versionName": "1.66", apkUrl v1.66 wali}.
- FLAG: Login/SignUp GUI is version me aa gaya hai jabke 2026-10-31 11:45
  ko user ne kaha tha auth wo khud baad me advance tareeqe se banayega —
  uski is file bhejne + upload order par push kiya hai; agar nahi chahiye
  to revert karna hoga (manifest wapas vc64 + code se SplashLogin hata).

## v1.66 change (31 Oct 2026) — back tarteeb, 5s intro, Login + Sign Up GUI (DEBUG, phone pe NAHI gayi)
- BACK STACK (MainActivity): go(X) = khule page ko stack me daal ke X kholo;
  goBack() = stack se pichhla page (khaali ho to Home). Saare sub-pages ka onBack
  ab goBack(); page-se-page raste (Settings rows/tiles, Opening → Camera/Viewer,
  Tom → TOM Count, Chat Settings → WA Format, App Info → System & Storage,
  Login → Sign Up) go() se. Dock tap aur Home/drawer se khulne wale pages stack
  saaf karte hain (unka back seedha Home). navBack flag se wapsi pe ulti slide.
  Emulator pe dekha: Settings → Calculator → back = Settings; Settings → Logout
  → Login → Sign Up → back = Login → back = Settings.
- INTRO (SplashLogin.kt SplashScreen, SPLASH_MS = 5000): logo pop-in + saans
  leti roshni, neeche 4 badalti English lines, gradient progress bar + % + dots.
  Bug fix: splash pehle tap rokta nahi tha (clickable enabled=false) — tap peeche
  Home tak jata tha; ab tap consume hota hai.
- LOGIN / SIGN UP (sirf GUI, pageState me values): Login = Employee ID/Gmail,
  password (aankh se dikhao/chhupao), Remember me, Forgot ID, Log In, "Create new
  account". Sign Up = naam, Employee ID, mobile, Gmail, station picker, shift,
  role, password + confirm (match na ho to laal note), confirm checkbox, Sign Up.
  Rasta: drawer My Account → Log Out, aur Settings ka Logout button. Dock in pe
  chhupa rehta hai.
- Settings ke andar ke pages (XHeader: Chat Settings, App Info, System & Storage,
  TOM Count waghera) bhi sabz se Settings ke jamni-gulabi rang me.
- PremiumRequestModal ki Roman Urdu line English ki (in-app English rule).

## v1.65 change (31 Oct 2026) — Settings naya rang, chat icon, calculator notes (DEBUG, phone pe NAHI gayi)
- Setup note: v1.64 zip me keystore (jaan boojh ke) aur app/build.gradle.kts
  (ghalti se) nahi the — dono v1.61 se liye; v1.61 folder D:\MetroPlus-v1.61-old.
  v1.64 ka apna change HANDOFF me likha nahi tha.
- Drawer row label "app Setting" → "Settings" (onMenu key wahi "app Setting").
- SettingsScreen poora naya: sabz hataya — hero indigo → jamni → gulabi, profile
  card hero ke andar (asal logo, tap = Edit Profile), QUICK SETTINGS 4 dabbe
  (Profile, Calculator, GPS, TL/RP → drawer wale pages; MainActivity onOpenPage me
  naye raste), group dabbe apne rang (Account teal, General gulabi, App neela),
  jamni→gulabi toggles, Logout gradient button. WhatsApp Report Format row HATA
  di (WA_FORMAT page/route code me hain, bas Settings se rasta nahi).
- Home chat button: sabz → teal 3D dabba + safed Forum icon + laal "3" badge (GUI).
- Calculator setting: chhote rang dabbe ki jagah Money Calculator wala asli note
  badge (MoneyCalculatorScreen.kt me internal NoteBadge + NoteColors; DenomRow bhi
  isi ko use karta hai); 5/2/1 gol sikke.
- Verify: emulator pe Home header, drawer, Settings (dono hisse), Calculator dekhe.

## v1.63 change (31 Oct 2026) — dock 4 tabs + 63 jaisa updater (DEBUG)
- Uska order (14:06 PKT): neeche dock se Settings icon hatao (sirf 4 rahenge:
  Opening, Home, Closing, Portal); Settings ab bhi drawer ki "app Setting"
  row aur Home ki Settings entry se khulti hai — NavTab.SETTINGS enum aur
  baaki raste chhede nahi, sirf dock ki list se entry hatayi hai.
- Uska order: update 63 jaisa same-to-same ho, nazar aaye ke kaise ho raha
  hai. 63 ke 60-updater.js se parha: silent top bar (gradient line + safed
  strip "Updating… X%"), 100% pe bar band + "Installing update…" toast phir
  install, fail pe "Download Fail" popup + Try again, manual check pe
  "You already have the latest version" toast. Native me farq tha: server
  size (content-length) na bheje to progress -1 rehta tha aur strip par
  sirf "Updating…" likha aata tha, % nahi — isi liye v1.62 ka update atka
  hua lag raha tha.
- Fix: UpdateManager.downloadUpdate ab (pct, downloaded, total) bhejta hai;
  strip par % mile to "Updating… X%", na mile to "Updating… X.X MB"
  (downloaded MB) — progress hamesha nazar aayega. 100% pe confetti ke
  sath "Installing update…" toast (63 jaisa). Fail pe bar chhupne ke sath
  "Download failed — please try again" toast (63 ke fail popup ka toast
  version; retry agle foreground check ya drawer Check Update se).
  Confetti wahi v1.54 wala (63 me relaunch pe thi, native me 100% pe).
- Verify (build 14:13 PKT): APK 12,452,343 bytes, SHA f832b144…, cert
  b13501ef EXACT match (v1.62 ke upar in-place update hogi), dex me
  "Version 1.63" + "Installing update" + "Download failed" + "Updating" + "MB".
  Dock 4-tab change compile se guzra (when exhaustive ke liye else branch).
- Manifest LIVE (14:38 PKT): vc64/1.63 JSON, commit 95c778e1, blob 30894f88,
  get_file_contents se verify. NOTE: pehli koshish (ef552c51) me content
  base64 de diya tha to file me base64 string save ho gayi thi — pakar ke
  raw JSON se dobara likha. Aage se manifest me content HAMESHA raw JSON.
- GitHub code head: 024a7066 (MainActivity, UpdateCheck, build.gradle, HANDOFF).

## v1.62 change (31 Oct 2026) — 63 ke reh gaye GUI pages, SIRF GUI (DEBUG, phone pe NAHI gayi)
- Uska order (11:40 + 11:45 PKT): comparison SIRF GUI ka ho, asli kaam baad me;
  Login/Signup/Onboarding abhi NAHI banana (wo wo khud baad me naye advance
  tareeqe se banayega). Baaki GUI gaps abhi bane — nayi file ui/ExtraGuiScreens.kt.
- Naye pages (63 ke raste wahi): TL PIN Lock (Settings ki TL/RP Login row se —
  blurred home, safed card, img_tl_padlock, "Enter Your 5 Digit PIN" + 5 boxes +
  keypad; 5 digit par sirf GUI note), Chat Settings (Bubble size/color/background
  + WhatsApp Format row), App Info (asal installed version + package + Check
  Update jo maujooda updater chalata hai + System & Storage row), System &
  Storage (demo stats + Sync/Clear/Export/Import/Delete rows), WhatsApp Format
  (paste box + Opening/Closing format fields + Save note), TOM Machines
  (1–5 options, 63 ka text wahi), GPS Photo Viewer (crop frame toggle + Share
  note), Splash (cold open pe 1.5s, phir Home).
- Maujooda screens me: Settings APP group me System & Storage + WhatsApp Format
  rows wapas (63 ki rows; v1.54 ki "bas" katoti sirf in dono rows tak khatam —
  warna naye pages ka rasta hi na hota), Tom Data hero shift chip ab
  OptionPickerSheet kholti hai (Morning/Evening, GUI state), hero ke neeche
  "TOM Machines: 3 machines" row (TOM Count page ka rasta), Opening Staff tab
  me week strip (aakhri 7 din, demo dots), Opening GPS tab ki photo tap par
  viewer, GPS Camera par "View Attendance" pill + attendance popup (demo staff
  + Share note), drawer TL/RP page par "Request Premium Access" button +
  premium request modal (naam/Gmail + Send note).
- Asli kaam NAHI hua (GUI phase): koi save/share/Firebase/camera/login wiring
  nahi; toggles/chips sirf GUI state; App Info version/package asal hain.
- Verify (build 12:01 PKT): BUILD SUCCESSFUL, APK 12,451,651 bytes, SHA
  4561f84c…, cert b13501ef EXACT match (wahi keystore — in-place update
  hoga), dex me "Version 1.62" + saare naye labels mile (TL PIN, System &
  Storage, WhatsApp Format, TOM Machines, Premium Request, View Attendance,
  This Week, Crop frame, Staff Attendance, splash text). Manifest bhi update
  ho gaya (12:31 PKT, commit be283d9c) — phone par agle open par v1.62 ka
  update aayega (link 2 Nov 2026 tak valid hai).

> **PRIVATE:** `keystore/debug.keystore` is the app's signing key. Keep this zip private.
> Without this exact key, new builds will NOT install over the app on the phone.
> Store password `android`, alias `androiddebugkey`, key password `android`.
> Cert SHA-256: `b13501ef91dd30d621c645ea596010d57446a26e2aa5ce26215cc7dc28019302`.

## v1.61 change (31 Oct 2026) — drawer settings pages advance GUI + rang (DEBUG, phone pe NAHI gayi)
- Uska order: drawer ki har setting (profile, Gps, Calculator, app Setting) ki GUI
  advance, cards, sab enhance, pyare gradient; baaki screens ke rang bhi.
- DrawerPages.kt poora naya: DpHero (gradient hero + bara 3D icon), DpSection
  (header pe 3D tile), gradient DpToggle/DpChoice/DpBigButton, DpStat.
  Edit Profile: avatar+naam+role/shift chips hero me, "Profile complete" %
  bar. Calculator: summary (enabled/notes/coins) + Enable all / Notes only +
  2-column denomination cards. Gps: status stats, Photo stamp (station +
  NAYE GUI toggles date/time, coordinates), accuracy Balanced/High/Max, map
  style preview cards. Tl/Rp: wahi content (contact + WhatsApp, TL login,
  role access with tags) naye cards me. Saari values pageState keys wahi.
- SettingsScreen: sabz gradient header, profile card teal→lime, group titles
  pe sabz patti. Opening: hero sunehra→narangi→gulabi, tiles pe halka rangeen
  gradient + 3D arrow. Closing: hero neela→jamni→magenta, cards lavender,
  section titles pe patti, PMBS arrow 3D.
- Sabak: Glow/Watermark ko hero me matchParentSize Box me rakho, warna hero
  lamba ho jata hai (Opening/drawer heroes me hua tha, theek kiya).
- Verify: build kamyab; emulator pe drawer pages + Opening dekhe. Settings/
  Closing ka aakhri screenshot session band hone se reh gaya.

## v1.60 change (31 Oct 2026) — hang fix + Home rang khile hue
- Uska order: poori app me hang/stuck ki kami theek karo; Home + rang pyare.
- HANG: emulator pe napa (scratchpad perf.ps1: Home scroll + banner swipe +
  Tom Data scroll, dumpsys gfxinfo). Wajah: debuggable APK — Android isko
  optimize nahi karta. Fix: debug buildType me isDebuggable = false (R8 ab bhi
  band, wahi keystore, assembleDebug hi). Natija: app khulna 6.0s → 2.2s,
  janky frames 30.7% → 21.6%, p90 61ms → 36ms; APK 17.6 MB → 12.4 MB.
  (Emulator software GPU pe hai — phone pe aur behtar. Aur behtari: final pe
  R8 release.) Note: ab debugger attach nahi hota — zaroorat ho to flag hatao.
- RANG: Home banners/minis/hubs ab vivid() 3-rang gradients (hue badalta),
  v1.58 ke phike ek-rang tint ki jagah.

## v1.59 change (31 Oct 2026) — purane logos hi, M logo enhance, shine hata, DEBUG build
- LOGO FAISLA (uska): saare feature/hub logos PURANE hi rahenge (img_money,
  img_tom, img_gps, img_portal, img_complaint, staff/leave 3d, padlock, Tom
  icons, ic_dock_* hubs). Rad kiye hue: haath ke bane vector 3D logos (2 dafa,
  "simple/ajeeb"), naya vector M, aur Microsoft Fluent Emoji 3D options —
  in me se kuch bhi app me NAHI. Dobara logo badalne ka mashwara na dena jab
  tak wo khud na kahe.
- Main M logo: asal logo_hd hi, sirf LogoEnhance ColorFilter (saturation 1.28,
  contrast 1.12, halki roshni) header + drawer pe; header me 46 → 50dp.
- Modifier.shine poori tarah hata di (calls + function) — logos pe koi
  animation nahi.

## v1.58 change (31 Oct 2026) — Home rang ek parivar + logos pe shine, DEBUG build
- Uska order: logos pyare + shine, Home ke alag alag rang achhe karo.
- Home banners/minis/hubs ab tintStops(section rang) se (bannerStops /
  miniStops / hubStops) — pehle har dabbe ke alag haath ke stops the. GPS
  accent teal 0891B2 (Portal neele se alag). Purane Yellow/Peach/Blue/Purple
  stops hata diye.
- Modifier.shine() (ui/Icon3D.kt): upar halki chamak + har ~4s tirchhi
  safed lakeer, SrcAtop offscreen (sirf tasveer pe). Laga: Home logo, banner
  art, mini images, hub icons, drawer avatar, Opening tiles/bus/leave, Money
  image, Settings padlock, Portal logos, Tom Data card icons.
- Logos ki nayi tasveerein NAHI banayi (asal art wahi) — sirf chamak/rang.

## v1.57 change (31 Oct 2026) — Home pe rang + 3D, DEBUG build, no R8
- Uska order: Home pe bhi overall rang + 3D icons.
- Banners/minis/hubs ko accent (section rang): kicker usi rang me, banner ka
  kala "Open" ab section rang ka 3D pill, minis + hubs ke safed arrow gole ab
  Icon3DTile. Hub tags Section.Opening/Closing/Portal. GPS = 2563EB.
- Section titles (More with Metro, Hubs) se pehle laal gradient patti; Home
  background safed → #F3EDED → halka gulabi gradient; chat button pe chamak
  + laal saya (63 wala bubble wahi).
- Section.Money B7791F → E29A0C (bhoora lagta tha).
- Welcome card, mascot, dock, tile gradients NAHI badle.
- Emulator (Medium_Phone_API_37) pe install + screenshot se dekha.

## v1.56 change (31 Oct 2026) — rang-nizaam + 3D icon tiles, DEBUG build, no R8
- Uska "ok kroo" (ideas 1, 2, 6): nayi file ui/Icon3D.kt.
- Brand laal: BrandRedLight E0454D, BrandRed B02A31, BrandRedDark 9E1F25,
  BrandRedDeep 7A1A1F. 11 ui files me ~20 milte-julte laal (E11D2E, C6434A,
  B91018, A32228, D93C44 ...) inhi tokens pe. NAHI chheda: Home welcome card,
  DockIcons, Confetti, MainActivity drawer header (user ke manzoor shuda).
- Section rang (object Section): Opening narangi, Closing purple, Money
  sunehra, Tom narangi-peach, Portal neela, Complaints violet, Settings sabz.
  Lage: Settings toggles sabz + har row ka apna rang; Portal buttons/icons
  neele; Money "TODAY'S TOM CLOSING" sunehra; Tom Data header icon + shift
  chip + History accents narangi. Heroes jo user ne khud chune the (Tom Data
  neela/gulabi, Money total card) NAHI badle.
- Icon3DTile (squircle gradient + upar chamak + kinari + rangeen saya):
  Settings rows, drawer rows, drawer pages ki Gps rows, Closing tiles,
  Complaint type cards.

## v1.55 change (31 Oct 2026) — code review fixes, DEBUG build, no R8
- Keyboard: edge-to-edge me koi imePadding nahi tha — chat/form ka neeche wala
  field keyboard ke peeche chhupta tha. Ab overlay ka neeche ka gap =
  max(keyboard, nav bar + 76dp dock), manifest me adjustResize, keyboard khula
  ho to dock chhup jata hai.
- Values page band karne pe mit jati thin: nayi file ui/PageState.kt
  (PageStateViewModel + pageState/pageList/pageMap) — chat messages, Edit
  Profile, Calculator/Gps/TlRp settings, Staff Edit, Complaint form, Portal
  login ab Activity ViewModel me.
- TL/RP WhatsApp button + Tom Data WhatsApp buttons ab img_icon_whatsapp_clean
  (grey dabba khatam; Tom Data pe chat icon ki jagah WhatsApp logo).
- Dock se page→page (aur camera se wapas) ab AnimatedContent slide; pehle
  content achanak badal jata tha.
- TL RP Chat tab me send ke baad scroll filtered list ke aakhri item tak.
- Home shift/date chip foreground pe dobara banta hai.
- Verify: vc56/1.55, DEBUG, cert b13501ef, SHA fbaa92ea, 17,647,876 bytes.
  Updater/manifest pe NAHI dala (user confirm ke baghair nahi).

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

## v1.64/vc65 (2026-10-31, main chat se)
- Dock order: Home, Opening, Closing, Portal (uske order pe).
- Swipe-back (MainActivity.kt, overlay level): page finger ke sath left khisakta hai;
  swipe page ke RIGHT half se shuru ho; aadhi screen tak ya tez flick (< -1000 px/s)
  par system back dispatch hota hai (sheet khuli ho to pehle sheet); wapas right
  kheenchne par cancel (spring-back); close par exit bhi left ko slide hota hai
  (swipeClosing flag). Back button waisa hi hai.
- Version bump TEEN jagah (rule): build.gradle.kts vc65/1.64 + drawer pill "Version 1.64"
  + UpdateCheck.kt CURRENT_VERSION_CODE = 65 (ye chhootne par install ke baad update
  dobara offer hota — v1.64 ki pehli build me yehi ghalti hui thi, dobara build ki).
- APK: MetroPlus-v1.64-debug.apk, 12,455,087 bytes, SHA 1b8f705f, cert b13501ef.
  Manifest live (commit 8190e44c). Code metroplus-native/ par push pending hai.
