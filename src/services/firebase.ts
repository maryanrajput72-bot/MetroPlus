import { initializeApp, getApps, getApp } from 'firebase/app';
import { getFirestore } from 'firebase/firestore';
import { getAuth } from 'firebase/auth';

export const METRO_FIREBASE_CONFIG = {
  apiKey: "AIzaSyCYFYARldvFUPCJ2_w4l4L1_aUKNm_hpIw",
  authDomain: "metro-plus-5966d.firebaseapp.com",
  projectId: "metro-plus-5966d",
  storageBucket: "metro-plus-5966d.firebasestorage.app",
  messagingSenderId: "872006480655",
  appId: "1:872006480655:web:a557d4c8d6f90f9f45c47a"
};

let app: any = null;
let db: any = null;
let auth: any = null;

try {
  app = getApps().length === 0 ? initializeApp(METRO_FIREBASE_CONFIG) : getApp();
  db = getFirestore(app);
  auth = getAuth(app);
} catch (e) {
  console.warn("Firebase initialization notice:", e);
}

export { app, db, auth };
