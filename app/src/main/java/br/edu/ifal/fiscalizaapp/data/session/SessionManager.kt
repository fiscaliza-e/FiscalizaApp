package br.edu.ifal.fiscalizaapp.data.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("FiscalizaAppPrefs", Context.MODE_PRIVATE)

    companion object {
        const val USER_API_ID = "user_api_id"
    }

    fun saveUserApiId(id: Int) {
        prefs.edit().putInt(USER_API_ID, id).apply()
    }

    fun getUserApiId(): Int {
        return prefs.getInt(USER_API_ID, -1)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
