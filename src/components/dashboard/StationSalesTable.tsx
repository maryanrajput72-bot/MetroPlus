import React, { useState } from 'react';
import { useMetro } from '../../context/MetroContext';
import { Modal } from '../common/Modal';
import { TrainTrack, DollarSign, Edit, Check, TrendingUp } from 'lucide-react';

export const StationSalesTable: React.FC = () => {
  const { stationSales, updateStationSales, totalNetworkSales } = useMetro();
  const [editingStation, setEditingStation] = useState<{ number: number; name: string; amount: number } | null>(null);
  const [newAmount, setNewAmount] = useState('');

  const handleOpenEdit = (station: { stationNumber: number; stationName: string; salesAmount: number }) => {
    setEditingStation({ number: station.stationNumber, name: station.stationName, amount: station.salesAmount });
    setNewAmount(String(station.salesAmount));
  };

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();
    if (!editingStation) return;
    const val = parseFloat(newAmount);
    if (isNaN(val) || val < 0) return alert('Please enter a valid amount');
    updateStationSales(editingStation.number, val);
    setEditingStation(null);
  };

  return (
    <div className="metro-card p-6">
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 pb-4 border-b border-[#C9A24A]/15">
        <div>
          <span className="text-[10px] font-bold tracking-[0.2em] text-[#C9A24A] uppercase flex items-center gap-1.5">
            <DollarSign className="w-3.5 h-3.5 text-[#55D88D]" />
            24 STATIONS COLLECTION BREAKDOWN
          </span>
          <h3 className="text-lg font-bold text-white mt-1 font-['Cinzel',serif]">
            Individual Station Ticket Sales & Collections
          </h3>
        </div>

        <div className="text-right">
          <span className="text-[10px] text-[#A39A8B] uppercase block">TOTAL NETWORK COLLECTION</span>
          <div className="text-xl font-mono font-black text-[#55D88D]">
            Rs {totalNetworkSales.toLocaleString()}
          </div>
        </div>
      </div>

      <div className="overflow-x-auto mt-4 max-h-[380px] overflow-y-auto">
        <table className="w-full text-left text-xs">
          <thead className="sticky top-0 bg-[#14110E] z-10">
            <tr className="border-b border-[#C9A24A]/15 text-[#6E6658] uppercase font-bold tracking-wider">
              <th className="pb-3 pl-2">#</th>
              <th className="pb-3">Station Name</th>
              <th className="pb-3">Code</th>
              <th className="pb-3 text-right">Daily Ticket Collection</th>
              <th className="pb-3 text-right">Estimated Passengers</th>
              <th className="pb-3 text-right">Network Share</th>
              <th className="pb-3 pr-2 text-right">Action</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-[#C9A24A]/10">
            {stationSales.map((item) => {
              const pct = ((item.salesAmount / (totalNetworkSales || 1)) * 100).toFixed(1);
              return (
                <tr key={item.stationNumber} className="hover:bg-white/[0.02] transition-colors">
                  <td className="py-3 pl-2 font-mono text-[#6E6658] font-bold">
                    #{item.stationNumber}
                  </td>

                  <td className="py-3 font-bold text-white flex items-center gap-2">
                    <TrainTrack className="w-3.5 h-3.5 text-[#C9A24A]" />
                    <span>{item.stationName}</span>
                  </td>

                  <td className="py-3 font-mono text-[#FFF1C4]">
                    {item.code}
                  </td>

                  <td className="py-3 text-right font-mono font-bold text-white text-sm">
                    Rs {item.salesAmount.toLocaleString()}
                  </td>

                  <td className="py-3 text-right font-mono text-[#A39A8B]">
                    {item.ticketCount.toLocaleString()} pax
                  </td>

                  <td className="py-3 text-right font-mono font-bold text-[#55D88D]">
                    {pct}%
                  </td>

                  <td className="py-3 pr-2 text-right">
                    <button
                      onClick={() => handleOpenEdit(item)}
                      className="px-2.5 py-1 text-[11px] font-bold text-[#FFF1C4] bg-white/[0.04] border border-[#C9A24A]/30 rounded-lg hover:border-[#C9A24A] hover:bg-[#C9A24A]/10 transition-colors flex items-center gap-1 ml-auto"
                    >
                      <Edit className="w-3 h-3 text-[#C9A24A]" />
                      <span>Edit Sale</span>
                    </button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>

      {/* Edit Station Sale Modal */}
      {editingStation && (
        <Modal
          isOpen={!!editingStation}
          onClose={() => setEditingStation(null)}
          title={`Edit Station Collection: ${editingStation.name}`}
          subtitle={`Station #${editingStation.number} Ticket Revenue`}
          maxWidth="sm"
        >
          <form onSubmit={handleSave} className="space-y-4">
            <div>
              <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                Total Collection Amount (PKR)
              </label>
              <input
                type="number"
                required
                value={newAmount}
                onChange={(e) => setNewAmount(e.target.value)}
                placeholder="e.g. 154200"
                className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white font-mono font-bold focus:outline-none focus:border-[#C9A24A]"
              />
            </div>

            <div className="flex items-center justify-end gap-3 pt-3 border-t border-[#C9A24A]/15">
              <button
                type="button"
                onClick={() => setEditingStation(null)}
                className="px-4 py-2 text-xs font-semibold text-zinc-400 hover:text-white"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="px-5 py-2 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] rounded-xl transition-all shadow-md"
              >
                Save Collection
              </button>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
};
