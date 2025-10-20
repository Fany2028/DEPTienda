package com.example.com.dep.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.com.dep.ui.navigation.Screens

@Composable
fun BottomBar(
    navController: NavController,
    cartItemsCount: Int = 0
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val items = listOf(
        BottomBarItem(
            route = Screens.HomeScreen.route,
            icon = Icons.Default.Home,
            label = "Inicio"
        ),
        BottomBarItem(
            route = Screens.CartScreen.route,
            icon = Icons.Default.ShoppingCart,
            label = "Carrito",
            badgeCount = cartItemsCount
        ),
        BottomBarItem(
            route = Screens.ProfileScreen.route,
            icon = Icons.Default.Person,
            label = "Perfil"
        )
    )

    NavigationBar {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    // ✅ VERSIÓN SIN BADGE - Más simple
                    Box(
                        modifier = Modifier.wrapContentSize(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label
                        )

                        // Badge personalizado
                        if (item.badgeCount > 0 && item.route == Screens.CartScreen.route) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .align(Alignment.TopEnd)
                                    .offset(x = 8.dp, y = (-4).dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (item.badgeCount > 99) "99+"
                                    else item.badgeCount.toString(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = MaterialTheme.shapes.small
                                        )
                                        .wrapContentSize()
                                )
                            }
                        }
                    }
                },
                label = { Text(item.label) }
            )
        }
    }
}

data class BottomBarItem(
    val route: String,
    val icon: ImageVector,
    val label: String,
    val badgeCount: Int = 0
)