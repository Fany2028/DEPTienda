package com.example.deptienda.data.dao

import com.example.deptienda.data.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserDaoTemp : UserDao {
    private val demoUsers = mutableListOf(
        User(
            id = "1",
            name = "Ana García",
            email = "ana@duocuc.cl",
            phone = "123456789",
            address = "Calle Principal 123",
            password = "123456"
        ),
        User(
            id = "2",
            name = "Carlos Pereira",
            email = "carlos@duocuc.cl",
            phone = "987654321",
            address = "Avenida Central 456",
            password = "654321"
        )
    )

    override suspend fun login(email: String, password: String): User? {
        return demoUsers.find { it.email == email && it.password == password }
    }

    override suspend fun getUserByEmail(email: String): User? {
        return demoUsers.find { it.email == email }
    }

    override suspend fun insertUser(user: User) {
        demoUsers.add(user)
    }

    override fun getUserById(userId: String): Flow<User?> {
        return flow {
            emit(demoUsers.find { it.id == userId })
        }
    }

    override suspend fun updateUser(user: User) {
        val index = demoUsers.indexOfFirst { it.id == user.id }
        if (index != -1) {
            demoUsers[index] = user
        }
    }

    override suspend fun deleteUser(user: User) {
        demoUsers.removeIf { it.id == user.id }
    }

    override fun getAllUsers(): Flow<List<User>> {
        return flow {
            emit(demoUsers.toList())
        }
    }

    override suspend fun userExists(email: String): Int {
        return if (demoUsers.any { it.email == email }) 1 else 0
    }

    override fun getUserByEmailFlow(email: String): Flow<User?> {
        return flow {
            emit(demoUsers.find { it.email == email })
        }
    }
}