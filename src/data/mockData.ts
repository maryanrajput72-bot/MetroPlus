import { Order, User, KYCRequest, WithdrawalRequest, Product, PackageRequest, LiveActivity } from '../types';

export const INITIAL_ORDERS: Order[] = [
  {
    id: 'ord-101',
    orderNumber: 'MT-94821',
    productName: 'Royal Chronograph Gold Edition',
    productImage: 'https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=300&auto=format&fit=crop&q=80',
    customerName: 'Zubair Al-Mansoor',
    customerEmail: 'zubair@dubaiholding.ae',
    customerCountry: 'United Arab Emirates',
    customerCity: 'Dubai',
    orderAmount: 489.00,
    commission: 73.35,
    supplierCost: 260.00,
    status: 'onway',
    trackingNumber: 'TRK-DXB-984210',
    originHub: 'Shenzhen Hub',
    destination: 'Dubai, UAE',
    createdAt: '2026-10-29T06:12:00Z',
    sellerId: 'usr-1',
    sellerName: 'Elite Prestige Stores'
  },
  {
    id: 'ord-102',
    orderNumber: 'MT-94820',
    productName: 'Wireless Noise Cancelling Earbuds Pro',
    productImage: 'https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=300&auto=format&fit=crop&q=80',
    customerName: 'Muhammad Hamza',
    customerEmail: 'hamza.dev@gmail.com',
    customerCountry: 'Pakistan',
    customerCity: 'Karachi',
    orderAmount: 145.50,
    commission: 21.80,
    supplierCost: 72.00,
    status: 'pending',
    trackingNumber: 'TRK-KHI-441029',
    originHub: 'Yiwu Logistics',
    destination: 'Karachi, Pakistan',
    createdAt: '2026-10-29T05:40:00Z',
    sellerId: 'usr-2',
    sellerName: 'Apex Cart Direct'
  },
  {
    id: 'ord-103',
    orderNumber: 'MT-94819',
    productName: 'Italian Calfskin Leather Wallet & Belt Set',
    productImage: 'https://images.unsplash.com/photo-1627123424574-724758594e93?w=300&auto=format&fit=crop&q=80',
    customerName: 'Arthur Pendelton',
    customerEmail: 'arthur.p@chelsea.co.uk',
    customerCountry: 'United Kingdom',
    customerCity: 'London',
    orderAmount: 220.00,
    commission: 33.00,
    supplierCost: 110.00,
    status: 'delivered',
    trackingNumber: 'TRK-LDN-662391',
    originHub: 'Istanbul Cargo',
    destination: 'London, UK',
    createdAt: '2026-10-28T22:15:00Z',
    sellerId: 'usr-1',
    sellerName: 'Elite Prestige Stores'
  },
  {
    id: 'ord-104',
    orderNumber: 'MT-94818',
    productName: 'Smart LED Ambient Aura Lamp',
    productImage: 'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=300&auto=format&fit=crop&q=80',
    customerName: 'Sarah Jenkins',
    customerEmail: 'sarah.j@manhattan-loft.com',
    customerCountry: 'United States',
    customerCity: 'New York',
    orderAmount: 185.00,
    commission: 27.75,
    supplierCost: 85.00,
    status: 'completed',
    trackingNumber: 'TRK-NYC-109283',
    originHub: 'Shenzhen Hub',
    destination: 'New York, USA',
    createdAt: '2026-10-28T18:45:00Z',
    sellerId: 'usr-3',
    sellerName: 'Luxe Living Co.'
  },
  {
    id: 'ord-105',
    orderNumber: 'MT-94817',
    productName: 'Nordic Titanium Mechanical Keyboard',
    productImage: 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=300&auto=format&fit=crop&q=80',
    customerName: 'Fahad Al-Saud',
    customerEmail: 'fahad@riyadhcapital.sa',
    customerCountry: 'Saudi Arabia',
    customerCity: 'Riyadh',
    orderAmount: 340.00,
    commission: 51.00,
    supplierCost: 175.00,
    status: 'onway',
    trackingNumber: 'TRK-RUH-883012',
    originHub: 'Dubai Airport Freezone',
    destination: 'Riyadh, Saudi Arabia',
    createdAt: '2026-10-28T15:20:00Z',
    sellerId: 'usr-4',
    sellerName: 'Gulf Express Traders'
  },
  {
    id: 'ord-106',
    orderNumber: 'MT-94816',
    productName: 'Ceramic Pour-Over Coffee Station',
    productImage: 'https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=300&auto=format&fit=crop&q=80',
    customerName: 'Usman Ghani',
    customerEmail: 'usman.g@lahorebazaar.pk',
    customerCountry: 'Pakistan',
    customerCity: 'Lahore',
    orderAmount: 98.00,
    commission: 14.70,
    supplierCost: 45.00,
    status: 'completed',
    trackingNumber: 'TRK-LHE-771920',
    originHub: 'Yiwu Logistics',
    destination: 'Lahore, Pakistan',
    createdAt: '2026-10-28T11:05:00Z',
    sellerId: 'usr-2',
    sellerName: 'Apex Cart Direct'
  }
];

export const INITIAL_USERS: User[] = [
  {
    id: 'usr-1',
    name: 'Syed Taimoor Shah',
    email: 'taimoor@eliteprestige.com',
    phone: '+92 300 8472910',
    role: 'seller',
    status: 'active',
    balance: 14820.50,
    salesCount: 1420,
    totalRevenue: 284500.00,
    totalProfit: 42675.00,
    country: 'Pakistan',
    city: 'Islamabad',
    avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&auto=format&fit=crop&q=80',
    joinedAt: '2025-08-14',
    kycStatus: 'verified'
  },
  {
    id: 'usr-2',
    name: 'Rashid Khan',
    email: 'rashid@apexcart.com',
    phone: '+92 321 4455667',
    role: 'seller',
    status: 'active',
    balance: 8940.00,
    salesCount: 890,
    totalRevenue: 142000.00,
    totalProfit: 21300.00,
    country: 'Pakistan',
    city: 'Lahore',
    avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&auto=format&fit=crop&q=80',
    joinedAt: '2025-11-20',
    kycStatus: 'verified'
  },
  {
    id: 'usr-3',
    name: 'Elena Rostova',
    email: 'elena@luxeliving.co',
    phone: '+1 212 555 0198',
    role: 'seller',
    status: 'active',
    balance: 6420.25,
    salesCount: 610,
    totalRevenue: 98400.00,
    totalProfit: 14760.00,
    country: 'United States',
    city: 'New York',
    avatar: 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=200&auto=format&fit=crop&q=80',
    joinedAt: '2026-01-10',
    kycStatus: 'verified'
  },
  {
    id: 'usr-4',
    name: 'Tariq Al-Hashemi',
    email: 'tariq@gulfexpress.ae',
    phone: '+971 50 123 4567',
    role: 'seller',
    status: 'active',
    balance: 11250.00,
    salesCount: 980,
    totalRevenue: 215000.00,
    totalProfit: 32250.00,
    country: 'United Arab Emirates',
    city: 'Dubai',
    avatar: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200&auto=format&fit=crop&q=80',
    joinedAt: '2025-09-02',
    kycStatus: 'verified'
  },
  {
    id: 'usr-5',
    name: 'Bilal Ahmed',
    email: 'bilal@fastretail.pk',
    phone: '+92 333 9988771',
    role: 'seller',
    status: 'pending',
    balance: 0.00,
    salesCount: 0,
    totalRevenue: 0.00,
    totalProfit: 0.00,
    country: 'Pakistan',
    city: 'Karachi',
    avatar: 'https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=200&auto=format&fit=crop&q=80',
    joinedAt: '2026-10-27',
    kycStatus: 'pending'
  }
];

export const INITIAL_KYC: KYCRequest[] = [
  {
    id: 'kyc-01',
    userId: 'usr-5',
    userName: 'Bilal Ahmed',
    userEmail: 'bilal@fastretail.pk',
    documentType: 'National ID (CNIC)',
    documentNumber: '42101-9876543-1',
    frontImage: 'https://images.unsplash.com/photo-1589829545856-d10d557cf95f?w=600&auto=format&fit=crop&q=80',
    backImage: 'https://images.unsplash.com/photo-1589829545856-d10d557cf95f?w=600&auto=format&fit=crop&q=80',
    submittedAt: '2026-10-27T14:30:00Z',
    status: 'pending'
  },
  {
    id: 'kyc-02',
    userId: 'usr-6',
    userName: 'Zainab Mustafa',
    userEmail: 'zainab@velvetglam.store',
    documentType: 'Passport',
    documentNumber: 'PK-8849201',
    frontImage: 'https://images.unsplash.com/photo-1544717305-2782549b5136?w=600&auto=format&fit=crop&q=80',
    backImage: 'https://images.unsplash.com/photo-1544717305-2782549b5136?w=600&auto=format&fit=crop&q=80',
    submittedAt: '2026-10-28T09:15:00Z',
    status: 'pending'
  }
];

export const INITIAL_WITHDRAWALS: WithdrawalRequest[] = [
  {
    id: 'wth-01',
    userId: 'usr-1',
    userName: 'Syed Taimoor Shah',
    amount: 3500.00,
    fee: 35.00,
    netAmount: 3465.00,
    method: 'Bank Transfer',
    accountDetails: 'Meezan Bank Ltd | IBAN: PK55MEZN00010928392019',
    status: 'pending',
    requestedAt: '2026-10-29T04:10:00Z'
  },
  {
    id: 'wth-02',
    userId: 'usr-4',
    userName: 'Tariq Al-Hashemi',
    amount: 5000.00,
    fee: 25.00,
    netAmount: 4975.00,
    method: 'USDT (TRC20)',
    accountDetails: 'TRX7xP9qN28KjsL99vVb1m8Zx32Qy77Aop',
    status: 'pending',
    requestedAt: '2026-10-28T19:30:00Z'
  },
  {
    id: 'wth-03',
    userId: 'usr-2',
    userName: 'Rashid Khan',
    amount: 1800.00,
    fee: 18.00,
    netAmount: 1782.00,
    method: 'Bank Transfer',
    accountDetails: 'HBL Islamic Banking | IBAN: PK82HABB00928172938102',
    status: 'approved',
    requestedAt: '2026-10-27T10:00:00Z',
    processedAt: '2026-10-27T16:20:00Z',
    transactionRef: 'FT-99201948'
  }
];

export const INITIAL_PRODUCTS: Product[] = [
  {
    id: 'prod-01',
    sku: 'MTR-WATCH-GLD',
    title: 'Royal Chronograph Gold Edition',
    category: 'Luxury Accessories',
    price: 489.00,
    costPrice: 260.00,
    profitMargin: 229.00,
    stock: 142,
    status: 'active',
    image: 'https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=300&auto=format&fit=crop&q=80',
    totalSales: 488,
    originSupplier: 'Shenzhen Precision Watch Works'
  },
  {
    id: 'prod-02',
    sku: 'MTR-EAR-PRO',
    title: 'Wireless Noise Cancelling Earbuds Pro',
    category: 'Consumer Electronics',
    price: 145.50,
    costPrice: 72.00,
    profitMargin: 73.50,
    stock: 310,
    status: 'active',
    image: 'https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=300&auto=format&fit=crop&q=80',
    totalSales: 924,
    originSupplier: 'Yiwu Smart Audio Factory'
  },
  {
    id: 'prod-03',
    sku: 'MTR-LTHR-SET',
    title: 'Italian Calfskin Leather Wallet & Belt Set',
    category: 'Men Fashion',
    price: 220.00,
    costPrice: 110.00,
    profitMargin: 110.00,
    stock: 18,
    status: 'low_stock',
    image: 'https://images.unsplash.com/photo-1627123424574-724758594e93?w=300&auto=format&fit=crop&q=80',
    totalSales: 312,
    originSupplier: 'Istanbul Artisan Leather Guild'
  },
  {
    id: 'prod-04',
    sku: 'MTR-LAMP-AURA',
    title: 'Smart LED Ambient Aura Lamp',
    category: 'Home & Decor',
    price: 185.00,
    costPrice: 85.00,
    profitMargin: 100.00,
    stock: 84,
    status: 'active',
    image: 'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=300&auto=format&fit=crop&q=80',
    totalSales: 540,
    originSupplier: 'Guangdong Illumination Labs'
  },
  {
    id: 'prod-05',
    sku: 'MTR-MECH-KB',
    title: 'Nordic Titanium Mechanical Keyboard',
    category: 'Gaming & Peripherals',
    price: 340.00,
    costPrice: 175.00,
    profitMargin: 165.00,
    stock: 0,
    status: 'out_of_stock',
    image: 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=300&auto=format&fit=crop&q=80',
    totalSales: 210,
    originSupplier: 'Shenzhen KeyTech Precision'
  }
];

export const INITIAL_PACKAGES: PackageRequest[] = [
  {
    id: 'pkg-01',
    userId: 'usr-1',
    userName: 'Syed Taimoor Shah',
    planName: 'VIP Diamond',
    durationMonths: 12,
    price: 1499.00,
    status: 'active',
    requestedAt: '2026-10-15T12:00:00Z',
    storeUrl: 'https://eliteprestige.metrocart.io'
  },
  {
    id: 'pkg-02',
    userId: 'usr-2',
    userName: 'Rashid Khan',
    planName: 'Gold Merchant',
    durationMonths: 6,
    price: 799.00,
    status: 'pending',
    requestedAt: '2026-10-28T16:45:00Z',
    storeUrl: 'https://apexcart.metrocart.io'
  }
];

export const LIVE_ACTIVITIES: LiveActivity[] = [
  {
    id: 'act-1',
    type: 'order_shipped',
    title: 'Parcel in Flight',
    subtitle: 'Royal Chronograph Gold en route to Dubai, UAE',
    amount: 489.00,
    timeAgo: 'Just now',
    location: 'Shenzhen Hub -> Dubai'
  },
  {
    id: 'act-2',
    type: 'profit_earned',
    title: 'Commission Paid',
    subtitle: '$73.35 credited to Elite Prestige Stores',
    amount: 73.35,
    timeAgo: '4m ago'
  },
  {
    id: 'act-3',
    type: 'withdrawal',
    title: 'Payout Request',
    subtitle: 'Tariq Al-Hashemi requested $5,000 USDT',
    amount: 5000.00,
    timeAgo: '12m ago'
  },
  {
    id: 'act-4',
    type: 'new_seller',
    title: 'New Seller Joined',
    subtitle: 'Bilal Ahmed submitted KYC from Karachi',
    timeAgo: '28m ago'
  }
];
