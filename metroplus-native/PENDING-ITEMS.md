# Zabii ki dictation — ek ek karke update karni hain (30 Oct 2026, shaam)

> ALLOW MIL GAYA (30 Oct 2026 ~15:55 PKT) — ek ek cheez DETAIL se banana
> hai: ek cheez bana lo, phir doosri, phir teesri, isi tartib se.
> DELIVERY CORRECTION (30 Oct 2026 ~16:01 PKT, uska wazeh order):
> banana ek ek karke hai, lekin UPDATER pe ek ek karke NAHI dena —
> saari batayi hui cheezein ek hi UPDATE me deni hain. v1.40 (sirf
> Item 1) pehle wale one-by-one order pe live ho chuki hai — usko wapas
> nahi lena; items 2–7 ek ek karke banenge aur AKHRI me EK hi version
> (v1.41) me updater pe jayenge, beech me koi version nahi.

## Agla batch — NOTE (31 Oct 2026, raat) — v1.54/vc55 me BAN GAYA (GUI ONLY)

> Uska order (31 Oct 2026 ~00:48 PKT): batch ki saari cheezein SIRF GUI
> me update karni thin — v1.54/vc55 (DEBUG, cert b13501ef, GitHub push +
> manifest live verified, koi link nahi bheja) me ye sab GUI ban gaya hai.
> Functionality/wiring (Portal link, chat Firebase, camera live, save
> actions) BAAD ka step hai. Neeche khule sawaal abhi bhi khule hain.
>
> KHULE SAWAAL (agle batch se pehle jawab chahiye):
> - Slide item 2 ko banner auto-slide samajh ke band kiya hai (upar wale
>   banners ka auto-slide). Agar page back-slide thi to batana.
> - TL/RP Settings WhatsApp button ka number TBD (abhi button GUI note
>   dikhata hai).
> - TL/RP login main Settings me bhi lana hai ya drawer me hi rehne dena
>   hai — PREMIUM ACCESS row dono jagah wahi hai, chheda nahi.

### Batch item 1 — Portal ka design (63 jaisa poora)
- v1.52 wala Portal screen usne dekha, lekin 63 jaisa nahi laga.
- 63 me Portal ke UPAR LOGOS lage hue the — header me 63 wala portal
  logo (img-portal) + "Online Portal" title waisa hi lagana hai.
- Website jahan khulti thi (iframe wali body): wahan LINK laga do, ya
  DUMMY show kar do. Abhi GUI phase hai — asli link BAAD me dalenge;
  filhal body me website ka dummy preview dikhana hai (portal site ka
  login page jaisa dummy).
- Matlab: Portal screen = 63 wala header (logo + title) + body me dummy
  website preview + link ki jagah (asli link baad me).

### Batch item 2 — Slide filhal rok do
- Jo slide (upar wali) backward slide ho rahi hai, usko FILHAL ROK DO.
- Jab usko chahiye hogi to wo phir se ADD karwa lega (slide wapas
  lagani hai uske kehne pe).
- NOTE (wazahat baaki): "upar wali slide" se kya matlab hai — home ke
  upar wale banners ka auto-slide, ya page wapas (back) jane wala slide?
  Us se ek lafz me confirm karke hi is item pe haath lagana hai.

### Batch item 3 — Home screen se search bar + refresh hata do
- Home screen ke header me jo SEARCH wala bar (pill) hai aur jo
  GHOOMNE wala REFRESH wala button (laal Refresh icon, updater check)
  hai — DONO yahan se KHATAM kar do.
- Wajah (uski): dono hatne se cards thore UPAR ho jayenge aur neeche
  wale bhi upar ho jayenge — thora achha lagega.
- Code nishan: HomeScreen header — search pill (44dp) + refresh circle
  (48dp, onRefreshClick = manualCheck). Search ki filtering (mini cards)
  bhi isi ke sath jayegi.

### Batch item 4 — Message (chat) button, 63 jaisa + andar 63 wala chat
- Home header me LIVE badge ke SATH wala MESSAGE (chat) button
  (ic_chat_bubble) — 63 APK se dekh ke US JAISA design karo.
- Uske ANDAR 63 wali hi cheez daalni hai — Group Chat, uske andar jo
  ek ek cheez thi wo SAARI fix/banao (poora 63 wala chat experience).
- Abhi ye button sirf "Group Chat" GUI note dikhata hai — is batch me
  uska poora design + andar ka chat banana hai.

### Batch item 5 — TL/RP Settings: WhatsApp button upar, neeche wala text khatam
- UPAR: jahan "Contact to admin for access" likha hai (TL LOGIN se
  pehle wala note card), wahan ek PYARA SA WhatsApp button bhi lagana
  hai — button pe SIRF "WhatsApp" likha ho (aur kuch nahi).
- Button ka kaam: admin se rabta (WhatsApp khole). Admin ka number /
  kis chat pe khule — ye us se poochna hai, apni taraf se number NAHI
  daalna.
- SAB SE NEECHE: "TL ACCESS" wala card (jo text hai ke "TL login
  admin-controlled hai: Zabii admin panel ke 'TL Access' tab me har TL
  ka Gmail + 5-digit password darj karta hai — app usi se match karke
  login deti hai") — ye FALTU letters samajh ke POORA KHATAM karna hai.
- Baaki wahi rahega: upar contact note + WhatsApp button, TL LOGIN
  card (Gmail + password), ROLE ACCESS card.

### Batch item 6 — App ke andar ke popup/toast/note SAB English me
- NAYA RULE (sirf APP ke andar ke text ke liye — chat replies ka Roman
  Urdu rule wahi rahega): jahan bhi app ke andar popup, toast, dialog
  ya note Roman Urdu me aata hai, wo SAARA English me aana chahiye.
- Misalein (uski): refresh ho gaya, app update ho gayi / ho rahi hai.
- Abhi Roman Urdu me maujood (build ke waqt poora audit karna hai):
  notYet dialog ("GUI phase: ye screen abhi sirf design me hai, iska
  feature agle step me banega"), updater toasts ("App pehle hi latest
  version pe hai", "Update check nahi ho saka, internet check karein"),
  logout note ("GUI phase: logout ka feature baad me"), aur screens ke
  andar ke Roman Urdu helper notes (jaise Portal/Data-Fill notes).
- In-app text ka mustaqil rule ban jayega: app ke andar English,
  chat me Roman Urdu.

### Batch item 7 — 63 jaisa SILENT updater (interface samet)
- 63 APK me dekh ke batana + lagana hai: 63 me updating kaise SILENTLY
  ho rahi thi aur update karte hue uska INTERFACE kaisa aata tha.
- 63 wo update SAARE mobiles pe silently karti thi — wahi feature is
  native app me bhi chahiye, usi interface ke sath.
- Research source (build se pehle detail me nikaalna hai):
  trial 63 ka 60-updater.js — maybeAutoStartSilentUpdate,
  showSilentUpdBar / hideSilentUpdBar, startSilentUpdPoll, updBarHtml,
  showUpdProgressCard (aur update ke waqt dikhne wala progress card/bar).
- NOTE (pehle se bataya hua sach, batch build ke waqt phir saaf karna
  hai): native updater ka in-app hissa pehle se silent hai (5s check,
  upar progress line, auto-install); aakhri system installer ki
  "Update" tap Android ka rule hai. 63 ne isko kaise handle kiya tha —
  yehi research me nikaal ke usko batana hai, phir usi ke mutabiq
  lagana hai.

### Batch item 8 — Animation directions (LAZMI NOTE — mehfooz rakha hai)
> Uska sakht order (31 Oct 2026 ~00:39 PKT): ye lazmi note karna hai,
> apne paas mehfooz rakhna hai, aur GUI me add karna hai.

#### 8a. Neeche wala static dock — Home select pe Settings fade se band hota hai (BUG)
- Dekha hua masla: dock se Home select karo ya Settings select karo to
  page slide ho ke right side se left side pe aata hai (khulna theek
  hai) — LEKIN jab Home screen select karte hain to Settings wala page
  sirf FADE se band hota hai, slide se nahi.
- Chahiye: Settings (ya koi bhi page) band karte hue wo LEFT se RIGHT
  slide hoke band ho — yaani page right ki taraf khisak ke jaye, sirf
  fade na ho. (Uska lafz: "left se right close hona chahiye".)
- Code nishan (build ke waqt diagnose karna hai): MainActivity ka
  sub-screen overlay — AnimatedVisibility, enter = slideInHorizontally
  (right se) + fadeIn, exit = slideOutHorizontally (right ki taraf) +
  fadeOut, 350ms FastOutSlowIn. Dock se Home pe exit animation fade
  jaisi kyun lagti hai — yehi dekh ke set karna hai.

#### 8b. Drawer (slider) se khulne wale pages — ULTI direction (NAYA RULE)
- Drawer = upar profile pe click karke jo slider aata hai. Us se koi
  cheez kholo to:
  - OPEN: LEFT side se RIGHT side pe (page left se aake right ki
    taraf slide hoke khule).
  - CLOSE: RIGHT se LEFT side pe (page right se wapas left ki taraf
    slide hoke band ho).
- OVERRIDE FLAG: Item 7 ke note me drawer pages ke liye Item 6 wali
  animation likhi thi (khulna right se left, band hona right ki taraf).
  YE NAYA ORDER usko drawer pages ke liye OVERRIDE karta hai — drawer
  pages ki direction ab upar wali (left→right open, right→left close)
  hai. Normal (dock se khulne wale) pages ki direction wahi Item 6 wali
  rahegi: khulna right→left, band hona left→right.

### Batch item 9 — Main Settings screen dobara set karni hai
> Target screen: dock wali main Settings (SettingsScreen.kt) — drawer
> wali TL/RP ya GPS setting NAHI. Abhi sirf itna theek karna hai;
> Settings me aur cheezein BAAD me dalni hain (uske kehne pe).

#### 9a. Sab se upar wala pehla card (ProfileCard) — GREEN gradient, chhota, pencil nahi
- Card ka rang: GREEN + LIGHT GREEN gradient me karna hai.
- Card pe likha hoga: pehle ROLE ka word (Operator ya TL ya jo bhi ho)
  aur aage BARA NAME (user ka naam bara).
- Card ko 63 SE DEKH KE THORA CHHOTA karna hai (63 ke card jaisa size).
- Is card me aage PENCIL ka nishan NAHI aayega (ProfileCard ka white
  Edit icon khatam).
- Card ke NEECHE: Supervisor Name wali row rahegi — aur us row pe jo
  PENCIL ka icon hai wo COLORFUL karna hai (card wali pencil khatam,
  supervisor row wali pencil colorful).

#### 9b. General Preference — naya tab/section + dropdown, andar 3 cheezein
- "General Preference" ka ek TAB (section) banao, us me dropdown ho
  taake wahan se khol sakein.
- Uske andar ye TEENON daal do (abhi ye teeno alag rows me hain):
  1. Dark mode ("Red & black theme (off = white & red)").
  2. Push Notifications ("Chat messages & alerts").
  3. Sounds & Vibration ("Notification sound + vibration").

#### 9c. Main Settings se Metro & GPS Settings KHATAM — baaki sirf 2 rows
- "Metro & GPS Settings" wali row main Settings ke andar se KHATAM kar
  do — kyunki wo side drawer me aata rahega, wahan se easily ho jayega.
- Main (app) Setting me BAS ye 2 rows rahengi (profile card + General
  Preference tab ke ilawa): Chat Settings + App Info.
- NOTE (build ke waqt verify karna hai): drawer me GPS setting wali row
  maujood honi chahiye — agar na hui to usko batana hai, apni taraf se
  nahi daalni.

#### 9d. TL/RP login wali line — SAAB NAHI HUI (poochna lazmi hai)
- Uska jumla ("TL/RP login ... yahan setting me isko premium ... kyunki
  wahan pe aa raha hai, doosri setting me aa raha hai") dictation me
  saaf nahi hua — samajh nahi aaya ke TL/RP login ko MAIN Settings me
  bhi lana hai, ya sirf drawer wali (doosri) setting me hi rehne dena
  hai kyunki wahan pehle se aa raha hai.
- Is item pe us se ek lafz me confirm kiye baghair haath NAHI lagana.

### Batch item 10 — Complaints ka UI pyara karo (63 jaisa) + POS Card Complaint section
> Target: Complaints — "All Complaints" list + har type ka form.
> Abhi sirf NOTE hai; content/design 63 se dekh ke banana hai.

#### 10a. Bahar wale type cards — pyare + achhe colours (63 jaisa)
- TOM Complaints ka UI abhi achha/pyara nahi bana hua — isko THORA
  PYARA banao, JAISE 63 APK ME THA.
- Cards thore pyare banao aur COLOURS bhi achhe karo.
- Jo LAAL rang aa raha hai (general/header) — wo bhi GRADIENT me ho,
  pyara ho, kisi AUR colour me aa jaye: BLUE me ya PURPLE me.

#### 10b. Andar wale cards + neeche wali chips — sab colourful
- Kisi bhi type pe click karo to uske ANDAR jo cards hain unke colours
  bhi sahi nahi aa rahe — andar wale cards ke bhi COLOUR hon.
- NEECHE lagi hui chips bhi COLOURFUL hon.
- Ye fix HAR type pe karna hai (sirf TOM pe nahi):
  - TOM Complaints (andar ke cards + chips).
  - AGM Complaint (andar ke cards me issues hain — theek karo + chips
    me colour).
  - TKM Complaint ("ATM wale complaints" — uski dictation me ATM aaya,
    matlab TKM) — wahi cards + chips fix.
  - Other Complaints — wahi cards + chips fix.

#### 10c. NAYA section — "POS Card Complaint" (sirf shell, content baad me)
- Other Complaints ke BAAD, neeche ek AUR card/section banao — us pe
  likha ho: "POS Card Complaint".
- Uske andar kya-kya cheezein honi chahiye, wo WO BAAD ME ADD karega —
  is batch me sirf section/card banana hai, andar ka content uske
  batane pe dalna hai.
- Code note: CmpType me naya type POS add hoga (TOM/AGM/TKM/OTHER/PMBS
  ke sath); list grid me uska card aayega.

### Batch item 11 — GPS Camera: 63 jaisa CameraX camera, pehle GUI same-to-same
- GPS Camera CameraX wala banana hai (pehle wala plan wahi hai).
- 63 me camera kaisa tha — dekh ke uska SAARA INTERFACE pehle GUI me
  bana lena hai; asli live camera ka kaam BAAD me hoga (uske apne plan
  ke mutabiq: pehle GUI, phir functionality).
- Uska sawaal ("agar GUI nahi ban sakti to batao") ka jawab — HAAN, GUI
  BAN SAKTI HAI: Compose me viewfinder + controls + stamp preview wali
  GUI same-to-same ban sakti hai; live CameraX feed + GPS stamp baad
  ka step hai.
- HAQEEQAT (build ke waqt yaad rakhna): 63 me camera koi WebView UI
  NAHI tha — wo owner ka native Kotlin + CameraX Activity tha
  (gpscam/GpsCameraActivity.kt, AndroidApp.openGpsCamera() se khulta
  tha); us Activity ka source workspace me NAHI hai (trial APK me sirf
  JS bridge hai). Is liye pixel-exact 63 camera ke liye build ke waqt
  uski screenshot/reference chahiye hogi — warna maujooda native
  GpsCameraScreen (jo uski Flutter camera file se copy hui thi, v1.28)
  + 63 ke stamp/rules se GUI banegi.

### Batch item 12 — Tom Data: hero chhota + hero icons khatam + button order/colour
> Target: TomDataScreen.kt — Opening mode + Closing mode dono.

#### 12a. Pehla card (hero) — thora aur chhota, WhatsApp + Saved icons khatam
- Tom Data ka PEHLA card (front pe hero — "Daily Shift Register",
  StationHero) THORA SA AUR CHHOTA karo.
- Usi pehle card me jo WhatsApp aur Saved wale CHHOTE-CHHOTE ICON
  (HeroMini save/WhatsApp) bane hue hain — unko KHATAM kar do.

#### 12b. Opening mode — button order + naam + colour (TOM Opening Balances ke neeche)
- Abhi order hai: [Send to WhatsApp + khaali jagah] phir Save Opening
  phir Copy Opening Report. NAYA order ye hoga:
  - LINE 1 (sab se upar, full width): "Save Opening" — TOM Opening
    Balances card ke neeche PEHLE ye aayegi.
  - LINE 2 (ek hi line/row me): "WhatsApp" + "Copy" — dono ek row me.
- Naam badlo: "Send to WhatsApp" ki jagah sirf "WhatsApp" (WhatsApp
  icon ke sath); "Copy Opening Report" ki jagah sirf "Copy".
- "Save Opening" ka COLOUR bhi change karna hai — PURPLE kar lo ya koi
  aur pyara colour (primary amber abhi hai).

#### 12c. Closing mode — hero Opening jitna chhota, hero icons khatam, Save & Lock ka colour
- Closing Data ka upar wala MAIN card bhi CHHOTA karo — OPENING JITNA
  hi (12a wale shrunk hero jaisa).
- Iske upar se bhi WhatsApp aur Saved wale options KHATAM kar do.
- Neeche sab OK hai, chhedna NAHI: TKM Roll, TOM A, TOM B ("Tom Bhi
  OK hai"), Daily Summary (GrandSummary) — sab wahi rahenge.
- "Clear / Reset All Register Fields" ko REHNE DO — sahi hai, pyara
  lag raha hai (koi change nahi).
- "Save & Lock Daily Register" ka COLOUR thora change karo — ye wala
  pyara nahi lag raha (abhi primary amber hai).
- NOTE: Closing ke WhatsApp/Copy buttons ke naam is order me NAHI
  badle — sirf Opening wale badlenge (usne Closing ke liye sirf hero +
  Save & Lock colour kaha hai).

### Batch item 13 — Update pe CONFETTI (63 wali same-to-same)
- App ke andar CONFETTI add karni hai — 63 me UPDATE pe confetti bhi
  aati thi, jisse thora pyara lagta hai.
- 63 se dekh ke SAME-TO-SAME wahi wali confetti daalni hai (koi apni
  taraf se nayi confetti nahi).
- Research source (build ke waqt dekhna hai): trial 63 ka
  60-updater.js — triggerConfetti(), confettiRibbon(),
  confettiShapes(), triggerBigConfetti(), showWelcomeBurst(name) — in
  me se update-complete pe kaunsi chalti thi, wahi lagani hai.


Status: HO GAYA (v1.40/vc41, DEBUG, updater pe live verify) — greeting +
date row; DEAR/station/Welcome/Operator pehle se theek the, nahi chheda;
signup se naam aana FUTURE kaam hai (signup banega tab).

1. **"DEAR Aryan"** — abhi GUI me Aryan hi rahega. Baad me (jab signup/login
   banega) "DEAR" ke aage wo naam aayega jo signup karte hue diya gaya ho —
   wahi 63 wala feature (naam profile se aata hai). Abhi signup/login bana
   nahi hai, is liye demo naam Aryan hi rahega.
2. **Station: Shaheed-e-Millat** — abhi yehi lagega (pehle se yehi hai).
3. **Shift chip** — waqt ke hisaab se Morning/Evening Shift (pehle se yehi hai).
4. **Date shift ke NEECHE** — abhi date chip shift chip ke BARABAR me hai
   ("Fri, Oct 30"). Badalna ye hai: shift chip apni row me, aur uske NEECHE
   apni row me date aayegi — poori shakal me: "Friday, 30 October"
   (Friday + October + date, aaj ke hisaab se; format "EEEE, d MMMM").
   Matlab: shift upar, date uske neeche.
5. **"Welcome to the Metro + app"** — waisa hi rahega (pehle se hai).
6. **"Operator" badge** — waisa hi rahega (upar-daein, pehle se hai).
7. **Sab se upar greeting** — abhi "Hey There 👋" likha hai (HomeScreen header).
   Badal ke **"Hi Aryan 👋"** karna hai — "Hi" + naam (abhi demo Aryan, baad
   me signup wala naam, item 1 jaisa). Wave emoji waisa hi rakha hai, usne
   hatane ko nahi kaha.

## Item 2 — Money Calculator, Cash Denominations screen

Status: BAN GAYA (code) — local build verify baaki; UPDATER PUSH NAHI
(ek-update plan: aakhri me v1.41 me jayega)

1. **Denomination cards bare hain — fit karo (63 ke hisaab se).** Check kiya:
   TotalCard aur TomClosingCard dono me bara Watermark hai (189dp / 212dp)
   jo card ko content se zyada lamba karta hai — TomCard wali wajah hi lagti
   hai. Denom rows (59dp wali) bhi 63 se match karke fit karni hain.
2. **Neeche wala hissa OK hai — usko chherna nahi.**
3. **Copy aur Clear All ke backgrounds ka radius sahi nahi** — wajah mili:
   un dono ka background bina shape ke rectangle hai (Clear All halki-laal,
   Copy dark gradient), jabke WhatsApp wale ka background 18dp rounded shape
   ke sath hai. Is liye Copy/Clear All ke kone square nazar aate hain —
   dono ko 18dp rounded background dena hai (WhatsApp jaisa).
4. **Total Cash Amount card thoda fit karo (63 jaisa)** — upar ka text
   ("TOTAL CASH AMOUNT" + notes pill) bhi aur card bhi, 63 se match karke.
5. **Poora Cash Denomination section 63 ke hisaab se fit karna hai.**

FIX (kya kiya):
- TotalCard: Glow (160dp) + Watermark (189dp) ab `matchParentSize` box me
  hain — nazar waisa hi aayega (trial ka ::after bhi absolute hota hai),
  lekin card ab sirf content jitna (~124dp) hoga, 189dp nahi.
- TomClosingCard: 212dp Watermark hata diya (trial-copy note me is card
  ke liye watermark ka zikr nahi tha; content ~157dp tha, card 212dp ho
  raha tha).
- Clear All + Copy: background ko 18dp rounded shape de di (WhatsApp
  jaisa) — pehle background clip se pehle lagta tha is liye kone
  square nazar aate the.
- Denom rows (neeche): OK the, unko haath nahi lagaya.

## Item 3 — Complaint page + More with Metro

Status: BAN GAYA (code + local DEBUG build verify 30 Oct 2026 ~16:12) —
UPDATER PUSH NAHI (ek-update plan: aakhri me v1.41 me jayega)

1. **Complaint page 63 ke hisaab se banana hai** — trial 2.1.63 ka complaint
   page dekh ke. Page me **4 cards** honge, aur har card pe click karke
   aage jo page aata hai wo bhi banana hai — **chaaron cards ke chaaron
   detail pages** (GUI phase; function baad me, pehle wale order jaisa).
   63 ke cards ke exact naam/content build ke waqt trial se nikalne hain.
2. **More with Metro — 4 dabbe barabar size ke hone chahiye.**
   CHECK KIYA (abhi): 4 minis hain (GPS Camera, Money Calc, Complaints,
   Tom Data), 2x2 grid me. Wajah mili: har tile sirf `minHeight = 160dp`
   hai, fixed height nahi — aur sub-text ("Tap to open complaints" lambi)
   kisi tile me 2 line ho jati hai to wo tile lambi, baaki chhoti reh jati
   hai. Is liye size barabar nahi aate. Fix: chaaron ki height barabar
   karni hai (fixed/equal height, 63 jaisi grid).
   "Data chhota aa raha hai" — iska matlab yehi liya hai ke chhoti tile
   ka data chhota nazar aata hai; agar kuch aur tha to wo batayega.

## Item 4 — Opening section (Hubs → Opening → Data tab) + Staff Edit page

Status: BAN GAYA (code + local DEBUG build verify 30 Oct 2026 ~16:17) —
UPDATER PUSH NAHI (ek-update plan: aakhri me v1.41 me jayega)

1. **Roll Opening card OK, Opening Balance card OK — inko chherna nahi.**
2. **Buttons ki nayi tartib (Data tab ke neeche):**
   - Abhi 3 bare buttons ek ke neeche ek hain: "Send to WhatsApp" →
     "Save Opening" → "Copy Opening Report".
   - Nayi tartib: **sab se pehle "Save Opening"** apni poori line me
     (Save waisa hi theek hai, sirf pehle number pe aayega).
   - Uske neeche **ek hi line me 2 buttons**: **"WhatsApp"** + **"Copy"**.
   - WhatsApp wala **chhota sa** hoga, aur uska label sirf **"WhatsApp"**
     hoga ("Send to WhatsApp" nahi).
   - Copy wale pe **"Opening Report" nahi likhna, sirf "Copy"** likhna hai.
   - Matlab: line 1 = Save Opening (poori), line 2 = WhatsApp | Copy
     (aadhe-aadhe, ek line me).
3. **Staff Attendance ka Edit page (63 se):** Opening → Staff tab me kisi
   staff entry ko edit karne par jo page khulta hai, wo bhi 63 se check
   karke banana hai. (Voice me "edit" sunai diya hai; agar kuch aur tha
   to wo batayega.)

## Item 5 — Leave Form (naam chips) + Overtime Detail (Generate)

Status: BAN GAYA (code + local DEBUG build verify 30 Oct 2026 ~16:22) —
UPDATER PUSH NAHI (ek-update plan: aakhri me v1.41 me jayega)

1. **Leave Form — naam select karne par chip gayab ho jaye.**
   Abhi: staff chip pe tap karne par naam + Employee ID fill hota hai aur
   chip wahi rehta hai (sirf laal border lagta hai).
   Chahiye: naam select karte hi wo chip **saamne (list) se chala jaye**
   — select hua naam dobara list me nazar na aaye. Station ke baqi naam
   bhi aise hi select karte hue ek ek karke gayab hote jayenge.
2. **Overtime Detail — station + naam Leave Form jaisa.**
   Abhi: Overtime card me station "Select station" hai aur naam ke neeche
   "No staff saved for this station" likha aata hai (koi chips nahi).
   Chahiye: station select ho, aur naam waghera **wahi (Leave Form wale
   staff chips)** hon.
3. **Generate pe click karke neeche wale items aane chahiye.**
   Abhi: "Generate Note" button sirf GUI note dikhata hai, uske neeche
   kuch nahi aata. Chahiye: Generate dabane par **neeche generated
   detail/note wala hissa** (rows) nazar aaye — **GUI abhi bana lo taake
   baad me issue na bane** (function baad me, structure abhi).
4. **Leave Form ka bhi (GUI) kar lo** — Leave Form ka hissa bhi isi
   tarah poora karo.

## Item 6 — Closing screen + har page ki animation + silent updater

Status: BAN GAYA (code + local DEBUG build verify 30 Oct 2026 ~16:25) —
UPDATER PUSH NAHI (ek-update plan: aakhri me v1.41 me jayega)

### Closing screen
1. **END OF SHIFT hero card (sab se upar wala) thoda sahi karo — 63 se
   dekh ke.** (Voice me "end of shift wala card" — Closing ka purple hero.)
2. **TOM / TKM / POS / RMBS Closing cards OK, Card Claim bhi OK — inko
   chherna nahi.**
3. **PMBS Complaint page nahi khulta — banana hai.** Abhi PMBS Complaint
   card sirf GUI note dikhata hai. 63 se dekh ke iska page (jo 63 me
   khulta hai) banana hai. (Voice me "PMDS" sunai diya — PMBS samjha hai.)

### Har page ki animation (GLOBAL RULE — sab pages + Settings)
- Page **khulne par RIGHT se LEFT ki taraf slide** hoke aaye.
- Page **band hone par RIGHT ki taraf wapas slide** hoke jaye.
- Ye rule **jitne bhi pages ban rahe hain, sab pe + Settings pe bhi**
  lagna hai. (Code me sub-screens ke liye yehi rule likha hai — naye
  pages pe verify karke lagana hai.)

### Silent updater — FIX (user ke mutabiq abhi silent nahi ho rahi)
**Chahiye (63 ki tarah):**
1. Update mile to **khud download shuru** — koi button, koi dialog nahi.
2. **Upar patli progress line** aaye jo percentage ke sath sath bharti
   jaye (jitni % utni line), aur **percentage sath sath nazar aaye**
   (1, 2, 3... 100 tak gintee ki tarah).
3. **100% hote hi automatic install/update ho jaye** — koi tap nahi.
   Phir app automatic band ho ke dobara khule (updated version).
**Abhi kharabi (user ke phone pe):**
- Download hote hi **neeche "Update" likha popup** aa jata hai, aur
  **click karke update karni parti hai — pehle aisa nahi tha.**
- Upar wali line + percentage bhi 63 jaisi nahi chal rahi.
**Note (code check):** Code me silent system pehle se likha hai (upar
line + "Updating… X%" + 100% pe 900ms baad khud install, koi button
nahi). Phone pe neeche popup kyun aa raha hai — build ke waqt diagnose
karna hai (system installer dialog hai ya app ka koi UI; progress UI
sub-screen ke peeche chhup to nahi rahi; com.metroplus.app ki one-time
"Install unknown apps" ijazat lagi hui hai ya nahi).

## Item 7 — Drawer (slider) ke pages — sab alag alag

Status: BAN GAYA (code + local DEBUG build verify 30 Oct 2026 ~16:30) —
UPDATER PUSH NAHI (ek-update plan: aakhri me v1.41 me jayega)

Drawer me rows (pehle se maujood, sab abhi GUI note dikhate hain):
1. **Edit Profile page** — drawer ke **upar wale Edit button** se bhi
   aur **ACCOUNT section / My Account ke andar wale Edit Profile** se
   bhi yehi page khulega (ek hi page, dono jagah se).
2. **Calculator setting page** — isme **wahi settings** aayengi jo
   **63 me calculator ki settings** thi (63 wali hi daalni hai).
3. **Gps setting page** — GPS settings yahan (drawer) se hi ho sakein.
4. **Tl/Rp access setting page** — TL/RP access setting yahan se hi ho
   sake. (Voice me "TLRPSS" sunai diya — drawer ki "Tl/Rp access
   setting" row hi samjhi hai.)
5. **app Setting page** — App Settings khulengi (main Settings screen).
- **Har ek ka ALAG page banega** (ek mila jula nahi).
- **Animation:** drawer se khulne wale pages pe bhi item 6 wali animation
  (khulne par right se left slide, band hone par right ki taraf wapas).
  Slider ki direction wali baat voice me thodi unclear thi — drawer
  pehle se left se khulta hai, pages right se aate hain (item 6); agar
  drawer ki side badalni hai to wo batayega.
