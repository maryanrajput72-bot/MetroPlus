import React, { useState } from 'react';
import { useMetro } from '../context/MetroContext';
import { AdminLayout } from '../components/layout/AdminLayout';
import { ShiftType } from '../types/metro';
import { buildRMBSReport, buildCardClaimReport } from '../utils/reportFormats';
import { 
  FileText, 
  Copy, 
  Check, 
  TrainTrack, 
  Calculator, 
  Sun, 
  Moon, 
  Calendar,
  Share2
} from 'lucide-react';

export const Reports: React.FC = () => {
  const { stations, selectedStation, setSelectedStation, selectedShift, setSelectedShift, showToast } = useMetro();

  // RMBS inputs for 5 TOMs
  const [tom1, setTom1] = useState('14200');
  const [tom2, setTom2] = useState('11800');
  const [tom3, setTom3] = useState('8500');
  const [tom4, setTom4] = useState('0');
  const [tom5, setTom5] = useState('0');
  const [rmbsCopied, setRmbsCopied] = useState(false);

  // Card Claim inputs
  const [claimDate, setClaimDate] = useState(new Date().toISOString().split('T')[0]);
  const [claimCount, setClaimCount] = useState('14');
  const [claimCopied, setClaimCopied] = useState(false);

  // Compute BC Total
  const bcTotal = [tom1, tom2, tom3, tom4, tom5].reduce((sum, v) => sum + (parseFloat(v) || 0), 0);
  const rmbsString = buildRMBSReport(selectedShift, selectedStation.number, bcTotal);

  const cardClaimString = buildCardClaimReport(selectedStation.name, selectedShift, claimDate, parseInt(claimCount) || 0);

  const handleCopyRMBS = () => {
    navigator.clipboard?.writeText(rmbsString);
    setRmbsCopied(true);
    showToast('RMBS report copied to clipboard');
    setTimeout(() => setRmbsCopied(false), 3000);
  };

  const handleCopyClaim = () => {
    navigator.clipboard?.writeText(cardClaimString);
    setClaimCopied(true);
    showToast('Card claim note copied to clipboard');
    setTimeout(() => setClaimCopied(false), 3000);
  };

  return (
    <AdminLayout title="Closing & RMBS Reports" subtitle="Byte-exact generator for RMBS & Card Claim WhatsApp messages">
      <div className="space-y-6">
        {/* Station and Shift bar */}
        <div className="metro-card p-4 sm:p-5 flex flex-col sm:flex-row items-center justify-between gap-4">
          <div className="flex items-center gap-3">
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

            <div className="flex items-center bg-white/[0.04] p-1 rounded-xl border border-white/10">
              <button
                onClick={() => setSelectedShift('Morning')}
                className={`flex items-center gap-1.5 px-3 py-1 text-xs font-bold rounded-lg transition-all ${
                  selectedShift === 'Morning'
                    ? 'bg-[#C9A24A] text-[#0B0B0C] shadow-sm'
                    : 'text-[#A39A8B] hover:text-white'
                }`}
              >
                <Sun className="w-3 h-3" />
                <span>Morning (SM)</span>
              </button>
              <button
                onClick={() => setSelectedShift('Evening')}
                className={`flex items-center gap-1.5 px-3 py-1 text-xs font-bold rounded-lg transition-all ${
                  selectedShift === 'Evening'
                    ? 'bg-[#C9A24A] text-[#0B0B0C] shadow-sm'
                    : 'text-[#A39A8B] hover:text-white'
                }`}
              >
                <Moon className="w-3 h-3" />
                <span>Evening (SE)</span>
              </button>
            </div>
          </div>

          <div className="text-xs text-[#A39A8B]">
            Target Station: <strong className="text-white">#{selectedStation.number} {selectedStation.name}</strong>
          </div>
        </div>

        {/* Two Generator Panels */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* RMBS Report Panel */}
          <div className="metro-card p-6 flex flex-col justify-between">
            <div>
              <div className="flex items-center justify-between pb-3 border-b border-[#C9A24A]/15">
                <span className="text-[10px] font-bold tracking-[0.2em] text-[#C9A24A] uppercase flex items-center gap-1.5">
                  <Calculator className="w-3.5 h-3.5 text-[#C9A24A]" />
                  RMBS WHATSAPP CLOSING
                </span>
                <span className="text-[10px] font-mono text-[#55D88D] font-bold">Rule: {'{SM|SE},SN{num},BC{total}...'}</span>
              </div>

              <p className="text-xs text-[#A39A8B] mt-2">
                Sum of all 5 Ticket Office Machines (TOM) closing balances:
              </p>

              {/* 5 TOM Inputs */}
              <div className="grid grid-cols-2 sm:grid-cols-3 gap-3 mt-4">
                {[
                  { label: 'TOM 1 Balance', val: tom1, set: setTom1 },
                  { label: 'TOM 2 Balance', val: tom2, set: setTom2 },
                  { label: 'TOM 3 Balance', val: tom3, set: setTom3 },
                  { label: 'TOM 4 Balance', val: tom4, set: setTom4 },
                  { label: 'TOM 5 Balance', val: tom5, set: setTom5 }
                ].map((tom, i) => (
                  <div key={i} className="p-2.5 rounded-xl bg-[#0E0C09] border border-white/5">
                    <label className="text-[10px] font-bold text-[#A39A8B] uppercase block mb-1">
                      {tom.label}
                    </label>
                    <input
                      type="number"
                      value={tom.val}
                      onChange={(e) => tom.set(e.target.value)}
                      placeholder="0"
                      className="w-full bg-transparent text-xs font-mono font-bold text-white focus:outline-none"
                    />
                  </div>
                ))}

                <div className="p-2.5 rounded-xl bg-[#2A2012] border border-[#C9A24A]/30 flex flex-col justify-center">
                  <span className="text-[10px] font-bold text-[#C9A24A] uppercase block">
                    TOTAL BC SUM
                  </span>
                  <div className="text-sm font-mono font-black text-[#FFF1C4]">
                    Rs {bcTotal.toLocaleString()}
                  </div>
                </div>
              </div>

              {/* Generated String */}
              <div className="mt-5 p-4 rounded-xl bg-[#090806] border border-white/10">
                <span className="text-[10px] uppercase font-bold text-[#A39A8B] block mb-1">
                  Byte-Exact Output String:
                </span>
                <div className="font-mono text-sm font-bold text-[#55D88D] tracking-wide select-all break-all">
                  {rmbsString}
                </div>
              </div>
            </div>

            <button
              onClick={handleCopyRMBS}
              className="mt-6 w-full py-2.5 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] rounded-xl transition-all shadow-md flex items-center justify-center gap-2"
            >
              {rmbsCopied ? <Check className="w-4 h-4" /> : <Copy className="w-4 h-4" />}
              <span>{rmbsCopied ? 'RMBS Copied!' : 'Copy RMBS String'}</span>
            </button>
          </div>

          {/* Card Claim Detail Panel */}
          <div className="metro-card p-6 flex flex-col justify-between">
            <div>
              <div className="flex items-center justify-between pb-3 border-b border-[#C9A24A]/15">
                <span className="text-[10px] font-bold tracking-[0.2em] text-[#C9A24A] uppercase flex items-center gap-1.5">
                  <FileText className="w-3.5 h-3.5 text-[#C9A24A]" />
                  CARD CLAIM DETAIL
                </span>
                <span className="text-[10px] font-mono text-[#55D88D] font-bold">Rule: Station dashes become dots</span>
              </div>

              <p className="text-xs text-[#A39A8B] mt-2">
                Card claim note format (Station dashes become dots, count zero-padded to 2 digits):
              </p>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 mt-4">
                <div className="p-2.5 rounded-xl bg-[#0E0C09] border border-white/5">
                  <label className="text-[10px] font-bold text-[#A39A8B] uppercase block mb-1">
                    Claim Date
                  </label>
                  <input
                    type="date"
                    value={claimDate}
                    onChange={(e) => setClaimDate(e.target.value)}
                    className="w-full bg-transparent text-xs text-white focus:outline-none cursor-pointer"
                  />
                </div>

                <div className="p-2.5 rounded-xl bg-[#0E0C09] border border-white/5">
                  <label className="text-[10px] font-bold text-[#A39A8B] uppercase block mb-1">
                    Card Claim Total
                  </label>
                  <input
                    type="number"
                    value={claimCount}
                    onChange={(e) => setClaimCount(e.target.value)}
                    placeholder="14"
                    className="w-full bg-transparent text-xs font-mono font-bold text-white focus:outline-none"
                  />
                </div>
              </div>

              {/* Generated WhatsApp Note */}
              <div className="mt-5 p-4 rounded-xl bg-[#090806] border border-white/10 font-mono text-xs text-[#FFF1C4] whitespace-pre-wrap leading-relaxed select-all">
                {cardClaimString}
              </div>
            </div>

            <button
              onClick={handleCopyClaim}
              className="mt-6 w-full py-2.5 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] rounded-xl transition-all shadow-md flex items-center justify-center gap-2"
            >
              {claimCopied ? <Check className="w-4 h-4" /> : <Copy className="w-4 h-4" />}
              <span>{claimCopied ? 'Card Claim Note Copied!' : 'Copy Card Claim Note'}</span>
            </button>
          </div>
        </div>
      </div>
    </AdminLayout>
  );
};
