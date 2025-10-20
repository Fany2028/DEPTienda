package com.example.deptienda.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.deptienda.data.models.Product
import com.example.com.dep.ui.navigation.Screens
import com.example.deptienda.viewmodel.MainViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreen.route
    ) {
        composable(Screens.HomeScreen.route) {
            HomeScreen(
                onProductClick = { product ->
                    selectedProduct = product
                    navController.navigate(Screens.ProductDetailScreen.route)
                },
                onCartClick = {
                    navController.navigate(Screens.CartScreen.route)
                },
                onCategoriesClick = {
                    navController.navigate(Screens.CategoriesScreen.route)
                },
                viewModel = mainViewModel
            )
        }

        composable(Screens.CartScreen.route) {
            CartScreen(
                onBackClick = { navController.popBackStack() },
                onContinueShopping = {
                    navController.navigate(Screens.HomeScreen.route) {
                        popUpTo(Screens.HomeScreen.route) { inclusive = true }
                    }
                },
                onCheckout = {
                    // TODO: Implementar pantalla de checkout
                },
                viewModel = mainViewModel
            )
        }

        composable(Screens.ProductDetailScreen.route) {
            selectedProduct?.let { product ->
                ProductDetailScreen(
                    product = product,
                    onBackClick = { navController.popBackStack() },
                    onAddToCart = { size, color ->
                        mainViewModel.addToCart(product, size, color)
                    },
                    viewModel = mainViewModel
                )
            } ?: run {
                // Manejar caso donde no hay producto seleccionado
                navController.popBackStack()
            }
        }

        composable(Screens.ProfileScreen.route) {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onEditProfile = {
                    // TODO: Navegar a editar perfil
                    // navController.navigate(Screens.EditProfileScreen.route)
                },
                onViewOrders = {
                    // TODO: Navegar a órdenes
                    // navController.navigate(Screens.OrdersScreen.route)
                },
                viewModel = mainViewModel
            )
        }

        composable(Screens.CategoriesScreen.route) {
            CategoriesScreen(
                onBackClick = { navController.popBackStack() },
                onCategoryClick = { category ->
                    // Filtrar productos por categoría y volver al home
                    mainViewModel.selectCategory(category)
                    navController.navigate(Screens.HomeScreen.route) {
                        popUpTo(Screens.HomeScreen.route) { inclusive = true }
                    }
                },
                viewModel = mainViewModel
            )
        }
    }
}