package com.example.deptienda.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.deptienda.ui.components.CartItem
import com.example.deptienda.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBackClick: () -> Unit,
    onContinueShopping: () -> Unit,
    onCheckout: () -> Unit,
    viewModel: MainViewModel = viewModel()
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val products by viewModel.products.collectAsState()
    val cartTotal by remember { derivedStateOf { viewModel.getCartTotal() } }
    val cartItemsCount by remember { derivedStateOf { viewModel.getCartItemsCount() } }

    val cartItemsWithProducts = remember(cartItems, products) {
        cartItems.mapNotNull { cartItem ->
            val product = products.find { it.id == cartItem.productId }
            if (product != null) {
                cartItem to product
            } else {
                null
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi Carrito") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            if (cartItemsWithProducts.isNotEmpty()) {
                Surface(
                    tonalElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total:",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "$${"%.2f".format(cartTotal)}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = onCheckout,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = cartItemsWithProducts.isNotEmpty()
                        ) {
                            Text("Proceder al Pago")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        if (cartItemsWithProducts.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Carrito vacío",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tu carrito está vacío",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Agrega algunos productos para continuar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onContinueShopping) {
                    Text("Seguir Comprando")
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Productos en carrito:")
                        Text(
                            "$cartItemsCount ${if (cartItemsCount == 1) "producto" else "productos"}",
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(
                        items = cartItemsWithProducts,
                        key = { (cartItem, _) ->
                            "${cartItem.productId}-${cartItem.selectedSize}-${cartItem.selectedColor}"
                        }
                    ) { (cartItem, product) ->
                        CartItem(
                            cartItem = cartItem,
                            product = product,
                            onQuantityChange = { newQuantity ->
                                viewModel.updateCartItemQuantity(
                                    productId = cartItem.productId,
                                    size = cartItem.selectedSize,
                                    color = cartItem.selectedColor,
                                    newQuantity = newQuantity
                                )
                            },
                            onRemove = {
                                viewModel.removeFromCart(
                                    productId = cartItem.productId,
                                    size = cartItem.selectedSize,
                                    color = cartItem.selectedColor
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}