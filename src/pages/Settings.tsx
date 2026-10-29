import React, { useState } from 'react';
import { useMetro } from '../context/MetroContext';
import { AdminLayout } from '../components/layout/AdminLayout';
import { Settings as SettingsIcon, Save, Shield, Sparkles, Check, Key, Database } from 'lucide-react';

export const Settings: React.FC = () => {
  const { featureFlags, toggleFeatureFlag, showToast, updaterConfig } = useMetro();

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();
    showToast('Metro+ platform settings saved');
  };

  return (
    <AdminLayout title="System Settings & Config" subtitle="Feature flags, Firebase project rules & app constraints">
      <form onSubmit={handleSave} className="space-y-6 max-w-4xl">
        {/* Firebase Config Info */}
        <div className="metro-card p-6 space-y-4">
          <div className="pb-3 border-b border-[#C9A24A]/15">
            <span className="text-[10px] font-bold tracking-[0.2em] text-[#C9A24A] uppercase flex items-center gap-1.5">
              <Database className="w-3.5 h-3.5 text-[#C9A24A]" />
              FIREBASE PROJECT (SPARK TIER)
            </span>
            <h3 className="text-lg font-bold text-white mt-1 font-['Cinzel',serif]">Backend Infrastructure</h3>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 text-xs">
            <div className="p-3 rounded-xl bg-[#0E0C09] border border-white/5 space-y-1">
              <span className="text-[#A39A8B] block text-[10px] uppercase font-bold">Project ID</span>
              <div className="font-mono text-white font-bold">metro-plus-5966d</div>
              <div className="text-[10px] text-[#55D88D]">Spark FREE Tier Only • No Billing</div>
            </div>

            <div className="p-3 rounded-xl bg-[#0E0C09] border border-white/5 space-y-1">
              <span className="text-[#A39A8B] block text-[10px] uppercase font-bold">App Package / ID</span>
              <div className="font-mono text-white font-bold">com.muneeb.metroapp</div>
              <div className="text-[10px] text-[#C9A24A]">Launcher Label: "Metro +"</div>
            </div>
          </div>
        </div>

        {/* Feature Flags */}
        <div className="metro-card p-6 space-y-4">
          <div className="pb-3 border-b border-[#C9A24A]/15">
            <span className="text-[10px] font-bold tracking-[0.2em] text-[#55D88D] uppercase flex items-center gap-1.5">
              <Sparkles className="w-3.5 h-3.5 text-[#55D88D]" />
              FEATURE FLAGS & GATES
            </span>
            <h3 className="text-lg font-bold text-white mt-1 font-['Cinzel',serif]">Active Feature Toggles</h3>
          </div>

          <div className="p-4 rounded-xl bg-white/[0.02] border border-white/5 flex items-center justify-between gap-4">
            <div>
              <div className="flex items-center gap-2">
                <span className="font-mono font-bold text-sm text-[#FFF1C4]">TL_OPEN_FEATURES</span>
                <span className="text-[10px] px-2 py-0.5 rounded-full font-bold bg-[#C9A24A]/15 text-[#E5C06E] border border-[#C9A24A]/30">
                  Team Lead Gate
                </span>
              </div>
              <p className="text-xs text-[#A39A8B] mt-1">
                When enabled, Team Leads can access the full app suite without the premium request lock screen.
              </p>
            </div>

            <button
              type="button"
              onClick={() => toggleFeatureFlag('TL_OPEN_FEATURES')}
              className={`px-4 py-2 rounded-xl text-xs font-bold transition-all shadow-md flex items-center gap-2 whitespace-nowrap ${
                featureFlags.TL_OPEN_FEATURES
                  ? 'bg-[#122B1E] text-[#55D88D] border border-[#1D4A33]'
                  : 'bg-[#2E1412] text-[#F38A7C] border border-[#592622]'
              }`}
            >
              <span>{featureFlags.TL_OPEN_FEATURES ? 'ENABLED' : 'DISABLED'}</span>
            </button>
          </div>
        </div>

        {/* Hard Constraints Summary */}
        <div className="metro-card p-6 space-y-3">
          <div className="pb-3 border-b border-[#C9A24A]/15">
            <span className="text-[10px] font-bold tracking-[0.2em] text-[#E0A42E] uppercase flex items-center gap-1.5">
              <Shield className="w-3.5 h-3.5 text-[#E0A42E]" />
              HARD CONSTRAINTS AUDIT
            </span>
            <h3 className="text-lg font-bold text-white mt-1 font-['Cinzel',serif]">Operating Guidelines</h3>
          </div>

          <ul className="space-y-2 text-xs text-[#A39A8B]">
            <li className="flex items-start gap-2">
              <Check className="w-4 h-4 text-[#55D88D] flex-shrink-0 mt-0.5" />
              <span><strong>Owner Signal 7280:</strong> Main production updater moves ONLY on the owner's 4-digit code '7280'.</span>
            </li>
            <li className="flex items-start gap-2">
              <Check className="w-4 h-4 text-[#55D88D] flex-shrink-0 mt-0.5" />
              <span><strong>Station Names:</strong> Never hardcode user station — always dynamic from login selection (24 stations).</span>
            </li>
            <li className="flex items-start gap-2">
              <Check className="w-4 h-4 text-[#55D88D] flex-shrink-0 mt-0.5" />
              <span><strong>GPS Delete Rule:</strong> Deleting a photo in the panel permanently removes the cloud doc.</span>
            </li>
            <li className="flex items-start gap-2">
              <Check className="w-4 h-4 text-[#55D88D] flex-shrink-0 mt-0.5" />
              <span><strong>Firebase Free Tier:</strong> Strictly Spark free tier — no billing or bank card connections.</span>
            </li>
          </ul>
        </div>
      </form>
    </AdminLayout>
  );
};
