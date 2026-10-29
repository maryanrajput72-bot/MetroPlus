import React, { useState } from 'react';
import { useAdmin } from '../context/AdminContext';
import { AdminLayout } from '../components/layout/AdminLayout';
import { Badge } from '../components/common/Badge';
import { Modal } from '../components/common/Modal';
import { WithdrawalRequest } from '../types';
import { CreditCard, Check, X, ArrowUpRight, DollarSign, Wallet, Shield } from 'lucide-react';

export const Withdrawals: React.FC = () => {
  const { withdrawals, formatCurrency, approveWithdrawal, rejectWithdrawal } = useAdmin();
  const [selectedWth, setSelectedWth] = useState<WithdrawalRequest | null>(null);
  const [txnRef, setTxnRef] = useState<string>('');

  const pendingAmount = withdrawals
    .filter((w) => w.status === 'pending')
    .reduce((sum, w) => sum + w.amount, 0);

  const completedAmount = withdrawals
    .filter((w) => w.status === 'approved')
    .reduce((sum, w) => sum + w.amount, 0);

  const handleOpenApprove = (w: WithdrawalRequest) => {
    setSelectedWth(w);
    setTxnRef(`TXN-${Math.floor(100000 + Math.random() * 900000)}`);
  };

  const handleConfirmApprove = () => {
    if (!selectedWth) return;
    approveWithdrawal(selectedWth.id, txnRef);
    setSelectedWth(null);
  };

  return (
    <AdminLayout title="Payouts & Withdrawals" subtitle="Manage seller wallet withdrawals and payment transfers">
      <div className="space-y-6">
        {/* Metric Cards */}
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
          <div className="metro-card p-5">
            <span className="text-[10px] uppercase font-bold text-[#C9A24A] tracking-wider">Awaiting Settlement</span>
            <div className="text-2xl font-black text-white font-mono mt-1">
              {formatCurrency(pendingAmount)}
            </div>
            <div className="text-xs text-[#A39A8B] mt-1">
              {withdrawals.filter((w) => w.status === 'pending').length} pending transfer requests
            </div>
          </div>

          <div className="metro-card p-5">
            <span className="text-[10px] uppercase font-bold text-[#55D88D] tracking-wider">Total Disbursed</span>
            <div className="text-2xl font-black text-white font-mono mt-1">
              {formatCurrency(completedAmount)}
            </div>
            <div className="text-xs text-[#A39A8B] mt-1">Successfully transferred to sellers</div>
          </div>

          <div className="metro-card p-5">
            <span className="text-[10px] uppercase font-bold text-[#4E7BD8] tracking-wider">Gateway Fee Margin</span>
            <div className="text-2xl font-black text-white font-mono mt-1">
              {formatCurrency(withdrawals.reduce((sum, w) => sum + w.fee, 0))}
            </div>
            <div className="text-xs text-[#A39A8B] mt-1">Platform transaction fee retained</div>
          </div>
        </div>

        {/* Withdrawals List */}
        <div className="metro-card p-6">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs">
              <thead>
                <tr className="border-b border-[#C9A24A]/15 text-[#6E6658] uppercase font-bold tracking-wider">
                  <th className="pb-3.5 pl-2">Merchant Name</th>
                  <th className="pb-3.5">Method</th>
                  <th className="pb-3.5">Account / Wallet Details</th>
                  <th className="pb-3.5 text-right">Gross Amount</th>
                  <th className="pb-3.5 text-right">Net Payout</th>
                  <th className="pb-3.5 text-center">Status</th>
                  <th className="pb-3.5 pr-2 text-right">Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[#C9A24A]/10">
                {withdrawals.map((w) => (
                  <tr key={w.id} className="hover:bg-white/[0.02] transition-colors">
                    <td className="py-4 pl-2">
                      <div className="font-bold text-white text-sm">{w.userName}</div>
                      <div className="text-[10px] text-[#A39A8B]">
                        {new Date(w.requestedAt).toLocaleString([], { dateStyle: 'short', timeStyle: 'short' })}
                      </div>
                    </td>

                    <td className="py-4">
                      <span className="px-2 py-0.5 rounded-lg bg-white/5 border border-white/10 text-white font-semibold">
                        {w.method}
                      </span>
                    </td>

                    <td className="py-4 font-mono text-[#FFF1C4] max-w-[280px] truncate">
                      {w.accountDetails}
                    </td>

                    <td className="py-4 text-right font-mono font-bold text-[#A39A8B]">
                      {formatCurrency(w.amount)}
                    </td>

                    <td className="py-4 text-right font-mono font-bold text-[#55D88D] text-sm">
                      {formatCurrency(w.netAmount)}
                    </td>

                    <td className="py-4 text-center">
                      <Badge status={w.status} />
                    </td>

                    <td className="py-4 pr-2 text-right">
                      {w.status === 'pending' ? (
                        <div className="flex items-center justify-end gap-2">
                          <button
                            onClick={() => handleOpenApprove(w)}
                            className="px-3 py-1.5 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] rounded-lg transition-colors flex items-center gap-1 shadow"
                          >
                            <Check className="w-3.5 h-3.5" />
                            <span>Approve</span>
                          </button>
                          <button
                            onClick={() => rejectWithdrawal(w.id)}
                            className="p-1.5 rounded-lg bg-[#2E1412] text-[#F38A7C] border border-[#592622] hover:bg-[#592622] transition-colors"
                            title="Reject"
                          >
                            <X className="w-4 h-4" />
                          </button>
                        </div>
                      ) : (
                        <span className="text-[10.5px] font-mono text-[#6E6658]">
                          {w.transactionRef || 'Settled'}
                        </span>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Approve Modal */}
      {selectedWth && (
        <Modal
          isOpen={!!selectedWth}
          onClose={() => setSelectedWth(null)}
          title={`Approve Payout: ${selectedWth.userName}`}
          subtitle={`Net Amount: ${formatCurrency(selectedWth.netAmount)}`}
        >
          <div className="space-y-4">
            <div className="p-3.5 rounded-xl bg-white/[0.02] border border-white/5 space-y-2 text-xs">
              <div className="flex justify-between text-[#A39A8B]">
                <span>Payment Method:</span>
                <span className="text-white font-bold">{selectedWth.method}</span>
              </div>
              <div className="flex justify-between text-[#A39A8B]">
                <span>Destination Account:</span>
                <span className="text-[#FFF1C4] font-mono font-bold">{selectedWth.accountDetails}</span>
              </div>
              <div className="flex justify-between text-[#A39A8B]">
                <span>Platform Handling Fee (1%):</span>
                <span className="text-zinc-400 font-mono">-{formatCurrency(selectedWth.fee)}</span>
              </div>
              <div className="flex justify-between text-[#A39A8B] pt-2 border-t border-white/10">
                <span>Total Disbursed:</span>
                <span className="text-[#55D88D] font-bold font-mono text-sm">{formatCurrency(selectedWth.netAmount)}</span>
              </div>
            </div>

            <div>
              <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1.5">
                Bank / Gateway Transaction Reference #
              </label>
              <input
                type="text"
                value={txnRef}
                onChange={(e) => setTxnRef(e.target.value)}
                placeholder="e.g. TXN-894210 or Bank Reference"
                className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
              />
            </div>

            <div className="flex items-center justify-end gap-3 pt-3 border-t border-[#C9A24A]/15">
              <button
                onClick={() => setSelectedWth(null)}
                className="px-4 py-2 text-xs font-semibold text-zinc-400 hover:text-white"
              >
                Cancel
              </button>
              <button
                onClick={handleConfirmApprove}
                className="px-5 py-2 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] rounded-xl transition-all shadow-md"
              >
                Confirm Payout Release
              </button>
            </div>
          </div>
        </Modal>
      )}
    </AdminLayout>
  );
};
