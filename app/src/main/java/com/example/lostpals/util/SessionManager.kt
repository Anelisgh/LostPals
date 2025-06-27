package com.example.lostpals.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import android.util.Log

// clasa care pastreaza toate datele de sesiune pentru utilizator
class SessionManager(context: Context) {
    // referinta la SharedPreferences, unde se salveaza datele local
    private val prefs: SharedPreferences =
        context.getSharedPreferences("LostPalsPrefs", Context.MODE_PRIVATE)

    companion object {
        const val KEY_USER_ID = "user_id"         // cheia pentru id-ul utilizatorului
        const val IS_LOGGED_IN = "is_logged_in"   // cheia pentru statusul de login
        const val KEY_USERNAME = "username"       // cheia pentru numele de utilizator
        const val KEY_PROFILE_IMAGE = "profile_image" // cheia pentru imaginea de profil
    }

    // salveaza id-ul utilizatorului in preferinte
    fun saveUserId(userId: Long) {
        prefs.edit { putLong(KEY_USER_ID, userId) }
    }

    // returneaza id-ul utilizatorului salvat, sau -1 daca nu exista
    fun getUserId(): Long {
        val id = prefs.getLong(KEY_USER_ID, -1)
        Log.d("SessionManager", "getUserId() = $id")
        return id
    }

    // salveaza numele de utilizator
    fun saveUsername(name: String) {
        prefs.edit { putString(KEY_USERNAME, name) }
    }

    // returneaza numele de utilizator salvat, sau null daca nu exista
    fun getUsername(): String? =
        prefs.getString(KEY_USERNAME, null)

    // verifica daca utilizatorul este logat
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(IS_LOGGED_IN, false)
    }

    // seteaza statusul de login (adevarat sau fals)
    fun setLoggedIn(isLoggedIn: Boolean) {
        prefs.edit { putBoolean(IS_LOGGED_IN, isLoggedIn) }
    }

    // sterge toate datele salvate (deconecteaza utilizatorul)
    fun logout() {
        prefs.edit { clear() }
    }

    // Functii pentru poza de profil
    // salveaza calea catre imaginea de profil
    fun saveProfileImage(imagePath: String) {
        prefs.edit { putString(KEY_PROFILE_IMAGE, imagePath) }
    }

    // returneaza calea catre imaginea de profil, sau null daca nu exista
    fun getProfileImage(): String? {
        return prefs.getString(KEY_PROFILE_IMAGE, null)
    }
}
