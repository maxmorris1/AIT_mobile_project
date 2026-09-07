package com.example.aitmobileproject.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aitmobileproject.BuildConfig
import com.example.aitmobileproject.data.remote.GitHubAsset
import com.example.aitmobileproject.data.remote.GitHubRelease
import com.example.aitmobileproject.data.remote.UpdateChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min

sealed class UpdateState {
    object Idle : UpdateState()
    object Checking : UpdateState()
    data class UpdateAvailable(val release: GitHubRelease) : UpdateState()
    data class Downloading(val progress: Float) : UpdateState()
    data class ReadyToInstall(val apkFile: File) : UpdateState()
    data class Error(val message: String) : UpdateState()
}

class UpdateViewModel(application: Application) : AndroidViewModel(application) {
    private val checker = UpdateChecker()
    private val client = OkHttpClient()

    private val _state = mutableStateOf<UpdateState>(UpdateState.Idle)
    val state: State<UpdateState> = _state

    fun checkForUpdates() {
        _state.value = UpdateState.Checking
        viewModelScope.launch(Dispatchers.IO) {
            val latestRelease = try {
                checker.checkForUpdate()
            } catch (e: Exception) {
                null
            }

            withContext(Dispatchers.Main) {
                if (latestRelease != null) {
                    val currentVersion = BuildConfig.VERSION_NAME
                    val latestVersion = latestRelease.tagName.removePrefix("v")
                    
                    if (isNewerVersion(currentVersion, latestVersion)) {
                        _state.value = UpdateState.UpdateAvailable(latestRelease)
                    } else {
                        _state.value = UpdateState.Idle
                    }
                } else {
                    _state.value = UpdateState.Idle
                }
            }
        }
    }

    private fun isNewerVersion(current: String, latest: String): Boolean {
        val currentParts = current.split(".").mapNotNull { it.toIntOrNull() }
        val latestParts = latest.split(".").mapNotNull { it.toIntOrNull() }
        
        for (i in 0 until min(currentParts.size, latestParts.size)) {
            if (latestParts[i] > currentParts[i]) return true
            if (latestParts[i] < currentParts[i]) return false
        }
        return latestParts.size > currentParts.size
    }

    fun downloadUpdate(release: GitHubRelease) {
        val asset = release.assets.find { it.name.endsWith(".apk") }
        if (asset == null) {
            _state.value = UpdateState.Error("No APK found in release assets")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _state.value = UpdateState.Downloading(0f)
            }
            try {
                val request = Request.Builder().url(asset.downloadUrl).build()
                val response = client.newCall(request).execute()
                
                if (!response.isSuccessful) throw Exception("Failed to download APK")

                val body = response.body ?: throw Exception("Empty response body")
                val totalBytes = body.contentLength()
                val updateDir = File(getApplication<Application>().cacheDir, "updates")
                if (!updateDir.exists()) updateDir.mkdirs()
                
                val apkFile = File(updateDir, "update.apk")
                body.byteStream().use { input ->
                    FileOutputStream(apkFile).use { output ->
                        val buffer = ByteArray(8192)
                        var bytesRead: Int
                        var totalRead = 0L
                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            output.write(buffer, 0, bytesRead)
                            totalRead += bytesRead
                            if (totalBytes > 0) {
                                withContext(Dispatchers.Main) {
                                    _state.value = UpdateState.Downloading(totalRead.toFloat() / totalBytes)
                                }
                            }
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    _state.value = UpdateState.ReadyToInstall(apkFile)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _state.value = UpdateState.Error(e.message ?: "Download failed")
                }
            }
        }
    }
    
    fun dismissUpdate() {
        _state.value = UpdateState.Idle
    }
}
