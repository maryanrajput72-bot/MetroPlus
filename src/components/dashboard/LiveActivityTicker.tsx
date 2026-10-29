import React, { useState, useEffect } from 'react';
import { Plane, TrendingUp, ShieldCheck, DollarSign, Sparkles } from 'lucide-react';
import { useAdmin } from '../../context/AdminContext';

export const LiveActivityTicker: React.FC = () => {
  const { activities, formatCurrency } = useAdmin();
  const [currentIndex, setCurrentIndex] = useState(0);

  useEffect(() => {
    if (activities.length <= 1) return;
    const interval = setInterval(() => {
      setCurrentIndex((prev) => (prev + 1) % activities.length);
    }, 4500);
    return () => clearInterval(interval);
  }, [activities.length]);

  if (!activities || activities.length === 0) return null;
  const current = activities[currentIndex];

  const getIcon = (type: string) => {
    switch (type) {
      case 'order_shipped':
        return <Plane className="w-3.5 h-3.5 text-[#4E7BD8] animate-pulse" />;
      case 'profit_earned':
        return <TrendingUp className="w-3.5 h-3.5 text-[#55D88D]" />;
      case 'withdrawal':
        return <DollarSign className="w-3.5 h-3.5 text-[#E0A42E]" />;
      case 'kyc_verified':
        return <ShieldCheck className="w-3.5 h-3.5 text-[#8E6BD0]" />;
      default:
        return <Sparkles className="w-3.5 h-3.5 text-[#C9A24A]" />;
    }
  };

  return (
    <div className="bg-gradient-to-r from-[#17130E] via-[#100D09] to-[#17130E] border-b border-[#C9A24A]/15 px-4 lg:px-8 py-2 overflow-hidden">
      <div className="flex items-center justify-between text-xs max-w-[1600px] mx-auto">
        <div className="flex items-center gap-3">
          <span className="flex items-center gap-1 text-[10px] uppercase tracking-widest font-bold text-[#C9A24A] bg-[#C9A24A]/10 px-2 py-0.5 rounded border border-[#C9A24A]/25">
            <span className="w-1.5 h-1.5 rounded-full bg-[#2F9A63] animate-ping" />
            DISPATCH RADAR
          </span>

          <div className="flex items-center gap-2 transition-all duration-500 animate-in fade-in">
            {getIcon(current.type)}
            <span className="font-semibold text-[#FFFDF8]">{current.title}:</span>
            <span className="text-[#A39A8B]">{current.subtitle}</span>
            {current.amount && (
              <span className="text-[#55D88D] font-mono font-bold bg-[#122B1E] px-1.5 py-0.2 rounded border border-[#1D4A33]">
                {formatCurrency(current.amount)}
              </span>
            )}
          </div>
        </div>

        <div className="hidden sm:flex items-center gap-3 text-[#6E6658]">
          <span>Event {currentIndex + 1} of {activities.length}</span>
          <span className="text-[#C9A24A]">•</span>
          <span>{current.timeAgo}</span>
        </div>
      </div>
    </div>
  );
};
