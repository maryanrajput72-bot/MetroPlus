import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useMetro } from '../../context/MetroContext';
import { 
  Users, 
  Crown, 
  ArrowUpRight, 
  TrendingUp, 
  CheckCircle2, 
  Clock, 
  UserX,
  ExternalLink 
} from 'lucide-react';

export const MetroDashboardCards: React.FC = () => {
  const { totalNetworkSales, stats, stationSales, todayAttendance } = useMetro();
  const [timeFilter, setTimeFilter] = useState<'All time' | 'Today'>('All time');

  // Spline data points based on sales
  const chartPoints = [
    { label: 'Oct 01', val: 1850000 },
    { label: 'Oct 08', val: 2100000 },
    { label: 'Oct 15', val: 2320000 },
    { label: 'Oct 22', val: 2540000 },
    { label: 'Today', val: totalNetworkSales }
  ];

  const maxVal = Math.max(...chartPoints.map(p => p.val)) * 1.1;
  const width = 500;
  const height = 140;
  const padX = 20;
  const padY = 20;

  const points = chartPoints.map((pt, i) => {
    const x = padX + (i / (chartPoints.length - 1)) * (width - padX * 2);
    const y = height - padY - (pt.val / maxVal) * (height - padY * 2);
    return { x, y, ...pt };
  });

  // SVG Spline Path
  let pathD = `M ${points[0].x} ${points[0].y}`;
  for (let i = 0; i < points.length - 1; i++) {
    const p1 = points[i];
    const p2 = points[i + 1];
    const cpX = (p1.x + p2.x) / 2;
    pathD += ` C ${cpX} ${p1.y}, ${cpX} ${p2.y}, ${p2.x} ${p2.y}`;
  }
  const areaD = `${pathD} L ${points[points.length - 1].x} ${height - padY} L ${points[0].x} ${height - padY} Z`;

  // Status Mix Donut
  const entries = todayAttendance.entries;
  const totalRoster = entries.length || 1;
  const onDuty = entries.filter(e => e.status === '').length;
  const waiting = entries.filter(e => e.status === 'WAITING').length;
  const off = entries.filter(e => e.status === 'OFF').length;
  const leave = entries.filter(e => e.status === 'LEAVE' || e.status === 'ABSENT').length;

  const donutSize = 130;
  const strokeWidth = 14;
  const radius = (donutSize - strokeWidth) / 2;
  const circumference = 2 * Math.PI * radius;

  const donutSegments = [
    { label: 'On Duty', count: onDuty, color: '#2F9A63' },
    { label: 'Waiting', count: waiting, color: '#E0A42E' },
    { label: 'Off', count: off, color: '#4E7BD8' },
    { label: 'Leave', count: leave, color: '#8E6BD0' }
  ];

  let currentOffset = 0;

  return (
    <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 items-stretch">
      {/* 1. Left Card: Total Revenue / Sales Spline Card (matching Image 1) */}
      <div className="lg:col-span-5 rounded-[22px] bg-[#14110E] border border-[#C9A24A]/25 p-6 flex flex-col justify-between shadow-xl">
        <div>
          <div className="flex items-center justify-between pb-2">
            <span className="text-[10px] font-bold tracking-[0.2em] text-[#A39A8B] uppercase">
              NETWORK REVENUE / SALES
            </span>
            <button
              onClick={() => setTimeFilter(prev => prev === 'All time' ? 'Today' : 'All time')}
              className="px-3 py-1 rounded-full text-xs font-bold bg-white/5 border border-white/10 text-[#FFF1C4] hover:bg-white/10 transition-colors flex items-center gap-1"
            >
              <TrendingUp className="w-3 h-3 text-[#C9A24A]" />
              <span>{timeFilter}</span>
            </button>
          </div>

          <div className="text-3xl font-black font-mono text-white mt-1">
            Rs {totalNetworkSales.toLocaleString()}
          </div>
          <div className="flex items-center gap-2 text-xs text-[#55D88D] mt-1 font-medium">
            <span className="w-2 h-2 rounded-full bg-[#2F9A63] animate-pulse" />
            <span>Live • from 24 Red Line stations</span>
          </div>
        </div>

        {/* SVG Spline */}
        <div className="relative mt-4">
          <svg viewBox={`0 0 ${width} ${height}`} className="w-full h-36 overflow-visible">
            <defs>
              <linearGradient id="heroSplineGrad" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stopColor="#C9A24A" stopOpacity="0.4" />
                <stop offset="100%" stopColor="#C9A24A" stopOpacity="0.0" />
              </linearGradient>
            </defs>

            {/* Area Fill */}
            <path d={areaD} fill="url(#heroSplineGrad)" />

            {/* Line */}
            <path d={pathD} fill="none" stroke="#C9A24A" strokeWidth="3" />

            {/* Glowing End Dot */}
            <circle
              cx={points[points.length - 1].x}
              cy={points[points.length - 1].y}
              r="6"
              fill="#FFF1C4"
              stroke="#C9A24A"
              strokeWidth="2.5"
            />
          </svg>
        </div>
      </div>

      {/* 2. Middle Card: Status Mix Donut (matching Image 1) */}
      <div className="lg:col-span-4 rounded-[22px] bg-[#14110E] border border-white/10 p-6 flex flex-col justify-between shadow-xl">
        <div className="flex items-center justify-between pb-2 border-b border-white/5">
          <div>
            <span className="text-[10px] font-bold tracking-[0.2em] text-[#C9A24A] uppercase">
              STATION ROSTER
            </span>
            <h3 className="text-base font-bold text-white font-['Cinzel',serif] mt-0.5">
              Status mix
            </h3>
          </div>

          <Link
            to="/attendance"
            className="text-xs font-bold text-[#C9A24A] hover:text-[#FFF1C4] flex items-center gap-1"
          >
            <span>Open</span>
            <span>➔</span>
          </Link>
        </div>

        <div className="flex items-center justify-center py-4">
          <div className="relative w-32 h-32 flex items-center justify-center">
            <svg className="w-full h-full -rotate-90 transform" viewBox={`0 0 ${donutSize} ${donutSize}`}>
              <circle
                cx={donutSize / 2}
                cy={donutSize / 2}
                r={radius}
                stroke="#1E1914"
                strokeWidth={strokeWidth}
                fill="transparent"
              />
              {donutSegments.map((seg, i) => {
                const dashLen = (seg.count / totalRoster) * circumference;
                const dashArr = `${dashLen} ${circumference - dashLen}`;
                const off = -currentOffset;
                currentOffset += dashLen;

                return (
                  <circle
                    key={i}
                    cx={donutSize / 2}
                    cy={donutSize / 2}
                    r={radius}
                    stroke={seg.color}
                    strokeWidth={strokeWidth}
                    strokeDasharray={dashArr}
                    strokeDashoffset={off}
                    fill="transparent"
                    className="transition-all duration-500"
                  />
                );
              })}
            </svg>

            <div className="absolute inset-0 flex flex-col items-center justify-center text-center">
              <span className="text-2xl font-black text-white font-mono leading-none">
                {totalRoster}
              </span>
              <span className="text-[9px] uppercase tracking-wider text-[#A39A8B] font-bold mt-1">
                STAFF
              </span>
            </div>
          </div>
        </div>

        <div className="flex items-center justify-between text-[11px] pt-2 border-t border-white/5 text-[#A39A8B]">
          <span className="flex items-center gap-1.5 text-white">
            <span className="w-2 h-2 rounded-full bg-[#2F9A63]" />
            On Duty ({onDuty})
          </span>
          <span className="flex items-center gap-1.5 text-white">
            <span className="w-2 h-2 rounded-full bg-[#E0A42E]" />
            Waiting ({waiting})
          </span>
          <span className="flex items-center gap-1.5 text-white">
            <span className="w-2 h-2 rounded-full bg-[#4E7BD8]" />
            Off ({off})
          </span>
        </div>
      </div>

      {/* 3. Right Stack: 2 Small Stat Cards (matching Image 1) */}
      <div className="lg:col-span-3 flex flex-col justify-between gap-4">
        {/* Top: Total Staff */}
        <Link
          to="/staff"
          className="flex-1 rounded-[20px] bg-[#14110E] border border-white/10 p-5 flex items-center gap-4 hover:border-[#C9A24A]/40 transition-all shadow-md group"
        >
          <div className="w-11 h-11 rounded-2xl bg-blue-500/10 border border-blue-500/20 flex items-center justify-center text-blue-400 group-hover:scale-110 transition-transform">
            <Users className="w-5 h-5" />
          </div>

          <div>
            <span className="text-[10px] font-bold tracking-wider text-[#A39A8B] uppercase block">
              TOTAL STAFF
            </span>
            <div className="text-2xl font-black font-mono text-white mt-0.5">
              {stats.totalStaff}
            </div>
            <div className="text-[10.5px] text-[#6E6658]">All registered staff</div>
          </div>
        </Link>

        {/* Bottom: Team Leads */}
        <Link
          to="/tl-access"
          className="flex-1 rounded-[20px] bg-[#14110E] border border-white/10 p-5 flex items-center gap-4 hover:border-[#C9A24A]/40 transition-all shadow-md group"
        >
          <div className="w-11 h-11 rounded-2xl bg-[#C9A24A]/10 border border-[#C9A24A]/30 flex items-center justify-center text-[#C9A24A] group-hover:scale-110 transition-transform">
            <Crown className="w-5 h-5" />
          </div>

          <div>
            <span className="text-[10px] font-bold tracking-wider text-[#C9A24A] uppercase block">
              TEAM LEADS (TL)
            </span>
            <div className="text-2xl font-black font-mono text-[#FFF1C4] mt-0.5">
              {stats.connectedStations}
            </div>
            <div className="text-[10.5px] text-[#6E6658]">Active across Red Line</div>
          </div>
        </Link>
      </div>
    </div>
  );
};
