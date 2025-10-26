package com.example.deptienda.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.deptienda.data.models.CartItem
import com.example.deptienda.data.models.Product

@Composable
fun CartItem(
    cartItem: CartItem,
    product: Product?,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (product != null) {
                val imageResource = getCartItemDrawableResource(context, product.imageUrl)

                if (imageResource != 0) {
                    Image(
                        painter = painterResource(id = imageResource),
                        contentDescription = product.name,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Img",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "?",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product?.name ?: "Producto",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Talla: ${cartItem.selectedSize}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Color: ${cartItem.selectedColor}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$${"%.2f".format(product?.price ?: 0.0)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Cantidad: ${cartItem.quantity}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            if (cartItem.quantity > 1) onQuantityChange(cartItem.quantity - 1)
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Disminuir cantidad"
                        )
                    }

                    Text(
                        text = cartItem.quantity.toString(),
                        modifier = Modifier.padding(horizontal = 8.dp),
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = { onQuantityChange(cartItem.quantity + 1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Aumentar cantidad"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar del carrito",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

fun getCartItemDrawableResource(context: android.content.Context, imageName: String): Int {
    return try {
        context.resources.getIdentifier(imageName, "drawable", context.packageName)
    } catch (e: Exception) {
        0
    }
}