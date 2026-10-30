package com.metroplus.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import com.metroplus.app.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Closing Time — trial 2.1.63 ke closingTimeScreen ki EXACT copy (computed CSS, 1px = 1dp):
// purple hero (kona 26) jiske andar REPORT DATE button, "SHIFT CLOSINGS" (TOM·TKM·POS, RMBS),
// "CLAIMS & REPORTS" (Card Claim stepper + Share, PMBS Complaint). Share/Save wiring agle step me.
private val ClInk = Color(0xFF14181C)
private val ClMuted = Color(0xFF6B7280)

// Card Claim count ViewModel me — page band karke dobara kholo to wahi rahe.
class ClosingViewModel : ViewModel() {
    val cardCount = mutableStateOf(0)
}

@Composable
fun ClosingScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    // Item 6: PMBS Complaint form (Item 3 wala form, type PMBS) — Closing ke upar slide hoke aata hai.
    var pmbsOpen by remember { mutableStateOf(false) }
    var reportDmy by remember { mutableStateOf(SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Date())) }
    var reportDateOpen by remember { mutableStateOf(false) }
    BackHandler { if (reportDateOpen) reportDateOpen = false else if (pmbsOpen) pmbsOpen = false else onBack() }
    val vm: ClosingViewModel = viewModel()
    var cardCount by vm.cardCount

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF3EDED))) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Header: white 94%, padding 14/16, 38dp gol back + "Closing Time" 18sp (baein).
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.White.copy(alpha = 0.94f)).statusBarsPadding().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(38.dp).clip(CircleShape).background(Color.White).border(1.dp, ClInk.copy(alpha = 0.09f), CircleShape).clickable { onBack() },
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = ClInk, modifier = Modifier.size(18.dp)) }
            Spacer(modifier = Modifier.width(12.dp))
            Text("Closing Time", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = ClInk, letterSpacing = (-0.3).sp)
        }
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            // Hero: 145° #211647→#6A4FD8→#4B32B0 (easypaisa), kona 26, padding 18, andar REPORT DATE button.
            val heroShape = RoundedCornerShape(26.dp)
            Box(
                modifier = Modifier.fillMaxWidth().cssShadow(14.dp, heroShape, Color(0xFF4B32B0).copy(alpha = 0.3f)).clip(heroShape)
                    .background(cssLinear(145f, 0f to Color(0xFF211647), 0.55f to Color(0xFF6A4FD8), 1f to Color(0xFF4B32B0)))
            ) {
                // Item 6: trial .cl-hero::after glow (190px, daein -50, upar -60, halka .08).
                // matchParentSize me — pehle 219dp logo watermark card ko zabardasti lamba karta tha.
                Box(Modifier.matchParentSize()) {
                    Glow(190.dp, x = 50.dp, y = (-60).dp, alpha = 0.08f, align = Alignment.TopEnd)
                }
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("END OF SHIFT", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color.White.copy(alpha = 0.9f), letterSpacing = 1.2.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Shaheed-e-Millat", fontSize = 23.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Pick the report date, then share each closing on WhatsApp.", fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.88f), lineHeight = 15.5.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().cssShadow(6.dp, RoundedCornerShape(16.dp), Color.Black.copy(alpha = 0.12f))
                            .clip(RoundedCornerShape(16.dp)).background(Color.White.copy(alpha = 0.96f)).clickable { reportDateOpen = true }
                            .padding(horizontal = 14.dp, vertical = 11.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(34.dp).background(Color(0xFFEFEAFE), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = Color(0xFF6A4FD8), modifier = Modifier.size(17.dp))
                        }
                        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                            Text("REPORT DATE", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF8A7FB5), letterSpacing = 1.sp)
                            Text(dmyToDisplay(reportDmy, "EEE, d MMM yyyy"), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = ClInk)
                        }
                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Color(0xFF8A7FB5), modifier = Modifier.size(20.dp))
                    }
                }
            }
            ClSection("SHIFT CLOSINGS", top = 22.dp)
            ClCard(watermark = 125.dp) {
                ClIconTile(Icons.Filled.ConfirmationNumber, Color(0xFFD93C44), Color(0xFFA32228))
                ClText("TOM · TKM · POS", "Ticket machines, TKM & POS closing", Modifier.weight(1f))
                ClWa { onGuiNote("TOM · TKM · POS") }
            }
            Spacer(modifier = Modifier.height(16.dp))
            ClCard(watermark = 122.dp) {
                ClIconTile(Icons.Filled.DirectionsBus, Color(0xFFFFC04D), Color(0xFFF08C00))
                ClText("RMBS Closing", "Red line station closing format", Modifier.weight(1f))
                ClWa { onGuiNote("RMBS Closing") }
            }
            ClSection("CLAIMS & REPORTS", top = 22.dp)
            // Card Claim: upar icon + text, neeche −/count/+ (green) aur chaura Share.
            // Height FIXED 140dp (trial jaisi — 14 + 52 + 12 + 48 + 14).
            val shape = RoundedCornerShape(22.dp)
            Box(
                modifier = Modifier.fillMaxWidth().height(140.dp).cssShadow(6.dp, shape, Color(0xFF281415).copy(alpha = 0.07f)).clip(shape)
                    .background(Color.White).border(1.dp, ClInk.copy(alpha = 0.06f), shape)
            ) {
                Watermark(219.dp, x = 200.dp, y = (-32).dp, alpha = 0.075f)
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ClIconTile(Icons.Filled.Badge, Color(0xFFD42D35), Color(0xFF0F8F80))
                        ClText("Card Claim", "How many cards to claim?", Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ClStep("−") { cardCount = (cardCount - 1).coerceAtLeast(0) }
                        Box(
                            modifier = Modifier.size(64.dp, 48.dp).background(Color.White, RoundedCornerShape(14.dp)).border(1.3.dp, Color(0xFFE8E1E1), RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) { Text("$cardCount", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = ClInk, textAlign = TextAlign.Center) }
                        ClStep("+") { cardCount = (cardCount + 1).coerceAtMost(999999) }
                        Row(
                            modifier = Modifier.weight(1f).height(48.dp).cssShadow(8.dp, RoundedCornerShape(16.dp), Color(0xFF128C7E).copy(alpha = 0.3f))
                                .clip(RoundedCornerShape(16.dp)).background(cssLinear(135f, 0f to Color(0xFF25D366), 1f to Color(0xFF128C7E)))
                                .clickable { onGuiNote("Card Claim Share") },
                            horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(painterResource(id = R.drawable.img_icon_whatsapp_clean), contentDescription = null, modifier = Modifier.size(26.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Share", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            ClCard(watermark = 122.dp, onClick = { pmbsOpen = true }) {
                ClIconTile(Icons.Filled.EditNote, Color(0xFFFF7A7A), Color(0xFFE53935))
                ClText("PMBS Complaint", "Write an incident complaint letter", Modifier.weight(1f))
                Box(modifier = Modifier.size(32.dp).background(Color(0xFFF5F1F1), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color(0xFF5B6B57), modifier = Modifier.size(18.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        }
        AnimatedVisibility(
            visible = pmbsOpen,
            enter = slideInHorizontally(animationSpec = tween(350, easing = FastOutSlowInEasing)) { it } + fadeIn(animationSpec = tween(350)),
            exit = slideOutHorizontally(animationSpec = tween(350, easing = FastOutSlowInEasing)) { it } + fadeOut(animationSpec = tween(350)),
            modifier = Modifier.fillMaxSize()
        ) {
            ComplaintFormScreen(type = CmpType.PMBS, onBack = { pmbsOpen = false }, onGuiNote = onGuiNote)
        }
        if (reportDateOpen) DatePickerSheet(reportDmy, { reportDmy = it }, { reportDateOpen = false })
    }
}

@Composable
private fun ClSection(title: String, top: Dp) {
    Text(title, fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF8A929C), letterSpacing = 1.sp,
        modifier = Modifier.padding(start = 4.dp, top = top, bottom = 14.dp))
}

// White card: kona 22, padding 14, halki border + saya, watermark daein,
// height FIXED 80dp (trial .cl-card jaisi — icon 52 + padding 28, har font size pe barabar).
@Composable
private fun ClCard(watermark: Dp, onClick: (() -> Unit)? = null, content: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit) {
    val shape = RoundedCornerShape(22.dp)
    Box(
        modifier = Modifier.fillMaxWidth().height(80.dp).cssShadow(6.dp, shape, Color(0xFF281415).copy(alpha = 0.07f)).clip(shape)
            .background(Color.White).border(1.dp, ClInk.copy(alpha = 0.06f), shape)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Watermark(watermark, x = 270.dp, y = (-21).dp, alpha = 0.075f)
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically, content = content)
    }
}

@Composable
private fun ClIconTile(icon: ImageVector, from: Color, to: Color) {
    Box(
        modifier = Modifier.size(52.dp).cssShadow(8.dp, RoundedCornerShape(16.dp), Color.Black.copy(alpha = 0.14f))
            .background(cssLinear(145f, 0f to from, 1f to to), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) { Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp)) }
}

@Composable
private fun ClText(title: String, sub: String, modifier: Modifier) {
    Column(modifier = modifier.padding(start = 14.dp, end = 10.dp)) {
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ClInk)
        Spacer(modifier = Modifier.height(2.dp))
        Text(sub, fontSize = 12.5.sp, color = ClMuted, lineHeight = 15.5.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}

// 48dp green WhatsApp square (kona 16) — asli WhatsApp logo, white background ke baghair,
// green button ke upar seedha (clean asset: background flood-fill se transparent).
@Composable
private fun ClWa(onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(48.dp).cssShadow(8.dp, RoundedCornerShape(16.dp), Color(0xFF128C7E).copy(alpha = 0.3f))
            .clip(RoundedCornerShape(16.dp)).background(cssLinear(135f, 0f to Color(0xFF25D366), 1f to Color(0xFF128C7E))).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) { Image(painterResource(id = R.drawable.img_icon_whatsapp_clean), contentDescription = "WhatsApp", modifier = Modifier.size(36.dp)) }
}

@Composable
private fun ClStep(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(44.dp, 48.dp).clip(RoundedCornerShape(14.dp)).background(Color(0xFFF9F5F5))
            .border(1.3.dp, Color(0xFFE8E1E1), RoundedCornerShape(14.dp)).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) { Text(label, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F8F80)) }
}
