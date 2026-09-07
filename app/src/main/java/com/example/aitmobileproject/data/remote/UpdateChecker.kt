package com.example.aitmobileproject.data.remote

import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class UpdateChecker(private val client: OkHttpClient = OkHttpClient()) {
    private val json = Json { ignoreUnknownKeys = true }
    private val repoUrl = "https://api.github.com/repos/maxmorris1/Hajio/releases/latest"

    suspend fun checkForUpdate(): GitHubRelease? {
        val request = Request.Builder()
            .url(repoUrl)
            .header("Accept", "application/vnd.github.v3+json")
            .build()

        return try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val body = response.body?.string()
                body?.let { json.decodeFromString<GitHubRelease>(it) }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
