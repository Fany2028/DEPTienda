package com.example.com.dep.ui.navigation

sealed class Screens(val route: String) {
    object HomeScreen : Screens("home_screen")
    object CartScreen : Screens("cart_screen")
    object ProfileScreen : Screens("profile_screen")
    object ProductDetailScreen : Screens("product_detail_screen")
    object CategoriesScreen : Screens("categories_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}