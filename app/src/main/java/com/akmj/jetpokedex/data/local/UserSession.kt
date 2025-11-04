package com.akmj.jetpokedex.data.local

import android.content.Context

class UserSession(context: Context) {

    private val sharedPref = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveUser(email: String) {
        sharedPref.edit()
            .putString("email", email)
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPref.contains("email")
    }
    fun logout() {
        sharedPref.edit().remove("email").apply()
    }

    fun getLoggedInEmail(): String? {
        return sharedPref.getString("email", null)
    }
}
