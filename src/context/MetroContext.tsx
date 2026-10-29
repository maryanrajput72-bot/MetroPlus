import React, { createContext, useContext, useState, useEffect, useMemo, useRef, useCallback } from 'react';
import {
  MetroStation, StaffVersion, DailyAttendanceDoc, GPSPhoto,
  TLAccessUser, UpdaterConfig, ShiftType, AttendanceStatus,
  ActivityLogEntry, ConnectionState, TodayTomClosing,
} from '../types/metro';
import { METRO_STATIONS, getStationBySlug } from '../data/metroStations';
import { buildWhatsAppAttendanceReport } from '../utils/reportFormats';
import {
  ConnState, classifyError, ensureAuth, todayStr,
  loadStaffData, loadPhotos, loadTlUsers, loadRosterAndDay, loadTodayRecords,
  loadGroupChat, loadVersionManifest,
  deletePhotoDoc, saveTlUserDoc, deleteTlUserDoc,
  saveStaffVersionDoc, saveStaffLoginDoc, saveStaffDayDoc,
  type RawStaffRow, type RawPhoto, type RawTlUser, type TodayRecordsResult,
} from '../services/liveData';

interface StationSale {
  stationNumber: number;
  stationName: string;
  code: string;
  salesAmount: number; // in PKR
  ticketCount: number;
  rollsUsed: number;
}

interface MetroContextType {
  stations: MetroStation[];
  selectedStation: MetroStation;
  setSelectedStation: (station: MetroStation) => void;
  selectedShift: ShiftType;
  setSelectedShift: (shift: ShiftType) => void;

  // Total Sales of all stations
  totalNetworkSales: number;
  totalRollsUsed: number;
  stationSales: StationSale[];
  updateStationSales: (stationNumber: number, amount: number, rolls?: number) => void;

  // Staff & Versions
  staffVersions: StaffVersion[];
  updateStaffVersion: (empId: string, updates: Partial<StaffVersion>) => void;
  addStaffMember: (member: Omit<StaffVersion, 'updatedAt'>) => void;

  // Attendance
  todayAttendance: DailyAttendanceDoc;
  updateAttendanceStatus: (uid: string, status: AttendanceStatus, time?: string) => void;
  copyWhatsAppReport: () => void;

  // GPS Photos
  gpsPhotos: GPSPhoto[];
  deleteGPSPhoto: (photoId: string) => void;

  // TL Access
  tlUsers: TLAccessUser[];
  saveTLUser: (user: Omit<TLAccessUser, 'id' | 'createdAt'>, existingId?: string) => void;
  deleteTLUser: (id: string) => void;

  // Updater (READ-ONLY: this panel never writes version.json)
  updaterConfig: UpdaterConfig;
  pushUpdateRelease: (channel: 'main' | 'trial', versionName: string, versionCode: number, url: string, signalPin?: string) => { success: boolean; error?: string };
  addTrialTester: (empId: string) => void;
  removeTrialTester: (empId: string) => void;

  // Feature Flags (panel-local only — the app's TL_OPEN_FEATURES is
  // hard-coded in the app and is not changed by this toggle)
  featureFlags: {
    TL_OPEN_FEATURES: boolean;
  };
  toggleFeatureFlag: (flagName: 'TL_OPEN_FEATURES') => void;

  // Live data health + derived activity feed + today's TOM closing
  connectionStatus: Record<string, ConnectionState>;
  activityLogs: ActivityLogEntry[];
  todayTom: TodayTomClosing | null;
  attendanceDate: string;
  setAttendanceDate: (date: string) => void;

  // Telemetry Stats
  stats: {
    totalStaff: number;
    onlineStaff: number;
    onDutyCount: number;
    offCount: number;
    waitingCount: number;
    leaveCount: number;
    absentCount: number;
    onMainVersion: number;
    onTrialVersion: number;
    outdatedCount: number;
    photosCount: number;
    connectedStations: number;
    totalSales: number;
    totalRollsUsed: number;
  };

  toastMessage: string | null;
  showToast: (msg: string) => void;
}

const MetroContext = createContext<MetroContextType | undefined>(undefined);

// Last-known REAL manifest values (from the live version.json fetch).
// Used only if the live manifest fetch fails; replaced on every load.
const FALLBACK_UPDATER: UpdaterConfig = {
  main: {
    versionName: '2.1.31',
    versionCode: 339,
    url: 'https://maryanrajput72-bot.github.io/metro-admin-panel/apk/MetroPlus-v2.1.31.apk',
    notes: 'Stable staff release',
  },
  trial: {
    versionName: '2.1.60',
    versionCode: 368,
    url: 'https://maryanrajput72-bot.github.io/metro-admin-panel/apk/MetroPlus-v2.1.60-trial.apk',
    notes: 'Trial channel',
  },
  testers: ['3526', 'MARYAN'],
};

const EMPTY_SALES: StationSale[] = METRO_STATIONS.map((st) => ({
  stationNumber: st.number,
  stationName: st.name,
  code: st.code,
  salesAmount: 0,
  ticketCount: 0,
  rollsUsed: 0,
}));

function emptyAttendance(station: MetroStation, shift: ShiftType, date: string): DailyAttendanceDoc {
  return { stationSlug: station.slug, stationName: station.name, shift, date, v: 2, entries: [] };
}

export const MetroProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [stations] = useState<MetroStation[]>(METRO_STATIONS);
  const [selectedStation, setSelectedStation] = useState<MetroStation>(METRO_STATIONS[0]);
  const [selectedShift, setSelectedShift] = useState<ShiftType>('Morning');
  const [attendanceDate, setAttendanceDate] = useState<string>(todayStr());

  const [stationSales, setStationSales] = useState<StationSale[]>(EMPTY_SALES);
  const [rawStaff, setRawStaff] = useState<RawStaffRow[]>([]);
  const [attendanceRaw, setAttendanceRaw] = useState<DailyAttendanceDoc>(() =>
    emptyAttendance(METRO_STATIONS[0], 'Morning', todayStr()),
  );
  const [gpsPhotos, setGpsPhotos] = useState<GPSPhoto[]>([]);
  const [tlUsers, setTlUsers] = useState<TLAccessUser[]>([]);
  const [updaterConfig, setUpdaterConfig] = useState<UpdaterConfig>(FALLBACK_UPDATER);
  const [todayTom, setTodayTom] = useState<TodayTomClosing | null>(null);
  const [todayDocStations, setTodayDocStations] = useState<string[]>([]);
  const [todayDocs, setTodayDocs] = useState<TodayRecordsResult['docs']>([]);
  const [chatMsgs, setChatMsgs] = useState<Array<{ text: string; by: string; ts: number }>>([]);
  const [connectionStatus, setConnectionStatus] = useState<Record<string, ConnectionState>>({
    auth: 'loading',
    staff_versions: 'loading',
    staff_logins: 'loading',
    presence: 'loading',
    photos: 'loading',
    tl_users: 'loading',
    group_chat: 'loading',
    records: 'loading',
    updater: 'loading',
  });

  const [featureFlags, setFeatureFlags] = useState<{ TL_OPEN_FEATURES: boolean }>(() => {
    try {
      const saved = localStorage.getItem('metro_feature_flags');
      return saved ? JSON.parse(saved) : { TL_OPEN_FEATURES: true };
    } catch {
      return { TL_OPEN_FEATURES: true };
    }
  });

  const [toastMessage, setToastMessage] = useState<string | null>(null);

  const staffRef = useRef<RawStaffRow[]>([]);
  staffRef.current = rawStaff;
  const attendanceRef = useRef(attendanceRaw);
  attendanceRef.current = attendanceRaw;

  const setConn = useCallback((key: string, state: ConnState) => {
    setConnectionStatus((prev) => (prev[key] === state ? prev : { ...prev, [key]: state }));
  }, []);

  const showToast = useCallback((msg: string) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage((curr) => (curr === msg ? null : curr)), 4000);
  }, []);

  useEffect(() => {
    try {
      localStorage.setItem('metro_feature_flags', JSON.stringify(featureFlags));
    } catch {
      /* ignore */
    }
  }, [featureFlags]);

  // ---- Global live loads (once, on mount) ----
  useEffect(() => {
    let cancelled = false;
    (async () => {
      try {
        await ensureAuth();
        if (!cancelled) setConn('auth', 'ok');
      } catch (e) {
        if (!cancelled) setConn('auth', classifyError(e));
      }
      // Staff (versions + logins + presence in one loader)
      try {
        const rows = await loadStaffData();
        if (cancelled) return;
        setRawStaff(rows);
        setConn('staff_versions', 'ok');
        setConn('staff_logins', 'ok');
        setConn('presence', 'ok');
      } catch (e) {
        if (!cancelled) {
          const s = classifyError(e);
          setConn('staff_versions', s);
          setConn('staff_logins', s);
          setConn('presence', s);
        }
      }
      try {
        const photos = await loadPhotos();
        if (cancelled) return;
        setGpsPhotos(
          photos.map((p: RawPhoto) => ({
            id: p.id,
            stationId: p.stationId,
            stationName: p.stationName,
            shift: p.shift,
            date: p.date,
            ts: p.ts,
            lat: p.lat,
            lng: p.lng,
            address: p.address,
            thumb: p.thumb,
            byPhone: p.byPhone,
            byName: p.byName,
          })),
        );
        setConn('photos', 'ok');
      } catch (e) {
        if (!cancelled) setConn('photos', classifyError(e));
      }
      try {
        const tls = await loadTlUsers();
        if (cancelled) return;
        setTlUsers(
          tls.map((t: RawTlUser) => ({
            id: t.id,
            empId: t.empId,
            name: t.name,
            gmail: t.gmail,
            passwordPin: t.passwordPin,
            station: t.station,
            shift: t.shift,
            active: t.active,
            createdAt: t.createdAt,
          })),
        );
        setConn('tl_users', 'ok');
      } catch (e) {
        if (!cancelled) setConn('tl_users', classifyError(e));
      }
      try {
        const msgs = await loadGroupChat();
        if (cancelled) return;
        setChatMsgs(msgs);
        setConn('group_chat', 'ok');
      } catch (e) {
        if (!cancelled) setConn('group_chat', classifyError(e));
      }
      try {
        const manifest = await loadVersionManifest();
        if (cancelled) return;
        setUpdaterConfig(manifest);
        setConn('updater', 'ok');
      } catch (e) {
        if (!cancelled) setConn('updater', classifyError(e));
      }
    })();
    return () => {
      cancelled = true;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // ---- Per-station live loads (station / shift / date changes) ----
  useEffect(() => {
    let cancelled = false;
    (async () => {
      try {
        const res = await loadRosterAndDay(selectedStation.slug, selectedShift, attendanceDate);
        if (cancelled) return;
        const roleById = new Map<string, 'TL' | 'Operator'>();
        staffRef.current.forEach((r) => roleById.set(r.employeeId, r.role));
        setAttendanceRaw({
          stationSlug: res.stationSlug,
          stationName: res.stationName || selectedStation.name,
          shift: res.shift,
          date: res.date,
          v: 2,
          entries: res.entries.map((e) => ({
            uid: e.uid,
            id: e.id,
            name: e.name,
            role: roleById.get(e.id.toUpperCase()) || 'Operator',
            time: e.time,
            status: e.status,
          })),
        });
        setConn('records', 'ok');
      } catch (e) {
        if (!cancelled) {
          setConn('records', classifyError(e));
          setAttendanceRaw(emptyAttendance(selectedStation, selectedShift, attendanceDate));
        }
      }
    })();
    return () => {
      cancelled = true;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedStation, selectedShift, attendanceDate]);

  // Today's records + today's TOM closing for the selected station/shift.
  useEffect(() => {
    let cancelled = false;
    (async () => {
      try {
        const [todayRes, dayRes] = await Promise.all([
          loadTodayRecords(selectedShift),
          loadRosterAndDay(selectedStation.slug, selectedShift, todayStr()),
        ]);
        if (cancelled) return;
        setStationSales(todayRes.sales);
        setTodayDocStations(todayRes.stationsWithDocs);
        setTodayDocs(todayRes.docs);
        setTodayTom(dayRes.tomClosing);
        setConn('records', 'ok');
      } catch (e) {
        if (!cancelled) setConn('records', classifyError(e));
      }
    })();
    return () => {
      cancelled = true;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedStation, selectedShift]);

  // ---- Derived: staff versions with channel flags ----
  const staffVersions: StaffVersion[] = useMemo(() => {
    return rawStaff.map((r) => {
      const isTester = updaterConfig.testers.includes(r.employeeId);
      const latest = isTester ? updaterConfig.trial.versionCode : updaterConfig.main.versionCode;
      return {
        employeeId: r.employeeId,
        name: r.name,
        station: r.station,
        stationSlug: r.stationSlug,
        shift: r.shift,
        role: r.role,
        versionName: r.versionName,
        versionCode: r.versionCode,
        platform: r.platform,
        updatedAt: r.updatedAt,
        isTrialTester: isTester,
        isOutdated: r.versionCode > 0 && latest > 0 && r.versionCode < latest,
        online: r.online,
        lastHeartbeat: r.lastHeartbeat,
        phone: r.phone,
      };
    });
  }, [rawStaff, updaterConfig]);

  // ---- Derived: attendance with roles joined from live staff ----
  const todayAttendance: DailyAttendanceDoc = useMemo(() => {
    const roleById = new Map<string, 'TL' | 'Operator'>();
    rawStaff.forEach((r) => roleById.set(r.employeeId, r.role));
    return {
      ...attendanceRaw,
      entries: attendanceRaw.entries.map((e) => ({
        ...e,
        role: roleById.get(e.id.toUpperCase()) || e.role || 'Operator',
      })),
    };
  }, [attendanceRaw, rawStaff]);

  // ---- Derived: activity feed from live data ----
  const activityLogs: ActivityLogEntry[] = useMemo(() => {
    const logs: ActivityLogEntry[] = [];
    const fmtTime = (ts: number) => {
      if (!ts) return '';
      try {
        return new Date(ts).toLocaleTimeString();
      } catch {
        return '';
      }
    };
    const stationLabel = (slugOrName: string) => {
      const st = getStationBySlug(slugOrName) || METRO_STATIONS.find((s) => s.name === slugOrName);
      return st ? `${st.name} (${st.code})` : slugOrName;
    };
    gpsPhotos.slice(0, 20).forEach((p) => {
      logs.push({
        id: `PH-${p.id}`,
        time: fmtTime(p.ts),
        category: 'attendance',
        user: p.byName || p.byPhone || 'GPS Camera',
        role: 'GPS Camera',
        action: 'GPS Photo Captured',
        station: stationLabel(String(p.stationId) || p.stationName),
        details: `GPS geotag ${p.lat.toFixed(4)}° N, ${p.lng.toFixed(4)}° E verified by CameraX`,
        status: 'success',
        ts: p.ts,
      });
    });
    staffVersions.slice(0, 20).forEach((s) => {
      const ts = Date.parse(s.updatedAt || '');
      if (!s.versionName) return;
      logs.push({
        id: `VR-${s.employeeId}`,
        time: fmtTime(Number.isNaN(ts) ? 0 : ts),
        category: 'updater',
        user: s.name,
        role: s.role,
        action: 'App Version Reported',
        station: s.station,
        details: `${s.versionName} (vc${s.versionCode}) on ${s.platform}`,
        status: s.isOutdated ? 'warning' : 'success',
        ts: Number.isNaN(ts) ? 0 : ts,
      });
    });
    chatMsgs.slice(0, 15).forEach((m, i) => {
      logs.push({
        id: `CH-${i}`,
        time: fmtTime(m.ts),
        category: 'system',
        user: m.by || 'Group Chat',
        role: 'Group Chat',
        action: 'Group Message',
        station: 'Network-wide',
        details: m.text.length > 80 ? m.text.slice(0, 80) + '…' : m.text,
        status: 'info',
        ts: m.ts,
      });
    });
    todayDocs.forEach((d, i) => {
      const ts = Date.parse(d.updatedAt || '');
      logs.push({
        id: `RC-${i}`,
        time: fmtTime(Number.isNaN(ts) ? 0 : ts),
        category: 'revenue',
        user: d.byName || 'TOM Operator',
        role: 'TOM Operator',
        action: 'TOM Record Synced',
        station: stationLabel(d.stationSlug),
        details: `Closing total Rs ${d.salesAmount.toLocaleString()} (${d.shift} Shift)`,
        status: 'success',
        ts: Number.isNaN(ts) ? 0 : ts,
      });
    });
    logs.sort((a, b) => b.ts - a.ts);
    return logs.slice(0, 60);
  }, [gpsPhotos, staffVersions, chatMsgs, todayDocs]);

  const totalNetworkSales = stationSales.reduce((sum, s) => sum + s.salesAmount, 0);
  const totalRollsUsed = stationSales.reduce((sum, s) => sum + s.rollsUsed, 0);

  // Panel-local table adjustment only: real sales come from today's saved
  // records and reload on station/shift change. Nothing is written to Firestore.
  const updateStationSales = (stationNumber: number, amount: number, rolls?: number) => {
    setStationSales((prev) =>
      prev.map((s) =>
        s.stationNumber === stationNumber
          ? {
              ...s,
              salesAmount: amount,
              ticketCount: Math.round(amount / 50),
              rollsUsed: rolls !== undefined ? rolls : s.rollsUsed,
            }
          : s,
      ),
    );
  };

  const updateStaffVersion = (empId: string, updates: Partial<StaffVersion>) => {
    const prev = rawStaff.find((s) => s.employeeId === empId);
    if (!prev) return;
    const next: RawStaffRow = {
      ...prev,
      name: updates.name ?? prev.name,
      station: updates.station ?? prev.station,
      stationSlug: updates.stationSlug ?? prev.stationSlug,
      shift: updates.shift ?? prev.shift,
      role: updates.role ?? prev.role,
      versionName: updates.versionName ?? prev.versionName,
      versionCode: updates.versionCode ?? prev.versionCode,
      platform: updates.platform ?? prev.platform,
      phone: updates.phone ?? prev.phone,
    };
    saveStaffVersionDoc(next)
      .then(() => {
        setRawStaff((list) => list.map((s) => (s.employeeId === empId ? next : s)));
        showToast(`Staff [${empId}] record updated`);
      })
      .catch((e) => {
        showToast(`Could not save staff record (${classifyError(e)}). No change made.`);
      });
  };

  const addStaffMember = (member: Omit<StaffVersion, 'updatedAt'>) => {
    const row: RawStaffRow = {
      employeeId: member.employeeId.trim().toUpperCase(),
      name: member.name,
      station: member.station,
      stationSlug: member.stationSlug,
      shift: member.shift,
      role: member.role,
      versionName: member.versionName,
      versionCode: member.versionCode,
      platform: member.platform || 'android',
      updatedAt: new Date().toISOString(),
      phone: member.phone || '',
      online: false,
      lastHeartbeat: '',
    };
    Promise.all([saveStaffVersionDoc(row), saveStaffLoginDoc(row)])
      .then(() => {
        setRawStaff((prev) => [row, ...prev.filter((s) => s.employeeId !== row.employeeId)]);
        showToast(`Staff member [${row.employeeId}] registered`);
      })
      .catch((e) => {
        showToast(`Could not register staff (${classifyError(e)}). No change made.`);
      });
  };

  const updateAttendanceStatus = (uid: string, status: AttendanceStatus, time?: string) => {
    const prev = attendanceRef.current;
    const now = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', hour12: true });
    const nextEntries = prev.entries.map((e) => {
      if (e.uid !== uid) return e;
      if (status === '') return { ...e, status, time: time || e.time || now };
      return { ...e, status, time: '' };
    });
    const next = { ...prev, entries: nextEntries };
    setAttendanceRaw(next);
    // Persist via the app's own merge mechanism: staffDay only, TOM untouched.
    saveStaffDayDoc(
      prev.stationSlug,
      prev.shift,
      prev.date,
      nextEntries.map((e) => ({ uid: e.uid, id: e.id, name: e.name, time: e.time, status: e.status })),
    )
      .then(() => {
        const label = status === '' ? 'ON DUTY' : status;
        showToast(`Attendance marked as ${label}`);
      })
      .catch((e) => {
        setAttendanceRaw(prev);
        showToast(`Could not save attendance (${classifyError(e)}). Change reverted.`);
      });
  };

  const copyWhatsAppReport = () => {
    const text = buildWhatsAppAttendanceReport(todayAttendance);
    try {
      navigator.clipboard?.writeText(text);
    } catch {
      /* clipboard unavailable */
    }
    showToast('WhatsApp Attendance Report copied to clipboard!');
  };

  const deleteGPSPhoto = (photoId: string) => {
    deletePhotoDoc(photoId)
      .then(() => {
        setGpsPhotos((prev) => prev.filter((p) => p.id !== photoId));
        showToast('GPS Photo deleted permanently from cloud storage');
      })
      .catch((e) => {
        showToast(`Could not delete photo (${classifyError(e)}). Photo kept.`);
      });
  };

  const saveTLUser = (user: Omit<TLAccessUser, 'id' | 'createdAt'>, existingId?: string) => {
    saveTlUserDoc(user, existingId)
      .then((id) => {
        const saved: TLAccessUser = {
          ...user,
          id,
          createdAt: new Date().toISOString().split('T')[0],
        };
        setTlUsers((prev) => {
          const rest = prev.filter((u) => u.id !== id && u.empId !== user.empId);
          return existingId ? prev.map((u) => (u.id === id ? saved : u)) : [saved, ...rest];
        });
        showToast(existingId ? `TL Access updated for ${user.name}` : `New TL Access created for ${user.name}`);
      })
      .catch((e) => {
        showToast(`Could not save TL Access (${classifyError(e)}). No change made.`);
      });
  };

  const deleteTLUser = (id: string) => {
    deleteTlUserDoc(id)
      .then(() => {
        setTlUsers((prev) => prev.filter((u) => u.id !== id));
        showToast('TL Access revoked');
      })
      .catch((e) => {
        showToast(`Could not revoke TL Access (${classifyError(e)}). No change made.`);
      });
  };

  // READ-ONLY UPDATER: this panel has no write path to version.json at all.
  // Releases are published only through the owner's main-chat process, and
  // the MAIN channel moves only on the owner's 4-digit signal in that chat.
  // The signal is never accepted or entered in this panel.
  const pushUpdateRelease = (
    _channel: 'main' | 'trial',
    _versionName: string,
    _versionCode: number,
    _url: string,
    _signalPin?: string,
  ) => {
    return {
      success: false,
      error:
        'READ-ONLY PANEL: this panel cannot push releases. Releases are published only through the owner\u2019s main-chat process; the MAIN channel moves only on the owner\u2019s 4-digit signal in that chat.',
    };
  };

  const addTrialTester = (_empId: string) => {
    showToast('READ-ONLY: trial testers are managed in version.json, not from this panel.');
  };

  const removeTrialTester = (_empId: string) => {
    showToast('READ-ONLY: trial testers are managed in version.json, not from this panel.');
  };

  const toggleFeatureFlag = (flagName: 'TL_OPEN_FEATURES') => {
    setFeatureFlags((prev) => {
      const next = !prev[flagName];
      showToast(`Feature flag ${flagName} set to ${next ? 'ENABLED' : 'DISABLED'} (panel-local only)`);
      return { ...prev, [flagName]: next };
    });
  };

  // Telemetry Stats
  const totalStaff = staffVersions.length;
  const onlineStaff = staffVersions.filter((s) => s.online).length;
  const onDutyCount = todayAttendance.entries.filter((e) => e.status === '').length;
  const offCount = todayAttendance.entries.filter((e) => e.status === 'OFF').length;
  const waitingCount = todayAttendance.entries.filter((e) => e.status === 'WAITING').length;
  const leaveCount = todayAttendance.entries.filter((e) => e.status === 'LEAVE').length;
  const absentCount = todayAttendance.entries.filter((e) => e.status === 'ABSENT').length;
  const onMainVersion = staffVersions.filter((s) => s.versionCode === updaterConfig.main.versionCode).length;
  const onTrialVersion = staffVersions.filter((s) => s.versionCode === updaterConfig.trial.versionCode).length;
  const outdatedCount = staffVersions.filter((s) => s.isOutdated).length;
  const photosToday = gpsPhotos.filter((p) => p.date === todayStr()).length;
  const connectedStations = new Set([
    ...todayDocStations,
    ...gpsPhotos.filter((p) => p.date === todayStr()).map((p) => String(p.stationId)),
  ]).size;

  const stats = {
    totalStaff,
    onlineStaff,
    onDutyCount,
    offCount,
    waitingCount,
    leaveCount,
    absentCount,
    onMainVersion,
    onTrialVersion,
    outdatedCount,
    photosCount: photosToday,
    connectedStations,
    totalSales: totalNetworkSales,
    totalRollsUsed,
  };

  return (
    <MetroContext.Provider
      value={{
        stations,
        selectedStation,
        setSelectedStation,
        selectedShift,
        setSelectedShift,
        totalNetworkSales,
        totalRollsUsed,
        stationSales,
        updateStationSales,
        staffVersions,
        updateStaffVersion,
        addStaffMember,
        todayAttendance,
        updateAttendanceStatus,
        copyWhatsAppReport,
        gpsPhotos,
        deleteGPSPhoto,
        tlUsers,
        saveTLUser,
        deleteTLUser,
        updaterConfig,
        pushUpdateRelease,
        addTrialTester,
        removeTrialTester,
        featureFlags,
        toggleFeatureFlag,
        connectionStatus,
        activityLogs,
        todayTom,
        attendanceDate,
        setAttendanceDate,
        stats,
        toastMessage,
        showToast,
      }}
    >
      {children}
    </MetroContext.Provider>
  );
};

export const useMetro = () => {
  const ctx = useContext(MetroContext);
  if (!ctx) throw new Error('useMetro must be used within a MetroProvider');
  return ctx;
};
