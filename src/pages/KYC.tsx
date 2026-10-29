import React, { useState } from 'react';
import { useAdmin } from '../context/AdminContext';
import { AdminLayout } from '../components/layout/AdminLayout';
import { Badge } from '../components/common/Badge';
import { Modal } from '../components/common/Modal';
import { KYCRequest } from '../types';
import { ShieldCheck, Check, X, FileText, Eye, AlertCircle } from 'lucide-react';

export const KYC: React.FC = () => {
  const { kycList, approveKYC, rejectKYC } = useAdmin();
  const [selectedKYC, setSelectedKYC] = useState<KYCRequest | null>(null);
  const [rejectReason, setRejectReason] = useState<string>('');
  const [showRejectBox, setShowRejectBox] = useState<boolean>(false);

  return (
    <AdminLayout title="KYC Verification" subtitle="Audit seller identity documents & business compliance">
      <div className="space-y-6">
        {/* Header Summary */}
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
          <div className="metro-card p-5">
            <span className="text-[10px] uppercase font-bold text-[#C9A24A] tracking-wider">Awaiting Audit</span>
            <div className="text-2xl font-black text-white font-mono mt-1">
              {kycList.filter((k) => k.status === 'pending').length} Requests
            </div>
            <div className="text-xs text-[#A39A8B] mt-1">Sellers waiting for activation</div>
          </div>
          <div className="metro-card p-5">
            <span className="text-[10px] uppercase font-bold text-[#55D88D] tracking-wider">Approved Merchants</span>
            <div className="text-2xl font-black text-white font-mono mt-1">
              {kycList.filter((k) => k.status === 'approved').length} Verified
            </div>
            <div className="text-xs text-[#A39A8B] mt-1">Fully compliant store owners</div>
          </div>
          <div className="metro-card p-5">
            <span className="text-[10px] uppercase font-bold text-[#D2553F] tracking-wider">Rejections</span>
            <div className="text-2xl font-black text-white font-mono mt-1">
              {kycList.filter((k) => k.status === 'rejected').length} Rejected
            </div>
            <div className="text-xs text-[#A39A8B] mt-1">Failed identity verification</div>
          </div>
        </div>

        {/* KYC List */}
        <div className="metro-card p-6">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs">
              <thead>
                <tr className="border-b border-[#C9A24A]/15 text-[#6E6658] uppercase font-bold tracking-wider">
                  <th className="pb-3.5 pl-2">Applicant</th>
                  <th className="pb-3.5">Document Type</th>
                  <th className="pb-3.5">Document Number</th>
                  <th className="pb-3.5">Submission Date</th>
                  <th className="pb-3.5 text-center">Status</th>
                  <th className="pb-3.5 pr-2 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[#C9A24A]/10">
                {kycList.map((kyc) => (
                  <tr key={kyc.id} className="hover:bg-white/[0.02] transition-colors">
                    <td className="py-4 pl-2">
                      <div className="font-bold text-white text-sm">{kyc.userName}</div>
                      <div className="text-[11px] text-[#A39A8B]">{kyc.userEmail}</div>
                    </td>

                    <td className="py-4">
                      <div className="flex items-center gap-1.5 text-white font-medium">
                        <FileText className="w-3.5 h-3.5 text-[#C9A24A]" />
                        <span>{kyc.documentType}</span>
                      </div>
                    </td>

                    <td className="py-4 font-mono font-bold text-[#FFF1C4]">
                      {kyc.documentNumber}
                    </td>

                    <td className="py-4 text-[#A39A8B]">
                      {new Date(kyc.submittedAt).toLocaleDateString([], {
                        month: 'short',
                        day: 'numeric',
                        year: 'numeric'
                      })}
                    </td>

                    <td className="py-4 text-center">
                      <Badge status={kyc.status} />
                    </td>

                    <td className="py-4 pr-2 text-right">
                      <div className="flex items-center justify-end gap-2">
                        <button
                          onClick={() => {
                            setSelectedKYC(kyc);
                            setShowRejectBox(false);
                          }}
                          className="px-3 py-1.5 text-xs font-bold text-[#FFF1C4] bg-white/[0.04] border border-[#C9A24A]/30 rounded-lg hover:border-[#C9A24A] hover:bg-[#C9A24A]/10 transition-colors flex items-center gap-1"
                        >
                          <Eye className="w-3.5 h-3.5 text-[#C9A24A]" />
                          <span>Review</span>
                        </button>

                        {kyc.status === 'pending' && (
                          <>
                            <button
                              onClick={() => approveKYC(kyc.id)}
                              className="p-1.5 rounded-lg bg-[#122B1E] text-[#55D88D] border border-[#1D4A33] hover:bg-[#1D4A33] transition-colors"
                              title="Approve KYC"
                            >
                              <Check className="w-4 h-4" />
                            </button>
                            <button
                              onClick={() => {
                                setSelectedKYC(kyc);
                                setShowRejectBox(true);
                              }}
                              className="p-1.5 rounded-lg bg-[#2E1412] text-[#F38A7C] border border-[#592622] hover:bg-[#592622] transition-colors"
                              title="Reject KYC"
                            >
                              <X className="w-4 h-4" />
                            </button>
                          </>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Review Modal */}
      {selectedKYC && (
        <Modal
          isOpen={!!selectedKYC}
          onClose={() => setSelectedKYC(null)}
          title={`KYC Audit: ${selectedKYC.userName}`}
          subtitle={`${selectedKYC.documentType} • ${selectedKYC.documentNumber}`}
          maxWidth="xl"
        >
          <div className="space-y-4">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <span className="text-xs font-semibold text-[#A39A8B] block mb-1">
                  Document Front Side
                </span>
                <div className="rounded-xl overflow-hidden border border-[#C9A24A]/30 bg-black aspect-video flex items-center justify-center">
                  <img
                    src={selectedKYC.frontImage}
                    alt="Front Document"
                    className="w-full h-full object-cover"
                  />
                </div>
              </div>

              <div>
                <span className="text-xs font-semibold text-[#A39A8B] block mb-1">
                  Document Back Side
                </span>
                <div className="rounded-xl overflow-hidden border border-[#C9A24A]/30 bg-black aspect-video flex items-center justify-center">
                  <img
                    src={selectedKYC.backImage}
                    alt="Back Document"
                    className="w-full h-full object-cover"
                  />
                </div>
              </div>
            </div>

            {showRejectBox && (
              <div className="p-4 rounded-xl bg-[#2E1412]/50 border border-[#592622] space-y-2">
                <div className="flex items-center gap-1.5 text-xs font-bold text-[#F38A7C]">
                  <AlertCircle className="w-4 h-4" />
                  <span>Specify Rejection Reason</span>
                </div>
                <textarea
                  value={rejectReason}
                  onChange={(e) => setRejectReason(e.target.value)}
                  placeholder="e.g. Blurry photo, expired ID card, or document name does not match user account..."
                  className="w-full bg-[#0E0C09] border border-[#592622] rounded-lg p-2.5 text-xs text-white focus:outline-none focus:border-red-500"
                  rows={3}
                />
              </div>
            )}

            <div className="flex items-center justify-between pt-4 border-t border-[#C9A24A]/15">
              <span className="text-xs text-[#A39A8B]">
                Status: <strong className="text-white uppercase">{selectedKYC.status}</strong>
              </span>

              <div className="flex items-center gap-2">
                {showRejectBox ? (
                  <button
                    onClick={() => {
                      if (!rejectReason) return alert('Please enter a rejection reason.');
                      rejectKYC(selectedKYC.id, rejectReason);
                      setSelectedKYC(null);
                    }}
                    className="px-4 py-2 text-xs font-bold text-white bg-red-600 hover:bg-red-700 rounded-xl transition-all"
                  >
                    Confirm Rejection
                  </button>
                ) : selectedKYC.status === 'pending' ? (
                  <>
                    <button
                      onClick={() => setShowRejectBox(true)}
                      className="px-4 py-2 text-xs font-bold text-[#F38A7C] bg-[#2E1412] border border-[#592622] rounded-xl hover:bg-[#592622]"
                    >
                      Reject Submission
                    </button>
                    <button
                      onClick={() => {
                        approveKYC(selectedKYC.id);
                        setSelectedKYC(null);
                      }}
                      className="px-5 py-2 text-xs font-bold text-[#0B0B0C] bg-[#C9A24A] hover:bg-[#FFF1C4] rounded-xl transition-all shadow-md"
                    >
                      Approve & Verify Seller
                    </button>
                  </>
                ) : (
                  <button
                    onClick={() => setSelectedKYC(null)}
                    className="px-4 py-2 text-xs font-semibold text-zinc-400 hover:text-white"
                  >
                    Close
                  </button>
                )}
              </div>
            </div>
          </div>
        </Modal>
      )}
    </AdminLayout>
  );
};
