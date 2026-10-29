import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useAdmin } from '../../context/AdminContext';
import { Badge } from '../common/Badge';
import { ShoppingBag, ArrowRight, ExternalLink, Truck, Check } from 'lucide-react';
import { Order } from '../../types';

export const RecentOrdersTable: React.FC = () => {
  const { orders, formatCurrency, updateOrderStatus } = useAdmin();
  const [selectedOrder, setSelectedOrder] = useState<Order | null>(null);

  const recent = orders.slice(0, 5);

  return (
    <div className="metro-card p-6">
      {/* Header */}
      <div className="flex items-center justify-between pb-4 border-b border-[#C9A24A]/15">
        <div>
          <span className="text-[10px] font-bold tracking-[0.2em] text-[#C9A24A] uppercase flex items-center gap-1.5">
            <ShoppingBag className="w-3.5 h-3.5 text-[#C9A24A]" />
            LIVE DISPATCH PIPELINE
          </span>
          <h3 className="text-lg font-bold text-[#FFFDF8] mt-1 font-['Cinzel',serif]">
            Recent Dropshipping Orders
          </h3>
        </div>

        <Link
          to="/orders"
          className="flex items-center gap-1.5 text-xs font-bold text-[#C9A24A] hover:text-[#FFF1C4] transition-colors"
        >
          <span>All Orders ({orders.length})</span>
          <ArrowRight className="w-3.5 h-3.5" />
        </Link>
      </div>

      {/* Orders Table */}
      <div className="overflow-x-auto mt-4">
        <table className="w-full text-left text-xs">
          <thead>
            <tr className="border-b border-[#C9A24A]/10 text-[#6E6658] uppercase font-bold tracking-wider">
              <th className="pb-3 pl-2">Product & ID</th>
              <th className="pb-3">Customer & Port</th>
              <th className="pb-3 text-right">Value</th>
              <th className="pb-3 text-right">Commission</th>
              <th className="pb-3 text-center">Status</th>
              <th className="pb-3 pr-2 text-right">Quick Action</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-[#C9A24A]/10">
            {recent.map((order) => (
              <tr key={order.id} className="hover:bg-white/[0.02] transition-colors group">
                {/* Product & ID */}
                <td className="py-3.5 pl-2">
                  <div className="flex items-center gap-3">
                    <img
                      src={order.productImage}
                      alt={order.productName}
                      className="w-10 h-10 rounded-xl object-cover border border-[#C9A24A]/30 flex-shrink-0"
                    />
                    <div>
                      <div className="font-bold text-white group-hover:text-[#FFF1C4] transition-colors line-clamp-1 max-w-[200px]">
                        {order.productName}
                      </div>
                      <div className="text-[10px] text-[#A39A8B] font-mono">
                        {order.orderNumber} • Hub: {order.originHub}
                      </div>
                    </div>
                  </div>
                </td>

                {/* Customer */}
                <td className="py-3.5">
                  <div className="font-semibold text-[#FFFDF8]">{order.customerName}</div>
                  <div className="text-[10px] text-[#6E6658]">{order.destination}</div>
                </td>

                {/* Value */}
                <td className="py-3.5 text-right font-mono font-bold text-white">
                  {formatCurrency(order.orderAmount)}
                </td>

                {/* Commission */}
                <td className="py-3.5 text-right font-mono font-bold text-[#55D88D]">
                  +{formatCurrency(order.commission)}
                </td>

                {/* Status */}
                <td className="py-3.5 text-center">
                  <Badge status={order.status} />
                </td>

                {/* Actions */}
                <td className="py-3.5 pr-2 text-right">
                  {order.status === 'pending' ? (
                    <button
                      onClick={() => updateOrderStatus(order.id, 'onway')}
                      className="inline-flex items-center gap-1 px-2.5 py-1 text-[11px] font-bold text-[#FFF1C4] bg-[#10213E] border border-[#1A386D] rounded-lg hover:border-[#4E7BD8] transition-colors"
                      title="Dispatch order now"
                    >
                      <Truck className="w-3 h-3 text-[#4E7BD8]" />
                      <span>Ship</span>
                    </button>
                  ) : order.status === 'onway' ? (
                    <button
                      onClick={() => updateOrderStatus(order.id, 'delivered')}
                      className="inline-flex items-center gap-1 px-2.5 py-1 text-[11px] font-bold text-[#FFF1C4] bg-[#241A3D] border border-[#3F2B6E] rounded-lg hover:border-[#8E6BD0] transition-colors"
                      title="Mark as delivered"
                    >
                      <Check className="w-3 h-3 text-[#8E6BD0]" />
                      <span>Deliver</span>
                    </button>
                  ) : (
                    <span className="text-[11px] text-[#6E6658] font-mono">Settled</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
