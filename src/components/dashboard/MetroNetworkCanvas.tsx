import React, { useRef, useEffect, useState } from 'react';
import { METRO_STATIONS } from '../../data/metroStations';
import { MetroStation } from '../../types/metro';
import { useMetro } from '../../context/MetroContext';
import { TrainTrack, Radio, ShieldCheck, MapPin, Sparkles } from 'lucide-react';

export const MetroNetworkCanvas: React.FC = () => {
  const canvasRef = useRef<HTMLCanvasElement | null>(null);
  const { selectedStation, setSelectedStation, staffVersions } = useMetro();
  const [hoveredStation, setHoveredStation] = useState<MetroStation | null>(null);

  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    let animationFrameId: number;
    let width = (canvas.width = canvas.parentElement?.clientWidth || 900);
    let height = (canvas.height = 360);

    const handleResize = () => {
      if (!canvas.parentElement) return;
      width = canvas.width = canvas.parentElement.clientWidth;
      height = canvas.height = 360;
    };
    window.addEventListener('resize', handleResize);

    // Calculate canvas coordinates for all 24 stations in an S-curved corridor
    const stationCoords = METRO_STATIONS.map((station, i) => {
      const t = i / (METRO_STATIONS.length - 1); // 0 to 1
      // S-curve corridor from bottom-left to top-right
      const paddingX = 60;
      const paddingY = 60;
      const x = paddingX + t * (width - paddingX * 2);
      // Gentle sine curve wave
      const y = height - paddingY - t * (height - paddingY * 2) + Math.sin(t * Math.PI * 2) * 35;
      return { station, x, y };
    });

    // Metro Train simulation
    let trainT1 = 0.2;
    let trainT2 = 0.7;
    let pulseAngle = 0;

    const render = () => {
      pulseAngle += 0.04;
      trainT1 = (trainT1 + 0.0018) % 1;
      trainT2 = (trainT2 + 0.0022) % 1;

      ctx.clearRect(0, 0, width, height);

      // 1. Draw Background Grid
      ctx.strokeStyle = 'rgba(201, 162, 74, 0.04)';
      ctx.lineWidth = 1;
      for (let x = 0; x < width; x += 50) {
        ctx.beginPath();
        ctx.moveTo(x, 0);
        ctx.lineTo(x, height);
        ctx.stroke();
      }
      for (let y = 0; y < height; y += 50) {
        ctx.beginPath();
        ctx.moveTo(0, y);
        ctx.lineTo(width, y);
        ctx.stroke();
      }

      // 2. Draw Zone Boundary: Rawalpindi vs Islamabad (Faizabad is #10 / IJP is #11)
      const faizabadCoord = stationCoords[9];
      if (faizabadCoord) {
        ctx.save();
        ctx.strokeStyle = 'rgba(201, 162, 74, 0.2)';
        ctx.setLineDash([4, 6]);
        ctx.beginPath();
        ctx.moveTo(faizabadCoord.x, 20);
        ctx.lineTo(faizabadCoord.x, height - 20);
        ctx.stroke();
        ctx.restore();

        // Zone Labels
        ctx.font = '10px Inter, sans-serif';
        ctx.fillStyle = '#C9A24A';
        ctx.fillText('◄ RAWALPINDI ZONE (#1 - #10)', faizabadCoord.x - 170, 35);
        ctx.fillStyle = '#55D88D';
        ctx.fillText('ISLAMABAD ZONE (#11 - #24) ►', faizabadCoord.x + 20, 35);
      }

      // 3. Draw Metro Track Line (Double track with ties)
      ctx.beginPath();
      stationCoords.forEach((pt, i) => {
        if (i === 0) ctx.moveTo(pt.x, pt.y);
        else ctx.lineTo(pt.x, pt.y);
      });
      ctx.strokeStyle = 'rgba(201, 162, 74, 0.25)';
      ctx.lineWidth = 6;
      ctx.stroke();

      // Center luminous rail
      ctx.beginPath();
      stationCoords.forEach((pt, i) => {
        if (i === 0) ctx.moveTo(pt.x, pt.y);
        else ctx.lineTo(pt.x, pt.y);
      });
      ctx.strokeStyle = '#C9A24A';
      ctx.lineWidth = 2;
      ctx.stroke();

      // 4. Moving Trains
      [trainT1, trainT2].forEach((trainT, idx) => {
        const segIndex = Math.floor(trainT * (stationCoords.length - 1));
        const nextIndex = Math.min(segIndex + 1, stationCoords.length - 1);
        const p1 = stationCoords[segIndex];
        const p2 = stationCoords[nextIndex];
        const localT = (trainT * (stationCoords.length - 1)) - segIndex;
        const tx = p1.x + (p2.x - p1.x) * localT;
        const ty = p1.y + (p2.y - p1.y) * localT;

        // Train Glow
        const grad = ctx.createRadialGradient(tx, ty, 0, tx, ty, 14);
        grad.addColorStop(0, 'rgba(255, 241, 196, 1)');
        grad.addColorStop(0.5, 'rgba(201, 162, 74, 0.7)');
        grad.addColorStop(1, 'rgba(201, 162, 74, 0)');
        ctx.fillStyle = grad;
        ctx.beginPath();
        ctx.arc(tx, ty, 12, 0, Math.PI * 2);
        ctx.fill();

        // Train Core
        ctx.fillStyle = idx === 0 ? '#FFF1C4' : '#55D88D';
        ctx.beginPath();
        ctx.arc(tx, ty, 4, 0, Math.PI * 2);
        ctx.fill();
      });

      // 5. Draw 24 Station Nodes
      stationCoords.forEach(({ station, x, y }) => {
        const isSelected = selectedStation.slug === station.slug;
        const isHovered = hoveredStation?.slug === station.slug;

        // Station Beacon Pulse if selected
        if (isSelected || isHovered) {
          const r = 8 + Math.sin(pulseAngle) * 4;
          ctx.beginPath();
          ctx.arc(x, y, r + 4, 0, Math.PI * 2);
          ctx.strokeStyle = '#C9A24A';
          ctx.lineWidth = 1.5;
          ctx.stroke();
        }

        // Node Circle
        ctx.beginPath();
        ctx.arc(x, y, isSelected ? 5.5 : 3.5, 0, Math.PI * 2);
        ctx.fillStyle = isSelected ? '#FFF1C4' : station.zone === 'Rawalpindi' ? '#C9A24A' : '#55D88D';
        ctx.fill();

        ctx.strokeStyle = '#0B0B0C';
        ctx.lineWidth = 1.5;
        ctx.stroke();

        // Label on key stations or hovered/selected
        const isTerminal = station.number === 1 || station.number === 24 || station.number === 9 || station.number === 10 || station.number === 19;
        if (isTerminal || isSelected || isHovered) {
          ctx.font = isSelected ? 'bold 10px Inter, sans-serif' : '9px Inter, sans-serif';
          ctx.fillStyle = isSelected ? '#FFF1C4' : '#A39A8B';
          const textY = station.number % 2 === 0 ? y + 16 : y - 10;
          ctx.fillText(`${station.code} ${station.name}`, x - 25, textY);
        }
      });

      animationFrameId = requestAnimationFrame(render);
    };

    render();

    // Canvas click event to select station
    const handleClick = (e: MouseEvent) => {
      const rect = canvas.getBoundingClientRect();
      const clickX = e.clientX - rect.left;
      const clickY = e.clientY - rect.top;

      let closest = stationCoords[0];
      let minDist = 9999;
      stationCoords.forEach(item => {
        const d = Math.hypot(item.x - clickX, item.y - clickY);
        if (d < minDist) {
          minDist = d;
          closest = item;
        }
      });

      if (minDist < 25) {
        setSelectedStation(closest.station);
      }
    };

    // Hover event
    const handleMouseMove = (e: MouseEvent) => {
      const rect = canvas.getBoundingClientRect();
      const mouseX = e.clientX - rect.left;
      const mouseY = e.clientY - rect.top;

      let found = null;
      for (const item of stationCoords) {
        if (Math.hypot(item.x - mouseX, item.y - mouseY) < 16) {
          found = item.station;
          break;
        }
      }
      setHoveredStation(found);
    };

    canvas.addEventListener('click', handleClick);
    canvas.addEventListener('mousemove', handleMouseMove);

    return () => {
      cancelAnimationFrame(animationFrameId);
      window.removeEventListener('resize', handleResize);
      canvas.removeEventListener('click', handleClick);
      canvas.removeEventListener('mousemove', handleMouseMove);
    };
  }, [selectedStation, hoveredStation, setSelectedStation]);

  // Find staff assigned to currently selected station
  const stationStaff = staffVersions.filter(s => s.stationSlug === selectedStation.slug);

  return (
    <div className="metro-card p-5 lg:p-6 overflow-hidden">
      {/* Header */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 pb-4 border-b border-[#C9A24A]/15">
        <div>
          <div className="flex items-center gap-2">
            <span className="text-[10px] font-bold tracking-[0.2em] text-[#C9A24A] uppercase flex items-center gap-1.5">
              <Radio className="w-3.5 h-3.5 text-[#2F9A63] animate-pulse" />
              RED LINE METRO CORRIDOR
            </span>
            <span className="text-[9px] px-2 py-0.5 rounded-full bg-[#122B1E] text-[#55D88D] border border-[#1D4A33] font-bold">
              24 STATIONS CONNECTED
            </span>
          </div>
          <h3 className="text-lg font-bold text-[#FFFDF8] mt-1 font-['Cinzel',serif]">
            Rawalpindi Saddar (1/201) ➔ Pak Secretariat (24/224)
          </h3>
        </div>

        {/* Selected Station Badge */}
        <div className="flex items-center gap-3">
          <div className="px-3.5 py-1.5 rounded-xl bg-gradient-to-r from-[#2A2012] to-[#1C160D] border border-[#C9A24A]/40 flex items-center gap-2">
            <MapPin className="w-4 h-4 text-[#C9A24A]" />
            <div>
              <div className="text-xs font-bold text-[#FFF1C4] leading-tight">
                {selectedStation.name} ({selectedStation.code})
              </div>
              <div className="text-[10px] text-[#A39A8B]">
                {selectedStation.zone} • Slug: {selectedStation.slug}
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Canvas */}
      <div className="relative mt-4 rounded-xl bg-[#090806] border border-[#C9A24A]/15 overflow-hidden">
        <canvas ref={canvasRef} className="w-full block cursor-crosshair" />

        {/* Bottom Legend */}
        <div className="absolute bottom-3 right-3 flex items-center gap-3 bg-[#0B0B0C]/85 backdrop-blur-md px-3 py-1.5 rounded-lg border border-white/10 text-[10px]">
          <span className="flex items-center gap-1.5 text-[#C9A24A]">
            <span className="w-2 h-2 rounded-full bg-[#C9A24A]" />
            Rawalpindi Zone (1-10)
          </span>
          <span className="flex items-center gap-1.5 text-[#55D88D]">
            <span className="w-2 h-2 rounded-full bg-[#55D88D]" />
            Islamabad Zone (11-24)
          </span>
          <span className="text-zinc-500 hidden sm:inline">Click any node to select station</span>
        </div>
      </div>
    </div>
  );
};
