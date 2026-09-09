package com.example.aitmobileproject.data.local

import android.content.Context
import android.util.Base64
import java.security.MessageDigest

class AuthManager(context: Context) {
    private val prefs = context.getSharedPreferences("hajio_auth_prefs", Context.MODE_PRIVATE)

    fun saveCredentials(username: String, pin: String, provider: String = "Local") {
        val hashedPin = if (pin.isNotEmpty()) hashPin(pin) else ""
        prefs.edit().apply {
            putString("username", username)
            putString("hashed_pin", hashedPin)
            putString("provider", provider)
            putBoolean("is_logged_in", true)
            apply()
        }
    }

    fun logout() {
        prefs.edit().putBoolean("is_logged_in", false).apply()
    }

    fun getUsername(): String? = prefs.getString("username", null)
    fun getProvider(): String = prefs.getString("provider", "Local") ?: "Local"
    
    fun verifyPin(pin: String): Boolean {
        val savedHash = prefs.getString("hashed_pin", null)
        return savedHash != null && savedHash == hashPin(pin)
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean("is_logged_in", false)
    fun isRegistered(): Boolean = getUsername() != null

    fun saveDismissedVersion(version: String) {
        prefs.edit().putString("dismissed_version", version).apply()
    }

    fun getDismissedVersion(): String? = prefs.getString("dismissed_version", null)

    private fun hashPin(pin: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(pin.toByteArray())
        return Base64.encodeToString(digest, Base64.DEFAULT)
    }
}
