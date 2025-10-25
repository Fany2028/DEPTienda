package com.example.deptienda.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.deptienda.data.dao.UserDao
import com.example.deptienda.data.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class UserRepository(
    private val context: Context,
    private val userDao: UserDao
) {

    private companion object {
        const val PREFS_NAME = "user_prefs"
        const val KEY_CURRENT_USER_ID = "current_user_id"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // --- OPERACIONES DE FLOW (observables) ---
    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()

    fun getUserById(userId: String): Flow<User?> = userDao.getUserById(userId)

    fun getUserByEmail(email: String): Flow<User?> = userDao.getUserByEmail(email)

    // --- OPERACIONES SUSPEND ---
    suspend fun registerUser(user: User): Boolean {
        // Verificar si el usuario ya existe
        val exists = userDao.userExists(user.email) > 0
        if (exists) {
            return false
        }

        userDao.insertUser(user)
        return true
    }

    suspend fun loginUser(email: String, password: String): User? {
        val user = userDao.login(email)
        // IMPORTANTE: Tu modelo User necesita tener campo password
        return user?.takeIf { it.password == password }
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    suspend fun userExists(email: String): Boolean {
        return userDao.userExists(email) > 0
    }

    // --- GESTIÓN DE SESIÓN ---
    fun setCurrentUser(user: User) {
        sharedPreferences.edit().putString(KEY_CURRENT_USER_ID, user.id).apply()
    }

    suspend fun getCurrentUser(): User? {
        val userId = sharedPreferences.getString(KEY_CURRENT_USER_ID, null)
        return if (userId != null) {
            getUserByIdOnce(userId)
        } else {
            null
        }
    }

    fun getCurrentUserId(): String? {
        return sharedPreferences.getString(KEY_CURRENT_USER_ID, null)
    }

    fun logout() {
        sharedPreferences.edit().remove(KEY_CURRENT_USER_ID).apply()
    }

    // Función auxiliar para obtener usuario una vez (sin Flow)
    private suspend fun getUserByIdOnce(userId: String): User? {
        return userDao.getUserById(userId).firstOrNull()
    }

    // Función auxiliar para obtener usuario por email una vez (sin Flow)
    suspend fun getUserByEmailOnce(email: String): User? {
        return userDao.getUserByEmail(email).firstOrNull()
    }
}