package com.example.deptienda.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey val id: String,
    val userId: String = "default_user",
    val itemsJson: String, // Para simplificar, guardamos como JSON
    val total: Double,
    val date: String,
    val status: String = "pending"
)