package com.example.deptienda.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.deptienda.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserRepository(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("dep_store_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val usersKey = "registered_users"
    private val currentUserKey = "current_user"

    // Guardar un nuevo usuario
    fun registerUser(user: User): Boolean {
        val users = getRegisteredUsers().toMutableList()

        // Verificar si el email ya existe
        if (users.any { it.email == user.email }) {
            return false
        }

        // Agregar el nuevo usuario con ID único
        val newUser = user.copy(id = System.currentTimeMillis().toString())
        users.add(newUser)

        // Guardar en SharedPreferences
        val usersJson = gson.toJson(users)
        sharedPreferences.edit().putString(usersKey, usersJson).apply()
        return true
    }

    // Obtener todos los usuarios registrados
    fun getRegisteredUsers(): List<User> {
        val usersJson = sharedPreferences.getString(usersKey, "[]") ?: "[]"
        val type = object : TypeToken<List<User>>() {}.type
        return gson.fromJson(usersJson, type) ?: emptyList()
    }

    // Login de usuario
    fun loginUser(email: String, password: String): User? {
        val users = getRegisteredUsers()
        return users.find { it.email == email && it.password == password }
    }

    // Guardar usuario actual (sesión)
    fun setCurrentUser(user: User?) {
        if (user == null) {
            sharedPreferences.edit().remove(currentUserKey).apply()
        } else {
            val userJson = gson.toJson(user)
            sharedPreferences.edit().putString(currentUserKey, userJson).apply()
        }
    }

    // Obtener usuario actual (sesión)
    fun getCurrentUser(): User? {
        val userJson = sharedPreferences.getString(currentUserKey, null) ?: return null
        return gson.fromJson(userJson, User::class.java)
    }

    // Cerrar sesión
    fun logout() {
        setCurrentUser(null)
    }
}