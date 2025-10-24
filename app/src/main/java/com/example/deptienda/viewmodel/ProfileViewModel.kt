package com.example.deptienda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deptienda.data.models.User
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
                    email = "ju.perez@duocuc.cl",
                    phone = "+56 9 8736 9183",
                    address = "Calle Principal 123, Santiago, Chile"
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