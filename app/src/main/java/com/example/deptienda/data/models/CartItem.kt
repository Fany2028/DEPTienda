package com.example.deptienda.data.models

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
    val productName: String
        get() = ""

    val imageUrl: String
        get() = ""

    val price: Double
        get() = 0.0

    val totalPrice: Double
        get() = price * quantity
}