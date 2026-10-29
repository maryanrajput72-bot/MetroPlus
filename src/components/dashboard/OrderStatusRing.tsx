import React from 'react';
import { useAdmin } from '../../context/AdminContext';
import { PieChart, Clock, Truck, CheckCircle2, ShieldAlert } from 'lucide-react';

export const OrderStatusRing: React.FC = () => {
  const { orders } = useAdmin();

  const total = orders.length || 1;
  const pending = orders.filter((o) => o.status === 'pending').length;
  const onway = orders.filter((o) => o.status === 'onway').length;
  const delivered = orders.filter((o) => o.status === 'delivered').length;
  const completed = orders.filter((o) => o.status === 'completed').length;

  const segments = [
    { label: 'Completed', count: completed, color: '#2F9A63', icon: CheckCircle2 },
    { label: 'On the Way', count: onway, color: '#4E7BD8', icon: Truck },
    { label: 'Pending', count: pending, color: '#E0A42E', icon: Clock },
    { label: 'Delivered', count: delivered, color: '#8E6BD0', icon: CheckCircle2 }
  ];

  // SVG Donut calculation
  const size = 150;
  const strokeWidth = 16;
  const radius = (size - strokeWidth) / 2;
  const circumference = 2 * Math.PI * radius;

  let currentOffset = 0;

  return (
    <div className="metro-card p-6 flex flex-col justify-between">
      {/* Header */}
      <div className="flex items-center justify-between pb-3 border-b border-[#C9A24A]/15">
        <div>
          <span className="text-[10px] font-bold tracking-[0.2em] text-[#C9A24A] uppercase flex items-center gap-1.5">
            <PieChart className="w-3.5 h-3.5 text-[#C9A24A]" />
            FULFILLMENT TELEMETRY
          </span>
          <h3 className="text-lg font-bold text-[#FFFDF8] mt-1 font-['Cinzel',serif]">
            Order Status Ring
          </h3>
        </div>
        <span className="text-xs font-mono font-bold text-[#C9A24A] bg-[#C9A24A]/10 px-2.5 py-1 rounded-lg border border-[#C9A24A]/25">
          {orders.length} TOTAL
        </span>
      </div>

      {/* Donut and Legend */}
      <div className="flex flex-col sm:flex-row items-center gap-6 mt-4">
        {/* SVG Donut */}
        <div className="relative w-36 h-36 flex-shrink-0 flex items-center justify-center">
          <svg className="w-full h-full -rotate-90 transform" viewBox={`0 0 ${size} ${size}`}>
            <circle
              cx={size / 2}
              cy={size / 2}
              r={radius}
              stroke="#1C1711"
              strokeWidth={strokeWidth}
              fill="transparent"
            />
            {segments.map((seg, i) => {
              const dashLength = (seg.count / total) * circumference;
              const dashArray = `${dashLength} ${circumference - dashLength}`;
              const strokeOffset = -currentOffset;
              currentOffset += dashLength;

              return (
                <circle
                  key={i}
                  cx={size / 2}
                  cy={size / 2}
                  r={radius}
                  stroke={seg.color}
                  strokeWidth={strokeWidth}
                  strokeDasharray={dashArray}
                  strokeDashoffset={strokeOffset}
                  fill="transparent"
                  className="transition-all duration-700 ease-out"
                />
              );
            })}
          </svg>

          {/* Center Counter */}
          <div className="absolute inset-0 flex flex-col items-center justify-center text-center">
            <span className="text-2xl font-black text-[#FFFDF8] font-mono leading-none">
              {orders.length}
            </span>
            <span className="text-[10px] text-[#A39A8B] uppercase tracking-wider font-semibold mt-1">
              Active Flow
            </span>
          </div>
        </div>

        {/* Legend List */}
        <div className="flex-1 w-full space-y-2">
          {segments.map((seg, i) => {
            const pct = Math.round((seg.count / total) * 100);
            return (
              <div key={i} className="flex items-center justify-between text-xs p-1.5 rounded-lg hover:bg-white/[0.03]">
                <div className="flex items-center gap-2">
                  <span className="w-2.5 h-2.5 rounded-full flex-shrink-0" style={{ backgroundColor: seg.color }} />
                  <span className="text-[#A39A8B] font-medium">{seg.label}</span>
                </div>
                <div className="flex items-center gap-2">
                  <span className="font-bold text-white font-mono">{seg.count}</span>
                  <span className="text-[11px] text-zinc-500 font-mono w-9 text-right">{pct}%</span>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};
