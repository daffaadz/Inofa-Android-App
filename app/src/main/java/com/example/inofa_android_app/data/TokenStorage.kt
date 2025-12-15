package com.example.inofa_android_app.data

import android.content.Context
import android.content.SharedPreferences

object TokenStorage {
    private const val PREF_NAME = "inofa_auth"
    private const val KEY_TOKEN = "token"
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        if (!::prefs.isInitialized) {
            prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }
    }

    fun saveToken(token: String?) {
        if (!::prefs.isInitialized) return
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        if (!::prefs.isInitialized) return null
        return prefs.getString(KEY_TOKEN, null)
    }

    fun clear() {
        if (!::prefs.isInitialized) return
        prefs.edit().remove(KEY_TOKEN).apply()
    }
}
