import { StaffVersion, DailyAttendanceDoc, GPSPhoto, TLAccessUser, UpdaterConfig } from '../types/metro';

export const INITIAL_UPDATER_CONFIG: UpdaterConfig = {
  main: {
    versionName: 'v2.1.31',
    versionCode: 339,
    url: 'https://muneeb-metroapp.web.app/updates/metro-plus-v2.1.31.apk',
    notes: 'Stable staff release — Red Line production build'
  },
  trial: {
    versionName: 'v2.1.60',
    versionCode: 368,
    url: 'https://muneeb-metroapp.web.app/updates/metro-plus-trial-v2.1.60.apk',
    notes: 'Trial channel with CameraX split stamp & live presence'
  },
  testers: ['3526', 'MARYAN', 'TL-ADMIN']
};

export const INITIAL_STAFF_VERSIONS: StaffVersion[] = [
  {
    employeeId: '3526',
    name: 'Muneeb Ur Rehman',
    station: 'Saddar',
    stationSlug: 'saddar',
    shift: 'Morning',
    role: 'TL',
    versionName: 'v2.1.60',
    versionCode: 368,
    platform: 'android',
    updatedAt: '2026-10-29T07:15:00Z',
    isTrialTester: true,
    isOutdated: false,
    online: true,
    lastHeartbeat: '2026-10-29T08:50:00Z',
    phone: '+92 300 1234567'
  },
  {
    employeeId: 'MARYAN',
    name: 'Maryam Bibi',
    station: 'Committee Chowk',
    stationSlug: 'committee_chowk',
    shift: 'Evening',
    role: 'TL',
    versionName: 'v2.1.60',
    versionCode: 368,
    platform: 'android',
    updatedAt: '2026-10-29T06:40:00Z',
    isTrialTester: true,
    isOutdated: false,
    online: true,
    lastHeartbeat: '2026-10-29T08:49:00Z',
    phone: '+92 301 9876543'
  },
  {
    employeeId: '2041',
    name: 'Muhammad Tariq',
    station: '6th Road',
    stationSlug: '6th_road',
    shift: 'Morning',
    role: 'Operator',
    versionName: 'v2.1.31',
    versionCode: 339,
    platform: 'android',
    updatedAt: '2026-10-28T14:20:00Z',
    isTrialTester: false,
    isOutdated: false,
    online: true,
    lastHeartbeat: '2026-10-29T08:45:00Z',
    phone: '+92 312 5551234'
  },
  {
    employeeId: '1892',
    name: 'Shahbaz Ahmed',
    station: 'Khatam-e-Nabuwat',
    stationSlug: 'shamsabad',
    shift: 'Morning',
    role: 'TL',
    versionName: 'v2.1.31',
    versionCode: 339,
    platform: 'android',
    updatedAt: '2026-10-27T10:10:00Z',
    isTrialTester: false,
    isOutdated: false,
    online: true,
    lastHeartbeat: '2026-10-29T08:48:00Z',
    phone: '+92 333 4447788'
  },
  {
    employeeId: '1088',
    name: 'Zeeshan Ali',
    station: 'Faizabad',
    stationSlug: 'faizabad',
    shift: 'Morning',
    role: 'Operator',
    versionName: 'v2.1.28',
    versionCode: 332,
    platform: 'android',
    updatedAt: '2026-10-20T11:00:00Z',
    isTrialTester: false,
    isOutdated: true, // Needs update!
    online: false,
    lastHeartbeat: '2026-10-28T16:30:00Z',
    phone: '+92 345 6677889'
  },
  {
    employeeId: '4412',
    name: 'Kashif Mehmood',
    station: 'PIMS',
    stationSlug: 'pims',
    shift: 'Morning',
    role: 'TL',
    versionName: 'v2.1.31',
    versionCode: 339,
    platform: 'android',
    updatedAt: '2026-10-28T09:15:00Z',
    isTrialTester: false,
    isOutdated: false,
    online: true,
    lastHeartbeat: '2026-10-29T08:50:00Z',
    phone: '+92 321 8899001'
  },
  {
    employeeId: '5501',
    name: 'Asad Ullah Khan',
    station: 'Pak Secretariat',
    stationSlug: 'pak_secretariat',
    shift: 'Evening',
    role: 'TL',
    versionName: 'v2.1.31',
    versionCode: 339,
    platform: 'android',
    updatedAt: '2026-10-28T18:00:00Z',
    isTrialTester: false,
    isOutdated: false,
    online: false,
    lastHeartbeat: '2026-10-28T22:30:00Z',
    phone: '+92 300 7711223'
  }
];

export const INITIAL_TODAY_ATTENDANCE: DailyAttendanceDoc = {
  stationSlug: 'saddar',
  stationName: 'Saddar (1/201)',
  shift: 'Morning',
  date: new Date().toISOString().split('T')[0],
  v: 2,
  entries: [
    { uid: 'u1', id: '3526', name: 'Muneeb Ur Rehman', role: 'TL', time: '05:45 AM', status: '' }, // '' = ON DUTY
    { uid: 'u2', id: '2011', name: 'Hamza Farooq', role: 'Operator', time: '05:52 AM', status: '' },
    { uid: 'u3', id: '2012', name: 'Noman Bashir', role: 'Operator', time: '06:05 AM', status: '' },
    { uid: 'u4', id: '2013', name: 'Adnan Qureshi', role: 'Operator', time: '06:10 AM', status: 'WAITING' },
    { uid: 'u5', id: '2014', name: 'Sajid Mahmood', role: 'Operator', time: '', status: 'OFF' },
    { uid: 'u6', id: '2015', name: 'Waleed Raza', role: 'Operator', time: '', status: 'LEAVE' }
  ]
};

export const INITIAL_GPS_PHOTOS: GPSPhoto[] = [
  {
    id: 'photo_01',
    stationId: '1',
    stationName: 'Saddar',
    shift: 'Morning',
    date: '2026-10-29',
    ts: Date.now() - 1000 * 60 * 25,
    lat: 33.5936,
    lng: 73.0538,
    address: 'Metro Bus Station Saddar, The Mall, Rawalpindi',
    thumb: 'https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?w=600&auto=format&fit=crop&q=80',
    byPhone: '+92 300 1234567',
    byName: 'Muneeb (TL)'
  },
  {
    id: 'photo_02',
    stationId: '8',
    stationName: '6th Road',
    shift: 'Morning',
    date: '2026-10-29',
    ts: Date.now() - 1000 * 60 * 45,
    lat: 33.6425,
    lng: 73.0772,
    address: '6th Road Metro Terminal, Murree Rd, Rawalpindi',
    thumb: 'https://images.unsplash.com/photo-1570125909232-eb263c188f7e?w=600&auto=format&fit=crop&q=80',
    byPhone: '+92 312 5551234',
    byName: 'Tariq (Operator)'
  },
  {
    id: 'photo_03',
    stationId: '19',
    stationName: 'PIMS',
    shift: 'Morning',
    date: '2026-10-29',
    ts: Date.now() - 1000 * 60 * 80,
    lat: 33.7225,
    lng: 73.0545,
    address: 'PIMS Hospital Station, Islamabad Sector G-8',
    thumb: 'https://images.unsplash.com/photo-1517649763962-0c623266ddc0?w=600&auto=format&fit=crop&q=80',
    byPhone: '+92 321 8899001',
    byName: 'Kashif (TL)'
  }
];

export const INITIAL_TL_USERS: TLAccessUser[] = [
  {
    id: 'tl_1',
    empId: '3526',
    name: 'Muneeb Ur Rehman',
    gmail: 'muneeb.tl@gmail.com',
    passwordPin: '48291',
    station: 'Saddar',
    shift: 'Morning',
    active: true,
    createdAt: '2026-08-01'
  },
  {
    id: 'tl_2',
    empId: 'MARYAN',
    name: 'Maryam Bibi',
    gmail: 'maryam.metro@gmail.com',
    passwordPin: '73910',
    station: 'Committee Chowk',
    shift: 'Evening',
    active: true,
    createdAt: '2026-08-15'
  },
  {
    id: 'tl_3',
    empId: '1892',
    name: 'Shahbaz Ahmed',
    gmail: 'shahbaz.kn@gmail.com',
    passwordPin: '55102',
    station: 'Khatam-e-Nabuwat',
    shift: 'Morning',
    active: true,
    createdAt: '2026-09-01'
  },
  {
    id: 'tl_4',
    empId: '4412',
    name: 'Kashif Mehmood',
    gmail: 'kashif.pims@gmail.com',
    passwordPin: '99201',
    station: 'PIMS',
    shift: 'Morning',
    active: true,
    createdAt: '2026-09-10'
  }
];
