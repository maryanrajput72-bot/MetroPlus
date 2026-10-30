package com.metroplus.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// 63 ke picker patterns, native me shared — poori app me yehi sheets khulti
// hain, koi native dropdown nahi:
// 1. StationPickerSheet — 63 ka pv-picker: "Select Station" + 24 pastel cards
//    (naam + number chip), chuna hua laal border + ring.
// 2. DatePickerSheet — 63 ka loDateSheet: month grid, day cards, Today/Close.
// 3. OptionPickerSheet — kisi bhi dropdown ki jagah: option cards wali sheet
//    (staff status menu isi se khulta hai).

// 63 PD_STATIONS (tarteeb wahi) + RMBS number (201-224).
val PickerStations = listOf(
    "Saddar" to "201", "Marrir Chowk" to "202", "Liaqat Bagh" to "203",
    "Committee Chowk" to "204", "Waris Khan" to "205", "Chandni Chowk" to "206",
    "Rehmanabad" to "207", "6th Road" to "208", "Khatam-e-Nabuwat" to "209",
    "Faizabad" to "210", "IJP" to "211", "Potohar" to "212",
    "Khayaban-e-Johar" to "213", "Faiz Ahmed Faiz" to "214",
    "Kashmir Highway" to "215", "Chaman" to "216", "Ibn-e-Sina" to "217",
    "Kachehry" to "218", "PIMS" to "219", "Stock Exchange" to "220",
    "7th Avenue" to "221", "Shaheed-e-Millat" to "222",
    "Parade Ground" to "223", "Pak Secretariat" to "224"
)

// 63 station cards: 120° pastel, idx % 4 (pink, yellow, green, blue).
private val StationPastels = listOf(
    listOf(Color(0xFFF7D7D9), Color(0xFFEFB1B4)),
    listOf(Color(0xFFFFF3C2), Color(0xFFFFE38A)),
    listOf(Color(0xFFD1FAE5), Color(0xFFA7F3D0)),
    listOf(Color(0xFFDBEAFE), Color(0xFFBFDBFE))
)

private val PkRed = Color(0xFF9E1F25)
private val PkInk = Color(0xFF14181C)
private val PkGray = Color(0xFF5B6470)

@Composable
private fun PkX(onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFE8ECEF)).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) { Icon(Icons.Filled.Close, contentDescription = "Close", tint = PkGray, modifier = Modifier.size(15.dp)) }
}

@Composable
fun StationPickerSheet(selected: String, onPick: (String) -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        val shape = RoundedCornerShape(22.dp)
        Column(
            modifier = Modifier.fillMaxWidth(0.82f).heightIn(max = 560.dp)
                .cssShadow(24.dp, shape, Color.Black.copy(alpha = 0.35f))
                .background(Color(0xFFF8F6F6), shape).clip(shape)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Select Station", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = PkInk, letterSpacing = (-0.3).sp, modifier = Modifier.weight(1f))
                PkX(onDismiss)
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 22.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(PickerStations) { i, (name, no) ->
                    val sel = name == selected
                    val c = StationPastels[i % StationPastels.size]
                    val cardShape = RoundedCornerShape(16.dp)
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .border(3.dp, if (sel) PkRed.copy(alpha = 0.18f) else Color.Transparent, RoundedCornerShape(19.dp)).padding(3.dp)
                            .clip(cardShape).background(cssLinear(120f, 0f to c[0], 1f to c[1]))
                            .border(1.5.dp, if (sel) PkRed else Color(0xFFE9EDF0), cardShape)
                            .clickable { onPick(name); onDismiss() }.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PkInk, modifier = Modifier.weight(1f))
                        Text(no, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9AA1AB),
                            modifier = Modifier.background(Color(0xFFF0F3F5), RoundedCornerShape(8.dp)).padding(horizontal = 9.dp, vertical = 4.dp))
                    }
                }
            }
        }
    }
}

// dd-MM-yyyy helpers (63 ki date format wahi hai).
fun formatDmy(cal: Calendar): String =
    "%02d-%02d-%04d".format(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR))

fun dmyToDisplay(dmy: String, pattern: String): String {
    return try {
        val p = dmy.split("-")
        val cal = Calendar.getInstance()
        cal.set(p[2].toInt(), p[1].toInt() - 1, p[0].toInt())
        SimpleDateFormat(pattern, Locale.ENGLISH).format(cal.time)
    } catch (e: Exception) {
        dmy
    }
}

private val MonthNames = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)

@Composable
fun DatePickerSheet(initialDmy: String, onPick: (String) -> Unit, onDismiss: () -> Unit) {
    val todayCal = Calendar.getInstance()
    val todayDmy = formatDmy(todayCal)
    var year by remember {
        mutableStateOf(
            try { initialDmy.split("-")[2].toInt() } catch (e: Exception) { todayCal.get(Calendar.YEAR) }
        )
    }
    var month by remember {
        mutableStateOf(
            try { initialDmy.split("-")[1].toInt() - 1 } catch (e: Exception) { todayCal.get(Calendar.MONTH) }
        )
    }
    fun move(dir: Int) {
        month += dir
        if (month < 0) { month = 11; year -= 1 }
        if (month > 11) { month = 0; year += 1 }
    }
    // 63 jaisa grid: Sunday se shuru, khaali khane pehle.
    val firstCal = Calendar.getInstance()
    firstCal.set(year, month, 1)
    val blanks = firstCal.get(Calendar.DAY_OF_WEEK) - 1 // SUNDAY = 1
    val daysInMonth = firstCal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val cells = (List(blanks) { null } + (1..daysInMonth).toList()).chunked(7)
    Dialog(onDismissRequest = onDismiss) {
        val shape = RoundedCornerShape(20.dp)
        Column(
            modifier = Modifier.fillMaxWidth(0.92f).widthIn(max = 340.dp)
                .cssShadow(18.dp, shape, Color.Black.copy(alpha = 0.28f))
                .background(Color.White, shape).padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFF0F3F5)).clickable { move(-1) },
                    contentAlignment = Alignment.Center
                ) { Text("‹", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = PkInk) }
                Text("${MonthNames[month]} $year", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = PkInk, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFF0F3F5)).clickable { move(1) },
                    contentAlignment = Alignment.Center
                ) { Text("›", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = PkInk) }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa").forEach {
                    Text(it, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF9AA1AB), textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            cells.forEach { week ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    week.forEach { day ->
                        if (day == null) {
                            Spacer(modifier = Modifier.weight(1f))
                        } else {
                            val dmy = "%02d-%02d-%04d".format(day, month + 1, year)
                            val isToday = dmy == todayDmy
                            val isSel = dmy == initialDmy
                            val dayShape = RoundedCornerShape(12.dp)
                            Box(
                                modifier = Modifier.weight(1f).clip(dayShape)
                                    .then(
                                        if (isSel) Modifier.background(cssLinear(135f, 0f to Color(0xFF9B35F2), 1f to Color(0xFFD82EDC)))
                                        else Modifier.background(Color(0xFFFAFBFC)).border(1.dp, if (isToday) Color(0xFFE11D2E) else Color(0xFFEEF1F3), dayShape)
                                    )
                                    .clickable { onPick(dmy); onDismiss() }.padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("$day", fontSize = 14.sp, fontWeight = FontWeight.Bold,
                                    color = if (isSel) Color.White else if (isToday) Color(0xFFB91018) else PkInk)
                            }
                        }
                    }
                    // Aakhri hafta chhota ho to bhi grid barabar rahe.
                    repeat(7 - week.size) { Spacer(modifier = Modifier.weight(1f)) }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp))
                        .background(cssLinear(135f, 0f to Color(0xFFE11D2E), 1f to Color(0xFFB91018)))
                        .clickable { onPick(todayDmy); onDismiss() }.padding(vertical = 11.dp),
                    contentAlignment = Alignment.Center
                ) { Text("Today", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) }
                Box(
                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(Color.White)
                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                        .clickable { onDismiss() }.padding(vertical = 11.dp),
                    contentAlignment = Alignment.Center
                ) { Text("Close", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = PkGray) }
            }
        }
    }
}

data class PickerOption(val label: String, val bg: List<Color>, val border: Color, val text: Color)

@Composable
fun OptionPickerSheet(title: String, options: List<PickerOption>, onPick: (PickerOption) -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        val shape = RoundedCornerShape(20.dp)
        Column(
            modifier = Modifier.fillMaxWidth(0.92f).widthIn(max = 340.dp)
                .cssShadow(18.dp, shape, Color.Black.copy(alpha = 0.28f))
                .background(Color.White, shape).padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = PkInk, modifier = Modifier.weight(1f))
                PkX(onDismiss)
            }
            Spacer(modifier = Modifier.height(12.dp))
            options.forEach { o ->
                Box(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                        .background(cssLinear(135f, 0f to o.bg[0], 1f to o.bg[1])).border(1.dp, o.border, RoundedCornerShape(12.dp))
                        .clickable { onPick(o); onDismiss() }.padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) { Text(o.label, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = o.text) }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}
