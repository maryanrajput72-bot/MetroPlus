import React, { useState, useEffect } from 'react';
import { 
  Menu, 
  Clock, 
  Sparkles,
  Calendar,
  CheckCircle2
} from 'lucide-react';
import { useMetro } from '../../context/MetroContext';

interface HeaderProps {
  onMenuClick: () => void;
  title: string;
  subtitle?: string;
}

export const Header: React.FC<HeaderProps> = ({ onMenuClick, title, subtitle }) => {
  const [time, setTime] = useState<string>('');
  const [selectedDate, setSelectedDate] = useState<string>(() => {
    return new Date().toISOString().split('T')[0];
  });

  useEffect(() => {
    const updateTime = () => {
      const now = new Date();
      setTime(now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: true }));
    };
    updateTime();
    const timer = setInterval(updateTime, 1000);
    return () => clearInterval(timer);
  }, []);

  return (
    <header className="sticky top-0 z-30 bg-[#0B0B0C]/90 backdrop-blur-md border-b border-[#C9A24A]/15 px-4 lg:px-8 py-3.5 flex items-center justify-between">
      {/* Left: Mobile Toggle & Page Title */}
      <div className="flex items-center gap-3 lg:gap-4">
        <button
          onClick={onMenuClick}
          className="lg:hidden p-2 rounded-xl bg-white/5 border border-white/10 text-[#FFFDF8] hover:bg-white/10"
        >
          <Menu className="w-5 h-5" />
        </button>

        <div>
          <div className="flex items-center gap-2">
            <span className="text-[10px] font-bold tracking-[0.2em] text-[#C9A24A] uppercase flex items-center gap-1">
              <Sparkles className="w-3 h-3 text-[#C9A24A]" /> Metro + Red Line
            </span>
            <span className="w-1 h-1 rounded-full bg-[#6E6658]" />
            <span className="text-[10px] text-[#A39A8B] font-medium hidden sm:inline">
              Rawalpindi – Islamabad Network
            </span>
          </div>
          <h2 className="text-xl lg:text-2xl font-extrabold text-[#FFFDF8] tracking-tight font-['Cinzel',serif]">
            {title}
          </h2>
        </div>
      </div>

      {/* Right Controls: Date Selector & Live Clock (Shift and Station removed from header as requested) */}
      <div className="flex items-center gap-3 sm:gap-4">
        {/* Date Selection Box (As requested: "sab se uper date selection ka option bhi hoo ok") */}
        <div className="flex items-center gap-2 bg-[#14110E] border border-[#C9A24A]/30 rounded-xl px-3.5 py-1.5 shadow-sm">
          <Calendar className="w-3.5 h-3.5 text-[#C9A24A] flex-shrink-0" />
          <input
            type="date"
            value={selectedDate}
            onChange={(e) => setSelectedDate(e.target.value)}
            className="bg-transparent text-xs font-mono font-bold text-white focus:outline-none cursor-pointer"
          />
        </div>

        {/* Live Clock */}
        <div className="hidden md:flex items-center gap-2 px-3 py-1.5 rounded-xl bg-white/[0.03] border border-[#C9A24A]/20 text-[#A39A8B]">
          <Clock className="w-3.5 h-3.5 text-[#C9A24A]" />
          <span className="text-xs font-mono font-medium text-[#FFFDF8]">{time}</span>
          <span className="text-[9px] uppercase px-1.5 py-0.5 rounded bg-[#122B1E] text-[#55D88D] font-bold border border-[#1D4A33]">
            PKT
          </span>
        </div>
      </div>
    </header>
  );
};
