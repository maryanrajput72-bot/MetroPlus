import { DailyAttendanceDoc, ShiftType } from '../types/metro';

/**
 * Byte-exact WhatsApp Staff Attendance Report, matching the Metro+ app
 * (js/98 staffAttendanceReportText):
 *  "ATTENDANCE REPORT", blank line, "Station:(name)", "Date: D Mon, YYYY",
 *  "Shift: X", blank line, then one line per staffer:
 *  "● [ID] Name  ➔  Time-or-StatusLabel" (two spaces around ➔).
 *  Status labels are capitalised like the app (Off/Waiting/Leave/Absent);
 *  on-duty rows show their time, or "-" when no time is stored.
 *  Rows with neither ID nor name are skipped. Always the full list.
 */
export function buildWhatsAppAttendanceReport(doc: DailyAttendanceDoc): string {
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  const d = new Date(doc.date + 'T00:00:00');
  const dateStr = Number.isNaN(d.getTime())
    ? doc.date
    : `${d.getDate()} ${months[d.getMonth()]}, ${d.getFullYear()}`;

  const lines: string[] = [
    'ATTENDANCE REPORT',
    '',
    `Station:(${doc.stationName})`,
    `Date: ${dateStr}`,
    `Shift: ${doc.shift}`,
    '',
  ];

  doc.entries.forEach((entry) => {
    if (!entry.id && !entry.name) return;
    const idPart = entry.id || 'New';
    const namePart = entry.name || '-';
    let timePart = '';
    if (!entry.status || entry.status === '') {
      timePart = entry.time || '-';
    } else if (entry.status === 'WAITING') {
      timePart = 'Waiting';
    } else if (entry.status === 'OFF') {
      timePart = 'Off';
    } else if (entry.status === 'LEAVE') {
      timePart = 'Leave';
    } else if (entry.status === 'ABSENT') {
      timePart = 'Absent';
    } else {
      timePart = entry.status;
    }
    lines.push(`● [${idPart}] ${namePart}  ➔  ${timePart}`);
  });

  return lines.join('\n');
}

/**
 * RMBS Closing Report Format (app js/20 ctShareWhatsApp 'rmbs'):
 *  {SM|SE},SN{stationNo},BC{total},TS00,CS00,TCS00,CI00,TC00,RC00,TI00,ST00,
 *  CR00,CT00,FC00,F00,TK00,UT00,TR00,TD00,TT00,E00,RS00,AT00,RT00,ET00,R00,N00
 * SM = morning / SE = evening.
 * SN = FULL station number (e.g. Saddar -> 201, Pak Secretariat -> 224).
 * BC = closing grand total passed in by the caller.
 */
export function buildRMBSReport(shift: ShiftType, stationFullNumber: number, bcTotal: number): string {
  const shiftPrefix = shift === 'Morning' ? 'SM' : 'SE';
  return (
    `${shiftPrefix},SN${stationFullNumber},BC${bcTotal}` +
    ',TS00,CS00,TCS00,CI00,TC00,RC00,TI00,ST00,CR00,CT00,FC00,F00,TK00,UT00,TR00,TD00,TT00,E00,RS00,AT00,RT00,ET00,R00,N00'
  );
}

/**
 * Card Claim WhatsApp Report (app js/20 ctShareWhatsApp 'cardclaim'):
 *  *Card claim Detail*
 *  Station: *{station-with-dots}*
 *  Shift: {shift}
 *  Date: {DD/MM/YYYY}
 *  Card claim Total={NN}
 *  =•=•=•=•=•=•=•=•=•=
 * Station dashes become dots ONLY (spaces are kept, e.g. Shaheed.e.Millat).
 * Count zero-padded to 2 digits.
 */
export function buildCardClaimReport(stationName: string, shift: ShiftType, dateStr: string, count: number): string {
  const stationDots = stationName.replace(/-/g, '.');
  const d = new Date(dateStr + 'T00:00:00');
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const dateFormatted = Number.isNaN(d.getTime()) ? dateStr : `${dd}/${mm}/${yyyy}`;
  const countPadded = String(count).padStart(2, '0');

  return `*Card claim Detail*\nStation: *${stationDots}*\nShift: ${shift}\nDate: ${dateFormatted}\nCard claim Total=${countPadded}\n=•=•=•=•=•=•=•=•=•=`;
}
