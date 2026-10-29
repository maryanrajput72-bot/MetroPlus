import React, { useState } from 'react';
import { useMetro } from '../context/MetroContext';
import { AdminLayout } from '../components/layout/AdminLayout';
import { METRO_STATIONS } from '../data/metroStations';
import { MetroStation } from '../types/metro';
import { 
  TrainTrack, 
  MapPin, 
  Users, 
  Camera, 
  Search, 
  ExternalLink,
  ShieldCheck,
  CheckCircle2
} from 'lucide-react';

export const Stations: React.FC = () => {
  const { setSelectedStation, staffVersions, gpsPhotos } = useMetro();
  const [search, setSearch] = useState('');
  const [zoneFilter, setZoneFilter] = useState('all');

  const filtered = METRO_STATIONS.filter(st => {
    const matchesSearch = 
      st.name.toLowerCase().includes(search.toLowerCase()) ||
      st.code.toLowerCase().includes(search.toLowerCase()) ||
      st.slug.toLowerCase().includes(search.toLowerCase());

    const matchesZone = zoneFilter === 'all' || st.zone === zoneFilter;
    return matchesSearch && matchesZone;
  });

  return (
    <AdminLayout title="24 Red Line Stations" subtitle="Complete Rawalpindi – Islamabad Metro Network Corridor">
      <div className="space-y-6">
        {/* Controls */}
        <div className="metro-card p-4 sm:p-5 flex flex-col sm:flex-row items-center justify-between gap-4">
          <div className="flex items-center gap-2">
            {['all', 'Rawalpindi', 'Islamabad'].map(z => (
              <button
                key={z}
                onClick={() => setZoneFilter(z)}
                className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all capitalize ${
                  zoneFilter === z
                    ? 'bg-[#C9A24A] text-[#0B0B0C] shadow-sm'
                    : 'bg-white/5 text-[#A39A8B] hover:text-white'
                }`}
              >
                {z === 'all' ? 'All 24 Stations' : `${z} Zone`}
              </button>
            ))}
          </div>

          <div className="relative w-full sm:w-72">
            <Search className="w-4 h-4 text-[#A39A8B] absolute left-3 top-1/2 -translate-y-1/2" />
            <input
              type="text"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="Search station, code e.g. 9/209..."
              className="w-full bg-[#0E0C09] border border-[#C9A24A]/25 rounded-xl pl-9 pr-3 py-2 text-xs text-white placeholder-zinc-500 focus:outline-none focus:border-[#C9A24A]"
            />
          </div>
        </div>

        {/* Stations Table */}
        <div className="metro-card p-6 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs">
              <thead>
                <tr className="border-b border-[#C9A24A]/15 text-[#6E6658] uppercase font-bold tracking-wider">
                  <th className="pb-3.5 pl-2">#</th>
                  <th className="pb-3.5">Station Display Name</th>
                  <th className="pb-3.5">Station Code</th>
                  <th className="pb-3.5">Cloud Slug (Firestore)</th>
                  <th className="pb-3.5">Zone</th>
                  <th className="pb-3.5 text-center">Assigned Staff</th>
                  <th className="pb-3.5 text-center">GPS Photos</th>
                  <th className="pb-3.5 pr-2 text-right">Coordinates</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[#C9A24A]/10">
                {filtered.map((st) => {
                  const staffCount = staffVersions.filter(s => s.stationSlug === st.slug).length;
                  const photoCount = gpsPhotos.filter(p => p.stationName.toLowerCase().includes(st.name.toLowerCase())).length;

                  return (
                    <tr key={st.slug} className="hover:bg-white/[0.02] transition-colors">
                      <td className="py-4 pl-2 font-mono text-[#6E6658] font-bold">
                        #{st.number}
                      </td>

                      <td className="py-4">
                        <div className="font-bold text-white text-sm flex items-center gap-2">
                          <TrainTrack className="w-4 h-4 text-[#C9A24A]" />
                          <span>{st.name}</span>
                        </div>
                        {st.number === 9 && (
                          <div className="text-[10px] text-[#E0A42E] font-medium mt-0.5">
                            Legacy ID: Shamsabad (display name Khatam-e-Nabuwat)
                          </div>
                        )}
                      </td>

                      <td className="py-4 font-mono font-bold text-[#FFF1C4] text-sm">
                        {st.code}
                      </td>

                      <td className="py-4 font-mono text-[#A39A8B]">
                        <code>{st.slug}</code>
                      </td>

                      <td className="py-4">
                        <span className={`px-2.5 py-0.5 rounded-full text-[10px] font-bold border ${
                          st.zone === 'Rawalpindi' 
                            ? 'bg-[#2A2012] text-[#FFF1C4] border-[#C9A24A]/30'
                            : 'bg-[#122B1E] text-[#55D88D] border-[#1D4A33]'
                        }`}>
                          {st.zone}
                        </span>
                      </td>

                      <td className="py-4 text-center font-mono font-bold text-white">
                        {staffCount} Staff
                      </td>

                      <td className="py-4 text-center font-mono font-bold text-[#C9A24A]">
                        {photoCount} Photos
                      </td>

                      <td className="py-4 pr-2 text-right">
                        <a
                          href={`https://maps.google.com/?q=${st.lat},${st.lng}`}
                          target="_blank"
                          rel="noreferrer"
                          className="inline-flex items-center gap-1 text-[11px] font-mono text-[#A39A8B] hover:text-[#FFF1C4]"
                        >
                          <span>{st.lat.toFixed(4)}°N</span>
                          <ExternalLink className="w-3 h-3 text-[#C9A24A]" />
                        </a>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </AdminLayout>
  );
};
