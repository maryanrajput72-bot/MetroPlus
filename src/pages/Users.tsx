import React, { useState } from 'react';
import { useAdmin } from '../context/AdminContext';
import { AdminLayout } from '../components/layout/AdminLayout';
import { Badge } from '../components/common/Badge';
import { Modal } from '../components/common/Modal';
import { User } from '../types';
import { 
  Users as UsersIcon, 
  Search, 
  DollarSign, 
  ShieldAlert, 
  CheckCircle, 
  Crown, 
  Plus, 
  Minus,
  MapPin
} from 'lucide-react';

export const Users: React.FC = () => {
  const { users, formatCurrency, toggleUserStatus, adjustUserBalance } = useAdmin();
  const [search, setSearch] = useState<string>('');
  const [selectedUser, setSelectedUser] = useState<User | null>(null);
  const [balanceAmount, setBalanceAmount] = useState<string>('');
  const [adjustType, setAdjustType] = useState<'credit' | 'debit'>('credit');

  const filtered = users.filter((u) =>
    u.name.toLowerCase().includes(search.toLowerCase()) ||
    u.email.toLowerCase().includes(search.toLowerCase()) ||
    u.city.toLowerCase().includes(search.toLowerCase()) ||
    u.country.toLowerCase().includes(search.toLowerCase())
  );

  const handleSaveBalance = () => {
    if (!selectedUser) return;
    const val = parseFloat(balanceAmount);
    if (isNaN(val) || val <= 0) return alert('Please enter a valid amount.');

    const signedAmount = adjustType === 'credit' ? val : -val;
    adjustUserBalance(selectedUser.id, signedAmount);
    setSelectedUser(null);
    setBalanceAmount('');
  };

  return (
    <AdminLayout title="Sellers & Merchants" subtitle="Manage merchant directory, permissions & store balances">
      <div className="space-y-6">
        {/* Controls */}
        <div className="metro-card p-4 sm:p-5 flex flex-col sm:flex-row items-center justify-between gap-4">
          <div className="flex items-center gap-2">
            <span className="text-xs font-bold text-[#A39A8B]">Directory Count:</span>
            <span className="text-xs font-bold text-[#C9A24A] bg-[#C9A24A]/10 px-2.5 py-1 rounded-lg border border-[#C9A24A]/25">
              {users.length} Registered Accounts
            </span>
          </div>

          <div className="relative w-full sm:w-72">
            <Search className="w-4 h-4 text-[#A39A8B] absolute left-3 top-1/2 -translate-y-1/2" />
            <input
              type="text"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="Search seller by name, city, email..."
              className="w-full bg-[#0E0C09] border border-[#C9A24A]/25 rounded-xl pl-9 pr-3 py-2 text-xs text-white placeholder-zinc-500 focus:outline-none focus:border-[#C9A24A]"
            />
          </div>
        </div>

        {/* Users Table */}
        <div className="metro-card p-6 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs">
              <thead>
                <tr className="border-b border-[#C9A24A]/15 text-[#6E6658] uppercase font-bold tracking-wider">
                  <th className="pb-3.5 pl-2">Merchant Name</th>
                  <th className="pb-3.5">Location & Contact</th>
                  <th className="pb-3.5 text-right">Wallet Balance</th>
                  <th className="pb-3.5 text-right">Total Orders</th>
                  <th className="pb-3.5 text-right">Net Profit</th>
                  <th className="pb-3.5 text-center">KYC Audit</th>
                  <th className="pb-3.5 text-center">Account Status</th>
                  <th className="pb-3.5 pr-2 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[#C9A24A]/10">
                {filtered.map((user) => (
                  <tr key={user.id} className="hover:bg-white/[0.02] transition-colors">
                    {/* User */}
                    <td className="py-4 pl-2">
                      <div className="flex items-center gap-3">
                        <div className="relative">
                          <img
                            src={user.avatar}
                            alt={user.name}
                            className="w-10 h-10 rounded-full object-cover border border-[#C9A24A]/30 flex-shrink-0"
                          />
                          {user.role === 'super_admin' && (
                            <Crown className="w-3.5 h-3.5 text-[#C9A24A] absolute -bottom-1 -right-1" />
                          )}
                        </div>
                        <div>
                          <div className="font-bold text-white text-sm">{user.name}</div>
                          <div className="text-[11px] text-[#A39A8B]">{user.email}</div>
                        </div>
                      </div>
                    </td>

                    {/* Location */}
                    <td className="py-4">
                      <div className="text-white font-medium flex items-center gap-1">
                        <MapPin className="w-3 h-3 text-[#C9A24A]" />
                        <span>{user.city}, {user.country}</span>
                      </div>
                      <div className="text-[10px] text-[#6E6658] font-mono">{user.phone}</div>
                    </td>

                    {/* Balance */}
                    <td className="py-4 text-right font-mono font-bold text-[#FFF1C4] text-sm">
                      {formatCurrency(user.balance)}
                    </td>

                    {/* Orders */}
                    <td className="py-4 text-right font-mono text-white">
                      {user.salesCount.toLocaleString()}
                    </td>

                    {/* Profit */}
                    <td className="py-4 text-right font-mono font-bold text-[#55D88D]">
                      {formatCurrency(user.totalProfit)}
                    </td>

                    {/* KYC */}
                    <td className="py-4 text-center">
                      <Badge status={user.kycStatus === 'verified' ? 'verified' : 'pending'} />
                    </td>

                    {/* Status */}
                    <td className="py-4 text-center">
                      <Badge status={user.status} />
                    </td>

                    {/* Actions */}
                    <td className="py-4 pr-2 text-right">
                      <div className="flex items-center justify-end gap-2">
                        <button
                          onClick={() => setSelectedUser(user)}
                          className="px-2.5 py-1 text-xs font-bold text-[#FFF1C4] bg-white/[0.04] border border-[#C9A24A]/30 rounded-lg hover:border-[#C9A24A] hover:bg-[#C9A24A]/10 transition-colors"
                          title="Adjust balance"
                        >
                          Balance ±
                        </button>

                        <button
                          onClick={() => toggleUserStatus(user.id)}
                          className={`px-2.5 py-1 text-xs font-bold rounded-lg border transition-colors ${
                            user.status === 'active'
                              ? 'bg-[#2E1412] text-[#F38A7C] border-[#592622] hover:bg-[#592622]'
                              : 'bg-[#122B1E] text-[#55D88D] border-[#1D4A33] hover:bg-[#1D4A33]'
                          }`}
                        >
                          {user.status === 'active' ? 'Suspend' : 'Activate'}
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

      {/* Adjust Balance Modal */}
      {selectedUser && (
        <Modal
          isOpen={!!selectedUser}
          onClose={() => setSelectedUser(null)}
          title={`Adjust Wallet: ${selectedUser.name}`}
          subtitle={`Current Balance: ${formatCurrency(selectedUser.balance)}`}
        >
          <div className="space-y-4">
            <div className="flex rounded-xl bg-white/[0.04] p-1 border border-white/10">
              <button
                onClick={() => setAdjustType('credit')}
                className={`flex-1 py-2 text-xs font-bold rounded-lg flex items-center justify-center gap-1.5 transition-all ${
                  adjustType === 'credit'
                    ? 'bg-[#122B1E] text-[#55D88D] border border-[#1D4A33]'
                    : 'text-[#A39A8B]'
                }`}
              >
                <Plus className="w-3.5 h-3.5" />
                <span>Credit Balance (Deposit)</span>
              </button>
              <button
                onClick={() => setAdjustType('debit')}
                className={`flex-1 py-2 text-xs font-bold rounded-lg flex items-center justify-center gap-1.5 transition-all ${
                  adjustType === 'debit'
                    ? 'bg-[#2E1412] text-[#F38A7C] border border-[#592622]'
                    : 'text-[#A39A8B]'
                }`}
              >
                <Minus className="w-3.5 h-3.5" />
                <span>Debit Balance (Deduct)</span>
              </button>
            </div>

            <div>
              <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1.5">
                Amount in USD ($)
              </label>
              <input
                type="number"
                step="0.01"
                value={balanceAmount}
                onChange={(e) => setBalanceAmount(e.target.value)}
                placeholder="e.g. 500.00"
                className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
              />
            </div>

            <div className="flex items-center justify-end gap-3 pt-3 border-t border-[#C9A24A]/15">
              <button
                onClick={() => setSelectedUser(null)}
                className="px-4 py-2 text-xs font-semibold text-zinc-400 hover:text-white"
              >
                Cancel
              </button>
              <button
                onClick={handleSaveBalance}
                className="px-5 py-2 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] rounded-xl transition-all shadow-md"
              >
                Apply Adjustment
              </button>
            </div>
          </div>
        </Modal>
      )}
    </AdminLayout>
  );
};
