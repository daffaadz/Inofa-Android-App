package com.example.inofa_android_app.data

import android.content.Context
import android.content.SharedPreferences

object UserRoleStorage {
    private const val PREF_NAME = "inofa_user"
    private const val KEY_ROLE = "role"
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        if (!::prefs.isInitialized) {
            prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }
    }

    fun saveRole(role: String?) {
        if (!::prefs.isInitialized) return
        prefs.edit().putString(KEY_ROLE, role).apply()
    }

    fun getRole(): String? {
        if (!::prefs.isInitialized) return null
        return prefs.getString(KEY_ROLE, null)
    }

    fun isClient(): Boolean = getRole() == "client"

    fun isDeveloper(): Boolean = getRole() == "developer"

    fun clear() {
        if (!::prefs.isInitialized) return
        prefs.edit().remove(KEY_ROLE).apply()
    }
}
