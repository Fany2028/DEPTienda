package com.example.deptienda.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.deptienda.data.models.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    product: Product,
    onProductClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .width(160.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = { onProductClick(product) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                val imageResource = getDrawableResource(context, product.imageUrl)

                if (imageResource != 0) {
                    Image(
                        painter = painterResource(id = imageResource),
                        contentDescription = product.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Imagen ${product.id}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                // BADGE DE DESCUENTO
                if (product.hasDiscount) {
                    Text(
                        text = "OFF",
                        color = MaterialTheme.colorScheme.onError,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .background(
                                color = MaterialTheme.colorScheme.error,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                PriceText(
                    price = product.price,
                    originalPrice = product.originalPrice,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onAddToCart(product) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Agregar al carrito"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Agregar")
                }
            }
        }
    }
}

fun getDrawableResource(context: android.content.Context, imageName: String): Int {
    return try {
        context.resources.getIdentifier(imageName, "drawable", context.packageName)
    } catch (e: Exception) {
        0
    }
}