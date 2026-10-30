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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// Group Chat — trial 63 ki chat screen jaisa GUI (97-chat.js + chat CSS).
// 63 ke tabs: 'all' (All Member) aur 'tlrp' (TL RP Chat). Header gradient
// #b32f36 → #9d1f25, apne messages #efc9cb, doosron ke safed, avatar
// gradient. Firebase wiring (presence + group_chat) agle step me hogi —
// abhi sirf GUI hai, send karne pe message local list me dikhega (demo).

private val ChatBg = Color(0xFFF4F6F8)
private val ChatHead1 = Color(0xFFB32F36)
private val ChatHead2 = Color(0xFF9D1F25)
private val ChatOwnBg = Color(0xFFEFC9CB)
private val ChatInk = Color(0xFF14181C)
private val ChatGray = Color(0xFF6B7280)

private data class ChatMsg(val name: String, val text: String, val time: String, val mine: Boolean, val tlrp: Boolean)

@Composable
fun GroupChatScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    var tab by remember { mutableStateOf(0) } // 0 = All Member, 1 = TL RP Chat
    val messages = remember {
        mutableStateListOf(
            ChatMsg("Ahsan", "TOM 2 closing sent.", "09:12", false, false),
            ChatMsg("Shehryar", "Shift started — all machines OK.", "09:20", false, false),
            ChatMsg("Aryan", "Received, thanks!", "09:21", true, false),
            ChatMsg("Tahir", "Printer 1 roll changed.", "09:35", false, false),
            ChatMsg("TL", "Please check closing reports before sending.", "09:40", false, true),
            ChatMsg("RP", "Reports checked, all good.", "09:42", false, true)
        )
    }
    val shown = if (tab == 0) messages else messages.filter { it.tlrp || it.mine }
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize().background(ChatBg)
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header: back | avatar + Group Chat + online | gradient.
            Row(
                modifier = Modifier.fillMaxWidth().background(cssLinear(135f, 0f to ChatHead1, 1f to ChatHead2))
                    .statusBarsPadding().padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(38.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.22f))
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(18.dp)) }
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.22f), CircleShape),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.Group, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp)) }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Group Chat", fontSize = 16.5.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = (-0.2).sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(7.dp).background(Color(0xFF4ADE80), CircleShape))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text("5 online", fontSize = 12.5.sp, color = Color.White.copy(alpha = 0.95f))
                    }
                }
            }
            // Tabs: pill tabs, active #9d1f25.
            Row(
                modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All Member", "TL RP Chat").forEachIndexed { i, label ->
                    val active = tab == i
                    Box(
                        modifier = Modifier.weight(1f).clip(RoundedCornerShape(999.dp))
                            .background(if (active) ChatHead2 else Color(0xFFF4EEEE))
                            .clickable { tab = i }.padding(vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(label, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = if (active) Color.White else Color(0xFF4B5563))
                    }
                }
            }
            // Messages.
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(shown) { m ->
                    if (m.mine) {
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.End) {
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(ChatOwnBg).padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(m.text, fontSize = 14.sp, color = ChatInk)
                                    Text(m.time, fontSize = 10.5.sp, color = ChatInk.copy(alpha = 0.55f))
                                }
                            }
                        }
                    } else {
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp), verticalAlignment = Alignment.Bottom) {
                            Box(
                                modifier = Modifier.size(30.dp).background(cssLinear(135f, 0f to Color(0xFFC6434A), 1f to Color(0xFFA82E34)), CircleShape),
                                contentAlignment = Alignment.Center
                            ) { Text(m.name.take(1), fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) }
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(Color.White)
                                    .border(1.dp, ChatInk.copy(alpha = 0.06f), RoundedCornerShape(16.dp)).padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Column {
                                    Text(m.name, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = ChatHead2)
                                    Text(m.text, fontSize = 14.sp, color = ChatInk)
                                    Text(m.time, fontSize = 10.5.sp, color = ChatInk.copy(alpha = 0.55f))
                                }
                            }
                        }
                    }
                }
            }
            // Input row.
            Row(
                modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(999.dp)).background(Color(0xFFF4EEEE))
                        .border(1.dp, ChatInk.copy(alpha = 0.06f), RoundedCornerShape(999.dp)).padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    BasicTextField(
                        value = input, onValueChange = { input = it }, singleLine = true,
                        textStyle = TextStyle(fontSize = 14.sp, color = ChatInk),
                        decorationBox = { inner ->
                            if (input.isEmpty()) Text("Type a message...", fontSize = 14.sp, color = ChatInk.copy(alpha = 0.4f))
                            inner()
                        }
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier.size(44.dp).clip(CircleShape)
                        .background(cssLinear(135f, 0f to ChatHead1, 1f to ChatHead2))
                        .clickable {
                            if (input.isNotBlank()) {
                                messages.add(ChatMsg("Aryan", input.trim(), "Now", true, tab == 1))
                                input = ""
                                scope.launch { listState.animateScrollToItem(messages.size - 1) }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(20.dp)) }
            }
        }
    }
}
