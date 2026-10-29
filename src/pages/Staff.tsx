import React, { useState } from 'react';
import { useMetro } from '../context/MetroContext';
import { AdminLayout } from '../components/layout/AdminLayout';
import { Badge } from '../components/common/Badge';
import { Modal } from '../components/common/Modal';
import { StaffVersion, ShiftType, StaffRole } from '../types/metro';
import { 
  Users, 
  Search, 
  Filter, 
  Smartphone, 
  Plus, 
  CheckCircle2, 
  AlertTriangle, 
  TrainTrack,
  Phone,
  Clock
} from 'lucide-react';

export const Staff: React.FC = () => {
  const { staffVersions, stations, addStaffMember, updateStaffVersion, updaterConfig } = useMetro();
  const [search, setSearch] = useState('');
  const [stationFilter, setStationFilter] = useState('all');
  const [roleFilter, setRoleFilter] = useState('all');
  const [versionFilter, setVersionFilter] = useState('all');

  // Modal
  const [isAddOpen, setIsAddOpen] = useState(false);
  const [newEmpId, setNewEmpId] = useState('');
  const [newName, setNewName] = useState('');
  const [newStation, setNewStation] = useState(stations[0].name);
  const [newShift, setNewShift] = useState<ShiftType>('Morning');
  const [newRole, setNewRole] = useState<StaffRole>('Operator');
  const [newPhone, setNewPhone] = useState('');

  const filtered = staffVersions.filter(s => {
    const matchesSearch = 
      s.name.toLowerCase().includes(search.toLowerCase()) ||
      s.employeeId.toLowerCase().includes(search.toLowerCase()) ||
      s.station.toLowerCase().includes(search.toLowerCase()) ||
      (s.phone && s.phone.includes(search));

    const matchesStation = stationFilter === 'all' || s.stationSlug === stationFilter;
    const matchesRole = roleFilter === 'all' || s.role === roleFilter;
    const matchesVersion = 
      versionFilter === 'all' ? true :
      versionFilter === 'outdated' ? s.isOutdated :
      versionFilter === 'trial' ? s.isTrialTester :
      versionFilter === 'main' ? s.versionCode === 339 : true;

    return matchesSearch && matchesStation && matchesRole && matchesVersion;
  });

  const handleCreate = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newEmpId || !newName) return alert('Please enter ID and Name');

    const st = stations.find(s => s.name === newStation) || stations[0];
    const isTester = updaterConfig.testers.includes(newEmpId.trim().toUpperCase());

    addStaffMember({
      employeeId: newEmpId.trim().toUpperCase(),
      name: newName,
      station: st.name,
      stationSlug: st.slug,
      shift: newShift,
      role: newRole,
      versionName: isTester ? updaterConfig.trial.versionName : updaterConfig.main.versionName,
      versionCode: isTester ? updaterConfig.trial.versionCode : updaterConfig.main.versionCode,
      platform: 'android',
      isTrialTester: isTester,
      isOutdated: false,
      online: true,
      phone: newPhone || '+92 300 0000000'
    });

    setIsAddOpen(false);
    setNewEmpId('');
    setNewName('');
    setNewPhone('');
  };

  return (
    <AdminLayout title="Staff & App Versions" subtitle="Firestore collection: staff_versions (doc ID = Employee ID UPPERCASE)">
      <div className="space-y-6">
        {/* Top Controls */}
        <div className="metro-card p-4 sm:p-5 flex flex-col md:flex-row items-stretch md:items-center justify-between gap-4">
          <div className="flex flex-wrap items-center gap-2">
            {/* Version Pills */}
            {[
              { id: 'all', label: 'All Staff', count: staffVersions.length },
              { id: 'main', label: 'Main (v2.1.31)', count: staffVersions.filter(s => s.versionCode === 339).length },
              { id: 'trial', label: 'Trial Testers', count: staffVersions.filter(s => s.isTrialTester).length },
              { id: 'outdated', label: 'Outdated Build', count: staffVersions.filter(s => s.isOutdated).length }
            ].map(tab => (
              <button
                key={tab.id}
                onClick={() => setVersionFilter(tab.id)}
                className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all whitespace-nowrap flex items-center gap-1.5 ${
                  versionFilter === tab.id
                    ? 'bg-[#C9A24A] text-[#0B0B0C] shadow-sm'
                    : 'bg-white/5 text-[#A39A8B] hover:text-white hover:bg-white/10'
                }`}
              >
                <span>{tab.label}</span>
                <span className={`text-[10px] px-1.5 py-0.2 rounded-full font-mono ${
                  versionFilter === tab.id ? 'bg-[#0B0B0C]/20 text-[#0B0B0C]' : 'bg-black/40 text-zinc-400'
                }`}>
                  {tab.count}
                </span>
              </button>
            ))}
          </div>

          <div className="flex items-center gap-3">
            <div className="relative w-full md:w-64">
              <Search className="w-4 h-4 text-[#A39A8B] absolute left-3 top-1/2 -translate-y-1/2" />
              <input
                type="text"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                placeholder="Search staff ID, name, station..."
                className="w-full bg-[#0E0C09] border border-[#C9A24A]/25 rounded-xl pl-9 pr-3 py-2 text-xs text-white placeholder-zinc-500 focus:outline-none focus:border-[#C9A24A]"
              />
            </div>

            <button
              onClick={() => setIsAddOpen(true)}
              className="px-4 py-2 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] rounded-xl transition-all shadow-md flex items-center gap-1.5 whitespace-nowrap"
            >
              <Plus className="w-4 h-4" />
              <span>Enroll Staff</span>
            </button>
          </div>
        </div>

        {/* Staff Table */}
        <div className="metro-card p-6 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs">
              <thead>
                <tr className="border-b border-[#C9A24A]/15 text-[#6E6658] uppercase font-bold tracking-wider">
                  <th className="pb-3.5 pl-2">Employee ID & Name</th>
                  <th className="pb-3.5">Station & Shift</th>
                  <th className="pb-3.5">Role</th>
                  <th className="pb-3.5">Installed Version</th>
                  <th className="pb-3.5">Channel</th>
                  <th className="pb-3.5">Heartbeat / Presence</th>
                  <th className="pb-3.5 pr-2 text-right">Quick Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[#C9A24A]/10">
                {filtered.map((staff) => (
                  <tr key={staff.employeeId} className="hover:bg-white/[0.02] transition-colors">
                    {/* ID & Name */}
                    <td className="py-4 pl-2">
                      <div className="font-bold text-white text-sm flex items-center gap-2">
                        <span className="font-mono text-[#C9A24A] bg-[#C9A24A]/10 px-2 py-0.5 rounded border border-[#C9A24A]/20">
                          {staff.employeeId}
                        </span>
                        <span>{staff.name}</span>
                      </div>
                      <div className="text-[10.5px] text-[#A39A8B] flex items-center gap-1 mt-0.5">
                        <Phone className="w-3 h-3 text-[#6E6658]" />
                        <span>{staff.phone || 'No phone'}</span>
                      </div>
                    </td>

                    {/* Station & Shift */}
                    <td className="py-4">
                      <div className="font-semibold text-white flex items-center gap-1">
                        <TrainTrack className="w-3.5 h-3.5 text-[#C9A24A]" />
                        <span>{staff.station}</span>
                      </div>
                      <div className="text-[10px] text-[#A39A8B] font-medium">
                        Shift: <span className="text-[#FFF1C4]">{staff.shift}</span>
                      </div>
                    </td>

                    {/* Role */}
                    <td className="py-4">
                      <Badge status={staff.role} />
                    </td>

                    {/* Installed Version */}
                    <td className="py-4 font-mono">
                      <div className="font-bold text-white flex items-center gap-1.5">
                        <Smartphone className="w-3.5 h-3.5 text-[#C9A24A]" />
                        <span>{staff.versionName}</span>
                        <span className="text-[10px] text-[#A39A8B]">(vc{staff.versionCode})</span>
                      </div>
                      <div className="text-[10px] text-[#6E6658]">
                        Synced: {new Date(staff.updatedAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                      </div>
                    </td>

                    {/* Channel */}
                    <td className="py-4">
                      {staff.isOutdated ? (
                        <span className="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-bold bg-[#2E1412] text-[#F38A7C] border border-[#592622]">
                          <AlertTriangle className="w-3 h-3" />
                          <span>OUTDATED</span>
                        </span>
                      ) : staff.isTrialTester ? (
                        <span className="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-bold bg-[#2C200C] text-[#FFF1C4] border border-[#C9A24A]/30">
                          <span>TRIAL TESTER</span>
                        </span>
                      ) : (
                        <span className="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-bold bg-[#122B1E] text-[#55D88D] border border-[#1D4A33]">
                          <span>MAIN CHANNEL</span>
                        </span>
                      )}
                    </td>

                    {/* Presence */}
                    <td className="py-4">
                      <div className="flex items-center gap-2">
                        <span className={`w-2 h-2 rounded-full ${staff.online ? 'bg-[#2F9A63] animate-ping' : 'bg-zinc-600'}`} />
                        <span className="text-xs font-medium text-white">
                          {staff.online ? 'Online' : 'Offline'}
                        </span>
                      </div>
                      <div className="text-[10px] text-[#6E6658]">30s heartbeat</div>
                    </td>

                    {/* Action */}
                    <td className="py-4 pr-2 text-right">
                      <button
                        onClick={() => {
                          const nextCode = staff.versionCode === 368 ? 339 : 368;
                          const nextName = nextCode === 368 ? 'v2.1.60' : 'v2.1.31';
                          updateStaffVersion(staff.employeeId, {
                            versionCode: nextCode,
                            versionName: nextName,
                            isOutdated: false
                          });
                        }}
                        className="px-2.5 py-1 text-xs font-bold text-[#FFF1C4] bg-white/[0.04] border border-[#C9A24A]/30 rounded-lg hover:border-[#C9A24A] hover:bg-[#C9A24A]/10 transition-colors"
                      >
                        Toggle Version
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Enroll Staff Modal */}
      {isAddOpen && (
        <Modal
          isOpen={isAddOpen}
          onClose={() => setIsAddOpen(false)}
          title="Enroll Metro+ Staff Member"
          subtitle="Reports to Firestore staff_versions collection"
        >
          <form onSubmit={handleCreate} className="space-y-4">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                  Employee ID (UPPERCASE)
                </label>
                <input
                  type="text"
                  required
                  value={newEmpId}
                  onChange={(e) => setNewEmpId(e.target.value.toUpperCase())}
                  placeholder="e.g. 3526 or MARYAN"
                  className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white uppercase font-mono focus:outline-none focus:border-[#C9A24A]"
                />
              </div>

              <div>
                <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                  Staff Full Name
                </label>
                <input
                  type="text"
                  required
                  value={newName}
                  onChange={(e) => setNewName(e.target.value)}
                  placeholder="e.g. Muneeb Ur Rehman"
                  className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
                />
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <div>
                <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                  Station
                </label>
                <select
                  value={newStation}
                  onChange={(e) => setNewStation(e.target.value)}
                  className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
                >
                  {stations.map(st => (
                    <option key={st.slug} value={st.name}>
                      {st.name} ({st.code})
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                  Shift
                </label>
                <select
                  value={newShift}
                  onChange={(e) => setNewShift(e.target.value as ShiftType)}
                  className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
                >
                  <option value="Morning">Morning</option>
                  <option value="Evening">Evening</option>
                </select>
              </div>

              <div>
                <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                  Role
                </label>
                <select
                  value={newRole}
                  onChange={(e) => setNewRole(e.target.value as StaffRole)}
                  className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
                >
                  <option value="Operator">Operator</option>
                  <option value="TL">Team Lead (TL)</option>
                </select>
              </div>
            </div>

            <div>
              <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                Mobile Number
              </label>
              <input
                type="text"
                value={newPhone}
                onChange={(e) => setNewPhone(e.target.value)}
                placeholder="+92 300 1234567"
                className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
              />
            </div>

            <div className="flex items-center justify-end gap-3 pt-3 border-t border-[#C9A24A]/15">
              <button
                type="button"
                onClick={() => setIsAddOpen(false)}
                className="px-4 py-2 text-xs font-semibold text-zinc-400 hover:text-white"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="px-5 py-2 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] rounded-xl transition-all shadow-md"
              >
                Enroll Staff Member
              </button>
            </div>
          </form>
        </Modal>
      )}
    </AdminLayout>
  );
};
