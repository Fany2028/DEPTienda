package com.example.com.dep.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.com.dep.ui.components.CategoryCard
import com.example.com.dep.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    onBackClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    viewModel: MainViewModel
) {
    val categories = listOf(
        "Camisetas" to "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=300",
        "Pantalones" to "https://images.unsplash.com/photo-1542272604-787c3835535d?w=300",
        "Sudaderas" to "https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=300",
        "Calzado" to "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=300",
        "Chaquetas" to "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=300",
        "Vestidos" to "https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=300"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Categorías",
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
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { (categoryName, imageUrl) ->
                CategoryCard(
                    categoryName = categoryName,
                    imageUrl = imageUrl,
                    onClick = { onCategoryClick(categoryName) }
                )
            }
        }
    }
}