package com.example.com.dep.data.dao

import androidx.room.*
import com.example.com.dep.data.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    // Obtener todos los usuarios
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    // Obtener usuario por ID
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: String): Flow<User?>

    // Obtener usuario por email
    @Query("SELECT * FROM users WHERE email = :email")
    fun getUserByEmail(email: String): Flow<User?>

    // Insertar o actualizar usuario
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    // Actualizar usuario
    @Update
    suspend fun updateUser(user: User)

    // Eliminar usuario
    @Delete
    suspend fun deleteUser(user: User)

    // Verificar si existe un usuario por email
    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun userExists(email: String): Int

    // Login - verificar credenciales
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun login(email: String): User?
}