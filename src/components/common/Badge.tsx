import React from 'react';
import { AttendanceStatus, StaffRole } from '../../types/metro';

interface BadgeProps {
  status: AttendanceStatus | StaffRole | 'ON DUTY' | 'MAIN' | 'TRIAL' | 'OUTDATED' | 'ONLINE' | 'OFFLINE' | 'verified' | string;
  size?: 'sm' | 'md';
}

export const Badge: React.FC<BadgeProps> = ({ status, size = 'sm' }) => {
  const normalized = (status || '').toUpperCase().trim();

  let styles = 'bg-zinc-800 text-zinc-300 border-zinc-700';
  let dotColor = 'bg-zinc-400';
  let label = status || 'ON DUTY';

  if (normalized === '' || normalized === 'ON DUTY' || normalized === 'ONLINE') {
    styles = 'bg-[#122B1E] text-[#55D88D] border-[#1D4A33]';
    dotColor = 'bg-[#2F9A63]';
    label = normalized === 'ONLINE' ? 'ONLINE' : 'ON DUTY';
  } else if (normalized === 'WAITING') {
    styles = 'bg-[#2C200C] text-[#F3C562] border-[#553C14]';
    dotColor = 'bg-[#E0A42E]';
    label = 'WAITING';
  } else if (normalized === 'OFF') {
    styles = 'bg-white/5 text-[#A39A8B] border-white/10';
    dotColor = 'bg-zinc-500';
    label = 'OFF';
  } else if (normalized === 'LEAVE') {
    styles = 'bg-[#241A3D] text-[#C0A2FB] border-[#3F2B6E]';
    dotColor = 'bg-[#8E6BD0]';
    label = 'LEAVE';
  } else if (normalized === 'ABSENT' || normalized === 'OUTDATED' || normalized === 'OFFLINE') {
    styles = 'bg-[#2E1412] text-[#F38A7C] border-[#592622]';
    dotColor = 'bg-[#D2553F]';
    label = normalized;
  } else if (normalized === 'TL') {
    styles = 'bg-[#2A2012] text-[#FFF1C4] border-[#C9A24A]/40 shadow-sm';
    dotColor = 'bg-[#C9A24A]';
    label = 'TEAM LEAD (TL)';
  } else if (normalized === 'OPERATOR') {
    styles = 'bg-[#10213E] text-[#86AEF8] border-[#1A386D]';
    dotColor = 'bg-[#4E7BD8]';
    label = 'OPERATOR';
  } else if (normalized === 'MAIN') {
    styles = 'bg-[#122B1E] text-[#55D88D] border-[#1D4A33]';
    dotColor = 'bg-[#2F9A63]';
    label = 'MAIN (v2.1.31)';
  } else if (normalized === 'TRIAL') {
    styles = 'bg-[#2C200C] text-[#FFF1C4] border-[#C9A24A]/40';
    dotColor = 'bg-[#C9A24A]';
    label = 'TRIAL (v2.1.60)';
  }

  const padding = size === 'sm' ? 'px-2.5 py-0.5 text-xs' : 'px-3 py-1 text-xs';

  return (
    <span className={`inline-flex items-center gap-1.5 font-bold rounded-full border ${padding} ${styles} uppercase tracking-wider`}>
      <span className={`w-1.5 h-1.5 rounded-full ${dotColor} animate-pulse`} />
      {label}
    </span>
  );
};
