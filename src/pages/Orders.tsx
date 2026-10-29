import React, { useState } from 'react';
import { useAdmin } from '../context/AdminContext';
import { AdminLayout } from '../components/layout/AdminLayout';
import { Badge } from '../components/common/Badge';
import { Modal } from '../components/common/Modal';
import { Order, OrderStatus } from '../types';
import { 
  Search, 
  Filter, 
  Truck, 
  Check, 
  X, 
  ExternalLink, 
  Package, 
  MapPin, 
  Calendar,
  Clock
} from 'lucide-react';

export const Orders: React.FC = () => {
  const { orders, formatCurrency, updateOrderStatus } = useAdmin();
  const [filter, setFilter] = useState<string>('all');
  const [search, setSearch] = useState<string>('');
  const [activeOrder, setActiveOrder] = useState<Order | null>(null);
  const [trackingInput, setTrackingInput] = useState<string>('');
  const [statusInput, setStatusInput] = useState<OrderStatus>('pending');

  const filtered = orders.filter((o) => {
    const matchesFilter = filter === 'all' || o.status === filter;
    const matchesSearch = 
      o.orderNumber.toLowerCase().includes(search.toLowerCase()) ||
      o.productName.toLowerCase().includes(search.toLowerCase()) ||
      o.customerName.toLowerCase().includes(search.toLowerCase()) ||
      o.trackingNumber.toLowerCase().includes(search.toLowerCase());
    return matchesFilter && matchesSearch;
  });

  const handleOpenStatusModal = (order: Order) => {
    setActiveOrder(order);
    setStatusInput(order.status);
    setTrackingInput(order.trackingNumber);
  };

  const handleSaveStatus = () => {
    if (!activeOrder) return;
    updateOrderStatus(activeOrder.id, statusInput, trackingInput);
    setActiveOrder(null);
  };

  return (
    <AdminLayout title="Live Orders" subtitle="Manage and dispatch platform dropshipping orders">
      <div className="space-y-6">
        {/* Top Controls: Filter Pills & Search */}
        <div className="metro-card p-4 sm:p-5 flex flex-col md:flex-row items-stretch md:items-center justify-between gap-4">
          {/* Status Tabs */}
          <div className="flex items-center gap-1.5 overflow-x-auto pb-1 md:pb-0">
            {[
              { id: 'all', label: 'All Orders', count: orders.length },
              { id: 'pending', label: 'Pending', count: orders.filter(o => o.status === 'pending').length },
              { id: 'onway', label: 'On The Way', count: orders.filter(o => o.status === 'onway').length },
              { id: 'delivered', label: 'Delivered', count: orders.filter(o => o.status === 'delivered').length },
              { id: 'completed', label: 'Completed', count: orders.filter(o => o.status === 'completed').length }
            ].map((tab) => (
              <button
                key={tab.id}
                onClick={() => setFilter(tab.id)}
                className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all whitespace-nowrap flex items-center gap-1.5 ${
                  filter === tab.id
                    ? 'bg-[#C9A24A] text-[#0B0B0C] shadow-sm'
                    : 'bg-white/5 text-[#A39A8B] hover:text-white hover:bg-white/10'
                }`}
              >
                <span>{tab.label}</span>
                <span className={`text-[10px] px-1.5 py-0.2 rounded-full font-mono ${
                  filter === tab.id ? 'bg-[#0B0B0C]/20 text-[#0B0B0C]' : 'bg-black/40 text-zinc-400'
                }`}>
                  {tab.count}
                </span>
              </button>
            ))}
          </div>

          {/* Search Box */}
          <div className="relative w-full md:w-72">
            <Search className="w-4 h-4 text-[#A39A8B] absolute left-3 top-1/2 -translate-y-1/2" />
            <input
              type="text"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="Search ID, customer, product..."
              className="w-full bg-[#0E0C09] border border-[#C9A24A]/25 rounded-xl pl-9 pr-3 py-2 text-xs text-white placeholder-zinc-500 focus:outline-none focus:border-[#C9A24A]"
            />
          </div>
        </div>

        {/* Orders Table */}
        <div className="metro-card p-6 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs">
              <thead>
                <tr className="border-b border-[#C9A24A]/15 text-[#6E6658] uppercase font-bold tracking-wider">
                  <th className="pb-3.5 pl-2">Order & Product</th>
                  <th className="pb-3.5">Customer & Port</th>
                  <th className="pb-3.5">Seller Store</th>
                  <th className="pb-3.5 text-right">Order Amount</th>
                  <th className="pb-3.5 text-right">Commission</th>
                  <th className="pb-3.5 text-center">Status</th>
                  <th className="pb-3.5 pr-2 text-right">Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[#C9A24A]/10">
                {filtered.map((order) => (
                  <tr key={order.id} className="hover:bg-white/[0.02] transition-colors group">
                    {/* Order & Product */}
                    <td className="py-4 pl-2">
                      <div className="flex items-center gap-3">
                        <img
                          src={order.productImage}
                          alt={order.productName}
                          className="w-12 h-12 rounded-xl object-cover border border-[#C9A24A]/30 flex-shrink-0"
                        />
                        <div>
                          <div className="font-bold text-white group-hover:text-[#FFF1C4] transition-colors line-clamp-1 max-w-[220px]">
                            {order.productName}
                          </div>
                          <div className="text-[10px] text-[#C9A24A] font-mono mt-0.5">
                            {order.orderNumber}
                          </div>
                          <div className="text-[10px] text-[#A39A8B] font-mono">
                            Track: {order.trackingNumber}
                          </div>
                        </div>
                      </div>
                    </td>

                    {/* Customer */}
                    <td className="py-4">
                      <div className="font-semibold text-white">{order.customerName}</div>
                      <div className="text-[10.5px] text-[#6E6658] flex items-center gap-1 mt-0.5">
                        <MapPin className="w-3 h-3 text-[#C9A24A]" />
                        <span>{order.destination}</span>
                      </div>
                    </td>

                    {/* Seller */}
                    <td className="py-4">
                      <div className="font-medium text-[#FFFDF8]">{order.sellerName}</div>
                      <div className="text-[10px] text-[#6E6658]">Origin: {order.originHub}</div>
                    </td>

                    {/* Amount */}
                    <td className="py-4 text-right font-mono font-bold text-white">
                      {formatCurrency(order.orderAmount)}
                    </td>

                    {/* Commission */}
                    <td className="py-4 text-right font-mono font-bold text-[#55D88D]">
                      +{formatCurrency(order.commission)}
                    </td>

                    {/* Status */}
                    <td className="py-4 text-center">
                      <Badge status={order.status} />
                    </td>

                    {/* Action */}
                    <td className="py-4 pr-2 text-right">
                      <button
                        onClick={() => handleOpenStatusModal(order)}
                        className="px-3 py-1.5 text-xs font-bold text-[#FFF1C4] bg-white/[0.04] border border-[#C9A24A]/30 rounded-lg hover:border-[#C9A24A] hover:bg-[#C9A24A]/10 transition-colors"
                      >
                        Manage
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>

            {filtered.length === 0 && (
              <div className="py-12 text-center text-[#A39A8B]">
                <Package className="w-10 h-10 mx-auto text-[#6E6658] mb-2" />
                <p className="text-sm font-semibold">No orders found matching this filter.</p>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Status & Tracking Modal */}
      {activeOrder && (
        <Modal
          isOpen={!!activeOrder}
          onClose={() => setActiveOrder(null)}
          title={`Order ${activeOrder.orderNumber}`}
          subtitle={activeOrder.productName}
        >
          <div className="space-y-4">
            <div>
              <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1.5">
                Update Fulfillment Status
              </label>
              <select
                value={statusInput}
                onChange={(e) => setStatusInput(e.target.value as OrderStatus)}
                className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
              >
                <option value="pending">Pending (Awaiting Supplier Dispatch)</option>
                <option value="onway">On The Way (In International Transit)</option>
                <option value="delivered">Delivered (Arrived at Customer Address)</option>
                <option value="completed">Completed (Confirmed & Settled)</option>
                <option value="cancelled">Cancelled (Void Order)</option>
              </select>
            </div>

            <div>
              <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1.5">
                Tracking Number / Carrier Reference
              </label>
              <input
                type="text"
                value={trackingInput}
                onChange={(e) => setTrackingInput(e.target.value)}
                placeholder="e.g. TRK-DXB-984210"
                className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
              />
            </div>

            <div className="p-3 rounded-xl bg-white/[0.02] border border-white/5 text-xs space-y-1">
              <div className="flex justify-between text-[#A39A8B]">
                <span>Customer:</span>
                <span className="text-white font-medium">{activeOrder.customerName} ({activeOrder.destination})</span>
              </div>
              <div className="flex justify-between text-[#A39A8B]">
                <span>Supplier Cost:</span>
                <span className="text-white font-mono">{formatCurrency(activeOrder.supplierCost)}</span>
              </div>
              <div className="flex justify-between text-[#A39A8B]">
                <span>Total Amount:</span>
                <span className="text-[#FFF1C4] font-bold font-mono">{formatCurrency(activeOrder.orderAmount)}</span>
              </div>
            </div>

            <div className="flex items-center justify-end gap-3 pt-3 border-t border-[#C9A24A]/15">
              <button
                onClick={() => setActiveOrder(null)}
                className="px-4 py-2 text-xs font-semibold text-zinc-400 hover:text-white"
              >
                Cancel
              </button>
              <button
                onClick={handleSaveStatus}
                className="px-5 py-2 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] rounded-xl transition-all shadow-md"
              >
                Save Changes
              </button>
            </div>
          </div>
        </Modal>
      )}
    </AdminLayout>
  );
};
