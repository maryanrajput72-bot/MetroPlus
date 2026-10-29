import { MetroStation } from '../types/metro';

export const METRO_STATIONS: MetroStation[] = [
  { number: 1, code: '1/201', name: 'Saddar', slug: 'saddar', zone: 'Rawalpindi', lat: 33.5936, lng: 73.0538 },
  { number: 2, code: '2/202', name: 'Marrir Chowk', slug: 'marrir_chowk', zone: 'Rawalpindi', lat: 33.5992, lng: 73.0612 },
  { number: 3, code: '3/203', name: 'Liaqat Bagh', slug: 'liaqat_bagh', zone: 'Rawalpindi', lat: 33.6063, lng: 73.0684 },
  { number: 4, code: '4/204', name: 'Committee Chowk', slug: 'committee_chowk', zone: 'Rawalpindi', lat: 33.6121, lng: 73.0725 },
  { number: 5, code: '5/205', name: 'Waris Khan', slug: 'waris_khan', zone: 'Rawalpindi', lat: 33.6180, lng: 73.0768 },
  { number: 6, code: '6/206', name: 'Chandni Chowk', slug: 'chandni_chowk', zone: 'Rawalpindi', lat: 33.6262, lng: 73.0792 },
  { number: 7, code: '7/207', name: 'Rehmanabad', slug: 'rehmanabad', zone: 'Rawalpindi', lat: 33.6338, lng: 73.0781 },
  { number: 8, code: '8/208', name: '6th Road', slug: '6th_road', zone: 'Rawalpindi', lat: 33.6425, lng: 73.0772 },
  { number: 9, code: '9/209', name: 'Khatam-e-Nabuwat', slug: 'shamsabad', zone: 'Rawalpindi', lat: 33.6521, lng: 73.0765 }, // Legacy ID is shamsabad
  { number: 10, code: '10/210', name: 'Faizabad', slug: 'faizabad', zone: 'Rawalpindi', lat: 33.6631, lng: 73.0841 },
  { number: 11, code: '11/211', name: 'IJP', slug: 'ijp', zone: 'Islamabad', lat: 33.6685, lng: 73.0721 },
  { number: 12, code: '12/212', name: 'Potohar', slug: 'potohar', zone: 'Islamabad', lat: 33.6738, lng: 73.0615 },
  { number: 13, code: '13/213', name: 'Khayaban-e-Johar', slug: 'khayaban_e_johar', zone: 'Islamabad', lat: 33.6792, lng: 73.0512 },
  { number: 14, code: '14/214', name: 'Faiz Ahmed Faiz', slug: 'faiz_ahmed_faiz', zone: 'Islamabad', lat: 33.6845, lng: 73.0421 },
  { number: 15, code: '15/215', name: 'Kashmir Highway', slug: 'kashmir_highway', zone: 'Islamabad', lat: 33.6912, lng: 73.0335 },
  { number: 16, code: '16/216', name: 'Chaman', slug: 'chaman', zone: 'Islamabad', lat: 33.6995, lng: 73.0381 },
  { number: 17, code: '17/217', name: 'Ibn-e-Sina', slug: 'ibn_e_sina', zone: 'Islamabad', lat: 33.7082, lng: 73.0435 },
  { number: 18, code: '18/218', name: 'Kachehry', slug: 'kachehry', zone: 'Islamabad', lat: 33.7155, lng: 73.0492 },
  { number: 19, code: '19/219', name: 'PIMS', slug: 'pims', zone: 'Islamabad', lat: 33.7225, lng: 73.0545 },
  { number: 20, code: '20/220', name: 'Stock Exchange', slug: 'stock_exchange', zone: 'Islamabad', lat: 33.7288, lng: 73.0612 },
  { number: 21, code: '21/221', name: '7th Avenue', slug: '7th_avenue', zone: 'Islamabad', lat: 33.7345, lng: 73.0675 },
  { number: 22, code: '22/222', name: 'Shaheed-e-Millat', slug: 'shaheed_e_millat', zone: 'Islamabad', lat: 33.7392, lng: 73.0742 },
  { number: 23, code: '23/223', name: 'Parade Ground', slug: 'parade_ground', zone: 'Islamabad', lat: 33.7431, lng: 73.0815 },
  { number: 24, code: '24/224', name: 'Pak Secretariat', slug: 'pak_secretariat', zone: 'Islamabad', lat: 33.7475, lng: 73.0902 }
];

export const getStationBySlug = (slug: string): MetroStation | undefined => {
  return METRO_STATIONS.find(s => s.slug.toLowerCase() === slug.toLowerCase());
};

export const getStationByName = (name: string): MetroStation | undefined => {
  return METRO_STATIONS.find(s => s.name.toLowerCase() === name.toLowerCase());
};
