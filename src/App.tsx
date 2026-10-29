import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { MetroProvider } from './context/MetroContext';
import { Dashboard } from './pages/Dashboard';
import { Staff } from './pages/Staff';
import { TLAccess } from './pages/TLAccess';
import { TomRecords } from './pages/TomRecords';
import { Stations } from './pages/Stations';
import { Photos } from './pages/Photos';
import { Attendance } from './pages/Attendance';
import { Updater } from './pages/Updater';
import { Settings } from './pages/Settings';
import { ActivityLogs } from './pages/ActivityLogs';

export const App: React.FC = () => {
  return (
    <MetroProvider>
      <BrowserRouter>
        <Routes>
          {/* Main Dashboard */}
          <Route path="/" element={<Dashboard />} />

          {/* User Requested Navigation Structure */}
          {/* 1. Staff Management */}
          <Route path="/staff-management" element={<Staff />} />
          <Route path="/staff" element={<Staff />} />

          {/* 2. TL/RP Accounts */}
          <Route path="/tl-rp-accounts" element={<TLAccess />} />
          <Route path="/tl-access" element={<TLAccess />} />

          {/* 3. TOM Records */}
          <Route path="/tom-records" element={<TomRecords />} />
          <Route path="/reports" element={<TomRecords />} />

          {/* 4. Station Configuration */}
          <Route path="/station-configuration" element={<Stations />} />
          <Route path="/stations" element={<Stations />} />

          {/* 5. Attendance & GPS */}
          <Route path="/attendance-gps" element={<Photos />} />
          <Route path="/photos" element={<Photos />} />

          {/* 6. Staff Attendance */}
          <Route path="/staff-attendance" element={<Attendance />} />
          <Route path="/attendance" element={<Attendance />} />

          {/* 7. All Team App Version */}
          <Route path="/all-team-app-version" element={<Updater />} />
          <Route path="/updater" element={<Updater />} />

          {/* 8. Setting */}
          <Route path="/setting" element={<Settings />} />
          <Route path="/settings" element={<Settings />} />

          {/* 9. Activity Logs */}
          <Route path="/activity-logs" element={<ActivityLogs />} />

          {/* Fallback */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </MetroProvider>
  );
};

export default App;
