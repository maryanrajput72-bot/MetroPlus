import React from 'react';
import { useMetro } from '../../context/MetroContext';
import { ClipboardCheck, CheckCircle2, Clock, UserX, AlertCircle } from 'lucide-react';

export const AttendanceStatusRing: React.FC = () => {
  const { todayAttendance, copyWhatsAppReport } = useMetro();

  const entries = todayAttendance.entries;
  const total = entries.length || 1;
  const onDuty = entries.filter(e => e.status === '').length;
  const waiting = entries.filter(e => e.status === 'WAITING').length;
  const off = entries.filter(e => e.status === 'OFF').length;
  const leave = entries.filter(e => e.status === 'LEAVE').length;
  const absent = entries.filter(e => e.status === 'ABSENT').length;

  const items = [
    { label: 'On Duty', count: onDuty, color: '#2F9A63' },
    { label: 'Waiting', count: waiting, color: '#E0A42E' },
    { label: 'Off', count: off, color: '#6E6658' },
    { label: 'Leave', count: leave, color: '#8E6BD0' },
    { label: 'Absent', count: absent, color: '#D2553F' }
  ];

  return (
    <div className="metro-card p-6 flex flex-col justify-between">
      <div className="flex items-center justify-between pb-3 border-b border-[#C9A24A]/15">
        <div>
          <span className="text-[10px] font-bold tracking-[0.2em] text-[#C9A24A] uppercase flex items-center gap-1.5">
            <ClipboardCheck className="w-3.5 h-3.5 text-[#55D88D]" />
            DAILY ATTENDANCE
          </span>
          <h3 className="text-lg font-bold text-[#FFFDF8] mt-1 font-['Cinzel',serif]">
            {todayAttendance.stationName}
          </h3>
        </div>

        <button
          onClick={copyWhatsAppReport}
          className="px-2.5 py-1 text-[11px] font-bold text-[#FFF1C4] bg-[#2A2012] border border-[#C9A24A]/40 rounded-lg hover:border-[#C9A24A] transition-all flex items-center gap-1 shadow-sm"
        >
          <span>WhatsApp Report</span>
        </button>
      </div>

      <div className="grid grid-cols-2 sm:grid-cols-5 gap-2.5 mt-4">
        {items.map((item, i) => (
          <div key={i} className="p-3 rounded-xl bg-[#0E0C09] border border-white/5 text-center">
            <div className="text-[10px] text-[#A39A8B] uppercase font-bold">{item.label}</div>
            <div className="text-xl font-extrabold font-mono mt-0.5" style={{ color: item.color }}>
              {item.count}
            </div>
          </div>
        ))}
      </div>

      <div className="mt-4 pt-3 border-t border-white/5 flex items-center justify-between text-xs text-[#A39A8B]">
        <span>Shift: <strong className="text-white">{todayAttendance.shift}</strong></span>
        <span>Roster: <strong className="text-white">{todayAttendance.entries.length} Staff</strong></span>
        <span>Date: <strong className="text-[#FFF1C4]">{todayAttendance.date}</strong></span>
      </div>
    </div>
  );
};
