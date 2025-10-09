package com.example.deptienda.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val address: String = ""
)

data class RegisterForm(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val address: String = ""
)

data class LoginForm(
    val email: String = "",
    val password: String = ""
)