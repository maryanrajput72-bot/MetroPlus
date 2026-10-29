export type OrderStatus = 'pending' | 'onway' | 'delivered' | 'completed' | 'cancelled';

export interface Order {
  id: string;
  orderNumber: string;
  productName: string;
  productImage: string;
  customerName: string;
  customerEmail: string;
  customerCountry: string;
  customerCity: string;
  orderAmount: number;
  commission: number;
  supplierCost: number;
  status: OrderStatus;
  trackingNumber: string;
  originHub: string;
  destination: string;
  createdAt: string;
  sellerId: string;
  sellerName: string;
}

export interface User {
  id: string;
  name: string;
  email: string;
  phone: string;
  role: 'super_admin' | 'manager' | 'seller' | 'customer';
  status: 'active' | 'suspended' | 'pending';
  balance: number;
  salesCount: number;
  totalRevenue: number;
  totalProfit: number;
  country: string;
  city: string;
  avatar: string;
  joinedAt: string;
  kycStatus: 'verified' | 'unverified' | 'pending' | 'rejected';
}

export interface KYCRequest {
  id: string;
  userId: string;
  userName: string;
  userEmail: string;
  documentType: 'National ID (CNIC)' | 'Passport' | 'Trade License';
  documentNumber: string;
  frontImage: string;
  backImage: string;
  submittedAt: string;
  status: 'pending' | 'approved' | 'rejected';
  rejectionReason?: string;
}

export interface WithdrawalRequest {
  id: string;
  userId: string;
  userName: string;
  amount: number;
  fee: number;
  netAmount: number;
  method: 'Bank Transfer' | 'USDT (TRC20)' | 'JazzCash' | 'EasyPaisa' | 'Wire';
  accountDetails: string;
  status: 'pending' | 'approved' | 'rejected';
  requestedAt: string;
  processedAt?: string;
  transactionRef?: string;
}

export interface Product {
  id: string;
  sku: string;
  title: string;
  category: string;
  price: number;
  costPrice: number;
  profitMargin: number;
  stock: number;
  status: 'active' | 'low_stock' | 'out_of_stock';
  image: string;
  totalSales: number;
  originSupplier: string;
}

export interface PackageRequest {
  id: string;
  userId: string;
  userName: string;
  planName: 'VIP Diamond' | 'Gold Merchant' | 'Pro Dropshipper' | 'Starter Boost';
  durationMonths: number;
  price: number;
  status: 'pending' | 'active' | 'rejected';
  requestedAt: string;
  storeUrl: string;
}

export interface LiveActivity {
  id: string;
  type: 'order_shipped' | 'new_seller' | 'withdrawal' | 'kyc_verified' | 'profit_earned';
  title: string;
  subtitle: string;
  amount?: number;
  timeAgo: string;
  location?: string;
}
