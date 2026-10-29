/**
 * Live Firestore data layer for the Metro+ admin panel.
 * Mirrors the old panel (v17) and the Metro+ app:
 *  - Anonymous auth (same as v17's auth-ensure + the app).
 *  - Direct Firestore reads. Photos use a where-free full read + client-side
 *    ts-desc sort — NEVER orderBy (a where+orderBy query needs a Firestore
 *    composite index and fails without one, which broke the app's GPS swipe
 *    in 2.1.57 and was fixed in 2.1.58 by client-side sorting).
 *  - Records doc ids: {stationSlug}_{Shift}_{YYYY-MM-DD}; roster doc uses
 *    date 1970-01-01 with field staff_roster; daily docs carry field tom
 *    (flat toma..e/bop/tom/tkm fields), field attendance, and field staffDay
 *    ({rows:[{uid,id,name,time,status}]}). Writes use merge so TOM data is
 *    never touched by attendance writes.
 */
import { app, db } from './firebase';
import { getAuth, signInAnonymously } from 'firebase/auth';
import {
  collection,
  doc,
  getDoc,
  getDocs,
  query,
  where,
  setDoc,
  deleteDoc,
} from 'firebase/firestore';
import { METRO_STATIONS } from '../data/metroStations';
import type { ShiftType } from '../types/metro';

export type ConnState = 'loading' | 'ok' | 'denied' | 'offline' | 'error';

export function classifyError(e: any): ConnState {
  const code = String((e && e.code) || '');
  const msg = String((e && e.message) || '');
  if (code === 'permission-denied' || /permission|PERMISSION_DENIED/i.test(code + ' ' + msg)) return 'denied';
  if (
    code === 'unavailable' ||
    code === 'failed-precondition' ||
    /network|offline|Failed to fetch|Load failed/i.test(msg)
  )
    return 'offline';
  return 'error';
}

let authPromise: Promise<any> | null = null;

export function ensureAuth(): Promise<any> {
  const auth: any = app ? getAuth(app) : null;
  if (!auth) return Promise.reject(new Error('Firebase auth unavailable'));
  if (auth.currentUser) return Promise.resolve(auth.currentUser);
  if (!authPromise) {
    authPromise = signInAnonymously(auth)
      .then((c: any) => c.user)
      .catch((e: any) => {
        authPromise = null;
        throw e;
      });
  }
  return authPromise;
}

export function todayStr(d = new Date()): string {
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, '0');
  const dd = String(d.getDate()).padStart(2, '0');
  return `${y}-${m}-${dd}`;
}

export function normalizeShift(s: any): ShiftType {
  return /morn/i.test(String(s || '')) ? 'Morning' : 'Evening';
}

export function slugify(name: string): string {
  return String(name || '')
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, '_')
    .replace(/^_+|_+$/g, '');
}

export function stationBySlug(slug: string) {
  return METRO_STATIONS.find((s) => s.slug.toLowerCase() === String(slug || '').toLowerCase());
}

export function stationByName(name: string) {
  return METRO_STATIONS.find((s) => s.name.toLowerCase() === String(name || '').toLowerCase());
}

export function stationSlugFor(name: string, fallbackSlug?: string): string {
  const byName = stationByName(name);
  if (byName) return byName.slug;
  if (fallbackSlug && stationBySlug(fallbackSlug)) return fallbackSlug;
  return slugify(name) || 'default';
}

export function toMillis(v: any): number {
  if (v == null) return 0;
  if (typeof v === 'number') return v;
  if (typeof v === 'string') {
    const t = Date.parse(v);
    return Number.isNaN(t) ? 0 : t;
  }
  if (typeof v.toMillis === 'function') {
    try {
      return v.toMillis();
    } catch {
      return 0;
    }
  }
  if (typeof v.seconds === 'number') return v.seconds * 1000;
  return 0;
}

export function toISO(v: any): string {
  const ms = toMillis(v);
  if (ms > 0) {
    try {
      return new Date(ms).toISOString();
    } catch {
      return '';
    }
  }
  return typeof v === 'string' ? v : '';
}

function num(v: any): number {
  const n = parseFloat(String(v ?? ''));
  return Number.isNaN(n) ? 0 : n;
}

export interface RawStaffRow {
  employeeId: string;
  name: string;
  station: string;
  stationSlug: string;
  shift: ShiftType;
  role: 'TL' | 'Operator';
  versionName: string;
  versionCode: number;
  platform: string;
  updatedAt: string;
  phone: string;
  online: boolean;
  lastHeartbeat: string;
}

export async function loadStaffData(): Promise<RawStaffRow[]> {
  await ensureAuth();
  const [verSnap, loginSnap, presSnap] = await Promise.all([
    getDocs(collection(db, 'staff_versions')),
    getDocs(collection(db, 'staff_logins')),
    getDocs(collection(db, 'presence')),
  ]);
  const logins = new Map<string, any>();
  loginSnap.forEach((d: any) => {
    const v = d.data() || {};
    logins.set(String(v.employeeId || d.id || '').trim().toUpperCase(), v);
  });
  const presence = new Map<string, any>();
  presSnap.forEach((d: any) => {
    presence.set(String(d.id || '').trim().toUpperCase(), d.data() || {});
  });
  const now = Date.now();
  const rows: RawStaffRow[] = [];
  verSnap.forEach((d: any) => {
    const v = d.data() || {};
    const empId = String(v.employeeId || d.id || '').trim().toUpperCase();
    if (!empId) return;
    const login = logins.get(empId) || {};
    const station = String(v.station || login.station || '');
    const pres = presence.get(empId) || {};
    const lastMs = toMillis(pres.lastSeen ?? pres.updatedAt ?? pres.ts);
    const online = pres.online !== false && lastMs > 0 && now - lastMs < 120000;
    rows.push({
      employeeId: empId,
      name: String(v.name || login.name || empId),
      station,
      stationSlug: stationSlugFor(station, v.stationSlug || login.stationSlug),
      shift: normalizeShift(v.shift || login.shift),
      role: String(login.role || v.role || 'Operator').trim().toUpperCase() === 'TL' ? 'TL' : 'Operator',
      versionName: String(v.versionName || ''),
      versionCode: Number(v.versionCode || 0),
      platform: String(v.platform || 'android'),
      updatedAt: toISO(v.updatedAt),
      phone: String(login.phone || v.phone || ''),
      online,
      lastHeartbeat: lastMs > 0 ? new Date(lastMs).toISOString() : '',
    });
  });
  // Staff registered in staff_logins but never reported a version still show.
  logins.forEach((v: any, empId: string) => {
    if (!rows.some((r) => r.employeeId === empId) && !v.removed) {
      const station = String(v.station || '');
      rows.push({
        employeeId: empId,
        name: String(v.name || empId),
        station,
        stationSlug: stationSlugFor(station, v.stationSlug),
        shift: normalizeShift(v.shift),
        role: String(v.role || 'Operator').trim().toUpperCase() === 'TL' ? 'TL' : 'Operator',
        versionName: '',
        versionCode: 0,
        platform: 'android',
        updatedAt: '',
        phone: String(v.phone || ''),
        online: false,
        lastHeartbeat: '',
      });
    }
  });
  rows.sort((a, b) => (a.name || '').localeCompare(b.name || ''));
  return rows;
}

export interface RawPhoto {
  id: string;
  stationId: string | number;
  stationName: string;
  shift: ShiftType;
  date: string;
  ts: number;
  lat: number;
  lng: number;
  address: string;
  thumb: string;
  byPhone: string;
  byName: string;
}

export async function loadPhotos(): Promise<RawPhoto[]> {
  await ensureAuth();
  const snap = await getDocs(collection(db, 'photos'));
  const out: RawPhoto[] = [];
  snap.forEach((d: any) => {
    const v = d.data() || {};
    const st = stationBySlug(v.stationId) || stationByName(v.stationName);
    out.push({
      id: d.id,
      stationId: v.stationId ?? (st ? st.slug : ''),
      stationName: String(v.stationName || (st ? st.name : '') || ''),
      shift: normalizeShift(v.shift),
      date: String(v.date || ''),
      ts: toMillis(v.ts),
      lat: num(v.lat),
      lng: num(v.lng),
      address: String(v.address || ''),
      thumb: String(v.thumb || ''),
      byPhone: String(v.byPhone || ''),
      byName: String(v.byName || ''),
    });
  });
  // Client-side newest-first sort (never orderBy — see header note).
  out.sort((a, b) => b.ts - a.ts);
  return out.slice(0, 200);
}

export interface RawTlUser {
  id: string;
  empId: string;
  name: string;
  gmail: string;
  passwordPin: string;
  station: string;
  shift: ShiftType;
  active: boolean;
  createdAt: string;
}

export async function loadTlUsers(): Promise<RawTlUser[]> {
  await ensureAuth();
  const snap = await getDocs(collection(db, 'tl_users'));
  const out: RawTlUser[] = [];
  snap.forEach((d: any) => {
    const v = d.data() || {};
    out.push({
      id: d.id,
      empId: String(v.empId || v.employeeId || d.id || '').trim().toUpperCase(),
      name: String(v.name || ''),
      gmail: String(v.gmail || '').trim().toLowerCase(),
      passwordPin: String(v.pin || v.passwordPin || '').trim(),
      station: String(v.station || ''),
      shift: normalizeShift(v.shift),
      active: v.active !== false,
      createdAt: String(v.createdAt || ''),
    });
  });
  out.sort((a, b) => (a.name || '').localeCompare(b.name || ''));
  return out;
}

export interface RosterDayResult {
  stationSlug: string;
  stationName: string;
  shift: ShiftType;
  date: string;
  entries: Array<{ uid: string; id: string; name: string; time: string; status: '' | 'OFF' | 'WAITING' | 'LEAVE' | 'ABSENT' }>;
  tomClosing: { a: string; b: string; c: string; d: string; e: string } | null;
  dayExists: boolean;
}

function normalizeStatus(s: any): '' | 'OFF' | 'WAITING' | 'LEAVE' | 'ABSENT' {
  const t = String(s || '').trim().toUpperCase();
  if (t === 'OFF') return 'OFF';
  if (t === 'WAITING') return 'WAITING';
  if (t === 'LEAVE') return 'LEAVE';
  if (t === 'ABSENT') return 'ABSENT';
  return '';
}

export async function loadRosterAndDay(
  stationSlug: string,
  shift: ShiftType,
  dateStr: string,
): Promise<RosterDayResult> {
  await ensureAuth();
  const rosterRef = doc(db, 'records', `${stationSlug}_${shift}_1970-01-01`);
  const dayRef = doc(db, 'records', `${stationSlug}_${shift}_${dateStr}`);
  const [rosterSnap, daySnap] = await Promise.all([getDoc(rosterRef), getDoc(dayRef)]);
  const rosterData: any = rosterSnap.exists() ? rosterSnap.data() : {};
  const dayData: any = daySnap.exists() ? daySnap.data() : {};
  const rosterArr: any[] = Array.isArray(rosterData.staff_roster) ? rosterData.staff_roster : [];
  const dayRows: any[] = Array.isArray(dayData.staffDay?.rows) ? dayData.staffDay.rows : [];
  const dayByKey = new Map<string, any>();
  dayRows.forEach((r: any) => {
    const key = String(r.uid || r.id || '').trim();
    if (key) dayByKey.set(key, r);
  });
  const entries = rosterArr
    .filter((r: any) => String(r.name || '').trim() || String(r.id || '').trim())
    .map((r: any) => {
      const key = String(r.uid || r.id || '').trim();
      const day = dayByKey.get(key) || dayByKey.get(String(r.id || '').trim());
      const status = day ? normalizeStatus(day.status) : '';
      return {
        uid: key || String(r.id || ''),
        id: String(r.id || ''),
        name: String(r.name || ''),
        time: day && status === '' ? String(day.time || '') : '',
        status,
      };
    });
  const tom: any = dayData.tom || dayData;
  const hasTom =
    tom &&
    (tom.toma_closing !== undefined ||
      tom.tomb_closing !== undefined ||
      tom.tomc_closing !== undefined ||
      tom.tomd_closing !== undefined ||
      tom.tome_closing !== undefined);
  const station = stationBySlug(stationSlug);
  return {
    stationSlug,
    stationName: String(dayData.stationName || rosterData.stationName || (station ? station.name : '') || ''),
    shift,
    date: dateStr,
    entries,
    tomClosing: hasTom
      ? {
          a: tom.toma_closing == null ? '' : String(tom.toma_closing),
          b: tom.tomb_closing == null ? '' : String(tom.tomb_closing),
          c: tom.tomc_closing == null ? '' : String(tom.tomc_closing),
          d: tom.tomd_closing == null ? '' : String(tom.tomd_closing),
          e: tom.tome_closing == null ? '' : String(tom.tome_closing),
        }
      : null,
    dayExists: daySnap.exists(),
  };
}

export interface TodayStationSale {
  stationNumber: number;
  stationName: string;
  code: string;
  salesAmount: number;
  ticketCount: number;
  rollsUsed: number;
}

export interface TodayRecordsResult {
  sales: TodayStationSale[];
  stationsWithDocs: string[];
  docs: Array<{ stationSlug: string; stationName: string; shift: ShiftType; byName: string; updatedAt: string; salesAmount: number }>;
}

const TOM_KEYS = ['toma', 'tomb', 'tomc', 'tomd', 'tome'];
const ROLL_KEYS = ['bop', 'tom', 'tkm'];

function docSale(tom: any): { sales: number; rolls: number } {
  let sales = 0;
  let rolls = 0;
  if (tom && typeof tom === 'object') {
    TOM_KEYS.forEach((k) => {
      const op = num(tom[`${k}_opening`]);
      const rg = num(tom[`${k}_recharge`]);
      const clRaw = tom[`${k}_closing`];
      const cl = clRaw === '' || clRaw == null ? NaN : num(clRaw);
      if (!Number.isNaN(cl)) sales += Math.max(0, op + rg - cl);
    });
    sales += num(tom.bop_sale_slips) * 30;
    ROLL_KEYS.forEach((k) => {
      const op = num(tom[`${k}_roll_opening`]);
      const rc = num(tom[`${k}_roll_receive`]);
      const ds = num(tom[`${k}_roll_distribute`]);
      const cl = num(tom[`${k}_roll_closing`]);
      if (
        tom[`${k}_roll_opening`] !== undefined ||
        tom[`${k}_roll_receive`] !== undefined ||
        tom[`${k}_roll_distribute`] !== undefined ||
        tom[`${k}_roll_closing`] !== undefined
      ) {
        rolls += Math.max(0, op + rc - ds - cl);
      }
    });
  }
  return { sales: Math.round(sales), rolls };
}

export async function loadTodayRecords(shift: ShiftType): Promise<TodayRecordsResult> {
  await ensureAuth();
  const today = todayStr();
  let docs: any[] = [];
  try {
    const snap = await getDocs(query(collection(db, 'records'), where('date', '==', today)));
    snap.forEach((d: any) => docs.push(d.data() || {}));
  } catch {
    docs = [];
  }
  if (docs.length === 0) {
    // Fallback: direct doc reads for the selected shift (no collection query).
    const reads = await Promise.all(
      METRO_STATIONS.map((st) =>
        getDoc(doc(db, 'records', `${st.slug}_${shift}_${today}`))
          .then((s: any) => (s.exists() ? s.data() : null))
          .catch(() => null),
      ),
    );
    docs = reads.filter(Boolean) as any[];
  }
  const byStation = new Map<string, { sales: number; rolls: number }>();
  const outDocs: TodayRecordsResult['docs'] = [];
  docs.forEach((d: any) => {
    if (normalizeShift(d.shift) !== shift) return;
    const slug = String(d.stationId || '');
    const st = stationBySlug(slug);
    if (!st) return;
    const { sales, rolls } = docSale(d.tom || d);
    const prev = byStation.get(slug) || { sales: 0, rolls: 0 };
    byStation.set(slug, { sales: prev.sales + sales, rolls: prev.rolls + rolls });
    outDocs.push({
      stationSlug: slug,
      stationName: String(d.stationName || st.name),
      shift,
      byName: String(d.byName || ''),
      updatedAt: toISO(d.updatedAt),
      salesAmount: sales,
    });
  });
  const sales: TodayStationSale[] = METRO_STATIONS.map((st) => {
    const v = byStation.get(st.slug) || { sales: 0, rolls: 0 };
    return {
      stationNumber: st.number,
      stationName: st.name,
      code: st.code,
      salesAmount: v.sales,
      ticketCount: Math.round(v.sales / 50),
      rollsUsed: v.rolls,
    };
  });
  return { sales, stationsWithDocs: Array.from(byStation.keys()), docs: outDocs };
}

export interface RawChatMsg {
  text: string;
  by: string;
  ts: number;
}

export async function loadGroupChat(): Promise<RawChatMsg[]> {
  await ensureAuth();
  const snap = await getDocs(collection(db, 'group_chat'));
  const out: RawChatMsg[] = [];
  snap.forEach((d: any) => {
    const v = d.data() || {};
    out.push({
      text: String(v.text || v.msg || v.message || ''),
      by: String(v.byName || v.by || v.sender || ''),
      ts: toMillis(v.ts ?? v.createdAt ?? v.updatedAt),
    });
  });
  out.sort((a, b) => b.ts - a.ts);
  return out.slice(0, 50);
}

export interface ManifestConfig {
  main: { versionName: string; versionCode: number; url: string; notes?: string };
  trial: { versionName: string; versionCode: number; url: string; notes?: string };
  testers: string[];
}

export async function loadVersionManifest(): Promise<ManifestConfig> {
  const res = await fetch(
    `https://maryanrajput72-bot.github.io/metro-admin-panel/version.json?t=${Date.now()}`,
    { cache: 'no-store' },
  );
  if (!res.ok) throw new Error(`Manifest HTTP ${res.status}`);
  const j: any = await res.json();
  if (!j || typeof j.versionName !== 'string') throw new Error('Manifest shape unexpected');
  return {
    main: {
      versionName: j.versionName,
      versionCode: Number(j.versionCode || 0),
      url: String(j.apkUrl || ''),
      notes: String(j.notes || ''),
    },
    trial: {
      versionName: String(j.trial?.versionName || ''),
      versionCode: Number(j.trial?.versionCode || 0),
      url: String(j.trial?.apkUrl || ''),
      notes: String(j.trial?.notes || ''),
    },
    testers: Array.isArray(j.trial?.testers) ? j.trial.testers.map((t: any) => String(t).toUpperCase()) : [],
  };
}

// ---- Writes (each surfaces permission errors to the caller) ----

export async function deletePhotoDoc(photoId: string): Promise<void> {
  await ensureAuth();
  await deleteDoc(doc(db, 'photos', photoId));
}

export async function saveTlUserDoc(
  user: { empId: string; name: string; gmail: string; passwordPin: string; station: string; shift: ShiftType; active: boolean },
  existingId?: string,
): Promise<string> {
  await ensureAuth();
  const id = existingId || user.empId.trim().toUpperCase();
  await setDoc(
    doc(db, 'tl_users', id),
    {
      name: user.name,
      gmail: user.gmail.trim().toLowerCase(),
      pin: user.passwordPin.trim(),
      role: 'TL',
      empId: user.empId.trim().toUpperCase(),
      station: user.station,
      shift: user.shift,
      active: user.active,
      updatedAt: new Date().toISOString(),
    },
    { merge: true },
  );
  return id;
}

export async function deleteTlUserDoc(id: string): Promise<void> {
  await ensureAuth();
  await deleteDoc(doc(db, 'tl_users', id));
}

export async function saveStaffVersionDoc(row: RawStaffRow): Promise<void> {
  await ensureAuth();
  await setDoc(
    doc(db, 'staff_versions', row.employeeId),
    {
      employeeId: row.employeeId,
      name: row.name,
      station: row.station,
      stationSlug: row.stationSlug,
      shift: row.shift,
      versionName: row.versionName,
      versionCode: row.versionCode,
      platform: row.platform || 'android',
      updatedAt: new Date().toISOString(),
    },
    { merge: true },
  );
}

export async function saveStaffLoginDoc(row: RawStaffRow): Promise<void> {
  await ensureAuth();
  await setDoc(
    doc(db, 'staff_logins', row.employeeId),
    {
      employeeId: row.employeeId,
      name: row.name,
      station: row.station,
      stationSlug: row.stationSlug,
      shift: row.shift,
      role: row.role,
      phone: row.phone || '',
      updatedAt: new Date().toISOString(),
    },
    { merge: true },
  );
}

export async function saveStaffDayDoc(
  stationSlug: string,
  shift: ShiftType,
  dateStr: string,
  rows: Array<{ uid: string; id: string; name: string; time: string; status: string }>,
): Promise<void> {
  await ensureAuth();
  // Merge only: TOM fields on the same doc are never touched.
  await setDoc(
    doc(db, 'records', `${stationSlug}_${shift}_${dateStr}`),
    {
      stationId: stationSlug,
      shift,
      date: dateStr,
      staffDay: { rows, updatedAt: new Date().toISOString(), byName: 'Panel', byRole: 'Admin' },
    },
    { merge: true },
  );
}
