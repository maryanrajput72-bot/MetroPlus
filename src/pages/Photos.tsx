import React, { useState } from 'react';
import { useMetro } from '../context/MetroContext';
import { AdminLayout } from '../components/layout/AdminLayout';
import { Modal } from '../components/common/Modal';
import { GPSPhoto } from '../types/metro';
import { 
  Camera, 
  Trash2, 
  MapPin, 
  ExternalLink, 
  Clock, 
  User, 
  Phone, 
  TrainTrack,
  Filter,
  Eye,
  AlertTriangle
} from 'lucide-react';

export const Photos: React.FC = () => {
  const { gpsPhotos, deleteGPSPhoto, stations } = useMetro();
  const [stationFilter, setStationFilter] = useState('all');
  const [selectedPhoto, setSelectedPhoto] = useState<GPSPhoto | null>(null);
  const [deleteConfirmId, setDeleteConfirmId] = useState<string | null>(null);

  const filtered = gpsPhotos.filter(p => {
    return stationFilter === 'all' || p.stationName.toLowerCase().includes(stationFilter.toLowerCase());
  });

  return (
    <AdminLayout title="Station GPS Photos" subtitle="CameraX GPS Captures (Firestore collection: photos)">
      <div className="space-y-6">
        {/* Filter Controls */}
        <div className="metro-card p-4 sm:p-5 flex flex-col sm:flex-row items-center justify-between gap-4">
          <div className="flex items-center gap-3">
            <span className="text-xs font-bold text-[#A39A8B]">Station Filter:</span>
            <select
              value={stationFilter}
              onChange={(e) => setStationFilter(e.target.value)}
              className="bg-[#0E0C09] border border-[#C9A24A]/30 text-xs font-bold text-white rounded-xl px-3 py-2 focus:outline-none focus:border-[#C9A24A]"
            >
              <option value="all">All 24 Stations</option>
              {stations.map(st => (
                <option key={st.slug} value={st.name}>
                  {st.name} ({st.code})
                </option>
              ))}
            </select>
          </div>

          <div className="text-xs text-[#A39A8B]">
            Showing <strong className="text-[#FFF1C4]">{filtered.length}</strong> CameraX GPS Captures
          </div>
        </div>

        {/* Photos Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
          {filtered.map((photo) => (
            <div
              key={photo.id}
              className="metro-card overflow-hidden flex flex-col justify-between group"
            >
              <div>
                {/* Image Box */}
                <div className="relative aspect-video overflow-hidden bg-black">
                  <img
                    src={photo.thumb}
                    alt={photo.stationName}
                    className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                  />

                  {/* Stamp Badges */}
                  <div className="absolute top-2.5 left-2.5 px-2.5 py-1 rounded-lg bg-black/80 backdrop-blur-md border border-white/10 text-[10px] font-mono text-[#FFF1C4] font-bold">
                    {photo.stationName} • {photo.shift}
                  </div>

                  <div className="absolute bottom-2.5 right-2.5 flex items-center gap-1">
                    <button
                      onClick={() => setSelectedPhoto(photo)}
                      className="p-1.5 rounded-lg bg-black/80 hover:bg-[#C9A24A] text-white hover:text-[#0B0B0C] transition-colors"
                      title="Inspect GPS Stamp"
                    >
                      <Eye className="w-3.5 h-3.5" />
                    </button>
                    <button
                      onClick={() => setDeleteConfirmId(photo.id)}
                      className="p-1.5 rounded-lg bg-red-950/80 hover:bg-red-600 text-red-200 hover:text-white transition-colors"
                      title="Total Delete"
                    >
                      <Trash2 className="w-3.5 h-3.5" />
                    </button>
                  </div>
                </div>

                {/* Metadata */}
                <div className="p-4 space-y-2 text-xs">
                  <div className="flex items-center justify-between text-[#A39A8B]">
                    <span className="flex items-center gap-1 font-semibold text-white">
                      <User className="w-3.5 h-3.5 text-[#C9A24A]" />
                      <span>{photo.byName}</span>
                    </span>
                    <span className="font-mono text-[10.5px]">
                      {new Date(photo.ts).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                    </span>
                  </div>

                  <div className="flex items-start gap-1.5 text-[#A39A8B] text-[11px]">
                    <MapPin className="w-3.5 h-3.5 text-[#55D88D] flex-shrink-0 mt-0.5" />
                    <span className="line-clamp-2">{photo.address}</span>
                  </div>

                  <div className="font-mono text-[10px] text-[#C9A24A]">
                    GPS: {photo.lat.toFixed(4)}°N, {photo.lng.toFixed(4)}°E
                  </div>
                </div>
              </div>

              {/* Card Footer */}
              <div className="p-3 bg-white/[0.02] border-t border-white/5 flex items-center justify-between text-[10.5px]">
                <span className="text-[#6E6658]">Station ID #{photo.stationId}</span>
                <a
                  href={`https://maps.google.com/?q=${photo.lat},${photo.lng}`}
                  target="_blank"
                  rel="noreferrer"
                  className="text-[#C9A24A] hover:text-[#FFF1C4] flex items-center gap-1 font-bold"
                >
                  <span>Google Maps</span>
                  <ExternalLink className="w-3 h-3" />
                </a>
              </div>
            </div>
          ))}
        </div>

        {filtered.length === 0 && (
          <div className="metro-card p-12 text-center text-[#A39A8B]">
            <Camera className="w-12 h-12 mx-auto text-[#6E6658] mb-3" />
            <p className="text-sm font-semibold">No GPS photos found for this station filter.</p>
          </div>
        )}
      </div>

      {/* Photo Viewer Modal */}
      {selectedPhoto && (
        <Modal
          isOpen={!!selectedPhoto}
          onClose={() => setSelectedPhoto(null)}
          title={`GPS Photo: ${selectedPhoto.stationName}`}
          subtitle={`${selectedPhoto.shift} Shift • Captured by ${selectedPhoto.byName}`}
          maxWidth="2xl"
        >
          <div className="space-y-4">
            <div className="rounded-xl overflow-hidden border border-[#C9A24A]/30 bg-black aspect-video">
              <img
                src={selectedPhoto.thumb}
                alt={selectedPhoto.stationName}
                className="w-full h-full object-contain"
              />
            </div>

            {/* CameraX Split GPS Stamp Details */}
            <div className="p-4 rounded-xl bg-white/[0.02] border border-[#C9A24A]/20 grid grid-cols-1 sm:grid-cols-2 gap-3 text-xs">
              <div>
                <span className="text-[#A39A8B] block text-[10px] uppercase font-bold">Station Address</span>
                <div className="font-semibold text-white mt-0.5">{selectedPhoto.address}</div>
              </div>
              <div>
                <span className="text-[#A39A8B] block text-[10px] uppercase font-bold">CameraX Coordinates</span>
                <div className="font-mono font-bold text-[#FFF1C4] mt-0.5">
                  LAT: {selectedPhoto.lat} • LNG: {selectedPhoto.lng}
                </div>
              </div>
              <div>
                <span className="text-[#A39A8B] block text-[10px] uppercase font-bold">Photographer</span>
                <div className="text-white mt-0.5">{selectedPhoto.byName} ({selectedPhoto.byPhone})</div>
              </div>
              <div>
                <span className="text-[#A39A8B] block text-[10px] uppercase font-bold">Timestamp</span>
                <div className="font-mono text-white mt-0.5">{new Date(selectedPhoto.ts).toLocaleString()}</div>
              </div>
            </div>

            <div className="flex items-center justify-between pt-3 border-t border-white/10">
              <span className="text-[10px] text-[#A39A8B]">Rules: CameraX split stamp, English text, no badge.</span>
              <button
                onClick={() => setSelectedPhoto(null)}
                className="px-4 py-2 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] rounded-xl transition-all"
              >
                Close View
              </button>
            </div>
          </div>
        </Modal>
      )}

      {/* Delete Confirmation Modal */}
      {deleteConfirmId && (
        <Modal
          isOpen={!!deleteConfirmId}
          onClose={() => setDeleteConfirmId(null)}
          title="Confirm Total Cloud Deletion"
          subtitle="v2.1.59+ Delete Rule"
          maxWidth="sm"
        >
          <div className="space-y-4">
            <p className="text-xs text-[#A39A8B]">
              Are you sure you want to delete this GPS photo? As per the app specification, this will permanently remove the cloud document from Firestore <code className="text-[#FFF1C4]">photos</code>.
            </p>

            <div className="flex items-center justify-end gap-3 pt-3 border-t border-[#C9A24A]/15">
              <button
                onClick={() => setDeleteConfirmId(null)}
                className="px-3 py-1.5 text-xs text-zinc-400 hover:text-white"
              >
                Cancel
              </button>
              <button
                onClick={() => {
                  deleteGPSPhoto(deleteConfirmId);
                  setDeleteConfirmId(null);
                }}
                className="px-4 py-1.5 text-xs font-bold text-white bg-red-600 hover:bg-red-700 rounded-xl transition-all"
              >
                Delete Permanently
              </button>
            </div>
          </div>
        </Modal>
      )}
    </AdminLayout>
  );
};
