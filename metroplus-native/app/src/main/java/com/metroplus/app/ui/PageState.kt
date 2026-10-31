package com.metroplus.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

// Page band karke dobara kholo to likhi hui values wahi rahein (rule) —
// chhote pages (chat, Edit Profile, drawer settings, Staff Edit, Complaint
// form, Portal login) ki values yahan Activity ke ViewModel me rehti hain.
// Har value ka apna key hai (jaise "profile_name"), pehli dafa init lagta hai.
class PageStateViewModel : ViewModel() {
    private val values = HashMap<String, MutableState<*>>()
    private val lists = HashMap<String, SnapshotStateList<*>>()
    private val maps = HashMap<String, SnapshotStateMap<*, *>>()

    @Suppress("UNCHECKED_CAST")
    fun <T> state(key: String, init: T): MutableState<T> =
        values.getOrPut(key) { mutableStateOf(init) } as MutableState<T>

    @Suppress("UNCHECKED_CAST")
    fun <T> list(key: String, init: () -> List<T>): SnapshotStateList<T> =
        lists.getOrPut(key) { mutableStateListOf<T>().apply { addAll(init()) } } as SnapshotStateList<T>

    @Suppress("UNCHECKED_CAST")
    fun <K, V> map(key: String, init: () -> Map<K, V>): SnapshotStateMap<K, V> =
        maps.getOrPut(key) { mutableStateMapOf<K, V>().apply { putAll(init()) } } as SnapshotStateMap<K, V>
}

@Composable
fun <T> pageState(key: String, init: T): MutableState<T> {
    val vm: PageStateViewModel = viewModel()
    return remember(key) { vm.state(key, init) }
}

@Composable
fun <T> pageList(key: String, init: () -> List<T>): SnapshotStateList<T> {
    val vm: PageStateViewModel = viewModel()
    return remember(key) { vm.list(key, init) }
}

@Composable
fun <K, V> pageMap(key: String, init: () -> Map<K, V>): SnapshotStateMap<K, V> {
    val vm: PageStateViewModel = viewModel()
    return remember(key) { vm.map(key, init) }
}
