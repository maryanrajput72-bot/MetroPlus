package com.metroplus.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metroplus.app.R

// v1.62: 63 ke woh GUI pages jo native me nahi the — SIRF GUI, koi wiring nahi.
// Login/Signup/Onboarding is me NAHI hain (uska order: wo wo khud baad me
// naye advance tareeqe se banayega). Har page ka rasta 63 jaisa hai:
// Settings ki rows se (System & Storage, WhatsApp Format, App Info, Chat
// Settings), TL lock Settings ki TL/RP Login row se, TOM Count Tom Data se.

private val XBg = Color(0xFFF6F2F2)
private val XInk = Color(0xFF14181C)
private val XMuted = Color(0xFF6B7280)

@Composable
private fun XHeader(title: String, onBack: () -> Unit) {
    val hShape = RoundedCornerShape(bottomStart = 26.dp, bottomEnd = 26.dp)
    Box(
        modifier = Modifier.fillMaxWidth().cssShadow(12.dp, hShape, Color(0xFF15803D).copy(alpha = 0.3f)).clip(hShape)
            .background(cssLinear(135f, 0f to Color(0xFF34D399), 0.55f to Color(0xFF16A34A), 1f to Color(0xFF0F766E)))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.size(38.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)).clickable { onBack() },
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(20.dp)) }
            Text(title, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = (-0.3).sp)
            Spacer(modifier = Modifier.size(38.dp))
        }
    }
}

@Composable
private fun XPage(title: String, onBack: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    BackHandler { onBack() }
    Column(modifier = Modifier.fillMaxSize().background(XBg)) {
        XHeader(title, onBack)
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), content = content)
    }
}

@Composable
private fun XCard(content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = Modifier.fillMaxWidth().cssShadow(6.dp, shape, Color.Black.copy(alpha = 0.07f))
            .background(Color.White, shape).padding(16.dp),
        content = content
    )
}

@Composable
private fun XSectionTitle(text: String) {
    Text(text, fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold, color = XMuted, letterSpacing = 1.sp,
        modifier = Modifier.padding(top = 16.dp, bottom = 6.dp))
}

@Composable
private fun XRow(icon: ImageVector, iconBg: Color, iconTint: Color, label: String, desc: String,
                 trailing: @Composable RowScope.() -> Unit = { Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color(0xFFB4BCB2), modifier = Modifier.size(20.dp)) },
                 onClick: (() -> Unit)? = null) {
    Row(
        modifier = Modifier.fillMaxWidth().then(if (onClick != null) Modifier.clickable { onClick() } else Modifier).padding(vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(38.dp).clip(RoundedCornerShape(12.dp)).background(iconBg),
            contentAlignment = Alignment.Center
        ) { Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(19.dp)) }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = XInk)
            Text(desc, fontSize = 12.5.sp, color = XMuted)
        }
        Spacer(modifier = Modifier.width(8.dp))
        trailing()
    }
}

@Composable
private fun XDivider() = Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF2F0F0)))

@Composable
private fun XGreenButton(text: String, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier.fillMaxWidth().height(50.dp).cssShadow(8.dp, shape, Color(0xFF16A34A).copy(alpha = 0.3f)).clip(shape)
            .background(cssLinear(135f, 0f to Color(0xFF34D399), 1f to Color(0xFF16A34A))).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) { Text(text, fontSize = 15.5.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) }
}

@Composable
private fun XChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(999.dp)
    Box(
        modifier = Modifier.clip(shape)
            .background(if (selected) Color(0xFF16A34A) else Color(0xFFF2F0F0))
            .border(1.dp, if (selected) Color(0xFF16A34A) else Color(0xFFE5E0E0), shape)
            .clickable { onClick() }.padding(horizontal = 16.dp, vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) { Text(label, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = if (selected) Color.White else XInk) }
}

// ---------------- TL PIN Lock (63 ke premium lock ki GUI) ----------------
// Spec (uska 2026-10-10 order): blurred home ke upar safed card, sabz/sunehri
// 3D padlock, "Enter Your 5 Digit PIN" + 5 boxes. GUI phase: PIN bharne par
// sirf note — asli lock/login wiring baad me. Back se wapas Settings.

@Composable
fun TlLockScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    BackHandler { onBack() }
    var pin by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()) {
        // Blurred home ki jagah gehra gradient + halke gole (GUI demo).
        Box(modifier = Modifier.fillMaxSize().background(cssLinear(135f, 0f to Color(0xFF1A0B0D), 0.5f to Color(0xFF8E1820), 1f to Color(0xFFD3343C))))
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.35f)))
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
            Box(
                modifier = Modifier.statusBarsPadding().padding(16.dp).size(38.dp).clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)).clickable { onBack() },
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(20.dp)) }
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
        ) {
            val cardShape = RoundedCornerShape(26.dp)
            Column(
                modifier = Modifier.fillMaxWidth().cssShadow(16.dp, cardShape, Color.Black.copy(alpha = 0.3f))
                    .background(Color.White, cardShape).padding(horizontal = 22.dp, vertical = 26.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painterResource(id = R.drawable.img_tl_padlock), contentDescription = "Padlock",
                    contentScale = ContentScale.Fit, modifier = Modifier.size(76.dp))
                Spacer(modifier = Modifier.height(14.dp))
                Text("Enter Your 5 Digit PIN", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = XInk)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Premium access for team leads", fontSize = 12.5.sp, color = XMuted)
                Spacer(modifier = Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    for (i in 0 until 5) {
                        val filled = i < pin.length
                        Box(
                            modifier = Modifier.size(48.dp, 56.dp)
                                .border(1.5.dp, if (filled) Color(0xFF16A34A) else Color(0xFFE5E0E0), RoundedCornerShape(14.dp))
                                .background(if (filled) Color(0xFFE2F7EA) else Color.White, RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (filled) Text(pin[i].toString(), fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = XInk)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(22.dp))
                val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "X")
                keys.chunked(3).forEach { rowKeys ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        rowKeys.forEach { k ->
                            Box(
                                modifier = Modifier.size(64.dp).clip(CircleShape)
                                    .background(if (k.isEmpty()) Color.Transparent else Color(0xFFF6F2F2))
                                    .clickable {
                                        if (k == "X") { if (pin.isNotEmpty()) pin = pin.dropLast(1) }
                                        else if (k.isNotEmpty() && pin.length < 5) {
                                            pin += k
                                            if (pin.length == 5) onGuiNote("TL PIN Login")
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (k == "X") Icon(Icons.Filled.Close, contentDescription = "Delete", tint = XInk, modifier = Modifier.size(20.dp))
                                else if (k.isNotEmpty()) Text(k, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = XInk)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text("Cancel", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE53935),
                    modifier = Modifier.clickable { onBack() }.padding(8.dp))
            }
        }
    }
}

// ---------------- Chat Settings (63 ki chatSettingsScreen ki GUI) ----------------

@Composable
fun ChatSettingsScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit, onOpenWaFormat: () -> Unit) {
    var size by remember { mutableStateOf("Medium") }
    var color by remember { mutableStateOf(Color(0xFFB02A31)) }
    var bg by remember { mutableStateOf("Default") }
    XPage("Chat Settings", onBack) {
        XCard {
            Text("Bubble Size", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = XInk)
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("Small", "Medium", "Large").forEach { s -> XChip(s, size == s) { size = s } }
            }
        }
        XSectionTitle("BUBBLE COLOR")
        XCard {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                listOf(Color(0xFFB02A31), Color(0xFF2563EB), Color(0xFF16A34A), Color(0xFF7C3AED), Color(0xFFEA580C)).forEach { c ->
                    Box(
                        modifier = Modifier.size(40.dp).clip(CircleShape).background(c)
                            .border(if (color == c) 3.dp else 1.dp, if (color == c) Color.White else Color(0xFFE5E0E0), CircleShape)
                            .clickable { color = c },
                        contentAlignment = Alignment.Center
                    ) { if (color == c) Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp)) }
                }
            }
        }
        XSectionTitle("BACKGROUND")
        XCard {
            listOf("Default", "Light", "Dark").forEach { b ->
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { bg = b }.padding(vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(b, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = XInk, modifier = Modifier.weight(1f))
                    if (bg == b) Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(20.dp))
                }
                if (b != "Dark") XDivider()
            }
        }
        XSectionTitle("FORMATS")
        XCard {
            XRow(Icons.Filled.Share, Color(0xFFE2F7EA), Color(0xFF16A34A), "WhatsApp Report Format", "Opening and closing formats",
                onClick = onOpenWaFormat)
        }
        Spacer(modifier = Modifier.height(18.dp))
        XGreenButton("Reset Chat Settings") { onGuiNote("Reset Chat Settings") }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

// ---------------- App Info (63 ki appInfoScreen ki GUI) ----------------

@Composable
fun AppInfoScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit, onCheckUpdate: () -> Unit, onOpenSystemStorage: () -> Unit) {
    val context = LocalContext.current
    val (version, pkg) = remember {
        try {
            val info = context.packageManager.getPackageInfo(context.packageName, 0)
            (info.versionName ?: "1.62") to context.packageName
        } catch (e: Exception) { "1.62" to "com.metroplus.app" }
    }
    XPage("App Info", onBack) {
        XCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painterResource(id = R.drawable.logo_hd), contentDescription = "Metro + logo", modifier = Modifier.size(56.dp))
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text("Metro +", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = XInk)
                    Text("Daily shift companion for metro staff", fontSize = 12.5.sp, color = XMuted)
                }
            }
        }
        XSectionTitle("ABOUT")
        XCard {
            XRow(Icons.Filled.Info, Color(0xFFF2E2E3), Color(0xFF2563EB), "App Version", version, trailing = {})
            XDivider()
            XRow(Icons.Filled.Info, Color(0xFFF2E2E3), Color(0xFF2563EB), "Package", pkg, trailing = {})
        }
        XSectionTitle("UPDATE")
        XCard {
            Text("App Update", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = XInk)
            Text("Installed: $version", fontSize = 12.5.sp, color = XMuted)
            Spacer(modifier = Modifier.height(12.dp))
            XGreenButton("Check Update") { onCheckUpdate() }
        }
        XSectionTitle("STORAGE")
        XCard {
            XRow(Icons.Filled.Storage, Color(0xFFE0E1E3), Color(0xFF334155), "System & Storage", "Sync, cache and data",
                onClick = onOpenSystemStorage)
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

// ---------------- System & Storage (63 ki systemStorageScreen ki GUI) ----------------

@Composable
fun SystemStorageScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    XPage("System & Storage", onBack) {
        XCard {
            Row {
                SysStat("12.4 MB", "App size", Modifier.weight(1f))
                SysStat("2.1 MB", "Cache (demo)", Modifier.weight(1f))
                SysStat("0.8 MB", "Data (demo)", Modifier.weight(1f))
            }
        }
        XSectionTitle("DATA")
        XCard {
            XRow(Icons.Filled.Sync, Color(0xFFE2F7EA), Color(0xFF16A34A), "Sync Data", "Sync with server",
                trailing = { Text("Sync Now", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA580C)) },
                onClick = { onGuiNote("Sync Data") })
            XDivider()
            XRow(Icons.Filled.Delete, Color(0xFFF2E2E3), Color(0xFF2563EB), "Clear Cache", "Clear",
                trailing = { Text("Clear", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = XMuted) },
                onClick = { onGuiNote("Clear Cache") })
            XDivider()
            XRow(Icons.Filled.Upload, Color(0xFFE2F7EA), Color(0xFF16A34A), "Export Data", "Save a backup file",
                onClick = { onGuiNote("Export Data") })
            XDivider()
            XRow(Icons.Filled.Download, Color(0xFFE2F7EA), Color(0xFF16A34A), "Import Data", "Restore from a backup file",
                onClick = { onGuiNote("Import Data") })
        }
        XSectionTitle("DANGER ZONE")
        XCard {
            XRow(Icons.Filled.Delete, Color(0xFFFDECEE), Color(0xFFE53935), "Delete All Data", "Delete all saved data and reset the app",
                trailing = { Text("Delete", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE53935)) },
                onClick = { onGuiNote("Delete All Data") })
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun RowScope.SysStat(value: String, label: String, modifier: Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = XInk)
        Text(label, fontSize = 11.5.sp, color = XMuted, textAlign = TextAlign.Center)
    }
}

// ---------------- WhatsApp Format (63 ki waFormatScreen ki GUI) ----------------

@Composable
fun WaFormatScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    var paste by remember { mutableStateOf("") }
    var opening by remember { mutableStateOf("Opening Report\nStation: {station}\nShift: {shift}\nDate: {date}") }
    var closing by remember { mutableStateOf("Closing Report\nStation: {station}\nShift: {shift}\nDate: {date}\nTotal: {total}") }
    XPage("WhatsApp Format", onBack) {
        XSectionTitle("AUTOMATIC SETUP")
        XCard {
            Text("Paste your format here, the fields below fill from it (GUI demo).", fontSize = 12.5.sp, color = XMuted, lineHeight = 18.sp)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = paste, onValueChange = { paste = it }, placeholder = { Text("Paste your format here...") },
                modifier = Modifier.fillMaxWidth(), minLines = 4)
        }
        XSectionTitle("OPENING REPORT FORMAT")
        XCard {
            OutlinedTextField(value = opening, onValueChange = { opening = it }, modifier = Modifier.fillMaxWidth(), minLines = 5)
        }
        XSectionTitle("CLOSING REPORT FORMAT")
        XCard {
            OutlinedTextField(value = closing, onValueChange = { closing = it }, modifier = Modifier.fillMaxWidth(), minLines = 6)
        }
        Spacer(modifier = Modifier.height(18.dp))
        XGreenButton("Save Formats") { onGuiNote("Save formats") }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

// ---------------- TOM Machines (63 ki tomCountScreen ki GUI) ----------------

@Composable
fun TomCountScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    var count by remember { mutableStateOf(3) }
    val options = listOf("TOM A", "TOM A, TOM B", "TOM A, TOM B, TOM C", "TOM A, TOM B, TOM C, TOM D", "TOM A, TOM B, TOM C, TOM D, TOM E")
    XPage("TOM Machines", onBack) {
        XCard {
            Text("How many TOM machines does this station have? The dashboard, Enter Data and auto-fill will show that many.",
                fontSize = 13.5.sp, color = XMuted, lineHeight = 20.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
        XCard {
            options.forEachIndexed { i, label ->
                val n = i + 1
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { count = n }.padding(vertical = 11.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(22.dp).clip(CircleShape)
                            .border(2.dp, if (count == n) Color(0xFF16A34A) else Color(0xFFD1D5DB), CircleShape)
                            .background(if (count == n) Color(0xFF16A34A) else Color.Transparent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) { if (count == n) Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp)) }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = XInk)
                }
                if (i < options.lastIndex) XDivider()
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        XGreenButton("Done") { onBack() }
        Spacer(modifier = Modifier.height(8.dp))
        Text("GUI phase: count selection saves in a later step.", fontSize = 12.sp, color = XMuted, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(20.dp))
    }
}

// ---------------- GPS Photo Viewer (63 ke viewer/crop modal ki GUI) ----------------

@Composable
fun PhotoViewerScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    BackHandler { onBack() }
    var cropOn by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.size(38.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.15f)).clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(20.dp)) }
                Text("GPS Photo", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Box(
                    modifier = Modifier.size(38.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.15f)).clickable { onGuiNote("Share on WhatsApp") },
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.Share, contentDescription = "Share", tint = Color.White, modifier = Modifier.size(19.dp)) }
            }
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Image, contentDescription = null, tint = Color.White.copy(alpha = 0.4f), modifier = Modifier.size(56.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("GPS photo preview (demo)", fontSize = 13.5.sp, color = Color.White.copy(alpha = 0.7f))
                    Text("Pak Secretariat — demo stamp", fontSize = 12.5.sp, color = Color.White.copy(alpha = 0.5f))
                }
                if (cropOn) {
                    Box(
                        modifier = Modifier.fillMaxWidth(0.86f).height(260.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text("Crop frame (demo)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black,
                            modifier = Modifier.background(Color.White, RoundedCornerShape(8.dp)).padding(horizontal = 10.dp, vertical = 4.dp))
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val cropShape = RoundedCornerShape(16.dp)
                Box(
                    modifier = Modifier.weight(1f).height(50.dp).clip(cropShape)
                        .background(if (cropOn) Color(0xFFB02A31) else Color.White.copy(alpha = 0.15f))
                        .clickable { cropOn = !cropOn },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Crop, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (cropOn) "Crop On" else "Crop", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
                Box(
                    modifier = Modifier.weight(1f).height(50.dp).clip(cropShape)
                        .background(cssLinear(135f, 0f to Color(0xFF25D366), 1f to Color(0xFF128C7E)))
                        .clickable { onGuiNote("Share on WhatsApp") },
                    contentAlignment = Alignment.Center
                ) { Text("Share on WhatsApp", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) }
            }
        }
    }
}

// ---------------- Splash (app khulte hi, 1.5s, phir Home) ----------------

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(cssLinear(135f, 0f to Color(0xFF1A0B0D), 0.5f to Color(0xFF8E1820), 1f to Color(0xFFD3343C))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painterResource(id = R.drawable.logo_hd), contentDescription = "Metro + logo", modifier = Modifier.size(120.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Metro +", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = (-0.5).sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text("Daily Shift Companion", fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.8f))
        }
    }
}

// ---------------- Premium Request modal (drawer TL/RP page ke liye) ----------------

@Composable
fun PremiumRequestModal(onClose: () -> Unit, onGuiNote: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var gmail by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.55f)).clickable { onClose() },
        contentAlignment = Alignment.Center) {
        val shape = RoundedCornerShape(24.dp)
        Column(
            modifier = Modifier.fillMaxWidth(0.88f).cssShadow(16.dp, shape, Color.Black.copy(alpha = 0.3f))
                .background(Color.White, shape).clickable {}.padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Request Premium Access", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = XInk, modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFF6F2F2)).clickable { onClose() },
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.Close, contentDescription = "Close", tint = XInk, modifier = Modifier.size(17.dp)) }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text("Admin ko request jayegi — TL ya RP access ke liye.", fontSize = 12.5.sp, color = XMuted, lineHeight = 18.sp)
            Spacer(modifier = Modifier.height(14.dp))
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Your name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = gmail, onValueChange = { gmail = it }, label = { Text("Gmail") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            XGreenButton("Send Request") { onClose(); onGuiNote("Premium Request") }
        }
    }
}

// ---------------- Attendance popup (GPS camera page ke liye) ----------------

@Composable
fun AttendancePopup(onClose: () -> Unit, onGuiNote: (String) -> Unit) {
    val staff = listOf(
        "Ahsan" to "Present", "Shehryar" to "Present", "Tahir" to "Present",
        "Shahid" to "Absent", "Usman" to "Present"
    )
    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)).clickable { onClose() },
        contentAlignment = Alignment.BottomCenter) {
        val shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
        Column(
            modifier = Modifier.fillMaxWidth().cssShadow(16.dp, shape, Color.Black.copy(alpha = 0.3f))
                .background(Color.White, shape).clickable {}.padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Groups, contentDescription = null, tint = Color(0xFFB02A31), modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("Staff Attendance", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = XInk, modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFF6F2F2)).clickable { onClose() },
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.Close, contentDescription = "Close", tint = XInk, modifier = Modifier.size(17.dp)) }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("Today — Shaheed-e-Millat (demo)", fontSize = 12.5.sp, color = XMuted)
            Spacer(modifier = Modifier.height(14.dp))
            staff.forEach { (name, status) ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFF6F2F2)),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Filled.Person, contentDescription = null, tint = XMuted, modifier = Modifier.size(18.dp)) }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = XInk, modifier = Modifier.weight(1f))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (status == "Present") Icons.Filled.CheckCircle else Icons.Filled.Cancel,
                            contentDescription = null,
                            tint = if (status == "Present") Color(0xFF16A34A) else Color(0xFF9CA3AF),
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(status, fontSize = 13.sp, fontWeight = FontWeight.Bold,
                            color = if (status == "Present") Color(0xFF16A34A) else Color(0xFF9CA3AF))
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            XGreenButton("Share on WhatsApp") { onGuiNote("Share Attendance") }
        }
    }
}
