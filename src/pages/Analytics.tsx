import React from 'react';
import { useAdmin } from '../context/AdminContext';
import { AdminLayout } from '../components/layout/AdminLayout';
import { GlobalRadarCanvas } from '../components/dashboard/GlobalRadarCanvas';
import { RevenueSplineChart } from '../components/dashboard/RevenueSplineChart';
import { TrendingUp, Globe, Plane, Radio, ShieldCheck, MapPin } from 'lucide-react';

export const Analytics: React.FC = () => {
  const { stats, formatCurrency } = useAdmin();

  const hubs = [
    { name: 'Shenzhen Tech Port', country: 'China', orders: 1420, avgDays: '3.8 Days', share: '42%' },
    { name: 'Yiwu Small Commodities', country: 'China', orders: 980, avgDays: '4.1 Days', share: '28%' },
    { name: 'Istanbul Guild Hub', country: 'Turkey', orders: 520, avgDays: '4.5 Days', share: '15%' },
    { name: 'Dubai DAFZA Freezone', country: 'UAE', orders: 340, avgDays: '2.4 Days', share: '10%' },
    { name: 'London Heathrow Gateway', country: 'UK', orders: 190, avgDays: '3.1 Days', share: '5%' }
  ];

  const destinations = [
    { city: 'Karachi & Lahore', country: 'Pakistan', volume: '$184,200', pct: '38%' },
    { city: 'Dubai & Abu Dhabi', country: 'UAE', volume: '$122,500', pct: '25%' },
    { city: 'Riyadh & Jeddah', country: 'Saudi Arabia', volume: '$78,900', pct: '16%' },
    { city: 'New York & Los Angeles', country: 'USA', volume: '$62,400', pct: '13%' },
    { city: 'London & Manchester', country: 'UK', volume: '$39,000', pct: '8%' }
  ];

  return (
    <AdminLayout title="Global Dispatch Analytics" subtitle="Real-time parcel telemetry, supplier ports & destination volume">
      <div className="space-y-6">
        {/* Radar Full View */}
        <GlobalRadarCanvas />

        {/* Charts & Hubs Breakdown */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Supplier Hubs Telemetry */}
          <div className="metro-card p-6">
            <div className="flex items-center justify-between pb-4 border-b border-[#C9A24A]/15">
              <div>
                <span className="text-[10px] font-bold tracking-[0.2em] text-[#C9A24A] uppercase flex items-center gap-1.5">
                  <Radio className="w-3.5 h-3.5 text-[#2F9A63] animate-pulse" />
                  ORIGIN HUBS
                </span>
                <h3 className="text-lg font-bold text-white mt-1 font-['Cinzel',serif]">Supplier Hub Throughput</h3>
              </div>
              <span className="text-xs font-mono text-[#C9A24A] bg-[#C9A24A]/10 px-2.5 py-1 rounded-lg border border-[#C9A24A]/25">
                5 Active Hubs
              </span>
            </div>

            <div className="space-y-3 mt-4">
              {hubs.map((hub, i) => (
                <div key={i} className="p-3 rounded-xl bg-white/[0.02] border border-white/5 flex items-center justify-between text-xs">
                  <div>
                    <div className="font-bold text-white flex items-center gap-1.5">
                      <span className="w-2 h-2 rounded-full bg-[#C9A24A]" />
                      <span>{hub.name}</span>
                    </div>
                    <div className="text-[10.5px] text-[#A39A8B]">{hub.country} • Avg Transit: {hub.avgDays}</div>
                  </div>

                  <div className="text-right">
                    <div className="font-mono font-bold text-[#FFF1C4]">{hub.orders} parcels</div>
                    <div className="text-[10px] text-[#55D88D]">{hub.share} of total traffic</div>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Destination Volume */}
          <div className="metro-card p-6">
            <div className="flex items-center justify-between pb-4 border-b border-[#C9A24A]/15">
              <div>
                <span className="text-[10px] font-bold tracking-[0.2em] text-[#C9A24A] uppercase flex items-center gap-1.5">
                  <Globe className="w-3.5 h-3.5 text-[#4E7BD8]" />
                  CONSUMER PORTS
                </span>
                <h3 className="text-lg font-bold text-white mt-1 font-['Cinzel',serif]">Top Destination Markets</h3>
              </div>
              <span className="text-xs font-mono text-[#55D88D] bg-[#122B1E] px-2.5 py-1 rounded-lg border border-[#1D4A33]">
                Global Reach
              </span>
            </div>

            <div className="space-y-3 mt-4">
              {destinations.map((dest, i) => (
                <div key={i} className="p-3 rounded-xl bg-white/[0.02] border border-white/5 flex items-center justify-between text-xs">
                  <div>
                    <div className="font-bold text-white flex items-center gap-1.5">
                      <MapPin className="w-3 h-3 text-[#55D88D]" />
                      <span>{dest.city}</span>
                    </div>
                    <div className="text-[10.5px] text-[#A39A8B]">{dest.country}</div>
                  </div>

                  <div className="text-right">
                    <div className="font-mono font-bold text-white">{dest.volume}</div>
                    <div className="text-[10px] text-[#C9A24A] font-bold">{dest.pct} share</div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Revenue Performance Graph */}
        <RevenueSplineChart />
      </div>
    </AdminLayout>
  );
};
