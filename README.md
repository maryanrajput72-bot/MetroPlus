# 🚇 Metro + | Red Line Staff Admin Control Center

An elite, high-performance administration console engineered for the **Metro+** staff app (`com.muneeb.metroapp`), governing all **24 Red Line Stations** (Rawalpindi Saddar to Islamabad Pak Secretariat).

Built with the **Signature Royal Luxury Dark & Gold Theme** inspired by Royal Cart, fully tailored to the specifications in `MetroPlus-AdminPanel-Source-v2.1.60.md`.

---

## 🌟 Core System Modules

1. **👑 Signature Royal Gold Theme**
   - Deep luxury charcoal background (`#0B0B0C`, `#14110E`), antique gold accents (`#C9A24A`, `#FFF1C4`), emerald operational indicators (`#55D88D`), and warm card trims.
   - Elegant typography with `Cinzel` display serif and `Inter` operational UI font.

2. **🌐 24 Red Line Stations Corridor Radar (Canvas)**
   - Interactive animated canvas visualizing all 24 stations along the Rawalpindi-Islamabad corridor from **Saddar (1/201)** through **Pak Secretariat (24/224)**.
   - Distinguishes **Rawalpindi Zone** (Stations #1 to #10) and **Islamabad Zone** (Stations #11 to #24).
   - Moving train pods, pulsing station beacons, and click-to-select station telemetry.

3. **📱 Staff & App Versions Monitor (`staff_versions`)**
   - Document ID = Employee ID (UPPERCASE).
   - Tracks staff across update channels: **Main Production (v2.1.31 / vc339)** vs **Trial (v2.1.60 / vc368)**.
   - Flags outdated versions automatically and checks 30-second heartbeat presence (`presence/{EMPID}`).

4. **📋 Daily Attendance & Roster (`records`)**
   - Roster per station + shift (Morning/Evening) with real 12-hour timestamps.
   - Statuses: **ON DUTY** (stored status `''`), **OFF**, **WAITING**, **LEAVE**, **ABSENT**.
   - **Byte-Exact WhatsApp Report Copier**: Generates the exact text note matching the mobile app.

5. **📸 Station GPS Camera Photos (`photos`)**
   - Real-time CameraX GPS captures with latitude, longitude, exact address, and timestamp.
   - Modal inspection for CameraX split stamps.
   - **Total Cloud Deletion**: Strictly deletes the cloud doc as per v2.1.59+ rule.

6. **🔑 TL Access & PIN Manager**
   - Team Lead Gmail + 5-digit Password PIN management for app login validation.
   - Feature flag toggle: **`TL_OPEN_FEATURES`** (Controls the Team Lead feature gate).

7. **🚀 Updater & Release Controller (`version.json`)**
   - **Hard Rule (Signal 7280)**: The MAIN channel moves ONLY on the owner's 4-digit code **`7280`**.
   - Trial Channel & `trial.testers` list management (e.g. `["3526", "MARYAN"]`).

8. **🧾 RMBS & Card Claim Closing Reports**
   - 5-TOM closing balances calculator generating `{SM|SE},SN{num},BC{total},TS00...`
   - Card Claim WhatsApp generator (`*Card claim Detail*...` with dot-separated station names).

---

## 🚉 24 Red Line Stations Reference

| # | Station Name | Code | Cloud Slug | Zone |
|---|--------------|------|------------|------|
| 1 | Saddar | 1/201 | saddar | Rawalpindi |
| 2 | Marrir Chowk | 2/202 | marrir_chowk | Rawalpindi |
| 3 | Liaqat Bagh | 3/203 | liaqat_bagh | Rawalpindi |
| 4 | Committee Chowk | 4/204 | committee_chowk | Rawalpindi |
| 5 | Waris Khan | 5/205 | waris_khan | Rawalpindi |
| 6 | Chandni Chowk | 6/206 | chandni_chowk | Rawalpindi |
| 7 | Rehmanabad | 7/207 | rehmanabad | Rawalpindi |
| 8 | 6th Road | 8/208 | 6th_road | Rawalpindi |
| 9 | Khatam-e-Nabuwat | 9/209 | shamsabad *(Legacy ID)* | Rawalpindi |
| 10 | Faizabad | 10/210 | faizabad | Rawalpindi |
| 11 | IJP | 11/211 | ijp | Islamabad |
| 12 | Potohar | 12/212 | potohar | Islamabad |
| 13 | Khayaban-e-Johar | 13/213 | khayaban_e_johar | Islamabad |
| 14 | Faiz Ahmed Faiz | 14/214 | faiz_ahmed_faiz | Islamabad |
| 15 | Kashmir Highway | 15/215 | kashmir_highway | Islamabad |
| 16 | Chaman | 16/216 | chaman | Islamabad |
| 17 | Ibn-e-Sina | 17/217 | ibn_e_sina | Islamabad |
| 18 | Kachehry | 18/218 | kachehry | Islamabad |
| 19 | PIMS | 19/219 | pims | Islamabad |
| 20 | Stock Exchange | 20/220 | stock_exchange | Islamabad |
| 21 | 7th Avenue | 21/221 | 7th_avenue | Islamabad |
| 22 | Shaheed-e-Millat | 22/222 | shaheed_e_millat | Islamabad |
| 23 | Parade Ground | 23/223 | parade_ground | Islamabad |
| 24 | Pak Secretariat | 24/224 | pak_secretariat | Islamabad |

---

## 🚀 Running the Project

```bash
cd "d:\project\admin panel"
npm run dev
```

Open [http://localhost:3000](http://localhost:3000) in your browser.
