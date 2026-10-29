import React from 'react';
import { Link } from 'react-router-dom';
import { 
  Users, 
  ClipboardCheck, 
  Camera, 
  KeyRound, 
  UploadCloud, 
  FileText, 
  ArrowRight,
  TrainTrack
} from 'lucide-react';
import { useMetro } from '../../context/MetroContext';

export const QuickStationGrid: React.FC = () => {
  const { stats, updaterConfig } = useMetro();

  const actions = [
    {
      title: 'Staff & Versions',
      kicker: 'Firestore staff_versions',
      sub: `${stats.outdatedCount} staff outdated • ${stats.totalStaff} enrolled`,
      href: '/staff',
      icon: Users,
      color: '#4E7BD8'
    },
    {
      title: 'Daily Attendance',
      kicker: 'Live Roster Audit',
      sub: `${stats.onDutyCount} On Duty • WhatsApp format ready`,
      href: '/attendance',
      icon: ClipboardCheck,
      color: '#2F9A63'
    },
    {
      title: 'GPS Station Photos',
      kicker: 'CameraX GPS Streams',
      sub: `${stats.photosCount} photos stored • Delete rule active`,
      href: '/photos',
      icon: Camera,
      color: '#C9A24A'
    },
    {
      title: 'TL Access & Credentials',
      kicker: 'Team Lead 5-Digit PIN',
      sub: 'Manage TL Gmail & credentials',
      href: '/tl-access',
      icon: KeyRound,
      color: '#8E6BD0'
    },
    {
      title: '24 Stations Network',
      kicker: 'Red Line Corridor',
      sub: 'Saddar 1/201 to Pak Sec 24/224',
      href: '/stations',
      icon: TrainTrack,
      color: '#E0A42E'
    },
    {
      title: 'Release Updater',
      kicker: 'Code 7280 Protected',
      sub: `Main ${updaterConfig.main.versionName} • Trial ${updaterConfig.trial.versionName}`,
      href: '/updater',
      icon: UploadCloud,
      color: '#55D88D'
    }
  ];

  return (
    <div className="metro-card p-6">
      <div className="pb-4 border-b border-[#C9A24A]/15">
        <span className="text-[10px] font-bold tracking-[0.2em] text-[#C9A24A] uppercase">
          OPERATIONAL SHORTCUTS
        </span>
        <h3 className="text-lg font-bold text-[#FFFDF8] mt-1 font-['Cinzel',serif]">
          Red Line Stations Command
        </h3>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3.5 mt-4">
        {actions.map((act, i) => {
          const Icon = act.icon;
          return (
            <Link
              key={i}
              to={act.href}
              className="group relative p-4 rounded-xl bg-[#0F0D0A] border border-[#C9A24A]/20 hover:border-[#C9A24A]/60 transition-all duration-200 hover:-translate-y-1 hover:shadow-xl hover:shadow-black/70 flex flex-col justify-between"
            >
              <div>
                <div className="flex items-center justify-between mb-2">
                  <div
                    className="w-9 h-9 rounded-lg flex items-center justify-center transition-transform group-hover:scale-110"
                    style={{ backgroundColor: `${act.color}15`, color: act.color }}
                  >
                    <Icon className="w-4 h-4" />
                  </div>
                </div>

                <div className="text-[10px] font-bold uppercase tracking-wider text-[#C9A24A]">
                  {act.kicker}
                </div>
                <div className="text-sm font-bold text-white group-hover:text-[#FFF1C4] transition-colors mt-0.5">
                  {act.title}
                </div>
                <div className="text-xs text-[#A39A8B] mt-1 line-clamp-1">
                  {act.sub}
                </div>
              </div>

              <div className="flex items-center gap-1 text-[11px] font-bold text-[#C9A24A] mt-3 group-hover:translate-x-1 transition-transform">
                <span>Access Console</span>
                <ArrowRight className="w-3.5 h-3.5" />
              </div>
            </Link>
          );
        })}
      </div>
    </div>
  );
};
