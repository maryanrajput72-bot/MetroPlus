export type ShiftType = 'Morning' | 'Evening';
export type StaffRole = 'TL' | 'Operator';
export type AttendanceStatus = '' | 'OFF' | 'WAITING' | 'LEAVE' | 'ABSENT';

export interface MetroStation {
  number: number;
  code: string; // e.g. "1/201"
  name: string; // display name e.g. "Khatam-e-Nabuwat"
  slug: string; // cloud id e.g. "shamsabad"
  zone: 'Rawalpindi' | 'Islamabad';
  lat: number;
  lng: number;
}

export interface StaffVersion {
  employeeId: string;
  name: string;
  station: string;
  stationSlug: string;
  shift: ShiftType;
  role: StaffRole;
  versionName: string;
  versionCode: number;
  platform: string;
  updatedAt: string;
  isTrialTester?: boolean;
  isOutdated?: boolean;
  online?: boolean;
  lastHeartbeat?: string;
  phone?: string;
}

export interface AttendanceEntry {
  uid: string;
  id: string; // Emp ID
  name: string;
  role: StaffRole;
  time: string; // HH:MM 12-hour
  status: AttendanceStatus; // '' = ON DUTY
}

export interface DailyAttendanceDoc {
  stationSlug: string;
  stationName: string;
  shift: ShiftType;
  date: string; // YYYY-MM-DD
  v: number;
  entries: AttendanceEntry[];
}

export interface GPSPhoto {
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

export interface TLAccessUser {
  id: string;
  empId: string;
  name: string;
  gmail: string;
  passwordPin: string; // 5-digit PIN
  station: string;
  shift: ShiftType;
  active: boolean;
  createdAt: string;
}

export interface UpdaterConfig {
  main: {
    versionName: string;
    versionCode: number;
    url: string;
    notes?: string;
  };
  trial: {
    versionName: string;
    versionCode: number;
    url: string;
    notes?: string;
  };
  testers: string[]; // e.g. ["3526", "MARYAN"]
}

export interface PresenceEntry {
  empId: string;
  name: string;
  station: string;
  shift: ShiftType;
  online: boolean;
  lastSeen: string;
}

export type ConnectionState = 'loading' | 'ok' | 'denied' | 'offline' | 'error';

export interface ActivityLogEntry {
  id: string;
  time: string;
  category: 'attendance' | 'revenue' | 'security' | 'system' | 'updater';
  user: string;
  role: string;
  action: string;
  station: string;
  details: string;
  status: 'success' | 'warning' | 'info';
  ts: number;
}

export interface TodayTomClosing {
  a: string;
  b: string;
  c: string;
  d: string;
  e: string;
}
