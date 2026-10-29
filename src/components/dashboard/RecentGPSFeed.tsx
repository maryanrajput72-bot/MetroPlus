import React from 'react';
import { useMetro } from '../../context/MetroContext';
import { Camera, MapPin, ExternalLink, ArrowRight } from 'lucide-react';
import { Link } from 'react-router-dom';

export const RecentGPSFeed: React.FC = () => {
  const { gpsPhotos } = useMetro();

  const recent = gpsPhotos.slice(0, 3);

  return (
    <div className="metro-card p-6">
      <div className="flex items-center justify-between pb-4 border-b border-[#C9A24A]/15">
        <div>
          <span className="text-[10px] font-bold tracking-[0.2em] text-[#C9A24A] uppercase flex items-center gap-1.5">
            <Camera className="w-3.5 h-3.5 text-[#C9A24A]" />
            LIVE CAMERAX DISPATCH
          </span>
          <h3 className="text-lg font-bold text-[#FFFDF8] mt-1 font-['Cinzel',serif]">
            Recent GPS Photos
          </h3>
        </div>

        <Link
          to="/photos"
          className="flex items-center gap-1 text-xs font-bold text-[#C9A24A] hover:text-[#FFF1C4] transition-colors"
        >
          <span>All Photos ({gpsPhotos.length})</span>
          <ArrowRight className="w-3.5 h-3.5" />
        </Link>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mt-4">
        {recent.map((photo) => (
          <div
            key={photo.id}
            className="group relative rounded-xl overflow-hidden border border-[#C9A24A]/25 bg-[#0A0907] hover:border-[#C9A24A] transition-all"
          >
            <div className="aspect-video relative overflow-hidden">
              <img
                src={photo.thumb}
                alt={photo.stationName}
                className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
              />
              <div className="absolute top-2 left-2 px-2 py-0.5 rounded bg-black/75 backdrop-blur-sm text-[9.5px] font-mono text-[#FFF1C4] border border-white/10 font-bold">
                {photo.stationName} • {photo.shift}
              </div>
            </div>

            <div className="p-3 text-xs">
              <div className="font-semibold text-white truncate">{photo.byName}</div>
              <div className="text-[10px] text-[#A39A8B] flex items-center gap-1 mt-0.5 truncate">
                <MapPin className="w-3 h-3 text-[#C9A24A] flex-shrink-0" />
                <span className="truncate">{photo.address}</span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
