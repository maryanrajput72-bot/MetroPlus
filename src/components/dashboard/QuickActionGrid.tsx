import React from 'react';
import { Link } from 'react-router-dom';
import { 
  ShoppingBag, 
  ShieldCheck, 
  CreditCard, 
  Package, 
  Boxes, 
  Users, 
  ArrowRight,
  Sparkles
} from 'lucide-react';
import { useAdmin } from '../../context/AdminContext';

export const QuickActionGrid: React.FC = () => {
  const { stats } = useAdmin();

  const actions = [
    {
      title: 'Process & Ship',
      kicker: 'Orders Desk',
      sub: `${stats.pendingOrders} pending fulfillment`,
      href: '/orders',
      icon: ShoppingBag,
      color: '#4E7BD8',
      badge: stats.pendingOrders > 0 ? `${stats.pendingOrders} wait` : 'Clean'
    },
    {
      title: 'Verify Sellers',
      kicker: 'KYC Desk',
      sub: `${stats.pendingKYCCount} identity audits pending`,
      href: '/kyc',
      icon: ShieldCheck,
      color: '#C9A24A',
      badge: stats.pendingKYCCount > 0 ? `${stats.pendingKYCCount} req` : 'Clear'
    },
    {
      title: 'Approve Payouts',
      kicker: 'Withdrawals Desk',
      sub: `$${stats.pendingWithdrawalsAmount.toLocaleString()} awaiting release`,
      href: '/withdrawals',
      icon: CreditCard,
      color: '#2F9A63',
      badge: stats.pendingWithdrawalsAmount > 0 ? `$${stats.pendingWithdrawalsAmount.toLocaleString()}` : '0'
    },
    {
      title: 'Traffic & Plans',
      kicker: 'Store VIP Upgrades',
      sub: 'Review VIP merchant subscriptions',
      href: '/packages',
      icon: Boxes,
      color: '#8E6BD0'
    },
    {
      title: 'Catalogue Manager',
      kicker: 'Dropship Items',
      sub: `${stats.totalProducts} active products in stock`,
      href: '/products',
      icon: Package,
      color: '#E0A42E'
    },
    {
      title: 'Manage Users',
      kicker: 'Directory & Wallets',
      sub: `${stats.activeUsersCount} verified merchants online`,
      href: '/users',
      icon: Users,
      color: '#55D88D'
    }
  ];

  return (
    <div className="metro-card p-6">
      <div className="flex items-center justify-between pb-4 border-b border-[#C9A24A]/15">
        <div>
          <span className="text-[10px] font-bold tracking-[0.2em] text-[#C9A24A] uppercase flex items-center gap-1.5">
            <Sparkles className="w-3.5 h-3.5 text-[#C9A24A]" />
            EXECUTIVE COMMAND TILES
          </span>
          <h3 className="text-lg font-bold text-[#FFFDF8] mt-1 font-['Cinzel',serif]">
            Direct Operation Shortcuts
          </h3>
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3.5 mt-4">
        {actions.map((act, i) => {
          const Icon = act.icon;
          return (
            <Link
              key={i}
              to={act.href}
              className="group relative p-4 rounded-xl bg-[#0F0D0A] border border-[#C9A24A]/20 hover:border-[#C9A24A]/60 transition-all duration-200 hover:-translate-y-1 hover:shadow-xl hover:shadow-black/70 flex flex-col justify-between"
            >
              <div>
                <div className="flex items-center justify-between mb-2">
                  <div
                    className="w-9 h-9 rounded-lg flex items-center justify-center transition-transform group-hover:scale-110"
                    style={{ backgroundColor: `${act.color}15`, color: act.color }}
                  >
                    <Icon className="w-4 h-4" />
                  </div>
                  {act.badge && (
                    <span className="text-[10px] font-bold px-2 py-0.5 rounded-full bg-white/5 border border-white/10 text-white font-mono">
                      {act.badge}
                    </span>
                  )}
                </div>

                <div className="text-[10px] font-bold uppercase tracking-wider text-[#C9A24A]">
                  {act.kicker}
                </div>
                <div className="text-sm font-bold text-white group-hover:text-[#FFF1C4] transition-colors mt-0.5">
                  {act.title}
                </div>
                <div className="text-xs text-[#A39A8B] mt-1 line-clamp-1">
                  {act.sub}
                </div>
              </div>

              <div className="flex items-center gap-1 text-[11px] font-bold text-[#C9A24A] mt-3 group-hover:translate-x-1 transition-transform">
                <span>Access Console</span>
                <ArrowRight className="w-3.5 h-3.5" />
              </div>
            </Link>
          );
        })}
      </div>
    </div>
  );
};
