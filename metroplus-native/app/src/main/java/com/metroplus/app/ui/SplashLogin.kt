package com.metroplus.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metroplus.app.R

// v1.66 (uska order): (1) app khulte hi 5 second ka intro — neeche progress bar
// aur app se mutalliq 4 lines jo badalti rehti hain; (2) Login aur Sign Up
// pages — SIRF GUI (asli login/Firebase baad me). Login drawer/Settings ke
// "Log Out" se khulta hai, Sign Up Login ke neeche wale link se.

const val SPLASH_MS = 6500

private val SpLines = listOf(
    "Preparing your shift dashboard…",
    "Loading TOM data & money calculator…",
    "Syncing station & staff details…",
    "Almost ready — have a great shift!"
)

@Composable
fun SplashScreen() {
    // 0 → 1 progress 5 second me; line har 1.25s pe badalti hai.
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) { progress.animateTo(1f, tween(SPLASH_MS - 200, easing = LinearEasing)) }
    val lineIndex = (progress.value * SpLines.size).toInt().coerceIn(0, SpLines.size - 1)
    // Logo halka sa saans leta hai (scale 1 ↔ 1.06) + peeche roshni ghoomti hai.
    val pulse by rememberInfiniteTransition(label = "splash").animateFloat(
        1f, 1.06f, infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "pulse"
    )
    val logoIn = remember { Animatable(0.6f) }
    LaunchedEffect(Unit) { logoIn.animateTo(1f, tween(700, easing = FastOutSlowInEasing)) }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(cssLinear(150f, 0f to Color(0xFF1A0B0D), 0.45f to Color(0xFF7A1222), 0.8f to Color(0xFFC1121F), 1f to Color(0xFFFF5A5F)))
            // Intro ke dauran tap peeche Home tak na pahunche (pehle enabled=false se tap guzar jata tha).
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {}
    ) {
        Box(Modifier.matchParentSize()) {
            Glow(320.dp, x = 120.dp, y = (-120).dp, alpha = 0.22f, align = Alignment.TopEnd)
            Glow(260.dp, x = (-110).dp, y = 120.dp, alpha = 0.10f, solid = true, align = Alignment.BottomStart)
            Box(modifier = Modifier.align(Alignment.Center).offset(y = (-70).dp).size(300.dp).border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape))
            Box(modifier = Modifier.align(Alignment.Center).offset(y = (-70).dp).size(230.dp).border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape))
        }
        Column(
            modifier = Modifier.align(Alignment.Center).offset(y = (-40).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.scale(logoIn.value)) {
                Box(modifier = Modifier.size(170.dp).scale(pulse).background(Brush.radialGradient(listOf(Color(0xFFFF2D55).copy(alpha = 0.55f), Color.Transparent)), CircleShape))
                Image(painterResource(id = R.drawable.logo_hd), contentDescription = "Metro + logo", colorFilter = LogoEnhance, modifier = Modifier.size(128.dp))
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text("Metro +", fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = (-0.6).sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("DAILY SHIFT COMPANION", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color.White.copy(alpha = 0.75f), letterSpacing = 2.5.sp)
        }
        // Neeche: badalti line + progress bar + %.
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().navigationBarsPadding().padding(start = 32.dp, end = 32.dp, bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = lineIndex,
                transitionSpec = { (slideInVertically(tween(350)) { it / 2 } + fadeIn(tween(350))) togetherWith (slideOutVertically(tween(300)) { -it / 2 } + fadeOut(tween(300))) },
                label = "splashLine"
            ) { i ->
                Text(SpLines[i], fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(999.dp)).background(Color.White.copy(alpha = 0.18f))) {
                Box(
                    modifier = Modifier.fillMaxWidth(progress.value).height(8.dp).clip(RoundedCornerShape(999.dp))
                        .background(Brush.horizontalGradient(listOf(Color(0xFFFFD166), Color(0xFFFF8A65), Color.White)))
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                // Chhote dots — kaunsi line chal rahi hai.
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.weight(1f)) {
                    SpLines.indices.forEach { i ->
                        Box(modifier = Modifier.size(width = if (i == lineIndex) 16.dp else 6.dp, height = 6.dp).clip(CircleShape)
                            .background(Color.White.copy(alpha = if (i <= lineIndex) 0.95f else 0.3f)))
                    }
                }
                Text("${(progress.value * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color.White.copy(alpha = 0.9f))
            }
        }
    }
}

// ---------------- Login / Sign Up (sirf GUI) ----------------

private val AuthGrad = listOf(Color(0xFFFF5A5F), Color(0xFFC1121F), Color(0xFF5B0F2E))
private val AuthInk = Color(0xFF14181C)
private val AuthGray = Color(0xFF6B7280)

@Composable
private fun AuthShell(onBack: (() -> Unit)?, title: String, sub: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF7F2F3)).verticalScroll(rememberScrollState())) {
        val shape = RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp)
        Box(
            modifier = Modifier.fillMaxWidth().cssShadow(16.dp, shape, Color(0xFFC1121F).copy(alpha = 0.35f)).clip(shape)
                .background(cssLinear(150f, 0f to AuthGrad[0], 0.55f to AuthGrad[1], 1f to AuthGrad[2]))
        ) {
            Box(Modifier.matchParentSize()) {
                Glow(240.dp, x = 100.dp, y = (-100).dp, alpha = 0.28f, align = Alignment.TopEnd)
                Glow(160.dp, x = (-70).dp, y = 60.dp, alpha = 0.10f, solid = true, align = Alignment.BottomStart)
            }
            Column(modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(start = 22.dp, end = 22.dp, top = 12.dp, bottom = 30.dp)) {
                if (onBack != null) Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.22f))
                        .border(1.dp, Color.White.copy(alpha = 0.35f), CircleShape).clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(20.dp)) }
                else Spacer(modifier = Modifier.height(40.dp))
                Spacer(modifier = Modifier.height(14.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painterResource(id = R.drawable.logo_hd), contentDescription = null, colorFilter = LogoEnhance, modifier = Modifier.size(58.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Metro +", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.White.copy(alpha = 0.9f))
                        Text("Daily Shift Companion", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.7f))
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                Text(title, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = (-0.5).sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(sub, fontSize = 13.5.sp, fontWeight = FontWeight.Medium, color = Color.White.copy(alpha = 0.85f), lineHeight = 18.sp)
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp).offset(y = (-22).dp)
                .cssShadow(12.dp, RoundedCornerShape(26.dp), Color(0xFF3C1419).copy(alpha = 0.12f))
                .background(Color.White, RoundedCornerShape(26.dp)).padding(20.dp),
            content = content
        )
        Spacer(modifier = Modifier.navigationBarsPadding().height(8.dp))
    }
}

@Composable
private fun AuthField(label: String, icon: ImageVector, value: String, onValue: (String) -> Unit, placeholder: String, password: Boolean = false) {
    var show by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(16.dp)
    Text(label, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = AuthGray, modifier = Modifier.padding(bottom = 6.dp))
    BasicTextField(
        value = value, onValueChange = onValue, singleLine = true,
        visualTransformation = if (password && !show) PasswordVisualTransformation() else VisualTransformation.None,
        textStyle = TextStyle(fontSize = 14.5.sp, fontWeight = FontWeight.SemiBold, color = AuthInk),
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { inner ->
            Row(
                modifier = Modifier.fillMaxWidth().clip(shape)
                    .background(Brush.horizontalGradient(listOf(Color(0xFFFFF1F2), Color(0xFFFAF7F7))))
                    .border(1.3.dp, if (value.isNotEmpty()) Color(0xFFE11D48).copy(alpha = 0.5f) else AuthInk.copy(alpha = 0.08f), shape)
                    .padding(horizontal = 14.dp, vertical = 13.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, tint = Color(0xFFE11D48), modifier = Modifier.size(19.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) Text(placeholder, fontSize = 14.5.sp, color = AuthInk.copy(alpha = 0.35f))
                    inner()
                }
                if (password) Icon(
                    if (show) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = "Show password",
                    tint = AuthGray, modifier = Modifier.size(19.dp).clickable { show = !show }
                )
            }
        }
    )
    Spacer(modifier = Modifier.height(14.dp))
}

@Composable
private fun AuthButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    val s = RoundedCornerShape(18.dp)
    Box(
        modifier = Modifier.fillMaxWidth().height(54.dp).cssShadow(12.dp, s, Color(0xFFC1121F).copy(alpha = 0.4f)).clip(s)
            .background(cssLinear(120f, 0f to AuthGrad[0], 0.55f to AuthGrad[1], 1f to AuthGrad[2])).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.22f), Color.Transparent))))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, fontSize = 16.5.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        }
    }
}

@Composable
private fun AuthCheck(checked: Boolean, text: String, onChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onChange(!checked) }) {
        Box(
            modifier = Modifier.size(22.dp).clip(RoundedCornerShape(7.dp))
                .background(if (checked) Brush.linearGradient(listOf(AuthGrad[0], AuthGrad[1])) else Brush.linearGradient(listOf(Color.White, Color.White)))
                .border(1.5.dp, if (checked) Color.Transparent else AuthInk.copy(alpha = 0.2f), RoundedCornerShape(7.dp)),
            contentAlignment = Alignment.Center
        ) { if (checked) Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp)) }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = AuthInk)
    }
}

@Composable
private fun AuthChoice(options: List<String>, selected: String, onPick: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        options.forEach { o ->
            val on = o == selected
            val s = RoundedCornerShape(14.dp)
            Box(
                modifier = Modifier.weight(1f).height(44.dp).clip(s)
                    .then(if (on) Modifier.background(cssLinear(135f, 0f to AuthGrad[0], 1f to AuthGrad[1]))
                        else Modifier.background(Color(0xFFFAF7F7)).border(1.3.dp, AuthInk.copy(alpha = 0.1f), s))
                    .clickable { onPick(o) },
                contentAlignment = Alignment.Center
            ) { Text(o, fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold, color = if (on) Color.White else AuthInk) }
        }
    }
    Spacer(modifier = Modifier.height(14.dp))
}

@Composable
fun LoginScreen(onBack: () -> Unit, onSignUp: () -> Unit, onGuiNote: (String) -> Unit) {
    BackHandler { onBack() }
    // v1.67 (uska order): Login = Number + Your ID. Employee ID/Gmail wala
    // field aur Password wala field dono hata diye; ID saaf nazar aati hai
    // (password jaisi chhupi nahi). Koi field khaali ho to login NAHI hoga
    // (validation function abhi; asli login baad me).
    var number by pageState("login_number", "")
    var yourId by pageState("login_id", "")
    var remember by pageState("login_remember", true)
    var loginError by remember { mutableStateOf(false) }
    val loginIncomplete = number.isBlank() || yourId.isBlank()
    AuthShell(onBack, "Welcome back", "Log in with your number and ID to start your shift.") {
        AuthField("Number", Icons.Filled.Phone, number, { number = it }, "03xx xxxxxxx")
        AuthField("Your ID", Icons.Filled.Badge, yourId, { yourId = it }, "e.g. 2022")
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) { AuthCheck(remember, "Remember me") { remember = it } }
            Text("Forgot ID?", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFE11D48), modifier = Modifier.clickable { onGuiNote("Forgot ID") })
        }
        Spacer(modifier = Modifier.height(18.dp))
        if (loginError && loginIncomplete) {
            Text("Please fill in all fields", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFE11D48), modifier = Modifier.padding(bottom = 10.dp))
        }
        AuthButton("Log In", Icons.Filled.Login) {
            if (loginIncomplete) loginError = true else { loginError = false; onGuiNote("Log In") }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1f).height(1.dp).background(AuthInk.copy(alpha = 0.08f)))
            Text("  or  ", fontSize = 12.sp, color = AuthGray)
            Box(modifier = Modifier.weight(1f).height(1.dp).background(AuthInk.copy(alpha = 0.08f)))
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Naya account: halka gulabi outline button.
        val s = RoundedCornerShape(18.dp)
        Box(
            modifier = Modifier.fillMaxWidth().height(50.dp).clip(s).background(Color(0xFFFFF1F2))
                .border(1.5.dp, Color(0xFFE11D48).copy(alpha = 0.4f), s).clickable { onSignUp() },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.PersonAdd, contentDescription = null, tint = Color(0xFFE11D48), modifier = Modifier.size(19.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Create new account", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFBE123C))
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text("Access is given by your admin. Contact admin if you need help.", fontSize = 12.sp, color = AuthGray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun SignUpScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    BackHandler { onBack() }
    var name by pageState("signup_name", "")
    var tlName by pageState("signup_tlName", "")
    var empId by pageState("signup_empId", "")
    var phone by pageState("signup_phone", "")
    var gmail by pageState("signup_gmail", "")
    var station by pageState("signup_station", "")
    var shift by pageState("signup_shift", "Morning")
    var role by pageState("signup_role", "Operator")
    var agree by pageState("signup_agree", false)
    var stationOpen by remember { mutableStateOf(false) }
    var signupError by remember { mutableStateOf(false) }
    // v1.67 (uska order): subtitle sirf "Fill in your details"; Full name ke
    // neeche TL Name ka naya section; Password + Confirm password dono hata
    // diye (role Operator/TL/RP wahi rahenge); koi field khaali ho ya
    // confirm na ho to sign up NAHI hoga (validation function abhi).
    val signupIncomplete = name.isBlank() || tlName.isBlank() || empId.isBlank() ||
        phone.isBlank() || gmail.isBlank() || station.isBlank() || !agree
    AuthShell(onBack, "Create account", "Fill in your details") {
        AuthField("Full name", Icons.Filled.Person, name, { name = it }, "Your full name")
        AuthField("TL Name", Icons.Filled.Person, tlName, { tlName = it }, "Your TL's name")
        AuthField("Employee ID", Icons.Filled.Badge, empId, { empId = it }, "e.g. 2022")
        AuthField("Mobile number", Icons.Filled.Phone, phone, { phone = it }, "03xx xxxxxxx")
        AuthField("Gmail", Icons.Filled.Email, gmail, { gmail = it }, "name@gmail.com")
        Text("Station", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = AuthGray, modifier = Modifier.padding(bottom = 6.dp))
        val sShape = RoundedCornerShape(16.dp)
        Row(
            modifier = Modifier.fillMaxWidth().clip(sShape)
                .background(Brush.horizontalGradient(listOf(Color(0xFFFFF1F2), Color(0xFFFAF7F7))))
                .border(1.3.dp, if (station.isNotEmpty()) Color(0xFFE11D48).copy(alpha = 0.5f) else AuthInk.copy(alpha = 0.08f), sShape)
                .clickable { stationOpen = true }.padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color(0xFFE11D48), modifier = Modifier.size(19.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(station.ifEmpty { "Select your station" }, fontSize = 14.5.sp, fontWeight = FontWeight.SemiBold,
                color = if (station.isEmpty()) AuthInk.copy(alpha = 0.35f) else AuthInk, modifier = Modifier.weight(1f))
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = AuthGray, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text("Shift", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = AuthGray, modifier = Modifier.padding(bottom = 6.dp))
        AuthChoice(listOf("Morning", "Evening"), shift) { shift = it }
        Text("Role", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = AuthGray, modifier = Modifier.padding(bottom = 6.dp))
        AuthChoice(listOf("Operator", "TL", "RP"), role) { role = it }
        AuthCheck(agree, "I confirm this detail") { agree = it }
        Spacer(modifier = Modifier.height(18.dp))
        if (signupError && signupIncomplete) {
            Text("Please fill in all fields", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFE11D48), modifier = Modifier.padding(bottom = 10.dp))
        }
        AuthButton("Sign Up", Icons.Filled.PersonAdd) {
            if (signupIncomplete) signupError = true else { signupError = false; onGuiNote("Sign Up") }
        }
        Spacer(modifier = Modifier.height(14.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text("Already have an account? ", fontSize = 13.sp, color = AuthGray)
            Text("Log in", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFE11D48), modifier = Modifier.clickable { onBack() })
        }
    }
    if (stationOpen) StationPickerSheet(station, { station = it }, { stationOpen = false })
}
