import React from 'react';
import { useAdmin } from '../../context/AdminContext';
import { Crown, Sparkles, TrendingUp, ArrowRight, Award } from 'lucide-react';
import { Link } from 'react-router-dom';

export const TopSellersLeaderboard: React.FC = () => {
  const { users, formatCurrency } = useAdmin();

  // Sort sellers by total revenue or balance
  const sellers = users
    .filter((u) => u.role === 'seller')
    .sort((a, b) => b.balance - a.balance);

  const champion = sellers[0];
  const runnersUp = sellers.slice(1, 4);

  const maxBal = champion?.balance || 1;

  return (
    <div className="metro-card p-6 flex flex-col justify-between">
      {/* Header */}
      <div className="flex items-center justify-between pb-4 border-b border-[#C9A24A]/15">
        <div>
          <span className="text-[10px] font-bold tracking-[0.2em] text-[#C9A24A] uppercase flex items-center gap-1.5">
            <Crown className="w-3.5 h-3.5 text-[#C9A24A]" />
            MERCHANT CHAMPIONS
          </span>
          <h3 className="text-lg font-bold text-[#FFFDF8] mt-1 font-['Cinzel',serif]">
            Top Store Leaderboard
          </h3>
        </div>

        <Link
          to="/users"
          className="flex items-center gap-1 text-xs font-bold text-[#C9A24A] hover:text-[#FFF1C4] transition-colors"
        >
          <span>All Sellers</span>
          <ArrowRight className="w-3.5 h-3.5" />
        </Link>
      </div>

      {/* #1 Champion Spotlight */}
      {champion && (
        <div className="mt-4 p-4 rounded-xl bg-gradient-to-r from-[#2B2212] via-[#1A140A] to-[#120F0B] border border-[#C9A24A]/40 relative overflow-hidden shadow-lg shadow-black/60">
          <div className="absolute top-2 right-2 flex items-center gap-1 px-2 py-0.5 rounded-full bg-[#C9A24A] text-[#0B0B0C] font-extrabold text-[10px] tracking-wider uppercase">
            <Crown className="w-3 h-3" />
            <span>#1 Merchant</span>
          </div>

          <div className="flex items-center gap-3.5">
            <div className="relative flex-shrink-0">
              <div className="w-13 h-13 rounded-full bg-gradient-to-tr from-[#FFF1C4] via-[#C9A24A] to-[#8A6A1F] p-[2px]">
                <img
                  src={champion.avatar}
                  alt={champion.name}
                  className="w-12 h-12 rounded-full object-cover"
                />
              </div>
              <span className="absolute -bottom-1 -right-1 w-5 h-5 rounded-full bg-[#C9A24A] text-[#0B0B0C] flex items-center justify-center font-black text-[11px] shadow">
                1
              </span>
            </div>

            <div className="flex-1 min-w-0 pr-16">
              <div className="text-sm font-bold text-white truncate">{champion.name}</div>
              <div className="text-xs text-[#A39A8B] truncate">{champion.city}, {champion.country}</div>

              <div className="flex items-center gap-4 mt-2">
                <div>
                  <div className="text-[10px] text-[#A39A8B] uppercase">Balance</div>
                  <div className="text-sm font-extrabold font-mono text-[#FFF1C4]">
                    {formatCurrency(champion.balance)}
                  </div>
                </div>
                <div>
                  <div className="text-[10px] text-[#A39A8B] uppercase">Sales</div>
                  <div className="text-sm font-extrabold font-mono text-white">
                    {champion.salesCount.toLocaleString()} orders
                  </div>
                </div>
                <div>
                  <div className="text-[10px] text-[#A39A8B] uppercase">Net Profit</div>
                  <div className="text-sm font-extrabold font-mono text-[#55D88D]">
                    {formatCurrency(champion.totalProfit)}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Runner Ups List */}
      <div className="space-y-3 mt-4">
        {runnersUp.map((seller, index) => {
          const rank = index + 2;
          const ratio = (seller.balance / maxBal) * 100;
          return (
            <div
              key={seller.id}
              className="p-3 rounded-xl bg-white/[0.02] border border-white/5 hover:border-[#C9A24A]/30 transition-all flex items-center justify-between gap-3 text-xs"
            >
              <div className="flex items-center gap-3 min-w-0 flex-1">
                <span className="w-5 h-5 rounded-full bg-white/5 font-mono text-zinc-400 font-bold flex items-center justify-center flex-shrink-0 text-[10px]">
                  {rank}
                </span>

                <img
                  src={seller.avatar}
                  alt={seller.name}
                  className="w-8 h-8 rounded-full object-cover border border-[#C9A24A]/20 flex-shrink-0"
                />

                <div className="min-w-0 flex-1">
                  <div className="font-bold text-white truncate">{seller.name}</div>
                  {/* Progress Bar of Balance */}
                  <div className="w-full bg-white/5 rounded-full h-1.5 mt-1 overflow-hidden">
                    <div
                      className="bg-gradient-to-r from-[#C9A24A] to-[#FFF1C4] h-full rounded-full transition-all duration-500"
                      style={{ width: `${ratio}%` }}
                    />
                  </div>
                </div>
              </div>

              <div className="text-right flex-shrink-0">
                <div className="font-mono font-bold text-[#FFF1C4]">
                  {formatCurrency(seller.balance)}
                </div>
                <div className="text-[10px] text-[#6E6658]">
                  {seller.salesCount} fulfilled
                </div>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};
