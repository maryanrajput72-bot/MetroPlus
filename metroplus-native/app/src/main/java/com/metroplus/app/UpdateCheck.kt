package com.metroplus.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

// Har baar app foreground me aaye (cold open ho ya background se wapas),
// ye counter barhta hai — MetroPlusApp is pe 5 second baad update check chalata hai.
// Sirf pehli composition pe check karna kafi nahi tha: background se wapas aane pe
// LaunchedEffect(Unit) dobara nahi chalta tha, is liye update kabhi nahi aati thi.
object ForegroundTicker {
    val resumeCount = mutableStateOf(0)
}

// In-app updater: app open ke 5 second baad check, aur search ke sath wale
// refresh circle se manual check. DownloadManager BILKUL NAHI — download
// app ke andar hi hoti hai (koi notification download nahi), progress
// app ke andar dialog me dikhta hai, complete hote hi Android ki install
// screen khulti hai. Pehli dafa sirf "Install unknown apps" on karna hota
// hai (system settings, ek dafa), uske baad update khud install screen
// tak pahunch jati hai.
// Manifest: https://raw.githubusercontent.com/maryanrajput72-bot/MetroPlus/main/metroplus-app-version.json
// Format: {"versionCode": 12, "versionName": "1.11", "apkUrl": "https://.../MetroPlus-v1.11.apk"}
object UpdateManager {
    const val MANIFEST_URL = "https://raw.githubusercontent.com/maryanrajput72-bot/MetroPlus/main/metroplus-app-version.json"
    const val CURRENT_VERSION_CODE = 68

    private val mainHandler = Handler(Looper.getMainLooper())
    @Volatile private var cancelled = false
    // Unknown sources on karne ke liye settings gaye waqt download shuda file yahan rakhi jati hai,
    // taake wapas aate hi install khul jaye (dobara download nahi karni parti).
    var pendingInstallFile: File? = null

    fun cancelDownload() { cancelled = true }

    fun checkForUpdate(
        context: Context,
        onUpdate: (versionName: String, apkUrl: String) -> Unit,
        onNoUpdate: () -> Unit = {},
        onError: () -> Unit = {}
    ) {
        Thread {
            try {
                val url = "$MANIFEST_URL?t=${System.currentTimeMillis()}"
                val conn = URL(url).openConnection() as HttpURLConnection
                conn.connectTimeout = 10000
                conn.readTimeout = 10000
                if (conn.responseCode != 200) { mainHandler.post { onError() }; return@Thread }
                val text = conn.inputStream.bufferedReader().readText()
                val json = JSONObject(text)
                val remoteCode = json.optInt("versionCode", 0)
                val apkUrl = json.optString("apkUrl", "")
                val versionName = json.optString("versionName", "")
                if (remoteCode <= CURRENT_VERSION_CODE || apkUrl.isBlank()) {
                    mainHandler.post { onNoUpdate() }
                } else {
                    mainHandler.post { onUpdate(versionName, apkUrl) }
                }
            } catch (_: Exception) {
                mainHandler.post { onError() }
            }
        }.start()
    }

    // v1.63: progress ke sath downloaded/total bytes bhi bhejo — 63 ki tarah
    // update hamesha NAZAR aaye. Server size na bheje to pct = -1 hota hai
    // (pehle sirf "Updating…" likha aata tha, % nahi) — ab us soorat me
    // UI downloaded MB dikhata hai, taake pata chalta rahe ke kaam ho raha hai.
    fun downloadUpdate(
        context: Context,
        apkUrl: String,
        onProgress: (percent: Int, downloaded: Long, total: Long) -> Unit,
        onComplete: (file: File) -> Unit,
        onError: () -> Unit
    ) {
        cancelled = false
        Thread {
            var outFile: File? = null
            try {
                val dir = File(context.cacheDir, "update").apply { mkdirs() }
                outFile = File(dir, "MetroPlus-update.apk")
                val conn = URL(apkUrl).openConnection() as HttpURLConnection
                conn.connectTimeout = 15000
                conn.readTimeout = 15000
                if (conn.responseCode != 200) { mainHandler.post { onError() }; return@Thread }
                val total = conn.contentLengthLong
                var downloaded = 0L
                // Sirf % badalne pe UI ko batao (har 8KB pe nahi) — main thread pe bojh na pare.
                var lastPct = -2
                conn.inputStream.use { input ->
                    FileOutputStream(outFile).use { output ->
                        val buffer = ByteArray(8192)
                        while (true) {
                            if (cancelled) { mainHandler.post { onError() }; return@Thread }
                            val read = input.read(buffer)
                            if (read < 0) break
                            output.write(buffer, 0, read)
                            downloaded += read
                            val pct = if (total > 0) ((downloaded * 100) / total).toInt().coerceIn(0, 100) else -1
                            if (pct != lastPct) {
                                lastPct = pct
                                mainHandler.post { onProgress(pct, downloaded, total) }
                            }
                        }
                    }
                }
                if (cancelled) { mainHandler.post { onError() }; return@Thread }
                mainHandler.post { onProgress(100, downloaded, total) }
                mainHandler.post { onComplete(outFile) }
            } catch (_: Exception) {
                try { outFile?.delete() } catch (_: Exception) { }
                mainHandler.post { onError() }
            }
        }.start()
    }

    fun canInstall(context: Context): Boolean {
        // Android 7 (API 24/25) me ye API hai hi nahi (call karte hi crash) — wahan
        // system ki "Unknown sources" setting chalti hai, is liye seedha install screen kholo.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return true
        return context.packageManager.canRequestPackageInstalls()
    }

    fun installUpdate(context: Context, file: File) {
        try {
            val uri = FileProvider.getUriForFile(
                context, "${context.packageName}.fileprovider", file
            )
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/vnd.android.package-archive")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(intent)
        } catch (_: Exception) {
        }
    }

    fun openUnknownSourcesSettings(context: Context) {
        try {
            val intent = Intent(
                android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                Uri.parse("package:${context.packageName}")
            ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
            context.startActivity(intent)
        } catch (_: Exception) {
        }
    }
}
