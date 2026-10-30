package com.metroplus.app.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// 63 ka Portal interface, native Compose me (GUI phase).
// 63 me Portal ek iframe tha (GCS site app ke andar khulti thi). Native me
// WebView SAKHT MANA hai (uska rule), is liye site yahan nahi khulti —
// "Open in Browser" button usay bahar browser me kholta hai. 63 ki baaki
// har cheez yahan hai: header ke 3 buttons (Fill / Saved Login / Reload),
// Saved Login sheet, Data-Fill sheet aur Quick Fill actions (Auto Fill,
// TOM A/B, TKM, ASKARI). Save/fill ki wiring agle step me hogi.

private val PoRed = Color(0xFF9E1F25)
private val PoInk = Color(0xFF14181C)
private val PoGray = Color(0xFF5B6470)
private const val PORTAL_URL = "https://shababulmohsinapp.ai.studio/"

@Composable
private fun PoHeaderBtn(icon: @Composable () -> Unit, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(38.dp).clip(CircleShape).background(Color.White)
            .border(1.dp, PoInk.copy(alpha = 0.09f), CircleShape).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) { icon() }
}

@Composable
private fun PoCard(title: String?, content: @Composable () -> Unit) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier = Modifier.fillMaxWidth().cssShadow(5.dp, shape, PoInk.copy(alpha = 0.08f))
            .background(Color.White, shape).border(1.dp, PoInk.copy(alpha = 0.06f), shape).padding(16.dp)
    ) {
        if (title != null) {
            Text(title, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = PoInk.copy(alpha = 0.55f), letterSpacing = 1.2.sp)
            Spacer(modifier = Modifier.height(10.dp))
        }
        content()
    }
}

@Composable
private fun PoField(label: String, icon: @Composable () -> Unit, value: String, onValue: (String) -> Unit, placeholder: String, password: Boolean = false) {
    var showPass by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(12.dp)
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)) {
        icon()
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = PoGray)
    }
    BasicTextField(
        value = value, onValueChange = onValue, singleLine = true,
        visualTransformation = if (password && !showPass) PasswordVisualTransformation() else VisualTransformation.None,
        textStyle = TextStyle(fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = PoInk),
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { inner ->
            Box(
                modifier = Modifier.fillMaxWidth().clip(shape).background(Color(0xFFF6F2F2)).border(1.dp, PoInk.copy(alpha = 0.1f), shape)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) Text(placeholder, fontSize = 13.5.sp, color = PoInk.copy(alpha = 0.35f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.weight(1f)) { inner() }
                    if (password) {
                        Icon(
                            if (showPass) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = null, tint = PoGray, modifier = Modifier.size(18.dp).clickable { showPass = !showPass }
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun PoQuickRow(color: Color, label: String, sub: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(color),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.List, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp)) }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PoInk)
            Text(sub, fontSize = 12.5.sp, color = PoGray)
        }
    }
}

@Composable
fun PortalScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    var showLogin by remember { mutableStateOf(false) }
    var showFill by remember { mutableStateOf(false) }
    // Sheet khuli ho to Back pehle sheet band kare, phir page (History wala pattern).
    BackHandler { if (showLogin) showLogin = false else if (showFill) showFill = false else onBack() }
    val ctx = LocalContext.current
    fun openBrowser() {
        try {
            ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PORTAL_URL)))
        } catch (e: Exception) {
            onGuiNote("Open Portal")
        }
    }
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF3EDED))
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 63 jaisa header: back | Online Portal | Fill + Key + Reload.
            Row(
                modifier = Modifier.fillMaxWidth().background(Color.White.copy(alpha = 0.94f)).statusBarsPadding().padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PoHeaderBtn({ Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = PoInk, modifier = Modifier.size(18.dp)) }, onBack)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Online Portal", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = PoInk, letterSpacing = (-0.3).sp, modifier = Modifier.weight(1f))
                PoHeaderBtn({ Icon(Icons.Filled.List, contentDescription = "Fill today's data", tint = PoInk, modifier = Modifier.size(18.dp)) }) { showFill = true }
                Spacer(modifier = Modifier.width(8.dp))
                PoHeaderBtn({ Icon(Icons.Filled.Key, contentDescription = "Saved login", tint = PoInk, modifier = Modifier.size(18.dp)) }) { showLogin = true }
                Spacer(modifier = Modifier.width(8.dp))
                PoHeaderBtn({ Icon(Icons.Filled.Refresh, contentDescription = "Reload", tint = PoInk, modifier = Modifier.size(18.dp)) }) { openBrowser() }
            }
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Portal kholo (63 me yahan iframe tha — native me browser).
                PoCard("GCS PORTAL") {
                    Text("Portal ki site app ke andar nahi khulti (WebView band hai) — ye button usay browser me kholega.", fontSize = 12.5.sp, color = PoGray, lineHeight = 18.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(PORTAL_URL, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PoRed)
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth().height(48.dp).cssShadow(6.dp, RoundedCornerShape(14.dp), PoRed.copy(alpha = 0.25f))
                            .clip(RoundedCornerShape(14.dp)).background(PoRed).clickable { openBrowser() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Language, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Open in Browser", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }
                    }
                }
                // 63 ka FAB menu, card ki shakal me: Quick Fill actions.
                PoCard("QUICK FILL") {
                    PoQuickRow(Color(0xFF22C55E), "Auto Fill", "Saved login bharo") { onGuiNote("Portal Auto Fill") }
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(PoInk.copy(alpha = 0.06f)))
                    PoQuickRow(Color(0xFFF97316), "TOM A", "Fill data") { onGuiNote("Portal Fill TOM A") }
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(PoInk.copy(alpha = 0.06f)))
                    PoQuickRow(Color(0xFF3B82F6), "TOM B", "Fill data") { onGuiNote("Portal Fill TOM B") }
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(PoInk.copy(alpha = 0.06f)))
                    PoQuickRow(Color(0xFFB8141C), "TKM", "Card sale fill") { onGuiNote("Portal Fill TKM") }
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(PoInk.copy(alpha = 0.06f)))
                    PoQuickRow(Color(0xFF0EA5E9), "ASKARI", "Voucher fill") { onGuiNote("Portal Fill ASKARI") }
                }
                // Saved Login ka khulasa (poori sheet Key button se khulti hai).
                PoCard("SAVED LOGIN") {
                    Text("Portal ka Gmail, Username aur Password sirf is device pe save hota hai — Key button se sheet kholo, save karo ya copy karo.", fontSize = 12.5.sp, color = PoGray, lineHeight = 18.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth().height(46.dp).clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFF6F2F2)).border(1.dp, PoInk.copy(alpha = 0.1f), RoundedCornerShape(14.dp))
                            .clickable { showLogin = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Key, contentDescription = null, tint = PoRed, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Portal Login kholo", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PoInk)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        // ---- 63 ki Saved Login sheet (nested overlay) ----
        if (showLogin) {
            var email by remember { mutableStateOf("") }
            var username by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            Box(
                modifier = Modifier.fillMaxSize().background(Color(0xFFF3EDED))
                    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { }
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().background(Color.White.copy(alpha = 0.94f)).statusBarsPadding().padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Portal Login", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = PoInk, letterSpacing = (-0.3).sp, modifier = Modifier.weight(1f))
                        PoHeaderBtn({ Icon(Icons.Filled.Close, contentDescription = "Close", tint = PoInk, modifier = Modifier.size(18.dp)) }) { showLogin = false }
                    }
                    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        PoCard(null) {
                            PoField("Gmail", { Icon(Icons.Filled.Person, contentDescription = null, tint = PoRed, modifier = Modifier.size(15.dp)) }, email, { email = it }, "Portal Gmail")
                            Spacer(modifier = Modifier.height(10.dp))
                            PoField("Username", { Icon(Icons.Filled.Person, contentDescription = null, tint = PoRed, modifier = Modifier.size(15.dp)) }, username, { username = it }, "User ID")
                            Spacer(modifier = Modifier.height(10.dp))
                            PoField("Password", { Icon(Icons.Filled.Lock, contentDescription = null, tint = PoRed, modifier = Modifier.size(15.dp)) }, password, { password = it }, "Password", password = true)
                            Spacer(modifier = Modifier.height(12.dp))
                            Box(
                                modifier = Modifier.fillMaxWidth().height(48.dp).cssShadow(6.dp, RoundedCornerShape(14.dp), PoRed.copy(alpha = 0.25f))
                                    .clip(RoundedCornerShape(14.dp)).background(PoRed).clickable { onGuiNote("Save Portal Login") },
                                contentAlignment = Alignment.Center
                            ) { Text("Save Login", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Save hone ke baad yahan se copy bhi kar sakoge — Auto-fill browser wale portal me kaam nahi karega (iframe nahi hai).", fontSize = 12.5.sp, color = PoGray, lineHeight = 18.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        // ---- 63 ki Data-Fill sheet (nested overlay) ----
        if (showFill) {
            val today = remember { SimpleDateFormat("EEEE, d MMMM", Locale.ENGLISH).format(Calendar.getInstance().time) }
            val fillRows = listOf("TOM A" to "TOM A", "TOM B" to "TOM B", "TKM" to "TKM", "BOP / POS" to "BOP")
            Box(
                modifier = Modifier.fillMaxSize().background(Color(0xFFF3EDED))
                    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { }
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().background(Color.White.copy(alpha = 0.94f)).statusBarsPadding().padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Fill Today's Data", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = PoInk, letterSpacing = (-0.3).sp, modifier = Modifier.weight(1f))
                        PoHeaderBtn({ Icon(Icons.Filled.Close, contentDescription = "Close", tint = PoInk, modifier = Modifier.size(18.dp)) }) { showFill = false }
                    }
                    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(today, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PoGray, modifier = Modifier.padding(horizontal = 4.dp))
                        PoCard(null) {
                            fillRows.forEachIndexed { i, (label, key) ->
                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Text("$label: —", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PoInk, modifier = Modifier.weight(1f))
                                    Box(
                                        modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(PoRed).clickable { onGuiNote("Portal Fill $key") }.padding(horizontal = 12.dp, vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) { Text("Auto-Fill", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White) }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(Color(0xFFF6F2F2)).border(1.dp, PoInk.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
                                            .clickable { onGuiNote("Copy Portal $key") }.padding(horizontal = 10.dp, vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) { Icon(Icons.Filled.ContentCopy, contentDescription = null, tint = PoGray, modifier = Modifier.size(16.dp)) }
                                }
                                if (i < fillRows.size - 1) Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(PoInk.copy(alpha = 0.06f)))
                            }
                        }
                        Text("Aaj ki closing Tom Data me save hote hi yahan values aayengi — Save ki wiring agle step me hogi.", fontSize = 12.5.sp, color = PoGray, lineHeight = 18.sp, modifier = Modifier.padding(horizontal = 4.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
