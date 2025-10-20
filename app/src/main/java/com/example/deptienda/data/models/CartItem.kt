package com.example.com.dep.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "cart_items",
    primaryKeys = ["userId", "productId", "selectedSize", "selectedColor"]
)
data class CartItem(
    val userId: String = "default_user",
    val productId: String,
    val selectedSize: String,
    val selectedColor: String,
    var quantity: Int = 1,
    val addedAt: Long = System.currentTimeMillis()
) {
    // Esta propiedad no se persiste en Room
    val totalPrice: Double
        get() = 0.0 // Se calculará cuando se combine con Product
}