import React, { useState } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { 
  LayoutDashboard, 
  Users, 
  KeyRound, 
  Calculator, 
  TrainTrack, 
  Camera, 
  ClipboardCheck, 
  UploadCloud, 
  Settings as SettingsIcon, 
  ScrollText, 
  LogOut, 
  ChevronRight, 
  X,
  AlertTriangle,
  ShieldCheck
} from 'lucide-react';
import { useMetro } from '../../context/MetroContext';

interface SidebarProps {
  mobileOpen: boolean;
  setMobileOpen: (open: boolean) => void;
}

export const Sidebar: React.FC<SidebarProps> = ({ mobileOpen, setMobileOpen }) => {
  const { stats, updaterConfig, showToast } = useMetro();
  const [showLogoutModal, setShowLogoutModal] = useState(false);
  const navigate = useNavigate();

  // Navigation Items exactly in the user's requested order:
  // 1. staff management
  // 2. tl/rp accounts
  // 3. tom recordes
  // 4. sttaion configration
  // 5. attendence & gps
  // 6. staff attendence
  // 7. all team app version
  // 8. setting
  // 9. activity logs
  // 10. logout
  const navItems = [
    {
      to: '/',
      label: 'Dashboard',
      sublabel: 'Control Center & Overview',
      icon: LayoutDashboard,
      badge: 'Live',
      badgeColor: 'bg-[#122B1E] text-[#55D88D] border-[#1D4A33]'
    },
    {
      to: '/staff-management',
      label: 'Staff Management',
      sublabel: 'TOM Operators & Team Roster',
      icon: Users,
      badge: stats.outdatedCount > 0 ? `${stats.outdatedCount} Outdated` : 'Synced',
      badgeColor: stats.outdatedCount > 0 ? 'bg-[#2E1412] text-[#F38A7C] border-[#592622]' : 'bg-[#122B1E] text-[#55D88D] border-[#1D4A33]'
    },
    {
      to: '/tl-rp-accounts',
      label: 'TL/RP Accounts',
      sublabel: 'Team Lead & RP Credentials',
      icon: KeyRound,
      badge: '48 Users'
    },
    {
      to: '/tom-records',
      label: 'TOM Records',
      sublabel: 'TOM 1–5 Closing Collections',
      icon: Calculator,
      badge: 'RMBS Sync',
      badgeColor: 'bg-[#251A0E] text-[#FFF1C4] border-[#C9A24A]/30'
    },
    {
      to: '/station-configuration',
      label: 'Station Configuration',
      sublabel: '24 Red Line Corridor Ports',
      icon: TrainTrack,
      badge: '24 Ports'
    },
    {
      to: '/attendance-gps',
      label: 'Attendance & GPS',
      sublabel: 'CameraX GPS Photo Captures',
      icon: Camera,
      badge: `${stats.photosCount} Today`
    },
    {
      to: '/staff-attendance',
      label: 'Staff Attendance',
      sublabel: 'Daily Roster & WhatsApp Dispatch',
      icon: ClipboardCheck,
      badge: `${stats.onDutyCount} Duty`,
      badgeColor: 'bg-[#122B1E] text-[#55D88D] border-[#1D4A33]'
    },
    {
      to: '/all-team-app-version',
      label: 'All Team App Version',
      sublabel: 'OTA Updater & Signal 7280',
      icon: UploadCloud,
      badge: updaterConfig.trial.versionName || 'v2.1.60'
    },
    {
      to: '/setting',
      label: 'Setting',
      sublabel: 'Feature Flags & Access Gate',
      icon: SettingsIcon
    },
    {
      to: '/activity-logs',
      label: 'Activity Logs',
      sublabel: 'Real-time Corridor Event Audit',
      icon: ScrollText,
      badge: 'Stream',
      badgeColor: 'bg-blue-500/10 text-blue-400 border-blue-500/20'
    }
  ];

  const handleConfirmLogout = () => {
    setShowLogoutModal(false);
    showToast('Admin session logged out successfully');
    navigate('/');
  };

  return (
    <>
      {/* Mobile Backdrop */}
      {mobileOpen && (
        <div 
          className="fixed inset-0 bg-black/80 backdrop-blur-sm z-40 lg:hidden"
          onClick={() => setMobileOpen(false)}
        />
      )}

      {/* Sidebar Container */}
      <aside
        className={`fixed top-0 bottom-0 left-0 z-50 w-72 bg-[#0E0C09] border-r border-[#C9A24A]/20 flex flex-col transition-transform duration-300 ease-in-out lg:translate-x-0 ${
          mobileOpen ? 'translate-x-0' : '-translate-x-full'
        }`}
      >
        {/* Brand Header */}
        <div className="p-6 border-b border-[#C9A24A]/15 relative">
          <button 
            className="lg:hidden absolute top-5 right-5 text-zinc-400 hover:text-white"
            onClick={() => setMobileOpen(false)}
          >
            <X className="w-5 h-5" />
          </button>

          <div className="flex items-center gap-3.5">
            <div className="relative">
              <div className="w-11 h-11 rounded-xl bg-gradient-to-br from-[#2A2012] via-[#1A140B] to-[#0A0805] border border-[#C9A24A]/50 flex items-center justify-center shadow-lg shadow-black/80">
                <img src="/logo.svg" alt="Metro +" className="w-7 h-7 drop-shadow-[0_0_8px_rgba(201,162,74,0.6)]" />
              </div>
              <span className="absolute -top-1 -right-1 w-3 h-3 rounded-full bg-[#2F9A63] border-2 border-[#0E0C09]" />
            </div>

            <div>
              <div className="flex items-center gap-1.5">
                <h1 className="text-xl font-extrabold tracking-wider text-white font-['Cinzel',serif]">
                  METRO <span className="text-[#C9A24A] font-sans font-bold">+</span>
                </h1>
                <span className="text-[9px] uppercase px-1.5 py-0.5 rounded bg-[#C9A24A]/15 text-[#E5C06E] border border-[#C9A24A]/30 font-bold tracking-widest">
                  v2.1.60
                </span>
              </div>
              <p className="text-[10px] uppercase tracking-[0.2em] text-[#A39A8B] font-semibold mt-0.5">
                Red Line Staff Command
              </p>
            </div>
          </div>
        </div>

        {/* Navigation List in exact user requested order */}
        <div className="flex-1 overflow-y-auto px-3.5 py-4 space-y-1">
          <div className="px-3 pb-1.5">
            <span className="text-[9.5px] font-bold tracking-[0.22em] text-[#8A6A1F] uppercase">
              Metro+ Operations
            </span>
          </div>

          {navItems.map((item) => {
            const Icon = item.icon;
            return (
              <NavLink
                key={item.to}
                to={item.to}
                end={item.to === '/'}
                onClick={() => setMobileOpen(false)}
                className={({ isActive }) =>
                  `group flex items-center justify-between px-3.5 py-2.5 rounded-xl text-xs transition-all duration-200 ${
                    isActive
                      ? 'bg-gradient-to-r from-[#2A2012] to-[#1C160D] text-[#FFF1C4] border border-[#C9A24A]/40 shadow-md shadow-black/40'
                      : 'text-[#A39A8B] hover:text-[#FFFDF8] hover:bg-white/[0.04]'
                  }`
                }
              >
                {({ isActive }) => (
                  <>
                    <div className="flex items-center gap-3">
                      <div
                        className={`w-7 h-7 rounded-lg flex items-center justify-center transition-colors ${
                          isActive
                            ? 'bg-[#C9A24A] text-[#0E0C09]'
                            : 'bg-white/5 text-[#A39A8B] group-hover:text-[#FFFDF8] group-hover:bg-white/10'
                        }`}
                      >
                        <Icon className="w-3.5 h-3.5" />
                      </div>
                      <div>
                        <div className={`font-semibold leading-tight ${isActive ? 'text-[#FFF1C4]' : ''}`}>
                          {item.label}
                        </div>
                        <div className="text-[9.5px] text-[#6E6658] font-normal">
                          {item.sublabel}
                        </div>
                      </div>
                    </div>

                    <div className="flex items-center gap-1.5">
                      {item.badge && (
                        <span
                          className={`text-[9px] font-bold px-1.5 py-0.5 rounded-full border ${
                            item.badgeColor || 'bg-white/5 text-[#A39A8B] border-white/10'
                          }`}
                        >
                          {item.badge}
                        </span>
                      )}
                      <ChevronRight
                        className={`w-3.5 h-3.5 transition-transform ${
                          isActive ? 'text-[#C9A24A] translate-x-0.5' : 'text-zinc-600 opacity-0 group-hover:opacity-100'
                        }`}
                      />
                    </div>
                  </>
                )}
              </NavLink>
            );
          })}

          {/* 10. Logout Button (Exact Item 10) */}
          <div className="pt-2">
            <button
              onClick={() => setShowLogoutModal(true)}
              className="w-full group flex items-center justify-between px-3.5 py-2.5 rounded-xl text-xs text-[#E57373] hover:text-white hover:bg-red-500/10 border border-transparent hover:border-red-500/20 transition-all duration-200"
            >
              <div className="flex items-center gap-3">
                <div className="w-7 h-7 rounded-lg bg-red-500/10 text-[#E57373] flex items-center justify-center group-hover:bg-red-500 group-hover:text-white transition-colors">
                  <LogOut className="w-3.5 h-3.5" />
                </div>
                <div className="text-left">
                  <div className="font-semibold leading-tight">Logout</div>
                  <div className="text-[9.5px] text-red-400/60 font-normal">End Controller Session</div>
                </div>
              </div>
              <ChevronRight className="w-3.5 h-3.5 text-red-400/50 opacity-0 group-hover:opacity-100 transition-opacity" />
            </button>
          </div>
        </div>

        {/* Footer Admin User Card */}
        <div className="p-4 border-t border-[#C9A24A]/15 bg-[#0A0907]">
          <div className="flex items-center justify-between p-2.5 rounded-xl bg-white/[0.03] border border-white/5">
            <div className="flex items-center gap-2.5">
              <div className="w-8 h-8 rounded-full bg-gradient-to-tr from-[#C9A24A] to-[#FFF1C4] p-[1.5px] flex items-center justify-center font-bold text-[#0B0B0C] text-xs">
                M+
              </div>

              <div>
                <div className="text-xs font-bold text-white leading-tight">Metro+ Controller</div>
                <div className="text-[10px] text-[#55D88D] font-mono">Firebase Online (5966d)</div>
              </div>
            </div>

            <div className="w-2.5 h-2.5 rounded-full bg-[#2F9A63] animate-ping" />
          </div>
        </div>
      </aside>

      {/* Logout Confirmation Modal */}
      {showLogoutModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/85 backdrop-blur-md animate-in fade-in duration-200">
          <div className="relative w-full max-w-sm rounded-2xl bg-[#14110E] border border-[#C9A24A]/40 p-6 shadow-2xl shadow-black">
            <div className="w-12 h-12 rounded-full bg-red-500/10 border border-red-500/20 text-red-400 flex items-center justify-center mx-auto mb-4">
              <AlertTriangle className="w-6 h-6" />
            </div>

            <h3 className="text-lg font-bold text-white text-center font-['Cinzel',serif]">
              Confirm Logout
            </h3>

            <p className="text-xs text-[#A39A8B] text-center mt-2 leading-relaxed">
              Are you sure you want to end your active session on the Metro+ Red Line Admin Panel?
            </p>

            <div className="grid grid-cols-2 gap-3 mt-6">
              <button
                onClick={() => setShowLogoutModal(false)}
                className="px-4 py-2.5 rounded-xl text-xs font-bold text-[#A39A8B] bg-white/5 hover:bg-white/10 border border-white/10 hover:text-white transition-all"
              >
                Cancel
              </button>
              <button
                onClick={handleConfirmLogout}
                className="px-4 py-2.5 rounded-xl text-xs font-bold text-white bg-red-600 hover:bg-red-500 shadow-lg shadow-red-600/30 transition-all flex items-center justify-center gap-1.5"
              >
                <LogOut className="w-3.5 h-3.5" />
                <span>Logout</span>
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};
