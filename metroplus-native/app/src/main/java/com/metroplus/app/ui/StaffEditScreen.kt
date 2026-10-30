package com.metroplus.app.ui

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Staff Edit page (trial 2.1.63 ki staffEditScreen ki copy, GUI phase).
// Hero: Edit Staff List + sub. Station card, staff list (delete GUI-local),
// Add Member form, Save Changes (save function agle step me).

@Composable
fun StaffEditScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    BackHandler { onBack() }
    val staff = remember { mutableStateListOf(*demoStaff.toTypedArray()) }
    var newId by remember { mutableStateOf("") }
    var newName by remember { mutableStateOf("") }
    var err by remember { mutableStateOf("") }
    val ink = Color(0xFF14181C)
    var station by remember { mutableStateOf("Shaheed-e-Millat") }
    var stationOpen by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF3EDED))
        // FIX (v1.42): khaali jagah ke tap Opening tab tak na pahunchein.
        .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { }) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Hero: purple gradient, back + pen tile + title + sub.
            val hShape = RoundedCornerShape(22.dp)
            Row(
                modifier = Modifier.fillMaxWidth().cssShadow(10.dp, hShape, Color(0xFF7B20D4).copy(alpha = 0.25f)).clip(hShape)
                    .background(cssLinear(135f, 0f to Color(0xFF9B35F2), 0.55f to Color(0xFF7B20D4), 1f to Color(0xFFD82EDC))).padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(38.dp).clip(CircleShape).background(Color.White).clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = ink, modifier = Modifier.size(18.dp)) }
                Spacer(modifier = Modifier.width(13.dp))
                Box(
                    modifier = Modifier.size(48.dp).cssShadow(7.dp, RoundedCornerShape(14.dp), Color(0xFF3C006E).copy(alpha = 0.3f))
                        .background(cssLinear(145f, 0f to Color(0xFFD8A5FF), 1f to Color(0xFF7B20D4)), RoundedCornerShape(14.dp)).clip(RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp)) }
                Spacer(modifier = Modifier.width(13.dp))
                Column {
                    Text("Edit Staff List", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = (-0.4).sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Manage the operator list for your station", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.96f))
                }
            }
            Text("Local list", fontSize = 12.sp, color = Color(0xFF9AA1AB), modifier = Modifier.padding(horizontal = 4.dp))
            // Station card.
            val cShape = RoundedCornerShape(18.dp)
            Column(
                modifier = Modifier.fillMaxWidth().cssShadow(5.dp, cShape, ink.copy(alpha = 0.08f))
                    .background(Color.White, cShape).border(1.dp, ink.copy(alpha = 0.06f), cShape).padding(16.dp)
            ) {
                Text("Station", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5B6470), modifier = Modifier.padding(bottom = 7.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().height(37.dp).clip(RoundedCornerShape(10.dp))
                        .background(cssLinear(135f, 0f to Color(0xFFDCFCE7), 1f to Color(0xFFBBF7D0))).border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(10.dp))
                        .clickable { stationOpen = true }.padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color(0xFF9E1F25), modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(station, fontSize = 13.5.sp, color = ink, modifier = Modifier.weight(1f))
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Color(0xFF9AA1AB), modifier = Modifier.size(18.dp))
                }
            }
            // Staff list rows (delete sirf is page ki list se hatata hai, GUI phase).
            staff.forEach { (name, id) ->
                val rShape = RoundedCornerShape(16.dp)
                Row(
                    modifier = Modifier.fillMaxWidth().cssShadow(3.dp, rShape, ink.copy(alpha = 0.05f))
                        .background(Color.White, rShape).border(1.dp, ink.copy(alpha = 0.06f), rShape).padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = ink, modifier = Modifier.weight(1f))
                    Text(id, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9AA1AB),
                        modifier = Modifier.background(Color(0xFFF0F3F5), RoundedCornerShape(8.dp)).padding(horizontal = 9.dp, vertical = 4.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(Icons.Filled.Delete, contentDescription = "Remove", tint = Color(0xFFE11D2E), modifier = Modifier.size(20.dp).clickable { staff.remove(name to id) })
                }
            }
            // Add Member form.
            Column(
                modifier = Modifier.fillMaxWidth().cssShadow(5.dp, cShape, ink.copy(alpha = 0.08f))
                    .background(Color.White, cShape).border(1.dp, ink.copy(alpha = 0.06f), cShape).padding(16.dp)
            ) {
                Text("Add Member", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = ink)
                Spacer(modifier = Modifier.height(10.dp))
                EditField("Staff ID", newId, { newId = it }, "Staff ID")
                Spacer(modifier = Modifier.height(10.dp))
                EditField("Operator name", newName, { newName = it }, "Operator name")
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().height(48.dp).cssShadow(6.dp, RoundedCornerShape(14.dp), Color(0xFF8B24E8).copy(alpha = 0.25f))
                        .clip(RoundedCornerShape(14.dp)).background(cssLinear(135f, 0f to Color(0xFF9B35F2), 1f to Color(0xFFD82EDC)))
                        .clickable {
                            if (newId.isBlank() || newName.isBlank()) err = "Staff ID aur Operator name dono likhein"
                            else {
                                staff.add(newName.trim() to newId.trim())
                                newId = ""
                                newName = ""
                                err = ""
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.PersonAdd, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Member", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
                if (err.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(err, fontSize = 12.5.sp, color = Color(0xFFE11D2E))
                }
            }
            // Save Changes (save function agle step me).
            Box(
                modifier = Modifier.fillMaxWidth().height(48.dp).cssShadow(6.dp, RoundedCornerShape(14.dp), Color(0xFF9E1F25).copy(alpha = 0.25f))
                    .clip(RoundedCornerShape(14.dp)).background(Color(0xFF9E1F25)).clickable { onGuiNote("Save Changes") },
                contentAlignment = Alignment.Center
            ) { Text("Save Changes", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) }
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (stationOpen) StationPickerSheet(station, { station = it }, { stationOpen = false })
    }
}

@Composable
private fun EditField(label: String, value: String, onValue: (String) -> Unit, placeholder: String) {
    val ink = Color(0xFF14181C)
    val shape = RoundedCornerShape(12.dp)
    Text(label, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5B6470), modifier = Modifier.padding(bottom = 6.dp))
    BasicTextField(
        value = value, onValueChange = onValue, singleLine = true,
        textStyle = TextStyle(fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = ink),
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { inner ->
            Box(
                modifier = Modifier.fillMaxWidth().clip(shape).background(Color(0xFFF6F2F2)).border(1.dp, ink.copy(alpha = 0.1f), shape)
                    .padding(horizontal = 10.dp, vertical = 9.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) Text(placeholder, fontSize = 13.5.sp, color = ink.copy(alpha = 0.35f))
                inner()
            }
        }
    )
}
