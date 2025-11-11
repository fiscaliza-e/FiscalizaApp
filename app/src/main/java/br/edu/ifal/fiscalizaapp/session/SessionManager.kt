package br.edu.ifal.fiscalizaapp.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("FiscalizaAppPrefs", Context.MODE_PRIVATE)

    companion object {
        const val USER_API_ID = "user_api_id"
    }

    fun saveUserApiId(id: Int) {
        val editor = prefs.edit()
        editor.putInt(USER_API_ID, id)
        editor.apply()
    }

    fun getUserApiId(): Int {
        // Retorna -1 se nenhum ID for encontrado
        return prefs.getInt(USER_API_ID, -1)
    }

    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}
