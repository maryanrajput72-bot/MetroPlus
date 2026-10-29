import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useMetro } from '../../context/MetroContext';
import { METRO_STATIONS } from '../../data/metroStations';
import { MetroBusCanvas } from './MetroBusCanvas';
import { 
  Sparkles, 
  MapPin, 
  Radio, 
  ArrowRight, 
  ClipboardCheck, 
  Camera, 
  Users, 
  TrainTrack 
} from 'lucide-react';

export const MetroHeroCard: React.FC = () => {
  const { totalNetworkSales, totalRollsUsed, stats } = useMetro();
  const [stationIndex, setStationIndex] = useState(0);
  const [updateIndex, setUpdateIndex] = useState(0);

  // Cycle through all 24 stations on the bus digital LED destination sign
  useEffect(() => {
    const timer = setInterval(() => {
      setStationIndex(prev => (prev + 1) % METRO_STATIONS.length);
    }, 2800);
    return () => clearInterval(timer);
  }, []);

  const currentStation = METRO_STATIONS[stationIndex];

  const updates = [
    { text: 'Station Saddar (1/201) • Morning Shift attendance synced', time: 'Just now' },
    { text: 'Faizabad Hub (10/210) • CameraX GPS photo verified', time: '3m ago' },
    { text: 'Committee Chowk (4/204) • 5 TOMs closing balance entered', time: '6m ago' },
    { text: 'PIMS (19/219) • Team Lead session active (vc368)', time: '11m ago' },
    { text: 'Pak Secretariat (24/224) • Terminal inspection complete', time: '15m ago' }
  ];

  useEffect(() => {
    const timer = setInterval(() => {
      setUpdateIndex(prev => (prev + 1) % updates.length);
    }, 4500);
    return () => clearInterval(timer);
  }, [updates.length]);

  return (
    <div className="relative rounded-[26px] bg-[#120F0B] border border-[#C9A24A]/30 p-6 lg:p-9 overflow-hidden shadow-2xl shadow-black/90">
      {/* Subtle Background Golden Grid */}
      <div 
        className="absolute inset-0 opacity-[0.035] pointer-events-none"
        style={{
          backgroundImage: 'radial-gradient(#C9A24A 1px, transparent 1px)',
          backgroundSize: '24px 24px'
        }}
      />

      {/* Ambient Radial Golden Glow behind the Metro Bus */}
      <div className="absolute right-4 top-1/2 -translate-y-1/2 w-[580px] h-[580px] bg-gradient-to-l from-[#C9A24A]/20 via-red-900/10 to-transparent rounded-full blur-3xl pointer-events-none" />

      <div className="relative z-10 grid grid-cols-1 lg:grid-cols-12 gap-8 items-center">
        {/* Left Column: Heading, Subtitle, Actions & 3-Column Metrics */}
        <div className="lg:col-span-6 flex flex-col justify-between space-y-6">
          <div>
            <span className="text-[10.5px] font-extrabold tracking-[0.25em] text-[#C9A24A] uppercase flex items-center gap-1.5">
              <span className="w-1.5 h-1.5 rounded-full bg-[#C9A24A] animate-ping" />
              METRO+ NETWORK OVERVIEW
            </span>

            <h2 className="text-2xl sm:text-3xl lg:text-[38px] font-bold text-white tracking-tight leading-[1.18] mt-2 font-['Cinzel',serif]">
              Manage your complete station operations
            </h2>

            <p className="text-sm text-[#A39A8B] leading-relaxed mt-3 max-w-lg">
              Staff attendance, 24 Red Line stations, CameraX GPS photos and ticket revenue in one live dashboard.
            </p>

            {/* Quick Action Buttons */}
            <div className="flex flex-wrap items-center gap-3 mt-6">
              <Link
                to="/attendance"
                className="px-6 py-2.5 rounded-full text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] transition-all shadow-lg shadow-[#C9A24A]/25 flex items-center gap-1.5"
              >
                <span>Live Attendance</span>
              </Link>

              <Link
                to="/photos"
                className="px-5 py-2.5 rounded-full text-xs font-bold text-white bg-white/[0.04] border border-white/20 hover:border-[#C9A24A] hover:text-[#FFF1C4] transition-all"
              >
                <span>GPS Photos</span>
              </Link>

              <Link
                to="/staff"
                className="px-5 py-2.5 rounded-full text-xs font-bold text-white bg-white/[0.04] border border-white/20 hover:border-[#C9A24A] hover:text-[#FFF1C4] transition-all"
              >
                <span>Staff Directory</span>
              </Link>
            </div>
          </div>

          {/* Lower 3-Column Metrics Row: TOTAL SALE | ROLLS USED | TOTAL ON DUTY */}
          <div className="pt-6 border-t border-[#C9A24A]/15 grid grid-cols-3 gap-4 text-left">
            {/* Metric 1: Total Sale */}
            <div>
              <div className="text-xl sm:text-2xl lg:text-[26px] font-black font-mono text-white tracking-tight">
                Rs {totalNetworkSales.toLocaleString()}
              </div>
              <div className="text-[10px] uppercase font-extrabold tracking-wider text-[#A39A8B] mt-1">
                TOTAL SALE
              </div>
            </div>

            {/* Metric 2: Rolls Used */}
            <div className="border-l border-white/10 pl-4">
              <div className="text-xl sm:text-2xl lg:text-[26px] font-black font-mono text-[#FFF1C4] tracking-tight">
                {totalRollsUsed} Rolls
              </div>
              <div className="text-[10px] uppercase font-extrabold tracking-wider text-[#A39A8B] mt-1">
                ROLLS USED
              </div>
            </div>

            {/* Metric 3: Total Staff on Duty */}
            <div className="border-l border-white/10 pl-4">
              <div className="text-xl sm:text-2xl lg:text-[26px] font-black font-mono text-[#55D88D] tracking-tight">
                {stats.onDutyCount} Staff
              </div>
              <div className="text-[10px] uppercase font-extrabold tracking-wider text-[#A39A8B] mt-1">
                TOTAL ON DUTY
              </div>
            </div>
          </div>
        </div>

        {/* Right Column: Animated Metro Bus with Cycling 24 Stations LED & Flashing Headlights */}
        <div className="lg:col-span-6 relative flex flex-col items-center justify-center min-h-[380px]">
          {/* Top Floating Bar: Kept above the bus, no overlaps */}
          <div className="w-full flex items-center justify-between mb-3 z-20">
            {/* Live Updates Pill */}
            <div className="px-3.5 py-1.5 rounded-xl bg-black/85 backdrop-blur-md border border-white/15 shadow-lg max-w-[260px]">
              <div className="flex items-center gap-1.5 text-[9.5px] uppercase font-bold text-[#FFF1C4]">
                <span className="w-1.5 h-1.5 rounded-full bg-red-500 animate-ping" />
                <span>LIVE UPDATES</span>
                <span className="text-zinc-500 font-mono">({updateIndex + 1}/{updates.length})</span>
              </div>
              <div className="text-[11px] text-white font-medium truncate mt-0.5">
                {updates[updateIndex].text}
              </div>
            </div>

            {/* Top Right Floating Pill: Total Sale */}
            <div className="px-3.5 py-1.5 rounded-xl bg-[#16130E]/95 backdrop-blur-md border border-[#C9A24A]/40 shadow-xl shadow-black/80 text-right">
              <span className="text-[9px] uppercase font-bold tracking-widest text-[#A39A8B] block">
                TOTAL SALE
              </span>
              <div className="text-base font-mono font-black text-[#55D88D]">
                Rs {totalNetworkSales.toLocaleString()}
              </div>
            </div>
          </div>

          {/* Centerpiece: Clean Metro Bus with Cycling 24 Stations LED Destination Display */}
          <div className="relative w-full max-w-[490px] flex flex-col items-center justify-center py-2">
            {/* Concentric Golden Radar Halo */}
            <div className="absolute w-[330px] h-[330px] rounded-full border border-[#C9A24A]/25 animate-radar-spin pointer-events-none" />
            <div className="absolute w-[250px] h-[250px] rounded-full border border-dashed border-[#C9A24A]/30 pointer-events-none" />
            <div className="absolute w-[180px] h-[180px] rounded-full bg-gradient-to-tr from-[#C9A24A]/20 via-red-600/10 to-transparent blur-2xl pointer-events-none" />

            {/* The Bus Container with 3D Floating Animation - 100% Unified Canvas Render, Zero Pasted Overlays */}
            <div className="relative z-10 animate-float-bus w-full max-w-[460px] flex items-center justify-center group cursor-pointer select-none">
              <MetroBusCanvas stationName={currentStation.name} />
            </div>

            {/* Dynamic Ground Shadow underneath */}
            <div className="w-[380px] h-6 rounded-[100%] bg-black/95 blur-md animate-bus-shadow -mt-2 pointer-events-none" />
          </div>

          {/* Bottom Bar: Cleanly below the bus, no overlaps */}
          <div className="w-full flex items-center justify-between mt-2 z-20">
            {/* Bottom Left Legend */}
            <div className="flex items-center gap-3 text-[10px] text-[#A39A8B] bg-black/60 backdrop-blur-sm px-3 py-1 rounded-lg border border-white/5">
              <span className="flex items-center gap-1 text-[#C9A24A]">
                <span className="w-1.5 h-1.5 rounded-full bg-[#C9A24A]" />
                Rawalpindi Zone
              </span>
              <span className="flex items-center gap-1 text-[#55D88D]">
                <span className="w-1.5 h-1.5 rounded-full bg-[#55D88D]" />
                Islamabad Zone
              </span>
            </div>

            {/* Bottom Right Floating Badge: 24 Stations Live */}
            <div className="px-3.5 py-1.5 rounded-xl bg-[#16130E]/95 backdrop-blur-md border border-white/10 shadow-xl flex items-center gap-2">
              <span className="text-base font-black font-mono text-white leading-none">
                24
              </span>
              <span className="text-[9.5px] uppercase font-bold tracking-wider text-[#A39A8B]">
                STATIONS LIVE
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
