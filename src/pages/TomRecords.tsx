import React, { useState, useEffect } from 'react';
import { useMetro } from '../context/MetroContext';
import { AdminLayout } from '../components/layout/AdminLayout';
import { buildRMBSReport, buildCardClaimReport } from '../utils/reportFormats';
import { 
  Calculator, 
  Copy, 
  Check, 
  TrainTrack, 
  Share2, 
  Coins, 
  Layers, 
  TrendingUp, 
  Sun, 
  Moon, 
  Calendar,
  Search,
  CheckCircle2,
  Clock,
  Printer
} from 'lucide-react';

export const TomRecords: React.FC = () => {
  const { 
    stations, 
    selectedStation, 
    setSelectedStation, 
    selectedShift, 
    setSelectedShift, 
    totalNetworkSales,
    totalRollsUsed,
    updateStationSales,
    showToast,
    todayTom 
  } = useMetro();

  // TOM Inputs for selected station
  const [tom1, setTom1] = useState('14200');
  const [tom2, setTom2] = useState('11800');
  const [tom3, setTom3] = useState('8500');
  const [tom4, setTom4] = useState('0');
  const [tom5, setTom5] = useState('0');
  const [rolls, setRolls] = useState('3');
  const [searchQuery, setSearchQuery] = useState('');
  const [rmbsCopied, setRmbsCopied] = useState(false);
  const [claimCopied, setClaimCopied] = useState(false);

  // Card Claim inputs
  const [claimDate, setClaimDate] = useState(new Date().toISOString().split('T')[0]);
  const [claimCount, setClaimCount] = useState('12');

  // Prefill TOM closings from today's saved record for the selected
  // station/shift (live Firestore). Blank = nothing saved yet; user edits kept.
  useEffect(() => {
    if (!todayTom) return;
    if (todayTom.a !== '') setTom1(todayTom.a);
    if (todayTom.b !== '') setTom2(todayTom.b);
    if (todayTom.c !== '') setTom3(todayTom.c);
    if (todayTom.d !== '') setTom4(todayTom.d);
    if (todayTom.e !== '') setTom5(todayTom.e);
  }, [todayTom]);

  const bcTotal = [tom1, tom2, tom3, tom4, tom5].reduce((sum, v) => sum + (parseFloat(v) || 0), 0);
  const fullStationNumber = Number(selectedStation.code.split('/')[1]) || selectedStation.number;
  const rmbsString = buildRMBSReport(selectedShift, fullStationNumber, bcTotal);
  const cardClaimString = buildCardClaimReport(selectedStation.name, selectedShift, claimDate, parseInt(claimCount) || 0);

  const handleCopyRMBS = () => {
    navigator.clipboard?.writeText(rmbsString);
    setRmbsCopied(true);
    showToast(`RMBS report for ${selectedStation.name} copied!`);
    setTimeout(() => setRmbsCopied(false), 3000);
  };

  const handleCopyClaim = () => {
    navigator.clipboard?.writeText(cardClaimString);
    setClaimCopied(true);
    showToast('Card claim note copied to clipboard!');
    setTimeout(() => setClaimCopied(false), 3000);
  };

  const handleSaveStationRecord = () => {
    updateStationSales(selectedStation.number, bcTotal, parseInt(rolls) || 0);
    showToast(`Saved ${selectedStation.name} TOM record: Rs ${bcTotal.toLocaleString()} (${rolls} rolls)`);
  };

  const filteredStations = stations.filter(s => 
    s.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
    s.code.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <AdminLayout 
      title="TOM Records" 
      subtitle="Ticket Operating Machine (TOM 1-5) closing balances, ticket rolls & RMBS reports"
    >
      <div className="space-y-6">
        {/* Top Metric Cards */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <div className="metro-card p-5">
            <div className="flex items-center justify-between">
              <span className="text-[10px] font-bold uppercase tracking-wider text-[#A39A8B]">
                Total Network Revenue
              </span>
              <div className="w-8 h-8 rounded-lg bg-[#55D88D]/10 border border-[#55D88D]/20 flex items-center justify-center text-[#55D88D]">
                <Coins className="w-4 h-4" />
              </div>
            </div>
            <div className="text-2xl font-black font-mono text-white mt-2">
              Rs {totalNetworkSales.toLocaleString()}
            </div>
            <div className="text-[11px] text-[#55D88D] font-medium mt-1">
              All 24 Red Line Stations Combined
            </div>
          </div>

          <div className="metro-card p-5">
            <div className="flex items-center justify-between">
              <span className="text-[10px] font-bold uppercase tracking-wider text-[#A39A8B]">
                Ticket Rolls Consumed
              </span>
              <div className="w-8 h-8 rounded-lg bg-[#C9A24A]/10 border border-[#C9A24A]/20 flex items-center justify-center text-[#C9A24A]">
                <Layers className="w-4 h-4" />
              </div>
            </div>
            <div className="text-2xl font-black font-mono text-[#FFF1C4] mt-2">
              {totalRollsUsed} Rolls
            </div>
            <div className="text-[11px] text-[#C9A24A] font-medium mt-1">
              Thermal rolls printed today
            </div>
          </div>

          <div className="metro-card p-5">
            <div className="flex items-center justify-between">
              <span className="text-[10px] font-bold uppercase tracking-wider text-[#A39A8B]">
                Selected Station Sale
              </span>
              <div className="w-8 h-8 rounded-lg bg-blue-500/10 border border-blue-500/20 flex items-center justify-center text-blue-400">
                <TrendingUp className="w-4 h-4" />
              </div>
            </div>
            <div className="text-2xl font-black font-mono text-white mt-2">
              Rs {bcTotal.toLocaleString()}
            </div>
            <div className="text-[11px] text-[#A39A8B] font-medium mt-1">
              {selectedStation.name} ({selectedStation.code})
            </div>
          </div>

          <div className="metro-card p-5">
            <div className="flex items-center justify-between">
              <span className="text-[10px] font-bold uppercase tracking-wider text-[#A39A8B]">
                Active Reporting Shift
              </span>
              <div className="w-8 h-8 rounded-lg bg-amber-500/10 border border-amber-500/20 flex items-center justify-center text-amber-400">
                {selectedShift === 'morning' ? <Sun className="w-4 h-4" /> : <Moon className="w-4 h-4" />}
              </div>
            </div>
            <div className="text-2xl font-black capitalize text-[#FFF1C4] mt-2">
              {selectedShift} Shift
            </div>
            <div className="text-[11px] text-[#55D88D] font-medium mt-1">
              Live automated closing sync
            </div>
          </div>
        </div>

        {/* Station Selector & Shift Bar */}
        <div className="metro-card p-5 flex flex-col md:flex-row items-center justify-between gap-4">
          <div className="flex items-center gap-3 w-full md:w-auto">
            <div className="flex items-center gap-2 bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3 py-2 text-xs">
              <TrainTrack className="w-4 h-4 text-[#C9A24A]" />
              <select
                value={selectedStation.number}
                onChange={(e) => {
                  const st = stations.find(s => s.number === parseInt(e.target.value));
                  if (st) setSelectedStation(st);
                }}
                className="bg-transparent text-white font-bold outline-none cursor-pointer"
              >
                {stations.map(st => (
                  <option key={st.number} value={st.number} className="bg-[#14110E] text-white">
                    {st.number}. {st.name} ({st.code})
                  </option>
                ))}
              </select>
            </div>

            <div className="flex rounded-xl bg-[#0E0C09] p-1 border border-white/10 text-xs">
              <button
                onClick={() => setSelectedShift('morning')}
                className={`flex items-center gap-1.5 px-3 py-1.5 rounded-lg font-bold transition-all ${
                  selectedShift === 'morning'
                    ? 'bg-[#C9A24A] text-[#0B0B0C] shadow'
                    : 'text-[#A39A8B] hover:text-white'
                }`}
              >
                <Sun className="w-3.5 h-3.5" />
                <span>Morning</span>
              </button>
              <button
                onClick={() => setSelectedShift('evening')}
                className={`flex items-center gap-1.5 px-3 py-1.5 rounded-lg font-bold transition-all ${
                  selectedShift === 'evening'
                    ? 'bg-[#C9A24A] text-[#0B0B0C] shadow'
                    : 'text-[#A39A8B] hover:text-white'
                }`}
              >
                <Moon className="w-3.5 h-3.5" />
                <span>Evening</span>
              </button>
            </div>
          </div>

          <div className="text-xs text-[#A39A8B] flex items-center gap-2">
            <span className="w-2 h-2 rounded-full bg-[#55D88D] animate-ping" />
            <span>Red Line Signal ID: <strong className="text-white font-mono">7280</strong></span>
          </div>
        </div>

        {/* 5 TOMs Input Form & RMBS Output Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
          {/* Left Column: 5 TOM Inputs */}
          <div className="lg:col-span-7 metro-card p-6 space-y-5">
            <div className="flex items-center justify-between border-b border-white/10 pb-3">
              <div>
                <h3 className="text-base font-bold text-white flex items-center gap-2">
                  <Calculator className="w-4 h-4 text-[#C9A24A]" />
                  <span>TOM 1–5 Closing Collections</span>
                </h3>
                <p className="text-xs text-[#A39A8B] mt-0.5">
                  Enter ticket operating booth closing balances for {selectedStation.name}
                </p>
              </div>
              <span className="text-[10px] font-mono px-2 py-1 rounded bg-[#C9A24A]/10 text-[#FFF1C4] border border-[#C9A24A]/20">
                {selectedStation.code}
              </span>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="text-[11px] font-bold text-[#A39A8B] uppercase tracking-wider block mb-1.5">
                  TOM 1 Collection (Rs)
                </label>
                <input
                  type="number"
                  value={tom1}
                  onChange={(e) => setTom1(e.target.value)}
                  className="w-full bg-[#0E0C09] border border-white/10 focus:border-[#C9A24A] rounded-xl px-4 py-2.5 text-sm font-mono text-white outline-none"
                  placeholder="0"
                />
              </div>

              <div>
                <label className="text-[11px] font-bold text-[#A39A8B] uppercase tracking-wider block mb-1.5">
                  TOM 2 Collection (Rs)
                </label>
                <input
                  type="number"
                  value={tom2}
                  onChange={(e) => setTom2(e.target.value)}
                  className="w-full bg-[#0E0C09] border border-white/10 focus:border-[#C9A24A] rounded-xl px-4 py-2.5 text-sm font-mono text-white outline-none"
                  placeholder="0"
                />
              </div>

              <div>
                <label className="text-[11px] font-bold text-[#A39A8B] uppercase tracking-wider block mb-1.5">
                  TOM 3 Collection (Rs)
                </label>
                <input
                  type="number"
                  value={tom3}
                  onChange={(e) => setTom3(e.target.value)}
                  className="w-full bg-[#0E0C09] border border-white/10 focus:border-[#C9A24A] rounded-xl px-4 py-2.5 text-sm font-mono text-white outline-none"
                  placeholder="0"
                />
              </div>

              <div>
                <label className="text-[11px] font-bold text-[#A39A8B] uppercase tracking-wider block mb-1.5">
                  TOM 4 Collection (Rs)
                </label>
                <input
                  type="number"
                  value={tom4}
                  onChange={(e) => setTom4(e.target.value)}
                  className="w-full bg-[#0E0C09] border border-white/10 focus:border-[#C9A24A] rounded-xl px-4 py-2.5 text-sm font-mono text-white outline-none"
                  placeholder="0"
                />
              </div>

              <div>
                <label className="text-[11px] font-bold text-[#A39A8B] uppercase tracking-wider block mb-1.5">
                  TOM 5 Collection (Rs)
                </label>
                <input
                  type="number"
                  value={tom5}
                  onChange={(e) => setTom5(e.target.value)}
                  className="w-full bg-[#0E0C09] border border-white/10 focus:border-[#C9A24A] rounded-xl px-4 py-2.5 text-sm font-mono text-white outline-none"
                  placeholder="0"
                />
              </div>

              <div>
                <label className="text-[11px] font-bold text-[#A39A8B] uppercase tracking-wider block mb-1.5">
                  Rolls Consumed
                </label>
                <input
                  type="number"
                  value={rolls}
                  onChange={(e) => setRolls(e.target.value)}
                  className="w-full bg-[#0E0C09] border border-white/10 focus:border-[#C9A24A] rounded-xl px-4 py-2.5 text-sm font-mono text-white outline-none"
                  placeholder="1"
                />
              </div>
            </div>

            {/* Total Balance Summary Bar */}
            <div className="p-4 rounded-xl bg-gradient-to-r from-[#1A140B] to-[#120F0B] border border-[#C9A24A]/30 flex items-center justify-between">
              <div>
                <span className="text-[10px] font-bold uppercase tracking-wider text-[#A39A8B] block">
                  Station Total (BC Balance)
                </span>
                <span className="text-2xl font-black font-mono text-[#55D88D]">
                  Rs {bcTotal.toLocaleString()}
                </span>
              </div>

              <button
                onClick={handleSaveStationRecord}
                className="px-5 py-2.5 rounded-xl bg-[#C9A24A] hover:bg-[#FFF1C4] text-[#0B0B0C] text-xs font-bold transition-all shadow-lg shadow-[#C9A24A]/20"
              >
                Save Record
              </button>
            </div>
          </div>

          {/* Right Column: WhatsApp RMBS & Card Claim Report */}
          <div className="lg:col-span-5 space-y-6">
            {/* RMBS Report Card */}
            <div className="metro-card p-6 space-y-4">
              <div className="flex items-center justify-between">
                <div>
                  <h3 className="text-sm font-bold text-white flex items-center gap-2">
                    <Share2 className="w-4 h-4 text-[#55D88D]" />
                    <span>WhatsApp RMBS Format</span>
                  </h3>
                  <p className="text-[11px] text-[#A39A8B]">
                    Byte-exact Metro+ format ready to send
                  </p>
                </div>

                <button
                  onClick={handleCopyRMBS}
                  className="px-3.5 py-1.5 rounded-lg text-xs font-bold bg-[#55D88D]/15 text-[#55D88D] border border-[#55D88D]/30 hover:bg-[#55D88D]/25 transition-all flex items-center gap-1.5"
                >
                  {rmbsCopied ? <Check className="w-3.5 h-3.5" /> : <Copy className="w-3.5 h-3.5" />}
                  <span>{rmbsCopied ? 'Copied' : 'Copy'}</span>
                </button>
              </div>

              <pre className="p-3.5 rounded-xl bg-[#090806] border border-white/10 font-mono text-[11px] text-white/90 leading-relaxed whitespace-pre-wrap select-all">
                {rmbsString}
              </pre>
            </div>

            {/* Card Claim Generator Card */}
            <div className="metro-card p-6 space-y-4">
              <div className="flex items-center justify-between">
                <div>
                  <h3 className="text-sm font-bold text-white flex items-center gap-2">
                    <Layers className="w-4 h-4 text-[#C9A24A]" />
                    <span>Card Claim Report</span>
                  </h3>
                  <p className="text-[11px] text-[#A39A8B]">
                    Passenger card claim tally note
                  </p>
                </div>

                <button
                  onClick={handleCopyClaim}
                  className="px-3.5 py-1.5 rounded-lg text-xs font-bold bg-[#C9A24A]/15 text-[#FFF1C4] border border-[#C9A24A]/30 hover:bg-[#C9A24A]/25 transition-all flex items-center gap-1.5"
                >
                  {claimCopied ? <Check className="w-3.5 h-3.5" /> : <Copy className="w-3.5 h-3.5" />}
                  <span>{claimCopied ? 'Copied' : 'Copy'}</span>
                </button>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="text-[10px] font-bold text-[#A39A8B] uppercase block mb-1">
                    Date
                  </label>
                  <input
                    type="date"
                    value={claimDate}
                    onChange={(e) => setClaimDate(e.target.value)}
                    className="w-full bg-[#0E0C09] border border-white/10 rounded-lg px-2.5 py-1.5 text-xs text-white outline-none"
                  />
                </div>
                <div>
                  <label className="text-[10px] font-bold text-[#A39A8B] uppercase block mb-1">
                    Cards Claimed
                  </label>
                  <input
                    type="number"
                    value={claimCount}
                    onChange={(e) => setClaimCount(e.target.value)}
                    className="w-full bg-[#0E0C09] border border-white/10 rounded-lg px-2.5 py-1.5 text-xs text-white font-mono outline-none"
                  />
                </div>
              </div>

              <pre className="p-3.5 rounded-xl bg-[#090806] border border-white/10 font-mono text-[11px] text-white/90 leading-relaxed whitespace-pre-wrap select-all">
                {cardClaimString}
              </pre>
            </div>
          </div>
        </div>

        {/* All 24 Stations TOM Closing Table */}
        <div className="metro-card p-6 space-y-4">
          <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
            <div>
              <h3 className="text-base font-bold text-white flex items-center gap-2">
                <TrainTrack className="w-4 h-4 text-[#C9A24A]" />
                <span>All 24 Stations TOM Closing Registry</span>
              </h3>
              <p className="text-xs text-[#A39A8B] mt-0.5">
                Red Line rawalpindi to islamabad corridor records
              </p>
            </div>

            <div className="relative w-full sm:w-64">
              <Search className="w-4 h-4 text-[#A39A8B] absolute left-3 top-1/2 -translate-y-1/2" />
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                placeholder="Search station..."
                className="w-full pl-9 pr-3 py-1.5 rounded-xl bg-[#0E0C09] border border-white/10 text-xs text-white placeholder-[#6E6658] outline-none focus:border-[#C9A24A]"
              />
            </div>
          </div>

          <div className="overflow-x-auto rounded-xl border border-white/5">
            <table className="w-full text-left text-xs">
              <thead className="bg-[#0E0C09] text-[#A39A8B] font-bold uppercase tracking-wider text-[10px] border-b border-white/10">
                <tr>
                  <th className="py-3 px-4">#</th>
                  <th className="py-3 px-4">Station Name</th>
                  <th className="py-3 px-4">Zone</th>
                  <th className="py-3 px-4 text-right">Closing Sale (Rs)</th>
                  <th className="py-3 px-4 text-center">Rolls Used</th>
                  <th className="py-3 px-4 text-center">Status</th>
                  <th className="py-3 px-4 text-right">Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-white/5">
                {filteredStations.map((st) => (
                  <tr 
                    key={st.number} 
                    className={`hover:bg-white/[0.02] transition-colors ${
                      st.number === selectedStation.number ? 'bg-[#C9A24A]/5' : ''
                    }`}
                  >
                    <td className="py-3 px-4 font-mono font-bold text-zinc-500">
                      {st.number}
                    </td>
                    <td className="py-3 px-4">
                      <div className="font-bold text-white">{st.name}</div>
                      <div className="text-[10px] font-mono text-[#A39A8B]">{st.code}</div>
                    </td>
                    <td className="py-3 px-4">
                      <span className={`text-[10px] font-bold px-2 py-0.5 rounded-full ${
                        st.zone === 'rawalpindi' 
                          ? 'bg-[#C9A24A]/10 text-[#FFF1C4] border border-[#C9A24A]/20' 
                          : 'bg-[#55D88D]/10 text-[#55D88D] border border-[#55D88D]/20'
                      }`}>
                        {st.zone === 'rawalpindi' ? 'Rawalpindi' : 'Islamabad'}
                      </span>
                    </td>
                    <td className="py-3 px-4 text-right font-mono font-black text-white">
                      Rs {(st.sales || 0).toLocaleString()}
                    </td>
                    <td className="py-3 px-4 text-center font-mono font-bold text-[#FFF1C4]">
                      {st.rollsUsed || 1}
                    </td>
                    <td className="py-3 px-4 text-center">
                      <span className="inline-flex items-center gap-1 text-[10px] text-[#55D88D] font-bold">
                        <CheckCircle2 className="w-3 h-3" />
                        <span>Synced</span>
                      </span>
                    </td>
                    <td className="py-3 px-4 text-right">
                      <button
                        onClick={() => {
                          setSelectedStation(st);
                          showToast(`Selected ${st.name} for editing`);
                        }}
                        className="px-2.5 py-1 rounded bg-white/5 hover:bg-[#C9A24A] hover:text-[#0B0B0C] text-zinc-300 font-bold transition-all text-[11px]"
                      >
                        Edit TOM
                      </button>
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
