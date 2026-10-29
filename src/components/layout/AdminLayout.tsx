import React, { useState } from 'react';
import { Sidebar } from './Sidebar';
import { Header } from './Header';
import { Toast } from '../common/Toast';
import { useMetro } from '../../context/MetroContext';
import { Radio, AlertCircle } from 'lucide-react';

interface AdminLayoutProps {
  children: React.ReactNode;
  title: string;
  subtitle?: string;
}

export const AdminLayout: React.FC<AdminLayoutProps> = ({ children, title, subtitle }) => {
  const [mobileOpen, setMobileOpen] = useState(false);
  const { stats, updaterConfig } = useMetro();

  return (
    <div className="min-h-screen bg-[#0B0B0C] text-[#FFFDF8] flex">
      {/* Sidebar */}
      <Sidebar mobileOpen={mobileOpen} setMobileOpen={setMobileOpen} />

      {/* Main Content Area */}
      <div className="flex-1 lg:pl-72 flex flex-col min-w-0">
        {/* Top Header */}
        <Header 
          onMenuClick={() => setMobileOpen(true)} 
          title={title} 
          subtitle={subtitle} 
        />

        {/* Real-time Metro Network Broadcast Bar */}
        <div className="bg-gradient-to-r from-[#17130E] via-[#100D09] to-[#17130E] border-b border-[#C9A24A]/15 px-4 lg:px-8 py-2 overflow-hidden">
          <div className="flex items-center justify-between text-xs max-w-[1600px] mx-auto">
            <div className="flex items-center gap-3">
              <span className="flex items-center gap-1 text-[10px] uppercase tracking-widest font-bold text-[#C9A24A] bg-[#C9A24A]/10 px-2 py-0.5 rounded border border-[#C9A24A]/25">
                <span className="w-1.5 h-1.5 rounded-full bg-[#2F9A63] animate-ping" />
                CORRIDOR TELEMETRY
              </span>

              <div className="flex items-center gap-3 text-white">
                <span className="text-[#A39A8B]">
                  Live Staff: <strong className="text-white">{stats.onlineStaff}/{stats.totalStaff} Online</strong>
                </span>
                <span className="text-zinc-600">•</span>
                <span className="text-[#A39A8B]">
                  Attendance: <strong className="text-[#55D88D]">{stats.onDutyCount} On Duty</strong>
                </span>
                <span className="text-zinc-600">•</span>
                <span className="text-[#A39A8B] hidden sm:inline">
                  Main Version: <strong className="text-[#C9A24A]">{updaterConfig.main.versionName}</strong> (vc{updaterConfig.main.versionCode})
                </span>
              </div>
            </div>

            <div className="hidden sm:flex items-center gap-2 text-[11px] text-[#A39A8B]">
              <span className="w-2 h-2 rounded-full bg-[#2F9A63]" />
              <span>Firebase Client Online: <strong>metro-plus-5966d</strong></span>
            </div>
          </div>
        </div>

        {/* Page Inner Content */}
        <main className="flex-1 p-4 lg:p-8 max-w-[1600px] w-full mx-auto">
          {children}
        </main>

        {/* Global Footer */}
        <footer className="px-6 py-5 border-t border-[#C9A24A]/10 text-center text-xs text-[#6E6658]">
          <div className="flex flex-col sm:flex-row items-center justify-between gap-2 max-w-[1600px] mx-auto">
            <div>
              <span className="font-bold text-[#A39A8B]">METRO +</span> Red Line Staff OS • Official Admin Console
            </div>
            <div className="flex items-center gap-4 text-[11px] text-[#A39A8B]">
              <span className="flex items-center gap-1.5">
                <span className="w-2 h-2 rounded-full bg-[#2F9A63] animate-pulse" />
                24 Stations Connected
              </span>
              <span>Trial Build {updaterConfig.trial.versionName}</span>
            </div>
          </div>
        </footer>
      </div>

      {/* Toast Notification Container */}
      <Toast />
    </div>
  );
};
