package com.example.deptienda.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun PriceText(
    price: Double,
    originalPrice: Double? = null,
    modifier: Modifier = Modifier
) {
    if (originalPrice != null && originalPrice > price) {
        Column(modifier = modifier) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )) {
                        append("$$originalPrice")
                    }
                },
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "$$price",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    } else {
        Text(
            text = "$$price",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier
        )
    }
}