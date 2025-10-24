package com.example.deptienda.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SizeSelector(
    sizes: List<String>,
    selectedSize: String,
    onSizeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Talla",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sizes.size) { index ->
                val size = sizes[index]
                val isSelected = size == selectedSize

                FilterChip(
                    selected = isSelected,
                    onClick = { onSizeSelected(size) },
                    label = { Text(size) },
                    modifier = Modifier.height(40.dp)
                )
            }
        }
    }
}