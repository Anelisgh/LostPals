package com.example.lostpals.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("LostPalsPrefs", Context.MODE_PRIVATE)

    companion object {
        const val KEY_USER_ID = "user_id"
        const val IS_LOGGED_IN = "is_logged_in"
        const val KEY_USERNAME      = "username"
    }

    fun saveUserId(userId: Long) {
        prefs.edit { putLong(KEY_USER_ID, userId) }
    }

    fun getUserId(): Long {
        return prefs.getLong(KEY_USER_ID, -1) // -1 adica niciun user
    }

    fun saveUsername(name: String) {
        prefs.edit { putString(KEY_USERNAME, name) }
    }
    fun getUsername(): String? =
        prefs.getString(KEY_USERNAME, null)

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(IS_LOGGED_IN, false)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        prefs.edit { putBoolean(IS_LOGGED_IN, isLoggedIn) }
    }

    fun logout() {
        prefs.edit { clear() }
    }
}