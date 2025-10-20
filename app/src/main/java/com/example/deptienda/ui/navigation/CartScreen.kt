package com.example.com.dep.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.com.dep.ui.components.CartItem
import com.example.com.dep.ui.components.EmptyState
import com.example.com.dep.ui.components.LoadingIndicator
import com.example.com.dep.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBackClick: () -> Unit,
    onContinueShopping: () -> Unit,
    viewModel: MainViewModel
) {
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val products by viewModel.products.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val cartTotal = viewModel.getCartTotal()
    val cartItemsCount = viewModel.getCartItemsCount()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Mi Carrito",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                },
                actions = {
                    if (cartItems.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                viewModel.clearCart()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Vaciar carrito"
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                CartBottomBar(
                    total = cartTotal,
                    itemCount = cartItemsCount,
                    onCheckout = { /* TODO: Implementar checkout */ },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    LoadingIndicator(message = "Cargando carrito...")
                }

                cartItems.isEmpty() -> {
                    EmptyCartState(onContinueShopping = onContinueShopping)
                }

                else -> {
                    CartContent(
                        cartItems = cartItems,
                        products = products,
                        onQuantityChange = { cartItem, newQuantity ->
                            viewModel.updateCartItemQuantity(
                                cartItem.productId, // ✅ Cambiado de product.id a productId
                                cartItem.selectedSize,
                                cartItem.selectedColor,
                                newQuantity
                            )
                        },
                        onRemoveItem = { cartItem ->
                            viewModel.removeFromCart(
                                cartItem.productId, // ✅ Cambiado de product.id a productId
                                cartItem.selectedSize,
                                cartItem.selectedColor
                            )
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun CartContent(
    cartItems: List<com.example.com.dep.data.models.CartItem>,
    products: List<com.example.com.dep.data.models.Product>,
    onQuantityChange: (com.example.com.dep.data.models.CartItem, Int) -> Unit,
    onRemoveItem: (com.example.com.dep.data.models.CartItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Resumen rápido
        CartSummary(
            itemCount = cartItems.sumOf { it.quantity },
            total = cartItems.sumOf { cartItem ->
                val product = products.find { it.id == cartItem.productId }
                (product?.price ?: 0.0) * cartItem.quantity
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Lista de items
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(cartItems, key = { "${it.productId}-${it.selectedSize}-${it.selectedColor}" }) { cartItem ->
                val product = products.find { it.id == cartItem.productId }
                CartItem(
                    cartItem = cartItem,
                    product = product, // ✅ Pasar el producto encontrado
                    onQuantityChange = { newQuantity ->
                        onQuantityChange(cartItem, newQuantity)
                    },
                    onRemove = {
                        onRemoveItem(cartItem)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun CartSummary(
    itemCount: Int,
    total: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Resumen del Pedido",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Items:")
                Text("$itemCount")
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Envío:")
                Text("Gratis")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Divider()

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${String.format("%.2f", total)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun CartBottomBar(
    total: Double,
    itemCount: Int,
    onCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        tonalElevation = 8.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$${String.format("%.2f", total)}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = "$itemCount items",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onCheckout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Pagar",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Proceder al Pago",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun EmptyCartState(
    onContinueShopping: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        EmptyState(
            title = "Tu carrito está vacío",
            message = "Explora nuestros productos y encuentra algo increíble para agregar a tu carrito."
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onContinueShopping,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Comenzar a Comprar")
        }
    }
}