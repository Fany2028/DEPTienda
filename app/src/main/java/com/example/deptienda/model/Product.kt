package com.example.deptienda.model

data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val category: String,
    val imageUrl: String,
    var stock: Int = 10
)

data class CartItem(
    val product: Product,
    var quantity: Int
)