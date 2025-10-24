package com.example.deptienda.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorSelector(
    colors: List<String>,
    selectedColor: String,
    onColorSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Color",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(colors.size) { index ->
                val colorName = colors[index]
                val isSelected = colorName == selectedColor

                val actualColor = when (colorName.lowercase()) {
                    "negro" -> Color.Black
                    "blanco" -> Color.White
                    "azul" -> Color.Blue
                    "rojo" -> Color.Red
                    "verde" -> Color.Green
                    "amarillo" -> Color.Yellow
                    "gris" -> Color.Gray
                    else -> Color.Gray
                }

                Card(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(2.dp),
                    shape = MaterialTheme.shapes.small,
                    border = if (isSelected) CardDefaults.outlinedCardBorder() else null,
                    elevation = if (isSelected) CardDefaults.cardElevation(4.dp) else CardDefaults.cardElevation(1.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(actualColor),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Text(
                                text = "✓",
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}