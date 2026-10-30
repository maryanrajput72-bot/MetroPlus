package com.metroplus.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlipCameraIos
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// GPS Metro Camera page — GUI copy of gps_metro_camera_page.dart (Flutter).
// Sirf GUI hai: live camera feed, location aur capture features baad me lagenge.
@Composable
fun GpsCameraScreen(onBack: () -> Unit, onGuiNote: (String) -> Unit) {
    var zoom2x by remember { mutableStateOf(false) }
    var torchOn by remember { mutableStateOf(false) }
    BackHandler { onBack() }

    // Dart file ke default demo values (live values features ke sath ayenge).
    val lat = "33.734690"
    val lng = "73.092580"
    val dateStr = remember {
        SimpleDateFormat("EEEE, dd/MM/yyyy hh:mm a", Locale.US).format(Date()) + " GMT +05:00"
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // 1. Fullscreen viewfinder (GUI phase: live feed ki jagah placeholder).
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(Icons.Filled.CameraAlt, contentDescription = null, tint = Color.White.copy(alpha = 0.25f), modifier = Modifier.size(48.dp))
        }

        // 2. Top header: back | zoom pill, torch, flip.
        Row(
            modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth().statusBarsPadding().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GpsHeaderIconButton(icon = Icons.Filled.ArrowBackIos, onClick = onBack)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.padding(end = 10.dp)
                        .border(1.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(20.dp))
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                        .clickable { zoom2x = !zoom2x }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(if (zoom2x) "2x" else "1x", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (zoom2x) Color(0xFFFFC107) else Color.White)
                }
                GpsHeaderIconButton(icon = if (torchOn) Icons.Filled.FlashOn else Icons.Filled.FlashOff, tint = if (torchOn) Color(0xFFFFC107) else Color.White, onClick = { torchOn = !torchOn })
                Spacer(modifier = Modifier.width(8.dp))
                GpsHeaderIconButton(icon = Icons.Filled.FlipCameraIos, onClick = { onGuiNote("Flip Camera") })
            }
        }

        // 3. Shutter button, stamp card se 150dp upar, center me.
        Box(modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(bottom = 150.dp)) {
            Box(
                modifier = Modifier.size(76.dp).border(4.5.dp, Color.White, CircleShape).padding(4.dp)
                    .clickable { onGuiNote("Capture") },
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.fillMaxSize().background(Color.White, CircleShape))
            }
        }

        // 4. Bottom GPS stamp card (black 82%, padding 14/12/14/18).
        // Kala background nav bar ke peeche tak, likhai nav bar ke upar (3-button phones pe chhupti thi).
        Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().background(Color.Black.copy(alpha = 0.82f)).navigationBarsPadding().padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 18.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                // Map thumbnail 90x90: dark bg + radar cone + red pin + Google tag.
                Box(
                    modifier = Modifier.size(90.dp)
                        .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                        .background(Color(0xFF1E2029), RoundedCornerShape(10.dp))
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.Map, contentDescription = null, tint = Color.White.copy(alpha = 0.54f), modifier = Modifier.size(36.dp))
                    }
                    Box(modifier = Modifier.size(54.dp).offset(x = 18.dp, y = 15.dp)) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val path = Path().apply {
                                val cx = size.width / 2
                                val cy = size.height / 2
                                moveTo(cx, cy)
                                arcTo(Rect(Offset.Zero, size), -109f, 57f, false)
                                close()
                            }
                            drawPath(
                                path,
                                Brush.radialGradient(
                                    listOf(Color(0xFF38BDF8).copy(alpha = 0.55f), Color(0xFF38BDF8).copy(alpha = 0.05f)),
                                    center = Offset(size.width / 2, size.height / 2),
                                    radius = size.width / 2
                                )
                            )
                        }
                    }
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color(0xFFEA4335), modifier = Modifier.size(22.dp))
                    }
                    Box(modifier = Modifier.fillMaxSize().padding(start = 6.dp, bottom = 3.dp), contentAlignment = Alignment.BottomStart) {
                        Text("Google", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color.White.copy(alpha = 0.9f), style = TextStyle(shadow = Shadow(color = Color.Black, blurRadius = 3f)))
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Location details.
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Islamabad, Pakistan 🇵🇰",
                            fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White,
                            maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f)
                        )
                        Box(
                            modifier = Modifier.border(0.8.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(4.dp))
                                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.CameraAlt, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(9.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text("GPS Camera", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(3.dp))
                    Text(
                        "Finding nearest Metro station...",
                        fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFC107),
                        maxLines = 1, overflow = TextOverflow.Ellipsis, lineHeight = 21.sp
                    )
                    Spacer(modifier = Modifier.size(3.dp))
                    Text(
                        "Fetching live address...",
                        fontSize = 11.5.sp, color = Color.White, lineHeight = 14.5.sp,
                        maxLines = 2, overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.size(3.dp))
                    Text("Lat $lat° Long $lng°", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold, color = Color.White)
                    Text(dateStr, fontSize = 11.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun GpsHeaderIconButton(icon: ImageVector, tint: Color = Color.White, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(40.dp)
            .border(1.dp, Color.White.copy(alpha = 0.14f), CircleShape)
            .background(Color.Black.copy(alpha = 0.6f), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
    }
}
