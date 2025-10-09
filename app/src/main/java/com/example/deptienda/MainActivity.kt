package com.example.deptienda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.deptienda.navigation.NavigationEvent
import com.example.deptienda.navigation.Screen
import com.example.deptienda.ui.screens.CartScreen
import com.example.deptienda.ui.screens.HomeScreen
import com.example.deptienda.ui.screens.LoginScreen
import com.example.deptienda.ui.screens.ProfileScreen
import com.example.deptienda.ui.screens.RegisterScreen
import com.example.deptienda.ui.theme.DEPTiendaTheme
import com.example.deptienda.viewmodel.DepViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DEPTiendaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: DepViewModel = viewModel()

                    // INICIALIZAR EL VIEWMODEL - AGREGAR ESTAS 2 LÍNEAS:
                    viewModel.initialize(this)
                    viewModel.checkCurrentUser()

                    val navController = rememberNavController()

                    // Escuchar eventos de navegación (SISTEMA DEL PROFESOR)
                    LaunchedEffect(Unit) {
                        viewModel.navigationEvents.collectLatest { event ->
                            when (event) {
                                is NavigationEvent.NavigateTo -> {
                                    navController.navigate(event.route.route) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                                is NavigationEvent.PopBackStack -> {
                                    navController.popBackStack()
                                }
                                is NavigationEvent.NavigateUp -> {
                                    navController.navigateUp()
                                }
                            }
                        }
                    }

                    // NAVEGACIÓN COMPLETA (SISTEMA DEL PROFESOR)
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(viewModel = viewModel)
                        }
                        composable(Screen.Cart.route) {
                            CartScreen(viewModel = viewModel)
                        }
                        composable(Screen.Profile.route) {
                            ProfileScreen(viewModel = viewModel)
                        }
                        composable(Screen.Login.route) {
                            LoginScreen(viewModel = viewModel)
                        }
                        composable(Screen.Register.route) {
                            RegisterScreen(viewModel = viewModel)
                        }
                        composable(Screen.Checkout.route) {
                            // Por ahora usamos ProfileScreen como placeholder
                            ProfileScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}