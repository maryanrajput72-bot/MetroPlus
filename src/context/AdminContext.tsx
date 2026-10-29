import React, { createContext, useContext, useState, useEffect } from 'react';
import { 
  Order, User, KYCRequest, WithdrawalRequest, Product, PackageRequest, 
  LiveActivity, OrderStatus 
} from '../types';
// NOTE: the live panel does not use this template context (App mounts only
// MetroProvider). Mock imports were removed; lists start empty.
const INITIAL_ORDERS: Order[] = [];
const INITIAL_USERS: User[] = [];
const INITIAL_KYC: KYCRequest[] = [];
const INITIAL_WITHDRAWALS: WithdrawalRequest[] = [];
const INITIAL_PRODUCTS: Product[] = [];
const INITIAL_PACKAGES: PackageRequest[] = [];
const LIVE_ACTIVITIES: LiveActivity[] = [];

export type Currency = 'USD' | 'PKR' | 'AED';

interface AdminContextType {
  orders: Order[];
  users: User[];
  kycList: KYCRequest[];
  withdrawals: WithdrawalRequest[];
  products: Product[];
  packages: PackageRequest[];
  activities: LiveActivity[];
  currency: Currency;
  setCurrency: (c: Currency) => void;
  formatCurrency: (val: number, options?: { showCents?: boolean }) => string;
  
  // Order actions
  updateOrderStatus: (orderId: string, status: OrderStatus, trackingNumber?: string) => void;
  
  // KYC actions
  approveKYC: (kycId: string) => void;
  rejectKYC: (kycId: string, reason: string) => void;
  
  // Withdrawal actions
  approveWithdrawal: (id: string, ref?: string) => void;
  rejectWithdrawal: (id: string) => void;
  
  // User actions
  toggleUserStatus: (userId: string) => void;
  adjustUserBalance: (userId: string, amount: number) => void;
  
  // Product actions
  addProduct: (product: Omit<Product, 'id' | 'totalSales'>) => void;
  updateProduct: (product: Product) => void;
  deleteProduct: (id: string) => void;
  
  // Package actions
  updatePackageStatus: (id: string, status: 'active' | 'rejected') => void;

  // Stats
  stats: {
    totalSales: number;
    totalOrders: number;
    pendingOrders: number;
    onWayOrders: number;
    completedOrders: number;
    totalCommission: number;
    totalProducts: number;
    activeUsersCount: number;
    pendingKYCCount: number;
    pendingWithdrawalsAmount: number;
  };

  // Toast
  toastMessage: string | null;
  showToast: (msg: string) => void;
}

const AdminContext = createContext<AdminContextType | undefined>(undefined);

const CURRENCY_RATES: Record<Currency, { symbol: string; rate: number; prefix: boolean }> = {
  USD: { symbol: '$', rate: 1, prefix: true },
  PKR: { symbol: 'Rs ', rate: 280, prefix: true },
  AED: { symbol: 'AED ', rate: 3.67, prefix: true }
};

export const AdminProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [orders, setOrders] = useState<Order[]>(() => {
    const saved = localStorage.getItem('metro_orders');
    return saved ? JSON.parse(saved) : INITIAL_ORDERS;
  });

  const [users, setUsers] = useState<User[]>(() => {
    const saved = localStorage.getItem('metro_users');
    return saved ? JSON.parse(saved) : INITIAL_USERS;
  });

  const [kycList, setKycList] = useState<KYCRequest[]>(() => {
    const saved = localStorage.getItem('metro_kyc');
    return saved ? JSON.parse(saved) : INITIAL_KYC;
  });

  const [withdrawals, setWithdrawals] = useState<WithdrawalRequest[]>(() => {
    const saved = localStorage.getItem('metro_withdrawals');
    return saved ? JSON.parse(saved) : INITIAL_WITHDRAWALS;
  });

  const [products, setProducts] = useState<Product[]>(() => {
    const saved = localStorage.getItem('metro_products');
    return saved ? JSON.parse(saved) : INITIAL_PRODUCTS;
  });

  const [packages, setPackages] = useState<PackageRequest[]>(() => {
    const saved = localStorage.getItem('metro_packages');
    return saved ? JSON.parse(saved) : INITIAL_PACKAGES;
  });

  const [activities, setActivities] = useState<LiveActivity[]>(LIVE_ACTIVITIES);
  const [currency, setCurrency] = useState<Currency>('USD');
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  // Sync to localStorage
  useEffect(() => {
    localStorage.setItem('metro_orders', JSON.stringify(orders));
  }, [orders]);
  useEffect(() => {
    localStorage.setItem('metro_users', JSON.stringify(users));
  }, [users]);
  useEffect(() => {
    localStorage.setItem('metro_kyc', JSON.stringify(kycList));
  }, [kycList]);
  useEffect(() => {
    localStorage.setItem('metro_withdrawals', JSON.stringify(withdrawals));
  }, [withdrawals]);
  useEffect(() => {
    localStorage.setItem('metro_products', JSON.stringify(products));
  }, [products]);
  useEffect(() => {
    localStorage.setItem('metro_packages', JSON.stringify(packages));
  }, [packages]);

  const showToast = (msg: string) => {
    setToastMessage(msg);
    setTimeout(() => {
      setToastMessage((current) => (current === msg ? null : current));
    }, 4000);
  };

  const formatCurrency = (val: number, options = { showCents: true }) => {
    const conf = CURRENCY_RATES[currency];
    const converted = val * conf.rate;
    const formatted = converted.toLocaleString(undefined, {
      minimumFractionDigits: options.showCents ? 2 : 0,
      maximumFractionDigits: options.showCents ? 2 : 0
    });
    return `${conf.symbol}${formatted}`;
  };

  const updateOrderStatus = (orderId: string, status: OrderStatus, trackingNumber?: string) => {
    setOrders(prev => prev.map(o => {
      if (o.id === orderId) {
        return {
          ...o,
          status,
          trackingNumber: trackingNumber || o.trackingNumber
        };
      }
      return o;
    }));
    showToast(`Order status updated to "${status.toUpperCase()}"`);
  };

  const approveKYC = (kycId: string) => {
    const kyc = kycList.find(k => k.id === kycId);
    if (!kyc) return;

    setKycList(prev => prev.map(k => k.id === kycId ? { ...k, status: 'approved' } : k));
    setUsers(prev => prev.map(u => u.id === kyc.userId ? { ...u, kycStatus: 'verified', status: 'active' } : u));
    showToast(`KYC Approved for ${kyc.userName}`);
  };

  const rejectKYC = (kycId: string, reason: string) => {
    const kyc = kycList.find(k => k.id === kycId);
    if (!kyc) return;

    setKycList(prev => prev.map(k => k.id === kycId ? { ...k, status: 'rejected', rejectionReason: reason } : k));
    setUsers(prev => prev.map(u => u.id === kyc.userId ? { ...u, kycStatus: 'rejected' } : u));
    showToast(`KYC Rejected for ${kyc.userName}`);
  };

  const approveWithdrawal = (id: string, ref = `TXN-${Math.floor(100000 + Math.random() * 900000)}`) => {
    const wth = withdrawals.find(w => w.id === id);
    if (!wth) return;

    setWithdrawals(prev => prev.map(w => w.id === id ? {
      ...w,
      status: 'approved',
      processedAt: new Date().toISOString(),
      transactionRef: ref
    } : w));

    // Deduct user balance
    setUsers(prev => prev.map(u => u.id === wth.userId ? {
      ...u,
      balance: Math.max(0, u.balance - wth.amount)
    } : u));

    showToast(`Withdrawal of $${wth.amount.toLocaleString()} approved! Ref: ${ref}`);
  };

  const rejectWithdrawal = (id: string) => {
    setWithdrawals(prev => prev.map(w => w.id === id ? { ...w, status: 'rejected' } : w));
    showToast('Withdrawal request rejected.');
  };

  const toggleUserStatus = (userId: string) => {
    setUsers(prev => prev.map(u => {
      if (u.id === userId) {
        const next = u.status === 'active' ? 'suspended' : 'active';
        showToast(`User ${u.name} is now ${next.toUpperCase()}`);
        return { ...u, status: next };
      }
      return u;
    }));
  };

  const adjustUserBalance = (userId: string, amount: number) => {
    setUsers(prev => prev.map(u => {
      if (u.id === userId) {
        const newBal = Number((u.balance + amount).toFixed(2));
        showToast(`Balance adjusted for ${u.name}: $${newBal.toLocaleString()}`);
        return { ...u, balance: newBal };
      }
      return u;
    }));
  };

  const addProduct = (product: Omit<Product, 'id' | 'totalSales'>) => {
    const newProd: Product = {
      ...product,
      id: `prod-${Date.now().toString().slice(-4)}`,
      totalSales: 0
    };
    setProducts(prev => [newProd, ...prev]);
    showToast(`Product "${product.title}" added to catalogue!`);
  };

  const updateProduct = (product: Product) => {
    setProducts(prev => prev.map(p => p.id === product.id ? product : p));
    showToast(`Product "${product.title}" updated.`);
  };

  const deleteProduct = (id: string) => {
    setProducts(prev => prev.filter(p => p.id !== id));
    showToast('Product removed from catalogue.');
  };

  const updatePackageStatus = (id: string, status: 'active' | 'rejected') => {
    setPackages(prev => prev.map(p => p.id === id ? { ...p, status } : p));
    showToast(`Package status updated to ${status.toUpperCase()}`);
  };

  // Aggregated Stats
  const totalSales = orders.reduce((sum, o) => sum + (o.status !== 'cancelled' ? o.orderAmount : 0), 0);
  const totalCommission = orders.reduce((sum, o) => sum + (o.status !== 'cancelled' ? o.commission : 0), 0);
  const pendingOrders = orders.filter(o => o.status === 'pending').length;
  const onWayOrders = orders.filter(o => o.status === 'onway').length;
  const completedOrders = orders.filter(o => o.status === 'completed' || o.status === 'delivered').length;
  const pendingKYCCount = kycList.filter(k => k.status === 'pending').length;
  const pendingWithdrawalsAmount = withdrawals
    .filter(w => w.status === 'pending')
    .reduce((sum, w) => sum + w.amount, 0);

  const stats = {
    totalSales,
    totalOrders: orders.length,
    pendingOrders,
    onWayOrders,
    completedOrders,
    totalCommission,
    totalProducts: products.length,
    activeUsersCount: users.filter(u => u.status === 'active').length,
    pendingKYCCount,
    pendingWithdrawalsAmount
  };

  return (
    <AdminContext.Provider
      value={{
        orders,
        users,
        kycList,
        withdrawals,
        products,
        packages,
        activities,
        currency,
        setCurrency,
        formatCurrency,
        updateOrderStatus,
        approveKYC,
        rejectKYC,
        approveWithdrawal,
        rejectWithdrawal,
        toggleUserStatus,
        adjustUserBalance,
        addProduct,
        updateProduct,
        deleteProduct,
        updatePackageStatus,
        stats,
        toastMessage,
        showToast
      }}
    >
      {children}
    </AdminContext.Provider>
  );
};

export const useAdmin = () => {
  const ctx = useContext(AdminContext);
  if (!ctx) throw new Error('useAdmin must be used within an AdminProvider');
  return ctx;
};
