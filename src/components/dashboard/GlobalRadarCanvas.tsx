import React, { useRef, useEffect, useState } from 'react';
import { Compass, Navigation, Radio, Plane, ShieldCheck, Sparkles } from 'lucide-react';

interface City {
  name: string;
  type: 'hub' | 'destination';
  country: string;
  x: number; // 0 to 1 normalized
  y: number; // 0 to 1 normalized
  activeParcels: number;
}

const HUBS: City[] = [
  { name: 'Shenzhen Hub', type: 'hub', country: 'China', x: 0.78, y: 0.48, activeParcels: 34 },
  { name: 'Yiwu Logistics', type: 'hub', country: 'China', x: 0.81, y: 0.44, activeParcels: 28 },
  { name: 'Istanbul Cargo', type: 'hub', country: 'Turkey', x: 0.55, y: 0.38, activeParcels: 19 },
  { name: 'Dubai Freezone', type: 'hub', country: 'UAE', x: 0.62, y: 0.46, activeParcels: 42 },
  { name: 'London Heathrow', type: 'hub', country: 'UK', x: 0.46, y: 0.28, activeParcels: 21 },
  { name: 'Los Angeles Gateway', type: 'hub', country: 'USA', x: 0.18, y: 0.38, activeParcels: 15 }
];

const DESTINATIONS: City[] = [
  { name: 'Karachi Central', type: 'destination', country: 'Pakistan', x: 0.66, y: 0.45, activeParcels: 16 },
  { name: 'Lahore Hub', type: 'destination', country: 'Pakistan', x: 0.68, y: 0.40, activeParcels: 12 },
  { name: 'Dubai Downtown', type: 'destination', country: 'UAE', x: 0.62, y: 0.46, activeParcels: 24 },
  { name: 'Riyadh Terminal', type: 'destination', country: 'Saudi Arabia', x: 0.59, y: 0.48, activeParcels: 18 },
  { name: 'New York JFK', type: 'destination', country: 'USA', x: 0.28, y: 0.34, activeParcels: 29 },
  { name: 'London City', type: 'destination', country: 'UK', x: 0.47, y: 0.29, activeParcels: 22 },
  { name: 'Toronto Pearson', type: 'destination', country: 'Canada', x: 0.26, y: 0.29, activeParcels: 14 },
  { name: 'Sydney Kingsford', type: 'destination', country: 'Australia', x: 0.88, y: 0.78, activeParcels: 11 }
];

interface FlightPath {
  from: City;
  to: City;
  progress: number;
  speed: number;
  curviness: number;
}

export const GlobalRadarCanvas: React.FC = () => {
  const canvasRef = useRef<HTMLCanvasElement | null>(null);
  const [hoveredCity, setHoveredCity] = useState<City | null>(null);
  const [activeFlightsCount, setActiveFlightsCount] = useState(8);

  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    let animationFrameId: number;
    let width = (canvas.width = canvas.parentElement?.clientWidth || 800);
    let height = (canvas.height = 360);

    const handleResize = () => {
      if (!canvas.parentElement) return;
      width = canvas.width = canvas.parentElement.clientWidth;
      height = canvas.height = 360;
    };
    window.addEventListener('resize', handleResize);

    // Build active flight routes
    const routes: FlightPath[] = [
      { from: HUBS[0], to: DESTINATIONS[0], progress: 0.2, speed: 0.003, curviness: -0.2 }, // Shenzhen -> Karachi
      { from: HUBS[1], to: DESTINATIONS[1], progress: 0.6, speed: 0.0035, curviness: -0.25 }, // Yiwu -> Lahore
      { from: HUBS[3], to: DESTINATIONS[3], progress: 0.8, speed: 0.004, curviness: 0.15 }, // Dubai -> Riyadh
      { from: HUBS[0], to: DESTINATIONS[4], progress: 0.4, speed: 0.0025, curviness: -0.35 }, // Shenzhen -> New York
      { from: HUBS[2], to: DESTINATIONS[5], progress: 0.7, speed: 0.003, curviness: -0.15 }, // Istanbul -> London
      { from: HUBS[4], to: DESTINATIONS[6], progress: 0.3, speed: 0.003, curviness: -0.2 }, // London -> Toronto
      { from: HUBS[0], to: DESTINATIONS[7], progress: 0.85, speed: 0.0028, curviness: 0.2 }, // Shenzhen -> Sydney
      { from: HUBS[3], to: DESTINATIONS[0], progress: 0.15, speed: 0.0045, curviness: -0.1 } // Dubai -> Karachi
    ];

    let pulseTime = 0;

    const render = () => {
      pulseTime += 0.03;
      ctx.clearRect(0, 0, width, height);

      // 1. Draw subtle background coordinate grid
      ctx.strokeStyle = 'rgba(201, 162, 74, 0.04)';
      ctx.lineWidth = 1;
      const step = 45;
      for (let x = 0; x < width; x += step) {
        ctx.beginPath();
        ctx.moveTo(x, 0);
        ctx.lineTo(x, height);
        ctx.stroke();
      }
      for (let y = 0; y < height; y += step) {
        ctx.beginPath();
        ctx.moveTo(0, y);
        ctx.lineTo(width, y);
        ctx.stroke();
      }

      // 2. Draw continent glow silhouettes (stylized dots)
      ctx.fillStyle = 'rgba(201, 162, 74, 0.08)';
      // Sample map points
      const continentDummies = [
        // North America
        [0.2, 0.25], [0.25, 0.28], [0.18, 0.35], [0.22, 0.38], [0.28, 0.36],
        // Europe
        [0.46, 0.26], [0.5, 0.3], [0.48, 0.35],
        // Middle East & South Asia
        [0.58, 0.42], [0.63, 0.46], [0.67, 0.42], [0.7, 0.48],
        // East Asia
        [0.78, 0.42], [0.82, 0.46], [0.76, 0.52],
        // Africa
        [0.5, 0.52], [0.54, 0.6], [0.52, 0.7],
        // Australia
        [0.85, 0.75], [0.88, 0.8]
      ];
      continentDummies.forEach(([cx, cy]) => {
        ctx.beginPath();
        ctx.arc(cx * width, cy * height, 3, 0, Math.PI * 2);
        ctx.fill();
      });

      // 3. Draw Flight Route Arcs & Flying Parcels
      routes.forEach((route) => {
        route.progress = (route.progress + route.speed) % 1;

        const x1 = route.from.x * width;
        const y1 = route.from.y * height;
        const x2 = route.to.x * width;
        const y2 = route.to.y * height;

        // Quadratic Bezier control point
        const midX = (x1 + x2) / 2;
        const midY = (y1 + y2) / 2;
        const dx = x2 - x1;
        const dy = y2 - y1;
        const normalX = -dy * route.curviness;
        const normalY = dx * route.curviness;
        const cpX = midX + normalX;
        const cpY = midY + normalY;

        // Draw curved trajectory path
        ctx.beginPath();
        ctx.moveTo(x1, y1);
        ctx.quadraticCurveTo(cpX, cpY, x2, y2);
        ctx.strokeStyle = 'rgba(201, 162, 74, 0.22)';
        ctx.lineWidth = 1.2;
        ctx.setLineDash([4, 6]);
        ctx.stroke();
        ctx.setLineDash([]);

        // Calculate current parcel position along quadratic curve
        const t = route.progress;
        const parcelX = (1 - t) * (1 - t) * x1 + 2 * (1 - t) * t * cpX + t * t * x2;
        const parcelY = (1 - t) * (1 - t) * y1 + 2 * (1 - t) * t * cpY + t * t * y2;

        // Glowing parcel dot
        const parcelGlow = ctx.createRadialGradient(parcelX, parcelY, 0, parcelX, parcelY, 10);
        parcelGlow.addColorStop(0, 'rgba(255, 241, 196, 1)');
        parcelGlow.addColorStop(0.4, 'rgba(201, 162, 74, 0.8)');
        parcelGlow.addColorStop(1, 'rgba(201, 162, 74, 0)');

        ctx.fillStyle = parcelGlow;
        ctx.beginPath();
        ctx.arc(parcelX, parcelY, 8, 0, Math.PI * 2);
        ctx.fill();

        ctx.fillStyle = '#FFF1C4';
        ctx.beginPath();
        ctx.arc(parcelX, parcelY, 2.5, 0, Math.PI * 2);
        ctx.fill();
      });

      // 4. Draw Hubs (Gold Beacons)
      HUBS.forEach((hub) => {
        const hx = hub.x * width;
        const hy = hub.y * height;

        // Pulse wave
        const pulseRadius = 5 + Math.sin(pulseTime + hub.x * 10) * 4;
        ctx.beginPath();
        ctx.arc(hx, hy, pulseRadius + 6, 0, Math.PI * 2);
        ctx.strokeStyle = 'rgba(201, 162, 74, 0.35)';
        ctx.lineWidth = 1;
        ctx.stroke();

        // Hub Core
        ctx.fillStyle = '#C9A24A';
        ctx.beginPath();
        ctx.arc(hx, hy, 4, 0, Math.PI * 2);
        ctx.fill();

        // Hub Label
        ctx.font = '10px Inter, sans-serif';
        ctx.fillStyle = '#FFF1C4';
        ctx.fillText(hub.name, hx + 8, hy + 3);
      });

      // 5. Draw Destinations (Emerald/Blue Beacons)
      DESTINATIONS.forEach((dest) => {
        const dx = dest.x * width;
        const dy = dest.y * height;

        ctx.fillStyle = '#2F9A63';
        ctx.beginPath();
        ctx.arc(dx, dy, 3, 0, Math.PI * 2);
        ctx.fill();

        ctx.font = '9px Inter, sans-serif';
        ctx.fillStyle = '#A39A8B';
        ctx.fillText(dest.name, dx + 6, dy + 3);
      });

      animationFrameId = requestAnimationFrame(render);
    };

    render();

    return () => {
      cancelAnimationFrame(animationFrameId);
      window.removeEventListener('resize', handleResize);
    };
  }, []);

  return (
    <div className="metro-card p-5 lg:p-6 overflow-hidden">
      {/* Card Header with telemetry stats */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 pb-4 border-b border-[#C9A24A]/15">
        <div>
          <div className="flex items-center gap-2">
            <span className="text-[10px] font-bold tracking-[0.2em] text-[#C9A24A] uppercase flex items-center gap-1.5">
              <Radio className="w-3.5 h-3.5 text-[#2F9A63] animate-pulse" />
              GLOBAL DISPATCH RADAR
            </span>
            <span className="text-[9px] px-2 py-0.5 rounded-full bg-[#122B1E] text-[#55D88D] border border-[#1D4A33] font-bold">
              8 ROUTES ACTIVE
            </span>
          </div>
          <h3 className="text-lg font-bold text-[#FFFDF8] mt-1 font-['Cinzel',serif]">
            Worldwide Supplier Hubs & Parcel Telemetry
          </h3>
        </div>

        <div className="flex items-center gap-4 text-xs">
          <div className="flex items-center gap-2 px-3 py-1.5 rounded-xl bg-white/[0.03] border border-white/5">
            <span className="w-2 h-2 rounded-full bg-[#C9A24A]" />
            <span className="text-[#A39A8B]">Supplier Hubs:</span>
            <span className="font-bold text-white">6 Ports</span>
          </div>

          <div className="flex items-center gap-2 px-3 py-1.5 rounded-xl bg-white/[0.03] border border-white/5">
            <span className="w-2 h-2 rounded-full bg-[#2F9A63]" />
            <span className="text-[#A39A8B]">Avg Transit:</span>
            <span className="font-bold text-[#55D88D]">4.2 Days</span>
          </div>
        </div>
      </div>

      {/* Radar Canvas Body */}
      <div className="relative mt-4 rounded-xl bg-[#090806] border border-[#C9A24A]/15 overflow-hidden">
        <canvas ref={canvasRef} className="w-full block" />

        {/* Ambient Corner Crosshairs & Legend */}
        <div className="absolute top-3 left-3 text-[10px] font-mono text-[#6E6658] uppercase tracking-wider flex items-center gap-2">
          <span>LAT 24.86°N • LON 67.00°E</span>
          <span className="text-[#C9A24A]">/</span>
          <span>SATELLITE SYNC</span>
        </div>

        <div className="absolute bottom-3 right-3 flex items-center gap-3 bg-[#0B0B0C]/85 backdrop-blur-md px-3 py-1.5 rounded-lg border border-white/10 text-[10px]">
          <span className="flex items-center gap-1.5 text-[#FFF1C4]">
            <span className="w-2 h-2 rounded-full bg-[#C9A24A]" />
            Origin Supplier Hub
          </span>
          <span className="flex items-center gap-1.5 text-[#A39A8B]">
            <span className="w-2 h-2 rounded-full bg-[#2F9A63]" />
            Destination Port
          </span>
        </div>
      </div>
    </div>
  );
};
