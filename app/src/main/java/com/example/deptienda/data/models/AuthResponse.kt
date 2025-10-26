package com.example.deptienda.data.models

data class AuthResponse(
    val success: Boolean,
    val user: User? = null,
    val message: String? = null
)