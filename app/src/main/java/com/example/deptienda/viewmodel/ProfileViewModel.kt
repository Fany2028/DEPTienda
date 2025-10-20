package com.example.com.dep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.com.dep.data.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // Simular carga de datos del usuario
                kotlinx.coroutines.delay(1000)

                val sampleUser = User(
                    id = "1",
                    name = "Juan Pérez",
                    email = "juan.perez@example.com",
                    phone = "+1234567890",
                    address = "Calle Principal 123, Ciudad"
                )

                _user.value = sampleUser
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar datos del usuario: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simular actualización
                kotlinx.coroutines.delay(500)
                _user.value = user
            } catch (e: Exception) {
                _errorMessage.value = "Error al actualizar perfil: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}