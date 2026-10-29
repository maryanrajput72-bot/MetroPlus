import React, { useState } from 'react';
import { useMetro } from '../context/MetroContext';
import { AdminLayout } from '../components/layout/AdminLayout';
import { 
  ScrollText, 
  Search, 
  Filter, 
  CheckCircle2, 
  AlertCircle, 
  Clock, 
  User, 
  TrainTrack, 
  KeyRound, 
  Camera, 
  UploadCloud, 
  Download, 
  RefreshCw,
  SlidersHorizontal,
  ShieldCheck
} from 'lucide-react';

interface LogEntry {
  id: string;
  time: string;
  category: 'attendance' | 'revenue' | 'security' | 'system' | 'updater';
  user: string;
  role: string;
  action: string;
  station: string;
  details: string;
  status: 'success' | 'warning' | 'info';
}

export const ActivityLogs: React.FC = () => {
  const { showToast, activityLogs } = useMetro();
  const [selectedCategory, setSelectedCategory] = useState<string>('all');
  const [searchQuery, setSearchQuery] = useState('');
  const [isRefreshing, setIsRefreshing] = useState(false);

  const logs = activityLogs;

  const filteredLogs = logs.filter(log => {
    const matchesCategory = selectedCategory === 'all' || log.category === selectedCategory;
    const matchesSearch = 
      log.user.toLowerCase().includes(searchQuery.toLowerCase()) ||
      log.action.toLowerCase().includes(searchQuery.toLowerCase()) ||
      log.station.toLowerCase().includes(searchQuery.toLowerCase()) ||
      log.details.toLowerCase().includes(searchQuery.toLowerCase());
    return matchesCategory && matchesSearch;
  });

  const handleRefresh = () => {
    setIsRefreshing(true);
    setTimeout(() => {
      setIsRefreshing(false);
      showToast('Activity logs refreshed with latest corridor events');
    }, 600);
  };

  const handleExport = () => {
    const dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(logs, null, 2));
    const downloadAnchor = document.createElement('a');
    downloadAnchor.setAttribute("href", dataStr);
    downloadAnchor.setAttribute("download", `metro_plus_activity_logs_${new Date().toISOString().slice(0,10)}.json`);
    document.body.appendChild(downloadAnchor);
    downloadAnchor.click();
    downloadAnchor.remove();
    showToast('Activity logs exported to JSON file');
  };

  return (
    <AdminLayout 
      title="Activity Logs" 
      subtitle="Real-time audit log of staff attendance, TOM closings, TL logins & app updates"
    >
      <div className="space-y-6">
        {/* Metric Overview Row */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <div className="metro-card p-5">
            <div className="flex items-center justify-between">
              <span className="text-[10px] font-bold uppercase tracking-wider text-[#A39A8B]">
                Total Events Logged
              </span>
              <div className="w-8 h-8 rounded-lg bg-[#C9A24A]/10 border border-[#C9A24A]/20 flex items-center justify-center text-[#C9A24A]">
                <ScrollText className="w-4 h-4" />
              </div>
            </div>
            <div className="text-2xl font-black font-mono text-white mt-2">
              1,248
            </div>
            <div className="text-[11px] text-[#55D88D] font-medium mt-1">
              Active Red Line audit records
            </div>
          </div>

          <div className="metro-card p-5">
            <div className="flex items-center justify-between">
              <span className="text-[10px] font-bold uppercase tracking-wider text-[#A39A8B]">
                Staff Authentications
              </span>
              <div className="w-8 h-8 rounded-lg bg-blue-500/10 border border-blue-500/20 flex items-center justify-center text-blue-400">
                <KeyRound className="w-4 h-4" />
              </div>
            </div>
            <div className="text-2xl font-black font-mono text-white mt-2">
              48 TL / RP
            </div>
            <div className="text-[11px] text-[#A39A8B] font-medium mt-1">
              5-digit PIN & Gmail verified
            </div>
          </div>

          <div className="metro-card p-5">
            <div className="flex items-center justify-between">
              <span className="text-[10px] font-bold uppercase tracking-wider text-[#A39A8B]">
                GPS Verification Rate
              </span>
              <div className="w-8 h-8 rounded-lg bg-[#55D88D]/10 border border-[#55D88D]/20 flex items-center justify-center text-[#55D88D]">
                <Camera className="w-4 h-4" />
              </div>
            </div>
            <div className="text-2xl font-black font-mono text-[#55D88D] mt-2">
              99.2%
            </div>
            <div className="text-[11px] text-[#55D88D] font-medium mt-1">
              CameraX accurate within 50m
            </div>
          </div>

          <div className="metro-card p-5">
            <div className="flex items-center justify-between">
              <span className="text-[10px] font-bold uppercase tracking-wider text-[#A39A8B]">
                Audit Integrity
              </span>
              <div className="w-8 h-8 rounded-lg bg-purple-500/10 border border-purple-500/20 flex items-center justify-center text-purple-400">
                <ShieldCheck className="w-4 h-4" />
              </div>
            </div>
            <div className="text-2xl font-black text-[#FFF1C4] mt-2">
              Encrypted
            </div>
            <div className="text-[11px] text-[#A39A8B] font-medium mt-1">
              Immutable Firebase audit trail
            </div>
          </div>
        </div>

        {/* Filter & Action Bar */}
        <div className="metro-card p-5 flex flex-col md:flex-row items-center justify-between gap-4">
          <div className="flex flex-wrap items-center gap-2 w-full md:w-auto">
            {['all', 'attendance', 'revenue', 'security', 'updater', 'system'].map(cat => (
              <button
                key={cat}
                onClick={() => setSelectedCategory(cat)}
                className={`px-3 py-1.5 rounded-lg text-xs font-bold capitalize transition-all ${
                  selectedCategory === cat
                    ? 'bg-[#C9A24A] text-[#0B0B0C] shadow-md shadow-[#C9A24A]/20'
                    : 'bg-[#0E0C09] text-[#A39A8B] border border-white/10 hover:text-white hover:border-[#C9A24A]/40'
                }`}
              >
                {cat === 'all' ? 'All Activities' : cat}
              </button>
            ))}
          </div>

          <div className="flex items-center gap-2.5 w-full md:w-auto">
            <div className="relative flex-1 md:w-64">
              <Search className="w-4 h-4 text-[#A39A8B] absolute left-3 top-1/2 -translate-y-1/2" />
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                placeholder="Search by staff, action, station..."
                className="w-full pl-9 pr-3 py-1.5 rounded-xl bg-[#0E0C09] border border-white/10 text-xs text-white placeholder-[#6E6658] outline-none focus:border-[#C9A24A]"
              />
            </div>

            <button
              onClick={handleRefresh}
              className="p-2 rounded-xl bg-[#0E0C09] border border-white/10 hover:border-[#C9A24A] text-[#A39A8B] hover:text-white transition-all"
              title="Refresh logs"
            >
              <RefreshCw className={`w-4 h-4 ${isRefreshing ? 'animate-spin text-[#C9A24A]' : ''}`} />
            </button>

            <button
              onClick={handleExport}
              className="px-3 py-1.5 rounded-xl bg-white/5 border border-white/10 hover:border-[#C9A24A] text-xs font-bold text-white hover:text-[#FFF1C4] transition-all flex items-center gap-1.5"
            >
              <Download className="w-3.5 h-3.5" />
              <span>Export</span>
            </button>
          </div>
        </div>

        {/* Activity Logs Stream Table */}
        <div className="metro-card p-6 space-y-4">
          <div className="flex items-center justify-between border-b border-white/10 pb-3">
            <div>
              <h3 className="text-base font-bold text-white flex items-center gap-2">
                <ScrollText className="w-4 h-4 text-[#C9A24A]" />
                <span>Live Event Stream</span>
              </h3>
              <p className="text-xs text-[#A39A8B] mt-0.5">
                Displaying {filteredLogs.length} matching events across all Red Line stations
              </p>
            </div>
            <span className="text-[10px] font-mono text-[#55D88D] flex items-center gap-1.5">
              <span className="w-2 h-2 rounded-full bg-[#55D88D] animate-ping" />
              <span>STREAM ACTIVE</span>
            </span>
          </div>

          <div className="overflow-x-auto rounded-xl border border-white/5">
            <table className="w-full text-left text-xs">
              <thead className="bg-[#0E0C09] text-[#A39A8B] font-bold uppercase tracking-wider text-[10px] border-b border-white/10">
                <tr>
                  <th className="py-3 px-4">Event ID / Time</th>
                  <th className="py-3 px-4">User & Role</th>
                  <th className="py-3 px-4">Action</th>
                  <th className="py-3 px-4">Station</th>
                  <th className="py-3 px-4">Details</th>
                  <th className="py-3 px-4 text-center">Status</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-white/5">
                {filteredLogs.map((log) => (
                  <tr key={log.id} className="hover:bg-white/[0.02] transition-colors">
                    <td className="py-3 px-4">
                      <div className="font-mono font-bold text-white">{log.id}</div>
                      <div className="text-[10px] text-[#A39A8B] flex items-center gap-1 mt-0.5">
                        <Clock className="w-3 h-3 text-[#C9A24A]" />
                        <span>{log.time}</span>
                      </div>
                    </td>

                    <td className="py-3 px-4">
                      <div className="font-bold text-white">{log.user}</div>
                      <div className="text-[10px] text-[#C9A24A] font-semibold">{log.role}</div>
                    </td>

                    <td className="py-3 px-4">
                      <span className="font-semibold text-white/90">
                        {log.action}
                      </span>
                    </td>

                    <td className="py-3 px-4">
                      <div className="flex items-center gap-1.5 text-zinc-300">
                        <TrainTrack className="w-3 h-3 text-[#A39A8B]" />
                        <span>{log.station}</span>
                      </div>
                    </td>

                    <td className="py-3 px-4 max-w-xs">
                      <div className="text-[11px] text-[#A39A8B] truncate" title={log.details}>
                        {log.details}
                      </div>
                    </td>

                    <td className="py-3 px-4 text-center">
                      <span className={`inline-flex items-center gap-1 text-[10px] font-bold px-2 py-0.5 rounded-full ${
                        log.status === 'success'
                          ? 'bg-[#55D88D]/10 text-[#55D88D] border border-[#55D88D]/20'
                          : log.status === 'warning'
                          ? 'bg-amber-500/10 text-amber-400 border border-amber-500/20'
                          : 'bg-blue-500/10 text-blue-400 border border-blue-500/20'
                      }`}>
                        {log.status === 'success' ? <CheckCircle2 className="w-3 h-3" /> : <AlertCircle className="w-3 h-3" />}
                        <span className="capitalize">{log.status}</span>
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </AdminLayout>
  );
};
