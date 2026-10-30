package com.metroplus.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WbSunny
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Tom Data Register — Shift History (trial 2.1.63 ka tomHistoryModal, native
// full page me): amber tile + "Shift History" + sub, Opening/Closing segment,
// pichhle 7 din (naye se purane), aaj ka din laal border + OPEN NOW chip,
// har row: date box, weekday + shift chip, Opening/Closing ✓ chips, Sale
// line (sirf sale > 0 ho to), View (laal) + Delete (halka laal) buttons.
// SIRF GUI: demo records hain — asli saved records Save banne pe wire honge;
// View/Delete abhi GUI note dikhate hain.
private val HistInk = Color(0xFF14181C)
private val HistMuted = Color(0xFF6B7280)
private val HistRed = Color(0xFF9E1F25)

private class HistDay(
    val day: Int, val month: String, val weekday: String, val shift: String,
    val openingSaved: Boolean, val closingSaved: Boolean, val sale: Int, val slips: Int,
    val isCurrent: Boolean = false
)

private val demoHist = listOf(
    HistDay(30, "Oct", "Friday", "Evening", true, true, 12450, 415, isCurrent = true),
    HistDay(29, "Oct", "Thursday", "Morning", true, true, 9870, 329),
    HistDay(28, "Oct", "Wednesday", "Evening", true, false, 7200, 240),
    HistDay(27, "Oct", "Tuesday", "Morning", true, true, 11100, 370),
    HistDay(26, "Oct", "Monday", "Evening", false, true, 8400, 280),
    HistDay(25, "Oct", "Sunday", "Morning", true, true, 6300, 210),
    HistDay(24, "Oct", "Saturday", "Evening", true, false, 0, 0)
)

@Composable
fun HistoryScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    var closing by remember { mutableStateOf(false) }
    val days = demoHist.filter { if (closing) it.closingSaved else it.openingSaved }
    // v1.42 rule: khula page poora opaque + tap neeche na jaye.
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF3EDED))
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {}
    ) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            // Header: 44dp amber tile + title + sub + close (trial .th-head).
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(44.dp).cssShadow(8.dp, RoundedCornerShape(14.dp), Color(0xFFF08C00).copy(alpha = 0.3f))
                        .clip(RoundedCornerShape(14.dp)).background(cssLinear(145f, 0f to Color(0xFFFFC04D), 1f to Color(0xFFF08C00))),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.History, contentDescription = null, tint = Color.White, modifier = Modifier.size(19.dp)) }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Shift History", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = HistInk)
                    Text(
                        "${days.size} saved ${if (days.size == 1) "day" else "days"} · last 7 days · Shaheed-e-Millat",
                        fontSize = 12.sp, color = HistMuted
                    )
                }
                Box(
                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFF4F0F0)).clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color(0xFF5B6B57), modifier = Modifier.size(18.dp)) }
            }
            // Segment: Opening / Closing (trial .th-seg).
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth().background(Color(0xFFF4F0F0), RoundedCornerShape(14.dp)).padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                HistSegButton("Opening", Icons.Filled.WbSunny, !closing, Modifier.weight(1f)) { closing = false }
                HistSegButton("Closing", Icons.Filled.DarkMode, closing, Modifier.weight(1f)) { closing = true }
            }
            // List (trial .th-list).
            Spacer(modifier = Modifier.height(14.dp))
            if (days.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 26.dp, horizontal = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Filled.FolderOpen, contentDescription = null, tint = Color(0xFFD3C7C8), modifier = Modifier.size(34.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("No saved ${if (closing) "closing" else "opening"} records yet.", fontSize = 13.5.sp, color = HistMuted)
                }
            } else {
                days.forEach { d ->
                    HistItem(d, onGuiNote)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun HistSegButton(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, active: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val shape = RoundedCornerShape(11.dp)
    Row(
        modifier = modifier
            .then(if (active) Modifier.cssShadow(2.dp, shape, Color.Black.copy(alpha = 0.08f)) else Modifier)
            .clip(shape).then(if (active) Modifier.background(Color.White) else Modifier)
            .clickable { onClick() }.padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = if (active) HistRed else Color(0xFF5B6B57), modifier = Modifier.size(15.dp))
        Spacer(modifier = Modifier.width(7.dp))
        Text(label, fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold, color = if (active) HistRed else Color(0xFF5B6B57))
    }
}

@Composable
private fun HistItem(d: HistDay, onGuiNote: (String) -> Unit) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier = Modifier.fillMaxWidth()
            .then(
                if (d.isCurrent) Modifier.cssShadow(3.dp, shape, HistRed.copy(alpha = 0.15f)).clip(shape)
                    .background(Color.White, shape).border(1.dp, HistRed, shape)
                else Modifier.cssShadow(3.dp, shape, Color(0xFF281415).copy(alpha = 0.05f)).clip(shape)
                    .background(Color.White, shape).border(1.dp, Color(0xFFF1ECEC), shape)
            ).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Date box (trial .th-date): 52x56, gulabi gradient, din + mahina.
        Column(
            modifier = Modifier.size(width = 52.dp, height = 56.dp)
                .clip(RoundedCornerShape(14.dp)).background(cssLinear(160f, 0f to Color(0xFFF8ECED), 1f to Color(0xFFF1D7D8))),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
        ) {
            Text(d.day.toString(), fontSize = 21.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7A1A1F), lineHeight = 21.sp)
            Text(d.month.uppercase(), fontSize = 10.5.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7A1A1F), letterSpacing = 0.5.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            // Line 1: weekday + shift chip + OPEN NOW (trial .th-l1).
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(d.weekday, fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold, color = HistInk)
                Spacer(modifier = Modifier.width(6.dp))
                HistChip(d.shift, Color(0xFFF2EEEE), Color(0xFF4B5B50))
                if (d.isCurrent) {
                    Spacer(modifier = Modifier.width(6.dp))
                    HistChip("OPEN NOW", Color(0xFFE0F2FE), Color(0xFF0369A1))
                }
            }
            // Line 2: Opening/Closing ✓ chips (trial .th-l2).
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                if (d.openingSaved) HistChip("Opening ✓", Color(0xFFF6DFE0), Color(0xFF7A1A1F))
                if (d.closingSaved) {
                    if (d.openingSaved) Spacer(modifier = Modifier.width(6.dp))
                    HistChip("Closing ✓", Color(0xFFF6DFE0), Color(0xFF7A1A1F))
                }
            }
            // Sale line — sirf sale > 0 ho to (trial logic).
            if (d.sale > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text("Sale ", fontSize = 12.5.sp, color = HistMuted)
                    Text("Rs. ${d.sale}", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                    Text(" · ${d.slips} slips", fontSize = 12.5.sp, color = HistMuted)
                }
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        // Actions (trial .th-acts): View laal + Delete halka laal.
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(HistRed).clickable { onGuiNote("View record") }
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Visibility, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(5.dp))
                Text("View", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            }
            Box(
                modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(Color(0xFFFDECEE)).clickable { onGuiNote("Delete record") }
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.DeleteOutline, contentDescription = null, tint = Color(0xFFE53935), modifier = Modifier.size(15.dp)) }
        }
    }
}

@Composable
private fun HistChip(text: String, bg: Color, fg: Color) {
    Text(
        text, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = fg,
        modifier = Modifier.background(bg, RoundedCornerShape(6.dp)).padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

// TomDataScreen ke andar History page ka slide-over (StaffEdit wali pattern).
@Composable
internal fun TomHistoryOverlay(visible: Boolean, onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(animationSpec = tween(350, easing = FastOutSlowInEasing)) { it } + fadeIn(animationSpec = tween(350)),
        exit = slideOutHorizontally(animationSpec = tween(350, easing = FastOutSlowInEasing)) { it } + fadeOut(animationSpec = tween(350)),
        modifier = Modifier.fillMaxSize()
    ) {
        HistoryScreen(onBack = onBack, onGuiNote = onGuiNote)
    }
}
