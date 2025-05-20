package com.example.geotrack.data.auth

import android.content.Context
import android.content.SharedPreferences
import com.example.geotrack.domain.auth.TokenStorage

class TokenStorageImpl(context: Context): TokenStorage {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "jwt_token"
    }
    override fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    override fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    override fun clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }
}