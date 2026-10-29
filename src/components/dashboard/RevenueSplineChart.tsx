import React, { useState } from 'react';
import { useAdmin } from '../../context/AdminContext';
import { TrendingUp, ArrowUpRight, DollarSign } from 'lucide-react';

interface ChartPoint {
  label: string;
  sales: number;
  commission: number;
}

const DATA_30D: ChartPoint[] = [
  { label: 'Oct 01', sales: 12400, commission: 1860 },
  { label: 'Oct 05', sales: 16800, commission: 2520 },
  { label: 'Oct 10', sales: 14200, commission: 2130 },
  { label: 'Oct 15', sales: 22500, commission: 3375 },
  { label: 'Oct 20', sales: 28900, commission: 4335 },
  { label: 'Oct 25', sales: 34100, commission: 5115 },
  { label: 'Today', sales: 41200, commission: 6180 }
];

export const RevenueSplineChart: React.FC = () => {
  const { formatCurrency } = useAdmin();
  const [activePoint, setActivePoint] = useState<ChartPoint | null>(DATA_30D[DATA_30D.length - 1]);
  const [hoverIndex, setHoverIndex] = useState<number | null>(DATA_30D.length - 1);

  const width = 600;
  const height = 180;
  const paddingX = 40;
  const paddingY = 25;

  const maxVal = Math.max(...DATA_30D.map((d) => d.sales)) * 1.15;
  const minVal = 0;

  // Compute coordinate points
  const points = DATA_30D.map((d, index) => {
    const x = paddingX + (index / (DATA_30D.length - 1)) * (width - paddingX * 2);
    const y = height - paddingY - ((d.sales - minVal) / (maxVal - minVal)) * (height - paddingY * 2);
    return { x, y, data: d };
  });

  // Build SVG smooth cubic bezier path
  let pathD = `M ${points[0].x} ${points[0].y}`;
  for (let i = 0; i < points.length - 1; i++) {
    const current = points[i];
    const next = points[i + 1];
    const cp1x = current.x + (next.x - current.x) / 2;
    const cp1y = current.y;
    const cp2x = current.x + (next.x - current.x) / 2;
    const cp2y = next.y;
    pathD += ` C ${cp1x} ${cp1y}, ${cp2x} ${cp2y}, ${next.x} ${next.y}`;
  }

  // Area fill under path
  const areaD = `${pathD} L ${points[points.length - 1].x} ${height - paddingY} L ${points[0].x} ${height - paddingY} Z`;

  return (
    <div className="metro-card p-6 flex flex-col justify-between">
      {/* Header */}
      <div className="flex items-start justify-between gap-4 pb-3 border-b border-[#C9A24A]/15">
        <div>
          <span className="text-[10px] font-bold tracking-[0.2em] text-[#C9A24A] uppercase flex items-center gap-1.5">
            <TrendingUp className="w-3.5 h-3.5 text-[#55D88D]" />
            FINANCIAL PERFORMANCE
          </span>
          <h3 className="text-lg font-bold text-[#FFFDF8] mt-1 font-['Cinzel',serif]">
            Platform Sales & Net Revenue
          </h3>
        </div>

        <div className="text-right">
          <div className="text-2xl font-extrabold text-[#FFFDF8] font-mono tracking-tight">
            {activePoint ? formatCurrency(activePoint.sales) : '$0'}
          </div>
          <div className="text-xs text-[#55D88D] flex items-center justify-end gap-1 font-medium mt-0.5">
            <ArrowUpRight className="w-3.5 h-3.5" />
            <span>+28.4% vs last period</span>
          </div>
        </div>
      </div>

      {/* Interactive SVG Chart */}
      <div className="relative mt-4">
        <svg
          viewBox={`0 0 ${width} ${height}`}
          className="w-full h-44 overflow-visible"
        >
          <defs>
            <linearGradient id="splineGradient" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stopColor="#C9A24A" stopOpacity="0.45" />
              <stop offset="60%" stopColor="#C9A24A" stopOpacity="0.08" />
              <stop offset="100%" stopColor="#C9A24A" stopOpacity="0.0" />
            </linearGradient>
            <filter id="lineGlow" x="-20%" y="-20%" width="140%" height="140%">
              <feGaussianBlur stdDeviation="3" result="blur" />
              <feComposite in="SourceGraphic" in2="blur" operator="over" />
            </filter>
          </defs>

          {/* Horizontal Gridlines */}
          {[0.25, 0.5, 0.75].map((pct, i) => {
            const lineY = paddingY + pct * (height - paddingY * 2);
            return (
              <line
                key={i}
                x1={paddingX}
                y1={lineY}
                x2={width - paddingX}
                y2={lineY}
                stroke="rgba(201, 162, 74, 0.1)"
                strokeDasharray="4 4"
              />
            );
          })}

          {/* Area Fill */}
          <path d={areaD} fill="url(#splineGradient)" />

          {/* Main Spline Curve */}
          <path
            d={pathD}
            fill="none"
            stroke="#C9A24A"
            strokeWidth="2.8"
            filter="url(#lineGlow)"
          />

          {/* Interactive Hover Guides & Dots */}
          {points.map((pt, i) => {
            const isHovered = hoverIndex === i;
            return (
              <g
                key={i}
                className="cursor-pointer"
                onMouseEnter={() => {
                  setHoverIndex(i);
                  setActivePoint(pt.data);
                }}
              >
                {/* Vertical guide line on active point */}
                {isHovered && (
                  <line
                    x1={pt.x}
                    y1={paddingY}
                    x2={pt.x}
                    y2={height - paddingY}
                    stroke="rgba(255, 241, 196, 0.35)"
                    strokeWidth="1.2"
                    strokeDasharray="3 3"
                  />
                )}

                {/* Outer Glow Circle */}
                <circle
                  cx={pt.x}
                  cy={pt.y}
                  r={isHovered ? 7 : 4}
                  fill={isHovered ? '#FFF1C4' : '#0B0B0C'}
                  stroke="#C9A24A"
                  strokeWidth={isHovered ? 3 : 2}
                  className="transition-all duration-150"
                />

                {/* X-axis labels */}
                <text
                  x={pt.x}
                  y={height - 6}
                  textAnchor="middle"
                  fill={isHovered ? '#FFF1C4' : '#6E6658'}
                  fontSize="10"
                  fontFamily="Inter, sans-serif"
                  fontWeight={isHovered ? '700' : '400'}
                >
                  {pt.data.label}
                </text>
              </g>
            );
          })}
        </svg>

        {/* Selected Data Tooltip Pill */}
        {activePoint && (
          <div className="flex items-center justify-between mt-2 pt-2 border-t border-white/5 text-xs">
            <div className="flex items-center gap-2">
              <span className="text-[#A39A8B]">Selected: <strong className="text-white">{activePoint.label}</strong></span>
              <span className="text-[#6E6658]">•</span>
              <span className="text-[#C9A24A]">Commission: <strong>{formatCurrency(activePoint.commission)}</strong></span>
            </div>
            <div className="text-[11px] text-[#A39A8B]">
              Margin: <span className="text-[#55D88D] font-bold font-mono">15.0% Guaranteed</span>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};
