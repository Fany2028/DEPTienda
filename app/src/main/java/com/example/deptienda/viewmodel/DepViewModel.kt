package com.example.deptienda.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigationevent.NavigationEvent
import com.example.deptienda.model.LoginForm
import com.example.deptienda.model.RegisterForm
import com.example.deptienda.model.User
import com.example.deptienda.navigation.NavigationEvent
import com.example.deptienda.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DepViewModel : ViewModel() {

    private lateinit var userRepository: UserRepository

    // Inicializar el repository con el contexto
    fun initialize(context: Context) {
        userRepository = UserRepository(context)
    }

    // --- NAVEGACIÓN ---
    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    // --- FORMULARIO DE REGISTRO ---
    private val _registerForm = MutableStateFlow(RegisterForm())
    val registerForm: StateFlow<RegisterForm> = _registerForm.asStateFlow()

    private val _registerErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val registerErrors: StateFlow<Map<String, String>> = _registerErrors.asStateFlow()

    // --- FORMULARIO DE LOGIN ---
    private val _loginForm = MutableStateFlow(LoginForm())
    val loginForm: StateFlow<LoginForm> = _loginForm.asStateFlow()

    private val _loginErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val loginErrors: StateFlow<Map<String, String>> = _loginErrors.asStateFlow()

    // --- ESTADO DE USUARIO ---
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // --- MENSAJES ---
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    // --- FUNCIONES DE NAVEACIÓN ---
    fun navigateTo(screen: Screen) {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(route = screen))
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.PopBackStack)
        }
    }

    // --- REGISTRO ---
    fun updateRegisterForm(
        name: String = _registerForm.value.name,
        email: String = _registerForm.value.email,
        phone: String = _registerForm.value.phone,
        password: String = _registerForm.value.password,
        confirmPassword: String = _registerForm.value.confirmPassword,
        address: String = _registerForm.value.address
    ) {
        _registerForm.update {
            it.copy(
                name = name,
                email = email,
                phone = phone,
                password = password,
                confirmPassword = confirmPassword,
                address = address
            )
        }
        validateRegisterForm()
    }

    private fun validateRegisterForm() {
        val errors = mutableMapOf<String, String>()

        if (_registerForm.value.name.isBlank()) {
            errors["name"] = "El nombre es requerido"
        }

        if (_registerForm.value.email.isBlank()) {
            errors["email"] = "El email es requerido"
        } else if (!isValidEmail(_registerForm.value.email)) {
            errors["email"] = "Email no válido"
        }

        if (_registerForm.value.phone.isBlank()) {
            errors["phone"] = "El teléfono es requerido"
        } else if (!isValidPhone(_registerForm.value.phone)) {
            errors["phone"] = "Teléfono no válido"
        }

        if (_registerForm.value.password.length < 6) {
            errors["password"] = "Mínimo 6 caracteres"
        }

        if (_registerForm.value.password != _registerForm.value.confirmPassword) {
            errors["confirmPassword"] = "Las contraseñas no coinciden"
        }

        if (_registerForm.value.address.isBlank()) {
            errors["address"] = "La dirección es requerida"
        }

        _registerErrors.value = errors
    }

    fun submitRegister() {
        validateRegisterForm()
        if (_registerErrors.value.isEmpty()) {
            val user = User(
                name = _registerForm.value.name,
                email = _registerForm.value.email,
                phone = _registerForm.value.phone,
                password = _registerForm.value.password,
                address = _registerForm.value.address
            )

            if (::userRepository.isInitialized) {
                val success = userRepository.registerUser(user)
                if (success) {
                    _message.value = "¡Cuenta creada exitosamente!"
                    userRepository.setCurrentUser(user)
                    _currentUser.value = user
                    _registerForm.value = RegisterForm() // Limpiar formulario
                    navigateTo(Screen.Home)
                } else {
                    _message.value = "El email ya está registrado"
                }
            }
        }
    }

    // --- LOGIN ---
    fun updateLoginForm(email: String = _loginForm.value.email, password: String = _loginForm.value.password) {
        _loginForm.update { it.copy(email = email, password = password) }
        validateLoginForm()
    }

    private fun validateLoginForm() {
        val errors = mutableMapOf<String, String>()

        if (_loginForm.value.email.isBlank()) {
            errors["email"] = "El email es requerido"
        } else if (!isValidEmail(_loginForm.value.email)) {
            errors["email"] = "Email no válido"
        }

        if (_loginForm.value.password.isBlank()) {
            errors["password"] = "La contraseña es requerida"
        } else if (_loginForm.value.password.length < 6) {
            errors["password"] = "Mínimo 6 caracteres"
        }

        _loginErrors.value = errors
    }

    fun submitLogin() {
        validateLoginForm()
        if (_loginErrors.value.isEmpty()) {
            if (::userRepository.isInitialized) {
                val user = userRepository.loginUser(_loginForm.value.email, _loginForm.value.password)
                if (user != null) {
                    userRepository.setCurrentUser(user)
                    _currentUser.value = user
                    _message.value = "¡Bienvenido ${user.name}!"
                    _loginForm.value = LoginForm() // Limpiar formulario
                    navigateTo(Screen.Home)
                } else {
                    _message.value = "Email o contraseña incorrectos"
                }
            }
        }
    }

    // --- SESIÓN ---
    fun checkCurrentUser() {
        if (::userRepository.isInitialized) {
            _currentUser.value = userRepository.getCurrentUser()
        }
    }

    fun logout() {
        if (::userRepository.isInitialized) {
            userRepository.logout()
            _currentUser.value = null
            _message.value = "Sesión cerrada"
            navigateTo(Screen.Home)
        }
    }

    // --- FUNCIONES DE VALIDACIÓN ---
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhone(phone: String): Boolean {
        return phone.length >= 9 && phone.all { it.isDigit() }
    }

    // Limpiar mensajes
    fun clearMessage() {
        _message.value = null
    }
}

annotation class UserRepository(val context: Context)