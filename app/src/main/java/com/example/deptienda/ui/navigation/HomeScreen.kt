package com.example.deptienda.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.deptienda.data.models.Product
import com.example.deptienda.ui.components.LoadingIndicator
import com.example.deptienda.ui.components.ProductCard
import com.example.deptienda.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProductClick: (Product) -> Unit,
    onCartClick: () -> Unit,
    onCategoriesClick: () -> Unit,
    viewModel: MainViewModel
) {
    val products by viewModel.products.collectAsStateWithLifecycle()
    val cartItemsCount by viewModel.cartItems.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val cartCount = cartItemsCount.sumOf { it.quantity }

    LaunchedEffect(products) {
        if (products.isNotEmpty()) {
            println("DEBUG - Total productos: ${products.size}")
            products.forEachIndexed { index, product ->
                println("DEBUG - Producto ${index + 1}: ${product.name} -> Imagen: ${product.imageUrl}")
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "DEP",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onCategoriesClick) {
                        Icon(
                            imageVector = Icons.Default.Category,
                            contentDescription = "Categorías"
                        )
                    }

                    BadgedBox(
                        badge = {
                            if (cartCount > 0) {
                                Badge {
                                    Text(cartCount.toString())
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = onCartClick) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Carrito"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            LoadingIndicator()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        onProductClick = onProductClick,
                        onAddToCart = { productToAdd ->
                            viewModel.addToCart(productToAdd)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}