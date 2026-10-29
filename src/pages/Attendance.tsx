import React, { useState } from 'react';
import { useMetro } from '../context/MetroContext';
import { AdminLayout } from '../components/layout/AdminLayout';
import { Badge } from '../components/common/Badge';
import { AttendanceStatus, ShiftType } from '../types/metro';
import { 
  ClipboardCheck, 
  Copy, 
  Check, 
  Clock, 
  TrainTrack, 
  Sun, 
  Moon, 
  Calendar,
  Share2
} from 'lucide-react';
import { buildWhatsAppAttendanceReport } from '../utils/reportFormats';

export const Attendance: React.FC = () => {
  const { 
    todayAttendance, 
    updateAttendanceStatus, 
    copyWhatsAppReport, 
    stations, 
    selectedStation, 
    setSelectedStation,
    selectedShift,
    setSelectedShift,
    attendanceDate,
    setAttendanceDate
  } = useMetro();


  const [copied, setCopied] = useState(false);

  const entries = todayAttendance.entries;
  const onDutyCount = entries.filter(e => e.status === '').length;
  const waitingCount = entries.filter(e => e.status === 'WAITING').length;
  const offCount = entries.filter(e => e.status === 'OFF').length;
  const leaveCount = entries.filter(e => e.status === 'LEAVE').length;
  const absentCount = entries.filter(e => e.status === 'ABSENT').length;

  const handleCopy = () => {
    copyWhatsAppReport();
    setCopied(true);
    setTimeout(() => setCopied(false), 3000);
  };

  const previewText = buildWhatsAppAttendanceReport(todayAttendance);

  return (
    <AdminLayout title="Daily Attendance" subtitle="Daily station roster & byte-exact WhatsApp reporting">
      <div className="space-y-6">
        {/* Top Controls: Station, Shift & Date */}
        <div className="metro-card p-4 sm:p-5 flex flex-col md:flex-row items-stretch md:items-center justify-between gap-4">
          <div className="flex flex-wrap items-center gap-3">
            {/* Station Dropdown */}
            <div className="flex items-center gap-2 bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3 py-2 text-xs">
              <TrainTrack className="w-3.5 h-3.5 text-[#C9A24A]" />
              <select
                value={selectedStation.slug}
                onChange={(e) => {
                  const st = stations.find(s => s.slug === e.target.value);
                  if (st) setSelectedStation(st);
                }}
                className="bg-transparent text-white font-semibold focus:outline-none cursor-pointer"
              >
                {stations.map(st => (
                  <option key={st.slug} value={st.slug} className="bg-[#14110E] text-white">
                    {st.name} ({st.code})
                  </option>
                ))}
              </select>
            </div>

            {/* Shift Picker */}
            <div className="flex items-center bg-white/[0.04] p-1 rounded-xl border border-white/10">
              <button
                onClick={() => setSelectedShift('Morning')}
                className={`flex items-center gap-1.5 px-3 py-1.5 text-xs font-bold rounded-lg transition-all ${
                  selectedShift === 'Morning'
                    ? 'bg-[#C9A24A] text-[#0B0B0C] shadow-sm'
                    : 'text-[#A39A8B] hover:text-white'
                }`}
              >
                <Sun className="w-3 h-3" />
                <span>Morning</span>
              </button>
              <button
                onClick={() => setSelectedShift('Evening')}
                className={`flex items-center gap-1.5 px-3 py-1.5 text-xs font-bold rounded-lg transition-all ${
                  selectedShift === 'Evening'
                    ? 'bg-[#C9A24A] text-[#0B0B0C] shadow-sm'
                    : 'text-[#A39A8B] hover:text-white'
                }`}
              >
                <Moon className="w-3 h-3" />
                <span>Evening</span>
              </button>
            </div>

            {/* Date Input */}
            <div className="flex items-center gap-2 bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3 py-2 text-xs text-white">
              <Calendar className="w-3.5 h-3.5 text-[#C9A24A]" />
              <input
                type="date"
                value={attendanceDate}
                onChange={(e) => setAttendanceDate(e.target.value)}
                className="bg-transparent focus:outline-none text-white cursor-pointer"
              />
            </div>
          </div>

          {/* WhatsApp Copy Button */}
          <button
            onClick={handleCopy}
            className="px-5 py-2.5 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] rounded-xl transition-all shadow-lg shadow-[#C9A24A]/20 flex items-center justify-center gap-2 whitespace-nowrap"
          >
            {copied ? <Check className="w-4 h-4 text-[#0B0B0C]" /> : <Copy className="w-4 h-4 text-[#0B0B0C]" />}
            <span>{copied ? 'Report Copied!' : 'Copy WhatsApp Report'}</span>
          </button>
        </div>

        {/* Counter Cards */}
        <div className="grid grid-cols-2 sm:grid-cols-6 gap-3">
          <div className="metro-card p-4 text-center">
            <span className="text-[10px] uppercase font-bold text-[#A39A8B]">TOTAL ROSTER</span>
            <div className="text-2xl font-black text-white font-mono mt-1">{entries.length}</div>
          </div>
          <div className="metro-card p-4 text-center">
            <span className="text-[10px] uppercase font-bold text-[#55D88D]">ON DUTY</span>
            <div className="text-2xl font-black text-[#55D88D] font-mono mt-1">{onDutyCount}</div>
          </div>
          <div className="metro-card p-4 text-center">
            <span className="text-[10px] uppercase font-bold text-[#E0A42E]">WAITING</span>
            <div className="text-2xl font-black text-[#E0A42E] font-mono mt-1">{waitingCount}</div>
          </div>
          <div className="metro-card p-4 text-center">
            <span className="text-[10px] uppercase font-bold text-[#A39A8B]">OFF</span>
            <div className="text-2xl font-black text-[#A39A8B] font-mono mt-1">{offCount}</div>
          </div>
          <div className="metro-card p-4 text-center">
            <span className="text-[10px] uppercase font-bold text-[#8E6BD0]">LEAVE</span>
            <div className="text-2xl font-black text-[#8E6BD0] font-mono mt-1">{leaveCount}</div>
          </div>
          <div className="metro-card p-4 text-center">
            <span className="text-[10px] uppercase font-bold text-[#D2553F]">ABSENT</span>
            <div className="text-2xl font-black text-[#D2553F] font-mono mt-1">{absentCount}</div>
          </div>
        </div>

        {/* Attendance Table & WhatsApp Preview */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Table */}
          <div className="lg:col-span-2 metro-card p-6 overflow-hidden">
            <div className="pb-3 border-b border-[#C9A24A]/15">
              <h3 className="text-lg font-bold text-white font-['Cinzel',serif]">Station Staff Roster</h3>
              <p className="text-xs text-[#A39A8B]">Status changes persist directly to daily attendance document</p>
            </div>

            <div className="overflow-x-auto mt-4">
              <table className="w-full text-left text-xs">
                <thead>
                  <tr className="border-b border-[#C9A24A]/15 text-[#6E6658] uppercase font-bold tracking-wider">
                    <th className="pb-3.5 pl-2">Staff ID & Name</th>
                    <th className="pb-3.5">Role</th>
                    <th className="pb-3.5">Marked Time</th>
                    <th className="pb-3.5 text-center">Current Status</th>
                    <th className="pb-3.5 pr-2 text-right">Change Status</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-[#C9A24A]/10">
                  {entries.map((staff) => (
                    <tr key={staff.uid} className="hover:bg-white/[0.02] transition-colors">
                      <td className="py-4 pl-2">
                        <div className="font-bold text-white text-sm flex items-center gap-2">
                          <span className="font-mono text-[#C9A24A] bg-[#C9A24A]/10 px-2 py-0.5 rounded border border-[#C9A24A]/20">
                            {staff.id}
                          </span>
                          <span>{staff.name}</span>
                        </div>
                      </td>

                      <td className="py-4">
                        <Badge status={staff.role} />
                      </td>

                      <td className="py-4 font-mono text-[#FFF1C4]">
                        {staff.time ? (
                          <span className="flex items-center gap-1.5">
                            <Clock className="w-3.5 h-3.5 text-[#55D88D]" />
                            <span>{staff.time}</span>
                          </span>
                        ) : (
                          <span className="text-[#6E6658]">—</span>
                        )}
                      </td>

                      <td className="py-4 text-center">
                        <Badge status={staff.status} />
                      </td>

                      <td className="py-4 pr-2 text-right">
                        <select
                          value={staff.status}
                          onChange={(e) => updateAttendanceStatus(staff.uid, e.target.value as AttendanceStatus)}
                          className="bg-[#0E0C09] border border-[#C9A24A]/30 text-xs font-bold text-white rounded-lg px-2.5 py-1 focus:outline-none focus:border-[#C9A24A] cursor-pointer"
                        >
                          <option value="">ON DUTY (Present)</option>
                          <option value="WAITING">WAITING</option>
                          <option value="OFF">OFF</option>
                          <option value="LEAVE">LEAVE</option>
                          <option value="ABSENT">ABSENT</option>
                        </select>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          {/* WhatsApp Byte-Exact Preview Box */}
          <div className="metro-card p-6 flex flex-col justify-between">
            <div>
              <div className="flex items-center justify-between pb-3 border-b border-[#C9A24A]/15">
                <span className="text-[10px] font-bold tracking-[0.2em] text-[#55D88D] uppercase flex items-center gap-1.5">
                  <Share2 className="w-3.5 h-3.5 text-[#55D88D]" />
                  WHATSAPP REPORT FORMAT
                </span>
                <span className="text-[10px] font-mono text-[#A39A8B]">Byte-Exact</span>
              </div>

              <div className="mt-4 p-4 rounded-xl bg-[#090806] border border-white/5 font-mono text-xs text-[#FFF1C4] whitespace-pre-wrap leading-relaxed select-all">
                {previewText}
              </div>
            </div>

            <div className="mt-4 pt-3 border-t border-white/5 text-[11px] text-[#A39A8B]">
              Format: <strong className="text-white">ATTENDANCE REPORT</strong> with station, date, shift, and bullet lines per staffer.
            </div>
          </div>
        </div>
      </div>
    </AdminLayout>
  );
};
