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
import com.example.deptienda.viewmodel.MainViewModel
import com.example.deptienda.data.models.Product

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
                onContinueShopping = { navController.popBackStack() },
                onCheckout = {
                    navController.navigate(Screens.CheckoutScreen.route)
                },
                viewModel = mainViewModel
            )
        }

        composable(Screens.CheckoutScreen.route) {
            CheckoutScreen(
                onBackClick = { navController.popBackStack() },
                onPaymentSuccess = {
                    mainViewModel.clearCart()
                    navController.navigate(Screens.HomeScreen.route) {
                        popUpTo(Screens.HomeScreen.route) { inclusive = true }
                    }
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
                navController.popBackStack()
            }
        }

        composable(Screens.ProfileScreen.route) {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onEditProfile = {
                    // TODO: Navegar a editar perfil
                },
                onViewOrders = {
                    // TODO: Navegar a órdenes
                },
                viewModel = mainViewModel
            )
        }

        composable(Screens.CategoriesScreen.route) {
            CategoriesScreen(
                onBackClick = { navController.popBackStack() },
                onCategoryClick = { category ->
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