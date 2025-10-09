package com.example.deptienda.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.deptienda.viewmodel.DepViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: DepViewModel = viewModel()) {
    val currentUser = viewModel.currentUser.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (currentUser != null) {
                        Text("Mi Perfil - ${currentUser.name}")
                    } else {
                        Text("Mi Perfil DEP")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (currentUser != null) {
                // USUARIO LOGUEADO - Mostrar información + botones
                Text(
                    text = "Información de tu cuenta:",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(text = "Nombre: ${currentUser.name}")
                Text(text = "Email: ${currentUser.email}")
                Text(text = "Teléfono: ${currentUser.phone}")
                Text(text = "Dirección: ${currentUser.address}")

                Spacer(modifier = Modifier.height(24.dp))

                // BOTÓN VOLVER AL INICIO
                Button(
                    onClick = { viewModel.navigateTo(com.example.deptienda.navigation.Screen.Home) },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Icon(Icons.Default.Home, contentDescription = "Inicio")
                    Text("Volver al Inicio", modifier = Modifier.padding(start = 8.dp))
                }

                // BOTÓN IR AL CARRITO
                Button(
                    onClick = { viewModel.navigateTo(com.example.deptienda.navigation.Screen.Cart) },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                    Text("Ir al Carrito", modifier = Modifier.padding(start = 8.dp))
                }

                // BOTÓN CERRAR SESIÓN
                Button(
                    onClick = { viewModel.logout() },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Cerrar Sesión")
                }

            } else {
                // USUARIO NO LOGUEADO - Mostrar opciones básicas
                Text(
                    text = "Mi Perfil - DEP Store",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Inicia sesión para ver tu información",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.navigateTo(com.example.deptienda.navigation.Screen.Cart) },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                    Text("Ver Carrito", modifier = Modifier.padding(start = 8.dp))
                }

                Button(
                    onClick = { viewModel.navigateTo(com.example.deptienda.navigation.Screen.Login) },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Iniciar Sesión")
                }
            }
        }
    }
}