import React, { useState } from 'react';
import { useAdmin } from '../context/AdminContext';
import { AdminLayout } from '../components/layout/AdminLayout';
import { Badge } from '../components/common/Badge';
import { Modal } from '../components/common/Modal';
import { Product } from '../types';
import { Package, Plus, Trash2, Edit, Search, DollarSign, Sparkles } from 'lucide-react';

export const Products: React.FC = () => {
  const { products, formatCurrency, addProduct, deleteProduct } = useAdmin();
  const [search, setSearch] = useState<string>('');
  const [isAddModalOpen, setIsAddModalOpen] = useState<boolean>(false);

  // Form states
  const [title, setTitle] = useState('');
  const [sku, setSku] = useState('');
  const [category, setCategory] = useState('Luxury Accessories');
  const [price, setPrice] = useState('');
  const [costPrice, setCostPrice] = useState('');
  const [stock, setStock] = useState('50');
  const [image, setImage] = useState('');
  const [supplier, setSupplier] = useState('');

  const filtered = products.filter((p) =>
    p.title.toLowerCase().includes(search.toLowerCase()) ||
    p.category.toLowerCase().includes(search.toLowerCase()) ||
    p.sku.toLowerCase().includes(search.toLowerCase())
  );

  const handleCreateProduct = (e: React.FormEvent) => {
    e.preventDefault();
    const sellPrice = parseFloat(price);
    const cost = parseFloat(costPrice);
    if (!title || isNaN(sellPrice) || isNaN(cost)) {
      return alert('Please fill in valid product details.');
    }

    const calculatedMargin = sellPrice - cost;
    const stockNum = parseInt(stock) || 0;
    const status = stockNum === 0 ? 'out_of_stock' : stockNum < 20 ? 'low_stock' : 'active';

    addProduct({
      sku: sku || `MTR-${Math.floor(1000 + Math.random() * 9000)}`,
      title,
      category,
      price: sellPrice,
      costPrice: cost,
      profitMargin: calculatedMargin,
      stock: stockNum,
      status,
      image: image || 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=300&auto=format&fit=crop&q=80',
      originSupplier: supplier || 'Shenzhen Dropship Network'
    });

    setIsAddModalOpen(false);
    // Reset form
    setTitle('');
    setSku('');
    setPrice('');
    setCostPrice('');
    setImage('');
    setSupplier('');
  };

  return (
    <AdminLayout title="Product Catalogue" subtitle="Manage dropshipping inventory, prices & profit margins">
      <div className="space-y-6">
        {/* Header Controls */}
        <div className="metro-card p-4 sm:p-5 flex flex-col sm:flex-row items-center justify-between gap-4">
          <div className="flex items-center gap-3">
            <span className="text-xs font-bold text-[#A39A8B]">Catalogue Size:</span>
            <span className="text-xs font-bold text-[#C9A24A] bg-[#C9A24A]/10 px-2.5 py-1 rounded-lg border border-[#C9A24A]/25">
              {products.length} Items Listed
            </span>
          </div>

          <div className="flex items-center gap-3 w-full sm:w-auto">
            <div className="relative flex-1 sm:w-64">
              <Search className="w-4 h-4 text-[#A39A8B] absolute left-3 top-1/2 -translate-y-1/2" />
              <input
                type="text"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                placeholder="Search products, SKU..."
                className="w-full bg-[#0E0C09] border border-[#C9A24A]/25 rounded-xl pl-9 pr-3 py-2 text-xs text-white placeholder-zinc-500 focus:outline-none focus:border-[#C9A24A]"
              />
            </div>

            <button
              onClick={() => setIsAddModalOpen(true)}
              className="px-4 py-2 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] rounded-xl transition-all shadow-md flex items-center gap-1.5 whitespace-nowrap"
            >
              <Plus className="w-4 h-4" />
              <span>Add Product</span>
            </button>
          </div>
        </div>

        {/* Products Table */}
        <div className="metro-card p-6 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs">
              <thead>
                <tr className="border-b border-[#C9A24A]/15 text-[#6E6658] uppercase font-bold tracking-wider">
                  <th className="pb-3.5 pl-2">Product Item & SKU</th>
                  <th className="pb-3.5">Category</th>
                  <th className="pb-3.5 text-right">Selling Price</th>
                  <th className="pb-3.5 text-right">Supplier Cost</th>
                  <th className="pb-3.5 text-right">Profit Margin</th>
                  <th className="pb-3.5 text-center">Stock</th>
                  <th className="pb-3.5 text-center">Status</th>
                  <th className="pb-3.5 pr-2 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[#C9A24A]/10">
                {filtered.map((prod) => (
                  <tr key={prod.id} className="hover:bg-white/[0.02] transition-colors">
                    {/* Item */}
                    <td className="py-4 pl-2">
                      <div className="flex items-center gap-3">
                        <img
                          src={prod.image}
                          alt={prod.title}
                          className="w-12 h-12 rounded-xl object-cover border border-[#C9A24A]/30 flex-shrink-0"
                        />
                        <div>
                          <div className="font-bold text-white text-sm line-clamp-1 max-w-[220px]">
                            {prod.title}
                          </div>
                          <div className="text-[10px] text-[#A39A8B] font-mono">
                            SKU: {prod.sku} • {prod.originSupplier}
                          </div>
                        </div>
                      </div>
                    </td>

                    {/* Category */}
                    <td className="py-4 font-medium text-[#FFFDF8]">
                      {prod.category}
                    </td>

                    {/* Price */}
                    <td className="py-4 text-right font-mono font-bold text-white text-sm">
                      {formatCurrency(prod.price)}
                    </td>

                    {/* Cost */}
                    <td className="py-4 text-right font-mono text-[#A39A8B]">
                      {formatCurrency(prod.costPrice)}
                    </td>

                    {/* Margin */}
                    <td className="py-4 text-right font-mono font-bold text-[#55D88D] text-sm">
                      +{formatCurrency(prod.profitMargin)}
                    </td>

                    {/* Stock */}
                    <td className="py-4 text-center font-mono font-bold text-white">
                      {prod.stock} units
                    </td>

                    {/* Status */}
                    <td className="py-4 text-center">
                      <Badge status={prod.status} />
                    </td>

                    {/* Action */}
                    <td className="py-4 pr-2 text-right">
                      <button
                        onClick={() => deleteProduct(prod.id)}
                        className="p-1.5 rounded-lg text-zinc-500 hover:text-red-400 hover:bg-white/5 transition-colors"
                        title="Delete product"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Add Product Modal */}
      {isAddModalOpen && (
        <Modal
          isOpen={isAddModalOpen}
          onClose={() => setIsAddModalOpen(false)}
          title="Add New Dropship Product"
          subtitle="List a new verified supplier product in the catalogue"
          maxWidth="xl"
        >
          <form onSubmit={handleCreateProduct} className="space-y-4">
            <div>
              <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                Product Title
              </label>
              <input
                type="text"
                required
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="e.g. Royal Chronograph Gold Edition"
                className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
              />
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                  SKU Code
                </label>
                <input
                  type="text"
                  value={sku}
                  onChange={(e) => setSku(e.target.value)}
                  placeholder="e.g. MTR-WATCH-GLD"
                  className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
                />
              </div>

              <div>
                <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                  Category
                </label>
                <select
                  value={category}
                  onChange={(e) => setCategory(e.target.value)}
                  className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
                >
                  <option value="Luxury Accessories">Luxury Accessories</option>
                  <option value="Consumer Electronics">Consumer Electronics</option>
                  <option value="Men Fashion">Men Fashion</option>
                  <option value="Home & Decor">Home & Decor</option>
                  <option value="Gaming & Peripherals">Gaming & Peripherals</option>
                </select>
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <div>
                <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                  Sell Price ($)
                </label>
                <input
                  type="number"
                  step="0.01"
                  required
                  value={price}
                  onChange={(e) => setPrice(e.target.value)}
                  placeholder="e.g. 489.00"
                  className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
                />
              </div>

              <div>
                <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                  Supplier Cost ($)
                </label>
                <input
                  type="number"
                  step="0.01"
                  required
                  value={costPrice}
                  onChange={(e) => setCostPrice(e.target.value)}
                  placeholder="e.g. 260.00"
                  className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
                />
              </div>

              <div>
                <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                  Stock Units
                </label>
                <input
                  type="number"
                  value={stock}
                  onChange={(e) => setStock(e.target.value)}
                  placeholder="e.g. 100"
                  className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
                />
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                  Image URL
                </label>
                <input
                  type="url"
                  value={image}
                  onChange={(e) => setImage(e.target.value)}
                  placeholder="https://images.unsplash.com/..."
                  className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
                />
              </div>

              <div>
                <label className="text-xs font-semibold text-[#A39A8B] uppercase block mb-1">
                  Supplier / Origin Hub
                </label>
                <input
                  type="text"
                  value={supplier}
                  onChange={(e) => setSupplier(e.target.value)}
                  placeholder="e.g. Shenzhen Precision Watch Works"
                  className="w-full bg-[#0E0C09] border border-[#C9A24A]/30 rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C9A24A]"
                />
              </div>
            </div>

            <div className="flex items-center justify-end gap-3 pt-4 border-t border-[#C9A24A]/15">
              <button
                type="button"
                onClick={() => setIsAddModalOpen(false)}
                className="px-4 py-2 text-xs font-semibold text-zinc-400 hover:text-white"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="px-5 py-2 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] rounded-xl transition-all shadow-md"
              >
                Add to Catalogue
              </button>
            </div>
          </form>
        </Modal>
      )}
    </AdminLayout>
  );
};
