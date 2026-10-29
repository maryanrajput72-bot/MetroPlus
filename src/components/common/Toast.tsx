import React from 'react';
import { useMetro } from '../../context/MetroContext';
import { CheckCircle2, X } from 'lucide-react';

export const Toast: React.FC = () => {
  const { toastMessage, showToast } = useMetro();

  if (!toastMessage) return null;

  return (
    <div className="fixed bottom-6 right-6 z-50 animate-in slide-in-from-bottom-5 fade-in duration-300">
      <div className="flex items-center gap-3 px-4 py-3 bg-[#191510] border border-[#C9A24A]/40 text-[#FFFDF8] rounded-xl shadow-2xl shadow-black/80">
        <div className="w-7 h-7 rounded-full bg-[#122B1E] border border-[#2F9A63]/50 flex items-center justify-center text-[#55D88D] flex-shrink-0">
          <CheckCircle2 className="w-4 h-4" />
        </div>
        <p className="text-xs font-semibold pr-2">{toastMessage}</p>
        <button
          onClick={() => showToast('')}
          className="text-[#A39A8B] hover:text-white p-1 rounded-lg"
        >
          <X className="w-4 h-4" />
        </button>
      </div>
    </div>
  );
};
