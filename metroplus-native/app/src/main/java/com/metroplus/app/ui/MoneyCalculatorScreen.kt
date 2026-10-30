package com.metroplus.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.metroplus.app.R
import java.util.Calendar
import java.util.Locale

// Money Calculator (Cash Denominations) — trial 2.1.63 ke moneyCalcScreen ki EXACT copy
// (computed CSS, 1px = 1dp): 44dp gol header buttons, neela-purple total card (kona 24),
// Clear/Copy/WhatsApp (kona 18), "TODAY'S TOM CLOSING" card, 59dp denom rows (kona 18),
// note jaise gradient badges (andar dashed lakeer), grey stepper.
private data class Denom(val value: Int, val gradStart: Color, val gradEnd: Color, val text: Color = Color.White)

// Trial me default dikhne wale 7 (75 / 5 / 2 / 1 settings se on hote hain).
private val visibleDenoms = listOf(
    Denom(5000, Color(0xFFF5C542), Color(0xFFB8860B)),
    Denom(1000, Color(0xFF5B8DEF), Color(0xFF2A4FB8)),
    Denom(500, Color(0xFF2DD4BF), Color(0xFF0E8C7E)),
    Denom(100, Color(0xFFFF6B6B), Color(0xFFC1121F)),
    Denom(50, Color(0xFFA78BFA), Color(0xFF6D28D9)),
    Denom(20, Color(0xFFFB923C), Color(0xFFC2410C)),
    Denom(10, Color(0xFFD6A46B), Color(0xFF8A5A2B))
)

private val McInk = Color(0xFF14181C)

private fun formatRs(n: Long): String = String.format(Locale.US, "%,d", n)

// Pakistani numbering: crore / lakh / thousand / hundred.
private fun amountInWords(n: Long): String {
    if (n == 0L) return "Zero Rupees Only"
    val ones = arrayOf("", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen")
    val tens = arrayOf("", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety")
    fun under100(x: Long): String = when {
        x < 20 -> ones[x.toInt()]
        x % 10L == 0L -> tens[(x / 10).toInt()]
        else -> tens[(x / 10).toInt()] + " " + ones[(x % 10).toInt()]
    }
    fun under1000(x: Long): String {
        val h = x / 100
        val r = x % 100
        return (if (h > 0) ones[h.toInt()] + " Hundred" + (if (r > 0) " " else "") else "") + (if (r > 0) under100(r) else "")
    }
    var rest = n
    val parts = ArrayList<String>()
    val crore = rest / 10000000
    if (crore > 0) { parts.add(under1000(crore) + " Crore"); rest %= 10000000 }
    val lakh = rest / 100000
    if (lakh > 0) { parts.add(under1000(lakh) + " Lakh"); rest %= 100000 }
    val thou = rest / 1000
    if (thou > 0) { parts.add(under1000(thou) + " Thousand"); rest %= 1000 }
    if (rest > 0) parts.add(under1000(rest))
    return parts.joinToString(" ") + " Rupees Only"
}

// Trial badge ka ::before — 3dp andar, kona 6, 0.67dp dashed safed 45%.
private fun Modifier.dashedInnerBorder(): Modifier = drawBehind {
    val inset = 3.dp.toPx()
    val stroke = Stroke(width = 0.67.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 3f), 0f))
    drawRoundRect(
        color = Color.White.copy(alpha = 0.45f), topLeft = Offset(inset, inset),
        size = androidx.compose.ui.geometry.Size(size.width - 2 * inset, size.height - 2 * inset),
        cornerRadius = CornerRadius(6.dp.toPx()), style = stroke
    )
}

// Counts ViewModel me — page band karke dobara kholo to likha hua wapas mile.
class MoneyCalcViewModel : ViewModel() {
    val counts = mutableStateOf<Map<Int, Int>>(emptyMap())
}

@Composable
fun MoneyCalculatorScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    BackHandler { onBack() }
    val vm: MoneyCalcViewModel = viewModel()
    var counts by vm.counts
    fun countOf(v: Int) = counts[v] ?: 0
    fun setCount(v: Int, c: Int) { counts = counts + (v to c.coerceIn(0, 999999)) }
    val totalNotes = counts.values.sum()
    val total = counts.entries.sumOf { (v, c) -> v.toLong() * c }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF3EDED))) {
        // Header: white 94%, 44dp gol buttons (halki border): back | "Cash Denominations" | sliders + laal reset.
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.White.copy(alpha = 0.94f)).statusBarsPadding().padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            McCircle(Icons.Filled.ArrowBack, McInk, "Back", onBack)
            Text("Cash Denominations", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = McInk, letterSpacing = (-0.3).sp,
                textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
            McCircle(Icons.Filled.Tune, McInk, "Manage Denominations") { onGuiNote("Denomination Settings") }
            Spacer(modifier = Modifier.width(8.dp))
            McCircle(Icons.Filled.Refresh, Color(0xFFFF5757), "Reset All") { counts = emptyMap() }
        }
        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { TotalCard(totalNotes, total) }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ActionBtn(1f, Icons.Filled.Delete, "Clear All", Color(0xFFFF5E5E), Color(0xFFFF5E5E), FontWeight.SemiBold,
                        Modifier.background(Color(0xFFEF4444).copy(alpha = 0.12f)), Color(0xFFEF4444).copy(alpha = 0.3f)) { counts = emptyMap() }
                    ActionBtn(1f, Icons.Filled.ContentCopy, "Copy", Color.White, Color(0xFFFF4D55), FontWeight.Bold,
                        Modifier.background(cssLinear(135f, 0f to Color(0xFF2A2A2E), 1f to Color(0xFF121214))), Color(0xFFE11D2A).copy(alpha = 0.55f)) { onGuiNote("Copy") }
                    ActionBtn(1.3f, Icons.Filled.Chat, "WhatsApp", Color.White, Color.White, FontWeight.Bold,
                        Modifier.cssShadow(6.dp, RoundedCornerShape(18.dp), Color(0xFF25D366).copy(alpha = 0.35f)).background(cssLinear(135f, 0f to Color(0xFF25D366), 1f to Color(0xFF128C7E)), RoundedCornerShape(18.dp)),
                        null) { onGuiNote("WhatsApp") }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
            item { TomClosingCard() }
            item {
                Text("PAKISTANI CURRENCY NOTES & COINS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7A7E8E), letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 6.dp, bottom = 12.dp))
            }
            items(visibleDenoms, key = { it.value }) { d -> DenomRow(d, countOf(d.value)) { setCount(d.value, it) } }
            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
    }
}

@Composable
private fun McCircle(icon: ImageVector, tint: Color, desc: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(44.dp).clip(CircleShape).background(Color.White).border(1.dp, McInk.copy(alpha = 0.09f), CircleShape).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) { Icon(icon, contentDescription = desc, tint = tint, modifier = Modifier.size(19.dp)) }
}

// Total card: 135° #5B6CFF→#7A4DFF→#A855F7, kona 24, padding 20, glow + watermark, "Rs." + 34sp amount,
// italic in-words, daein 84dp safed gol me paise ki tasveer.
@Composable
private fun TotalCard(totalNotes: Int, total: Long) {
    val shape = RoundedCornerShape(24.dp)
    Box(
        modifier = Modifier.fillMaxWidth().cssShadow(16.dp, shape, Color(0xFF6E50FF).copy(alpha = 0.35f)).clip(shape)
            .background(cssLinear(135f, 0f to Color(0xFF5B6CFF), 0.55f to Color(0xFF7A4DFF), 1f to Color(0xFFA855F7)))
    ) {
        Glow(160.dp, x = 40.dp, y = (-50).dp, alpha = 0.35f, align = Alignment.TopEnd)
        Watermark(189.dp, x = 235.dp, y = (-32).dp, alpha = 0.2f)
        Row(modifier = Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("TOTAL CASH AMOUNT", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.85f), letterSpacing = 0.8.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("$totalNotes NOTES", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color.White, letterSpacing = 0.8.sp,
                        modifier = Modifier.background(Color.White.copy(alpha = 0.22f), RoundedCornerShape(12.dp)).padding(horizontal = 8.dp, vertical = 3.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("Rs.", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.85f), letterSpacing = (-0.5).sp, modifier = Modifier.padding(bottom = 3.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(formatRs(total), fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = (-0.5).sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(amountInWords(total), fontSize = 11.5.sp, fontStyle = FontStyle.Italic, color = Color.White.copy(alpha = 0.8f))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                painterResource(id = R.drawable.img_money), contentDescription = "Cash", contentScale = ContentScale.Crop,
                modifier = Modifier.size(84.dp).cssShadow(4.dp, CircleShape, McInk.copy(alpha = 0.15f)).background(Color.White, CircleShape).clip(CircleShape)
            )
        }
    }
}

@Composable
private fun RowScope.ActionBtn(
    weight: Float, icon: ImageVector, label: String, fg: Color, iconTint: Color, fw: FontWeight,
    bg: Modifier, border: Color?, onClick: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier = Modifier.weight(weight).height(41.dp).then(bg).clip(shape)
            .then(if (border != null) Modifier.border(1.dp, border, shape) else Modifier)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(15.dp))
        Spacer(modifier = Modifier.width(7.dp))
        Text(label, fontSize = 13.sp, fontWeight = fw, color = fg)
    }
}

// "TODAY'S TOM CLOSING": white→#FBF2F2 (160°), kona 22, padding 14, upar kicker + station·shift,
// daein "—" / "Closing total", neeche note (closing save nahi hua).
@Composable
private fun TomClosingCard() {
    val shape = RoundedCornerShape(22.dp)
    val shift = if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 12) "Morning" else "Evening"
    Box(
        modifier = Modifier.fillMaxWidth().cssShadow(6.dp, shape, Color(0xFF3C1416).copy(alpha = 0.08f)).clip(shape)
            .background(cssLinear(160f, 0f to Color.White, 1f to Color(0xFFFBF2F2))).border(1.dp, Color(0xFF9E1F25).copy(alpha = 0.18f), shape)
    ) {
        Watermark(212.dp, x = 218.dp, y = (-35).dp, alpha = 0.075f)
        Column(modifier = Modifier.padding(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.PointOfSale, contentDescription = null, tint = Color(0xFF9E1F25), modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("TODAY'S TOM CLOSING", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF9E1F25), letterSpacing = 1.sp)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Shaheed-e-Millat · $shift", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = McInk, maxLines = 1)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("—", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = McInk)
                    Text("Closing total", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7280))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Today's closing is not saved yet. Save the closing in TOM Register to match it with your cash.",
                fontSize = 13.sp, color = Color(0xFF6B7280), lineHeight = 19.sp,
                modifier = Modifier.fillMaxWidth().background(Color(0xFFFAF6F6), RoundedCornerShape(14.dp)).padding(12.dp)
            )
        }
    }
}

// Denom row: white, kona 18, padding 10/14; note badge (gradient, kona 9, dashed andar) × stepper | subtotal.
@Composable
private fun DenomRow(d: Denom, c: Int, onCount: (Int) -> Unit) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            .cssShadow(2.dp, shape, McInk.copy(alpha = 0.05f)).background(Color.White, shape).border(1.dp, McInk.copy(alpha = 0.08f), shape)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.cssShadow(4.dp, RoundedCornerShape(9.dp), Color.Black.copy(alpha = 0.14f))
                .background(cssLinear(135f, 0f to d.gradStart, 1f to d.gradEnd), RoundedCornerShape(9.dp))
                .defaultMinSize(minWidth = 56.dp).height(32.dp).dashedInnerBorder().padding(horizontal = 9.dp),
            contentAlignment = Alignment.Center
        ) { Text("${d.value}", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = d.text, letterSpacing = 0.3.sp) }
        Text("×", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF9AA1AB), modifier = Modifier.padding(horizontal = 6.dp))
        Row(
            modifier = Modifier.background(McInk.copy(alpha = 0.05f), RoundedCornerShape(14.dp)).border(1.dp, McInk.copy(alpha = 0.1f), RoundedCornerShape(14.dp))
                .padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StepButton("−") { onCount(c - 1) }
            BasicTextField(
                value = if (c == 0) "" else "$c",
                onValueChange = { t -> onCount(t.filter { it.isDigit() }.take(6).toIntOrNull() ?: 0) },
                singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = McInk, textAlign = TextAlign.Center),
                modifier = Modifier.width(68.dp).padding(horizontal = 4.dp),
                decorationBox = { inner ->
                    Box(contentAlignment = Alignment.Center) {
                        if (c == 0) Text("0", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9AA1AB))
                        inner()
                    }
                }
            )
            StepButton("+") { onCount(c + 1) }
        }
        Text("Rs. ${formatRs(d.value.toLong() * c)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD3343C), letterSpacing = (-0.2).sp,
            textAlign = TextAlign.End, maxLines = 1, modifier = Modifier.weight(1f).padding(start = 6.dp))
    }
}

@Composable
private fun StepButton(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(30.dp).clip(RoundedCornerShape(10.dp)).background(Color.White)
            .border(1.dp, McInk.copy(alpha = 0.10f), RoundedCornerShape(10.dp)).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) { Text(label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = McInk) }
}
