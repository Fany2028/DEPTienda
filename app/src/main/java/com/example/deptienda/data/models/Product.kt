package com.example.com.dep.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val id: String,
    val name: String,
    val price: Double,
    val originalPrice: Double? = null,
    val imageUrl: String,
    val category: String,
    val sizes: List<String> = listOf("S", "M", "L", "XL"),
    val colors: List<String> = listOf("Negro", "Blanco", "Azul", "Rojo"),
    val description: String = "",
    val inStock: Boolean = true,
    val rating: Double = 4.5
) {
    val hasDiscount: Boolean
        get() = originalPrice != null && originalPrice > price
}