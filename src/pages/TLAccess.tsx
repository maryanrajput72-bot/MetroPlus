import React, { useState } from 'react';
import { useMetro } from '../context/MetroContext';
import { AdminLayout } from '../components/layout/AdminLayout';
import { Modal } from '../components/common/Modal';
import { TLAccessUser, ShiftType } from '../types/metro';
import { 
  KeyRound, 
  Mail, 
  Plus, 
  Trash2, 
  Edit, 
  ShieldCheck, 
  Check, 
  X, 
  Sparkles,
  Lock,
  TrainTrack
} from 'lucide-react';

export const TLAccess: React.FC = () => {
  const { tlUsers, saveTLUser, deleteTLUser, stations, featureFlags, toggleFeatureFlag } = useMetro();

  const [isOpenModal, setIsOpenModal] = useState(false);
  const [editingId, setEditingId] = useState<string | undefined>(undefined);
  const [empId, setEmpId] = useState('');
  const [name, setName] = useState('');
  const [gmail, setGmail] = useState('');
  const [pin, setPin] = useState('');
  const [station, setStation] = useState(stations[0].name);
  const [shift, setShift] = useState<ShiftType>('Morning');

  const handleOpenAdd = () => {
    setEditingId(undefined);
    setEmpId('');
    setName('');
    setGmail('');
    setPin('');
    setStation(stations[0].name);
    setShift('Morning');
    setIsOpenModal(true);
  };

  const handleOpenEdit = (user: TLAccessUser) => {
    setEditingId(user.id);
    setEmpId(user.empId);
    setName(user.name);
    setGmail(user.gmail);
    setPin(user.passwordPin);
    setStation(user.station);
    setShift(user.shift);
    setIsOpenModal(true);
  };

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();
    if (!gmail.includes('@') || pin.length !== 5) {
      return alert('Validation Error: Must provide a valid Gmail and a 5-digit numeric PIN.');
    }

    saveTLUser({
      empId: empId.trim().toUpperCase(),
      name,
      gmail: gmail.trim().toLowerCase(),
      passwordPin: pin.trim(),
      station,
      shift,
      active: true
    }, editingId);

    setIsOpenModal(false);
  };

  return (
    <AdminLayout title="TL Access & Credentials" subtitle="Manage Team Lead Gmail & 5-digit password credentials for app validation">
      <div className="space-y-6">
        {/* Premium Gate / Feature Flag Bar */}
        <div className="metro-card p-5 flex flex-col sm:flex-row items-center justify-between gap-4 border-[#C9A24A]/40 bg-gradient-to-r from-[#1E170F] to-[#14110E]">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-[#C9A24A]/15 border border-[#C9A24A]/30 flex items-center justify-center text-[#C9A24A]">
              <Sparkles className="w-5 h-5" />
            </div>
            <div>
              <div className="text-sm font-bold text-white flex items-center gap-2">
                <span>TL Premium Gate Flag:</span>
                <code className="text-[#FFF1C4] font-mono text-xs bg-black/40 px-2 py-0.5 rounded border border-white/10">
                  TL_OPEN_FEATURES
                </code>
              </div>
              <div className="text-xs text-[#A39A8B]">
                Controls whether Team Leads can access the full suite of operational features
              </div>
            </div>
          </div>

          <button
            onClick={() => toggleFeatureFlag('TL_OPEN_FEATURES')}
            className={`px-4 py-2 rounded-xl text-xs font-bold transition-all shadow-md flex items-center gap-2 ${
              featureFlags.TL_OPEN_FEATURES
                ? 'bg-[#122B1E] text-[#55D88D] border border-[#1D4A33] hover:bg-[#1D4A33]'
                : 'bg-[#2E1412] text-[#F38A7C] border border-[#592622] hover:bg-[#592622]'
            }`}
          >
            <span className={`w-2 h-2 rounded-full ${featureFlags.TL_OPEN_FEATURES ? 'bg-[#2F9A63]' : 'bg-[#D2553F]'}`} />
            <span>{featureFlags.TL_OPEN_FEATURES ? 'FLAG ACTIVE (ENABLED)' : 'FLAG DISABLED (GATE LOCKED)'}</span>
          </button>
        </div>

        {/* TL List Header */}
        <div className="metro-card p-4 sm:p-5 flex items-center justify-between gap-4">
          <div>
            <span className="text-xs font-bold text-[#A39A8B]">Authorized TL Credentials:</span>
            <span className="ml-2 text-xs font-bold text-[#C9A24A] bg-[#C9A24A]/10 px-2.5 py-1 rounded-lg border border-[#C9A24A]/25">
              {tlUsers.length} Team Leads
            </span>
          </div>

          <button
            onClick={handleOpenAdd}
            className="px-4 py-2 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] rounded-xl transition-all shadow-md flex items-center gap-1.5"
          >
            <Plus className="w-4 h-4" />
            <span>Add Team Lead Access</span>
          </button>
        </div>

        {/* TL Table */}
        <div className="metro-card p-6 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs">
              <thead>
                <tr className="border-b border-[#C9A24A]/15 text-[#6E6658] uppercase font-bold tracking-wider">
                  <th className="pb-3.5 pl-2">Team Lead Name</th>
                  <th className="pb-3.5">Employee ID</th>
                  <th className="pb-3.5">Station & Shift</th>
                  <th className="pb-3.5">Authorized Gmail</th>
                  <th className="pb-3.5">5-Digit Password PIN</th>
                  <th className="pb-3.5 text-center">Status</th>
                  <th className="pb-3.5 pr-2 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[#C9A24A]/10">
                {tlUsers.map((user) => (
                  <tr key={user.id} className="hover:bg-white/[0.02] transition-colors">
                    <td className="py-4 pl-2 font-bold text-white text-sm">
                      {user.name}
                    </td>

                    <td className="py-4 font-mono font-bold text-[#C9A24A]">
                      {user.empId}
                    </td>

                    <td className="py-4">
                      <div className="font-semibold text-white">{user.station}</div>
                      <div className="text-[10px] text-[#A39A8B]">{user.shift} Shift</div>
                    </td>

                    <td className="py-4 font-mono text-[#FFF1C4]">
                      {user.gmail}
                    </td>

                    <td className="py-4 font-mono font-bold text-[#55D88D] text-sm tracking-widest">
                      ••••• ({user.passwordPin})
                    </td>

                    <td className="py-4 text-center">
                      <span className="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-[10px] font-bold bg-[#122B1E] text-[#55D88D] border border-[#1D4A33]">
                        <Check className="w-3 h-3" />
                        <span>ACTIVE</span>
                      </span>
                    </td>

                    <td className="py-4 pr-2 text-right">
                      <div className="flex items-center justify-end gap-2">
                        <button
                          onClick={() => handleOpenEdit(user)}
                          className="p-1.5 rounded-lg text-zinc-400 hover:text-white hover:bg-white/5 transition-colors"
                          title="Edit TL"
                        >
                          <Edit className="w-4 h-4" />
                        </button>
                        <button
                          onClick={() => deleteTLUser(user.id)}
                          className="p-1.5 rounded-lg text-zinc-500 hover:text-red-400 hover:bg-white/5 transition-colors"
                          title="Revoke Access"
                        >
                          <Trash2 className="w-4 h-4" />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Add / Edit TL Modal */}
      {isOpenModal && (
        <Modal
          isOpen={isOpenModal}
          onClose={() => setIsOpenModal(false)}
          title={editingId ? 'Edit Team Lead Credentials' : 'Add Team Lead Credentials'}
          subtitle="Owner enters Gmail + 5-digit PIN for app login validation"
        >
          <form onSubmit={handleSave} className="space-y-4">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                  Employee ID
                </label>
                <input
                  type="text"
                  required
                  value={empId}
                  onChange={(e) => setEmpId(e.target.value.toUpperCase())}
                  placeholder="e.g. 3526"
                  className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white uppercase font-mono focus:outline-none focus:border-[#C9A24A]"
                />
              </div>

              <div>
                <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                  Full Name
                </label>
                <input
                  type="text"
                  required
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  placeholder="e.g. Muneeb Ur Rehman"
                  className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
                />
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                  TL Gmail Address
                </label>
                <input
                  type="email"
                  required
                  value={gmail}
                  onChange={(e) => setGmail(e.target.value)}
                  placeholder="e.g. muneeb.tl@gmail.com"
                  className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
                />
              </div>

              <div>
                <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                  5-Digit Password PIN
                </label>
                <input
                  type="text"
                  maxLength={5}
                  minLength={5}
                  required
                  pattern="[0-9]{5}"
                  value={pin}
                  onChange={(e) => setPin(e.target.value.replace(/[^0-9]/g, ''))}
                  placeholder="e.g. 48291"
                  className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white font-mono tracking-widest text-center text-sm font-bold focus:outline-none focus:border-[#C9A24A]"
                />
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                  Assigned Station
                </label>
                <select
                  value={station}
                  onChange={(e) => setStation(e.target.value)}
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
                  Assigned Shift
                </label>
                <select
                  value={shift}
                  onChange={(e) => setShift(e.target.value as ShiftType)}
                  className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
                >
                  <option value="Morning">Morning</option>
                  <option value="Evening">Evening</option>
                </select>
              </div>
            </div>

            <div className="flex items-center justify-end gap-3 pt-4 border-t border-[#C9A24A]/15">
              <button
                type="button"
                onClick={() => setIsOpenModal(false)}
                className="px-4 py-2 text-xs font-semibold text-zinc-400 hover:text-white"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="px-5 py-2 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] rounded-xl transition-all shadow-md"
              >
                {editingId ? 'Save Changes' : 'Save TL Credentials'}
              </button>
            </div>
          </form>
        </Modal>
      )}
    </AdminLayout>
  );
};
