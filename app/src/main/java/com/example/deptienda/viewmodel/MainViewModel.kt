package com.example.deptienda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deptienda.data.models.CartItem
import com.example.deptienda.data.models.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    init {
        loadSampleProducts()
    }

    private fun loadSampleProducts() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                kotlinx.coroutines.delay(1500)
                val sampleProducts = listOf(
                    Product(
                        id = "1",
                        name = "Camiseta Básica Negra",
                        price = 19.99,
                        originalPrice = 24.99,
                        imageUrl = "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=500",
                        category = "Camisetas",
                        description = "Camiseta de algodón 100% básica, perfecta para uso diario"
                    ),
                    Product(
                        id = "2",
                        name = "Jeans Slim Fit",
                        price = 49.99,
                        imageUrl = "https://images.unsplash.com/photo-1542272604-787c3835535d?w=500",
                        category = "Pantalones",
                        description = "Jeans ajustados de corte moderno, cómodos y elegantes"
                    ),
                    Product(
                        id = "3",
                        name = "Sudadera con Capucha",
                        price = 39.99,
                        originalPrice = 49.99,
                        imageUrl = "https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=500",
                        category = "Sudaderas",
                        description = "Sudadera cómoda con capucha, ideal para días frescos"
                    ),
                    Product(
                        id = "4",
                        name = "Zapatos Deportivos",
                        price = 79.99,
                        imageUrl = "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500",
                        category = "Calzado",
                        description = "Zapatos deportivos ultraligeros para máximo rendimiento"
                    ),
                    Product(
                        id = "5",
                        name = "Chaqueta Denim",
                        price = 59.99,
                        originalPrice = 69.99,
                        imageUrl = "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=500",
                        category = "Chaquetas",
                        description = "Chaqueta denim clásica, nunca pasa de moda"
                    ),
                    Product(
                        id = "6",
                        name = "Vestido Verano",
                        price = 34.99,
                        imageUrl = "https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=500",
                        category = "Vestidos",
                        description = "Vestido ligero perfecto para los días de verano"
                    )
                )

                _products.value = sampleProducts
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar productos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectProduct(product: Product) {
        _selectedProduct.value = product
    }

    fun addToCart(product: Product, size: String = "M", color: String = "Negro") {
        viewModelScope.launch {
            try {
                val existingItem = _cartItems.value.find {
                    it.productId == product.id && it.selectedSize == size && it.selectedColor == color
                }

                if (existingItem != null) {
                    val updatedItems = _cartItems.value.map { item ->
                        if (item.productId == product.id && item.selectedSize == size && item.selectedColor == color) {
                            item.copy(quantity = item.quantity + 1)
                        } else {
                            item
                        }
                    }
                    _cartItems.value = updatedItems
                } else {
                    val newItem = CartItem(
                        userId = "default_user",
                        productId = product.id,
                        selectedSize = size,
                        selectedColor = color,
                        quantity = 1
                    )
                    _cartItems.value = _cartItems.value + newItem
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al agregar al carrito: ${e.message}"
            }
        }
    }

    fun updateCartItemQuantity(productId: String, size: String, color: String, newQuantity: Int) {
        viewModelScope.launch {
            try {
                val updatedItems = _cartItems.value.map { item ->
                    if (item.productId == productId && item.selectedSize == size && item.selectedColor == color) {
                        if (newQuantity <= 0) {
                            return@map null
                        }
                        item.copy(quantity = newQuantity)
                    } else {
                        item
                    }
                }.filterNotNull()

                _cartItems.value = updatedItems
            } catch (e: Exception) {
                _errorMessage.value = "Error al actualizar cantidad: ${e.message}"
            }
        }
    }

    fun removeFromCart(productId: String, size: String, color: String) {
        viewModelScope.launch {
            try {
                val updatedItems = _cartItems.value.filterNot {
                    it.productId == productId && it.selectedSize == size && it.selectedColor == color
                }
                _cartItems.value = updatedItems
            } catch (e: Exception) {
                _errorMessage.value = "Error al eliminar del carrito: ${e.message}"
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            try {
                _cartItems.value = emptyList()
            } catch (e: Exception) {
                _errorMessage.value = "Error al vaciar carrito: ${e.message}"
            }
        }
    }

    fun getCartTotal(): Double {
        return _cartItems.value.sumOf { cartItem ->
            val product = _products.value.find { it.id == cartItem.productId }
            (product?.price ?: 0.0) * cartItem.quantity
        }
    }

    fun getCartItemsCount(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun getProductsByCategory(category: String): List<Product> {
        return _products.value.filter { it.category == category }
    }

    fun getProductForCartItem(cartItem: CartItem): Product? {
        return _products.value.find { it.id == cartItem.productId }
    }

    fun getCartItemTotal(cartItem: CartItem): Double {
        val product = getProductForCartItem(cartItem)
        return (product?.price ?: 0.0) * cartItem.quantity
    }

    fun getCartItemsWithProducts(): List<Pair<CartItem, Product?>> {
        return _cartItems.value.map { cartItem ->
            cartItem to getProductForCartItem(cartItem)
        }
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun getFilteredProducts(): List<Product> {
        return _selectedCategory.value?.let { category ->
            _products.value.filter { it.category == category }
        } ?: _products.value
    }

    fun searchProducts(query: String): List<Product> {
        return if (query.isBlank()) {
            _products.value
        } else {
            _products.value.filter { product ->
                product.name.contains(query, ignoreCase = true) ||
                        product.description.contains(query, ignoreCase = true) ||
                        product.category.contains(query, ignoreCase = true)
            }
        }
    }

    fun getCategories(): List<String> {
        return _products.value.map { it.category }.distinct()
    }

    fun isProductInCart(productId: String): Boolean {
        return _cartItems.value.any { it.productId == productId }
    }

    fun getProductQuantityInCart(productId: String): Int {
        return _cartItems.value
            .filter { it.productId == productId }
            .sumOf { it.quantity }
    }

    fun refreshProducts() {
        loadSampleProducts()
    }
}