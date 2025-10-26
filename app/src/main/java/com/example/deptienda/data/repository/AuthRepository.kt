package com.example.deptienda.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.deptienda.data.dao.UserDao
import com.example.deptienda.data.models.User

class AuthRepository(
    private val userDao: UserDao,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun login(email: String, password: String): Boolean {
        return try {
            val user = userDao.login(email, password)
            if (user != null) {
                sharedPreferences.edit {
                    putBoolean("is_logged_in", true)
                    putString("user_id", user.id)
                    putString("user_email", user.email)
                    putString("user_name", user.name)
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
    }

    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }
}