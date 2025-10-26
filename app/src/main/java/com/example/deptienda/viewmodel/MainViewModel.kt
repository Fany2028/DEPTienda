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
                kotlinx.coroutines.delay(500)
                val sampleProducts = listOf(
                    Product(
                        id = "1",
                        name = "Jean Baggy",
                        price = 29.990,
                        originalPrice = 34.990,
                        imageUrl = "img1_baggyplomo",
                        category = "Pantalones",
                        description = "Perfecto y cómodo para uso diario"
                    ),
                    Product(
                        id = "2",
                        name = "Sueter diseño mariposa",
                        price = 17.990,
                        imageUrl = "img2_suetermariposa",
                        category = "Sudaderas",
                        description = "Sudadera con bello diseño de mariposa"
                    ),
                    Product(
                        id = "3",
                        name = "Conjunto de verano",
                        price = 39.990,
                        originalPrice = 49.990,
                        imageUrl = "img_3conjuntoverano",
                        category = "Pantalones",
                        description = "Conjunto de verano ideal para días veraniegos"
                    ),
                    Product(
                        id = "4",
                        name = "Sueter a rayas",
                        price = 24.990,
                        imageUrl = "img4_sueterrayas",
                        category = "Sudaderas",
                        description = "Sueter con estilo a rayas y elegante"
                    ),
                    Product(
                        id = "5",
                        name = "Sueter beige",
                        price = 25.990,
                        originalPrice = 29.990,
                        imageUrl = "img5_sueterbeige",
                        category = "Sudaderas",
                        description = "Sudadera cómoda"
                    ),
                    Product(
                        id = "6",
                        name = "Pantalón vaquero corto",
                        price = 26.990,
                        imageUrl = "img6_pantalonvaquerocorto",
                        category = "Pantalones",
                        description = "Pantalón clásico, nunca pasa de moda"
                    ),
                    Product(
                        id = "7",
                        name = "Polerón blanco",
                        price = 19.990,
                        originalPrice = 25.990,
                        imageUrl = "img7_poleronblanco",
                        category = "Sudaderas",
                        description = "Sudadera cómoda con capucha, ideal para días frescos"
                    ),
                    Product(
                        id = "8",
                        name = "Sudadera diseñada",
                        price = 32.990,
                        originalPrice = 40.990,
                        imageUrl = "img8_sudaderarengoku",
                        category = "Sudaderas",
                        description = "Sudadera con diseño personalizado"
                    ),
                    Product(
                        id = "9",
                        name = "Camiseta Harajaku",
                        price = 12.990,
                        imageUrl = "img9_camisetaharajaku",
                        category = "Camisetas",
                        description = "Camiseta de corte moderno"
                    ),
                    Product(
                        id = "10",
                        name = "Sueter blanco",
                        price = 15.990,
                        originalPrice = 17.990,
                        imageUrl = "img10_sueterblanco",
                        category = "Sudaderas",
                        description = "Sudadera cómoda"
                    ),
                    Product(
                        id = "11",
                        name = "Sudadera squelette",
                        price = 34.990,
                        imageUrl = "img11_suetersquelette",
                        category = "Sudaderas",
                        description = "Sudadera cómoda interesante diseño"
                    ),
                    Product(
                        id = "12",
                        name = "Jean holgado",
                        price = 34.990,
                        imageUrl = "img_jeanholgado",
                        category = "Pantalones",
                        description = "Pantalones cómodos para estilo y uso diario"
                    ),
                    Product(
                        id = "13",
                        name = "Vestido color azul",
                        price = 39.99,
                        originalPrice = 49.99,
                        imageUrl = "img13_vestidoazul",
                        category = "Vestidos",
                        description = "Vestido de material cómodo y ligero"
                    ),
                    Product(
                        id = "14",
                        name = "Sudadera con Capucha",
                        price = 19.990,
                        originalPrice = 25.990,
                        imageUrl = "img14_sueteroveresize",
                        category = "Sudaderas",
                        description = "Sudadera cómoda"
                    ),
                    Product(
                        id = "15",
                        name = "Pantalón vaquero largo",
                        price = 29.990,
                        originalPrice = 39.990,
                        imageUrl = "img_pantalonvaquero",
                        category = "Pantalones",
                        description = "Pantalón clásico, nunca pasa de moda"
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