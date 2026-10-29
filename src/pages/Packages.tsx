import React from 'react';
import { useAdmin } from '../context/AdminContext';
import { AdminLayout } from '../components/layout/AdminLayout';
import { Badge } from '../components/common/Badge';
import { Boxes, Crown, Sparkles, Check, X, ExternalLink, ShieldCheck } from 'lucide-react';

export const Packages: React.FC = () => {
  const { packages, formatCurrency, updatePackageStatus } = useAdmin();

  const tiers = [
    {
      name: 'VIP Diamond Merchant',
      price: 1499,
      duration: '12 Months',
      features: ['Unlimited Dropship Orders', 'Priority Express Air Freight', 'Zero Payout Fees', 'Dedicated Account Manager'],
      color: '#C9A24A'
    },
    {
      name: 'Gold Merchant',
      price: 799,
      duration: '6 Months',
      features: ['Up to 5,000 Orders/mo', '4.2-Day Fast Line Routing', '1% Low Payout Fee', '24/7 Priority Support'],
      color: '#E0A42E'
    },
    {
      name: 'Pro Dropshipper',
      price: 399,
      duration: '3 Months',
      features: ['Up to 1,500 Orders/mo', 'Standard Hub Access', 'Standard 2% Fee', 'Live Dispatch Radar'],
      color: '#4E7BD8'
    }
  ];

  return (
    <AdminLayout title="Traffic & VIP Plans" subtitle="Merchant subscription tiers and traffic package requests">
      <div className="space-y-6">
        {/* Tier Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-5">
          {tiers.map((tier, i) => (
            <div
              key={i}
              className="metro-card p-6 flex flex-col justify-between relative overflow-hidden"
            >
              <div>
                <div className="flex items-center justify-between">
                  <span className="text-[10px] font-extrabold tracking-widest text-[#C9A24A] uppercase">
                    TIER LEVEL {i + 1}
                  </span>
                  <Crown className="w-4 h-4 text-[#C9A24A]" />
                </div>

                <h3 className="text-lg font-bold text-white mt-2 font-['Cinzel',serif]">{tier.name}</h3>

                <div className="mt-4 flex items-baseline gap-1">
                  <span className="text-3xl font-black font-mono text-[#FFF1C4]">{formatCurrency(tier.price)}</span>
                  <span className="text-xs text-[#A39A8B]">/ {tier.duration}</span>
                </div>

                <div className="mt-5 space-y-2.5 pt-4 border-t border-white/10 text-xs">
                  {tier.features.map((feat, fIdx) => (
                    <div key={fIdx} className="flex items-center gap-2 text-[#FFFDF8]">
                      <Check className="w-3.5 h-3.5 text-[#55D88D] flex-shrink-0" />
                      <span>{feat}</span>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Requests Table */}
        <div className="metro-card p-6">
          <div className="pb-4 border-b border-[#C9A24A]/15">
            <h3 className="text-lg font-bold text-white font-['Cinzel',serif]">Merchant Subscription Requests</h3>
            <p className="text-xs text-[#A39A8B]">Review incoming plan upgrades and domain activations</p>
          </div>

          <div className="overflow-x-auto mt-4">
            <table className="w-full text-left text-xs">
              <thead>
                <tr className="border-b border-[#C9A24A]/15 text-[#6E6658] uppercase font-bold tracking-wider">
                  <th className="pb-3.5 pl-2">Store Owner</th>
                  <th className="pb-3.5">Requested Tier</th>
                  <th className="pb-3.5">Store URL</th>
                  <th className="pb-3.5 text-right">Fee Paid</th>
                  <th className="pb-3.5 text-center">Status</th>
                  <th className="pb-3.5 pr-2 text-right">Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[#C9A24A]/10">
                {packages.map((pkg) => (
                  <tr key={pkg.id} className="hover:bg-white/[0.02] transition-colors">
                    <td className="py-4 pl-2 font-bold text-white text-sm">
                      {pkg.userName}
                    </td>

                    <td className="py-4">
                      <span className="px-2.5 py-1 rounded-lg bg-[#C9A24A]/15 text-[#FFF1C4] border border-[#C9A24A]/30 font-bold">
                        {pkg.planName}
                      </span>
                    </td>

                    <td className="py-4 text-[#A39A8B] font-mono">
                      <a href={pkg.storeUrl} target="_blank" rel="noreferrer" className="hover:text-white flex items-center gap-1">
                        <span>{pkg.storeUrl}</span>
                        <ExternalLink className="w-3 h-3 text-[#C9A24A]" />
                      </a>
                    </td>

                    <td className="py-4 text-right font-mono font-bold text-[#55D88D] text-sm">
                      {formatCurrency(pkg.price)}
                    </td>

                    <td className="py-4 text-center">
                      <Badge status={pkg.status} />
                    </td>

                    <td className="py-4 pr-2 text-right">
                      {pkg.status === 'pending' ? (
                        <div className="flex items-center justify-end gap-2">
                          <button
                            onClick={() => updatePackageStatus(pkg.id, 'active')}
                            className="px-3 py-1.5 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] rounded-lg transition-colors flex items-center gap-1"
                          >
                            <Check className="w-3.5 h-3.5" />
                            <span>Activate</span>
                          </button>
                          <button
                            onClick={() => updatePackageStatus(pkg.id, 'rejected')}
                            className="p-1.5 rounded-lg bg-[#2E1412] text-[#F38A7C] border border-[#592622] hover:bg-[#592622]"
                          >
                            <X className="w-4 h-4" />
                          </button>
                        </div>
                      ) : (
                        <span className="text-zinc-500 font-mono text-[11px]">Plan Active</span>
                      )}
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
