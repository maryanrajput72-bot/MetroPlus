import React, { useState } from 'react';
import { useMetro } from '../context/MetroContext';
import { AdminLayout } from '../components/layout/AdminLayout';
import { Modal } from '../components/common/Modal';
import { 
  UploadCloud, 
  ShieldCheck, 
  Lock, 
  Smartphone, 
  UserCheck, 
  Plus, 
  Trash2, 
  AlertTriangle, 
  Check, 
  ExternalLink,
  Sparkles
} from 'lucide-react';

export const Updater: React.FC = () => {
  const { updaterConfig, pushUpdateRelease, addTrialTester, removeTrialTester } = useMetro();

  const [channelToPush, setChannelToPush] = useState<'main' | 'trial' | null>(null);
  const [versionName, setVersionName] = useState('');
  const [versionCode, setVersionCode] = useState('');
  const [downloadUrl, setDownloadUrl] = useState('');
  const [signalPin, setSignalPin] = useState('');
  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const [newTesterId, setNewTesterId] = useState('');

  const handleOpenPush = (channel: 'main' | 'trial') => {
    setChannelToPush(channel);
    const curr = updaterConfig[channel];
    setVersionName(curr.versionName);
    setVersionCode(String(curr.versionCode + 1));
    setDownloadUrl(curr.url);
    setSignalPin('');
    setErrorMsg(null);
  };

  const handleConfirmPush = (e: React.FormEvent) => {
    e.preventDefault();
    if (!channelToPush) return;
    const vCode = parseInt(versionCode);
    if (!versionName || isNaN(vCode)) return alert('Please enter valid version info');

    const result = pushUpdateRelease(channelToPush, versionName, vCode, downloadUrl, signalPin);
    if (!result.success) {
      setErrorMsg(result.error || 'Failed to push release');
      return;
    }

    setChannelToPush(null);
  };

  const handleAddTester = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newTesterId) return;
    addTrialTester(newTesterId);
    setNewTesterId('');
  };

  return (
    <AdminLayout title="Updater & Channels" subtitle="Manage version.json channels, APK URLs & trial testers">
      <div className="space-y-6">
        {/* Safety Warning Banner */}
        <div className="p-4 rounded-2xl bg-[#2C200C]/70 border border-[#553C14] flex items-start gap-3.5">
          <ShieldCheck className="w-5 h-5 text-[#E0A42E] flex-shrink-0 mt-0.5" />
          <div className="text-xs">
            <span className="font-bold text-[#FFF1C4] uppercase tracking-wide block">
              Hard Protection Rule (Signal 7280)
            </span>
            <p className="text-[#A39A8B] mt-0.5 leading-relaxed">
              This panel is READ-ONLY: it cannot push releases. Releases are published only through the owner’s main-chat process, and the MAIN (staff) production channel moves only on the owner’s signal in that chat.
            </p>
          </div>
        </div>

        {/* Channel Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {/* Main Production Channel */}
          <div className="metro-card p-6 flex flex-col justify-between relative overflow-hidden">
            <div>
              <div className="flex items-center justify-between pb-3 border-b border-white/10">
                <div className="flex items-center gap-2">
                  <div className="w-8 h-8 rounded-lg bg-[#122B1E] border border-[#1D4A33] flex items-center justify-center text-[#55D88D]">
                    <ShieldCheck className="w-4 h-4" />
                  </div>
                  <div>
                    <h3 className="text-base font-bold text-white font-['Cinzel',serif]">Main Channel (Production)</h3>
                    <span className="text-[10px] text-[#A39A8B]">General Red Line Station Staff</span>
                  </div>
                </div>

                <span className="px-2.5 py-1 rounded-full text-[10px] font-bold bg-[#122B1E] text-[#55D88D] border border-[#1D4A33]">
                  ACTIVE RELEASE
                </span>
              </div>

              <div className="mt-4 space-y-3 text-xs">
                <div className="flex justify-between p-2.5 rounded-lg bg-[#0E0C09] border border-white/5">
                  <span className="text-[#A39A8B]">Current Version Name:</span>
                  <span className="font-mono font-bold text-white">{updaterConfig.main.versionName}</span>
                </div>
                <div className="flex justify-between p-2.5 rounded-lg bg-[#0E0C09] border border-white/5">
                  <span className="text-[#A39A8B]">Current Version Code:</span>
                  <span className="font-mono font-bold text-[#FFF1C4]">{updaterConfig.main.versionCode}</span>
                </div>
                <div className="p-2.5 rounded-lg bg-[#0E0C09] border border-white/5 space-y-1">
                  <span className="text-[#A39A8B] block text-[10.5px]">APK Remote URL:</span>
                  <div className="font-mono text-[#55D88D] text-[11px] truncate">{updaterConfig.main.url}</div>
                </div>
              </div>
            </div>

            <div className="mt-6 pt-4 border-t border-white/10 flex items-center justify-between">
              <span className="text-[11px] text-[#A39A8B] flex items-center gap-1">
                <Lock className="w-3.5 h-3.5 text-[#E0A42E]" />
                <span>Protected by '7280'</span>
              </span>
              <button
                disabled
                title="This panel is read-only and cannot push releases"
                className="px-4 py-2 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A]/40 rounded-xl cursor-not-allowed"
              >
                Push Disabled (Read-Only)
              </button>
            </div>
          </div>

          {/* Trial Channel */}
          <div className="metro-card p-6 flex flex-col justify-between relative overflow-hidden">
            <div>
              <div className="flex items-center justify-between pb-3 border-b border-white/10">
                <div className="flex items-center gap-2">
                  <div className="w-8 h-8 rounded-lg bg-[#2C200C] border border-[#553C14] flex items-center justify-center text-[#FFF1C4]">
                    <Sparkles className="w-4 h-4" />
                  </div>
                  <div>
                    <h3 className="text-base font-bold text-white font-['Cinzel',serif]">Trial Channel (Experimental)</h3>
                    <span className="text-[10px] text-[#A39A8B]">Restricted to trial.testers list</span>
                  </div>
                </div>

                <span className="px-2.5 py-1 rounded-full text-[10px] font-bold bg-[#2C200C] text-[#FFF1C4] border border-[#C9A24A]/30">
                  TRIAL STAGE
                </span>
              </div>

              <div className="mt-4 space-y-3 text-xs">
                <div className="flex justify-between p-2.5 rounded-lg bg-[#0E0C09] border border-white/5">
                  <span className="text-[#A39A8B]">Trial Version Name:</span>
                  <span className="font-mono font-bold text-white">{updaterConfig.trial.versionName}</span>
                </div>
                <div className="flex justify-between p-2.5 rounded-lg bg-[#0E0C09] border border-white/5">
                  <span className="text-[#A39A8B]">Trial Version Code:</span>
                  <span className="font-mono font-bold text-[#FFF1C4]">{updaterConfig.trial.versionCode}</span>
                </div>
                <div className="p-2.5 rounded-lg bg-[#0E0C09] border border-white/5 space-y-1">
                  <span className="text-[#A39A8B] block text-[10.5px]">Trial APK Download:</span>
                  <div className="font-mono text-[#C9A24A] text-[11px] truncate">{updaterConfig.trial.url}</div>
                </div>
              </div>
            </div>

            <div className="mt-6 pt-4 border-t border-white/10 flex items-center justify-between">
              <span className="text-[11px] text-[#A39A8B]">Direct Link Only</span>
              <button
                disabled
                title="This panel is read-only and cannot push releases"
                className="px-4 py-2 text-xs font-bold text-[#FFF1C4]/50 bg-white/[0.04] border border-[#C9A24A]/40 rounded-xl cursor-not-allowed"
              >
                Push Disabled (Read-Only)
              </button>
            </div>
          </div>
        </div>

        {/* Trial Testers Management */}
        <div className="metro-card p-6">
          <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 pb-4 border-b border-[#C9A24A]/15">
            <div>
              <div className="flex items-center gap-2">
                <UserCheck className="w-4 h-4 text-[#C9A24A]" />
                <h3 className="text-base font-bold text-white font-['Cinzel',serif]">Trial Channel Testers</h3>
              </div>
              <p className="text-xs text-[#A39A8B]">
                Staffers whose Employee ID matches this list receive the trial channel updater automatically
              </p>
            </div>

            {/* Add Tester Form */}
            <form onSubmit={handleAddTester} className="flex items-center gap-2 w-full sm:w-auto">
              <input
                type="text"
                required
                value={newTesterId}
                onChange={(e) => setNewTesterId(e.target.value.toUpperCase())}
                placeholder="Staff ID e.g. 3526"
                className="bg-[#0E0C09] border border-[#C9A24A]/30 text-xs text-white uppercase font-mono rounded-xl px-3 py-2 focus:outline-none focus:border-[#C9A24A] w-full sm:w-44"
              />
              <button
                type="submit"
                className="px-3.5 py-2 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] rounded-xl transition-all shadow-md flex items-center gap-1 whitespace-nowrap"
              >
                <Plus className="w-3.5 h-3.5" />
                <span>Add Tester</span>
              </button>
            </form>
          </div>

          <div className="grid grid-cols-2 sm:grid-cols-4 lg:grid-cols-6 gap-3 mt-4">
            {updaterConfig.testers.map((testerId) => (
              <div
                key={testerId}
                className="p-3 rounded-xl bg-[#0E0C09] border border-[#C9A24A]/25 flex items-center justify-between"
              >
                <div className="font-mono font-bold text-sm text-[#FFF1C4]">{testerId}</div>
                <button
                  onClick={() => removeTrialTester(testerId)}
                  className="text-zinc-500 hover:text-red-400 p-1"
                  title="Remove tester"
                >
                  <Trash2 className="w-3.5 h-3.5" />
                </button>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Push Release Modal */}
      {channelToPush && (
        <Modal
          isOpen={!!channelToPush}
          onClose={() => setChannelToPush(null)}
          title={`Push Release: ${channelToPush.toUpperCase()} Channel`}
          subtitle="Updates version.json remote updater configuration"
        >
          <form onSubmit={handleConfirmPush} className="space-y-4">
            {channelToPush === 'main' && (
              <div className="p-3.5 rounded-xl bg-[#2E1412] border border-[#592622] space-y-2">
                <div className="flex items-center gap-2 text-xs font-bold text-[#F38A7C]">
                  <Lock className="w-4 h-4" />
                  <span>Owner Authorization Signal Required</span>
                </div>
                <p className="text-[11px] text-[#F38A7C]/90">
                  Enter the owner's 4-digit confirmation signal code ('7280') to deploy this build to production staff.
                </p>
                <input
                  type="password"
                  maxLength={4}
                  required
                  value={signalPin}
                  onChange={(e) => setSignalPin(e.target.value)}
                  placeholder="Enter 4-digit signal (7280)"
                  className="w-full bg-[#0E0C09] border border-red-500/50 rounded-lg p-2 text-xs text-white font-mono tracking-widest text-center focus:outline-none focus:border-red-400"
                />
              </div>
            )}

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                  Version Name
                </label>
                <input
                  type="text"
                  required
                  value={versionName}
                  onChange={(e) => setVersionName(e.target.value)}
                  placeholder="e.g. v2.1.32"
                  className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white font-mono focus:outline-none focus:border-[#C9A24A]"
                />
              </div>

              <div>
                <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                  Version Code (Integer)
                </label>
                <input
                  type="number"
                  required
                  value={versionCode}
                  onChange={(e) => setVersionCode(e.target.value)}
                  placeholder="e.g. 340"
                  className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white font-mono focus:outline-none focus:border-[#C9A24A]"
                />
              </div>
            </div>

            <div>
              <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                APK Download URL
              </label>
              <input
                type="url"
                required
                value={downloadUrl}
                onChange={(e) => setDownloadUrl(e.target.value)}
                placeholder="https://..."
                className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white font-mono focus:outline-none focus:border-[#C9A24A]"
              />
            </div>

            {errorMsg && (
              <div className="p-3 rounded-xl bg-red-950/80 border border-red-600 text-xs text-red-200">
                {errorMsg}
              </div>
            )}

            <div className="flex items-center justify-end gap-3 pt-4 border-t border-[#C9A24A]/15">
              <button
                type="button"
                onClick={() => setChannelToPush(null)}
                className="px-4 py-2 text-xs font-semibold text-zinc-400 hover:text-white"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="px-5 py-2 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] rounded-xl transition-all shadow-md"
              >
                Deploy Update to {channelToPush.toUpperCase()}
              </button>
            </div>
          </form>
        </Modal>
      )}
    </AdminLayout>
  );
};
