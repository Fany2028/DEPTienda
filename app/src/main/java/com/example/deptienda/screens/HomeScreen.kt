package com.example.deptienda.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.deptienda.viewmodel.DepViewModel
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.collectAsState
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: DepViewModel = viewModel()) {
    val currentUser = viewModel.currentUser.collectAsState().value
    val message = viewModel.message.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (currentUser != null) {
                        Text("Hola, ${currentUser.name}!")
                    } else {
                        Text("DEP Store - Ropa Urbana")
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
            Text(
                text = "¡Bienvenido a DEP Store!",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Donde tu estilo encuentra su voz",
                style = MaterialTheme.typography.bodyMedium
            )

            // Mostrar mensaje de bienvenida/despedida
            if (!message.isNullOrEmpty()) {
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Mostrar estado de sesión
            if (currentUser != null) {
                Text(
                    text = "Sesión activa: ${currentUser.email}",
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Text(
                    text = "Inicia sesión para una experiencia personalizada",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // BOTÓN VER CARRITO
            Button(
                onClick = { viewModel.navigateTo(com.example.deptienda.navigation.Screen.Cart) },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                Text("Ver Carrito", modifier = Modifier.padding(start = 8.dp))
            }

            // BOTÓN MI PERFIL
            Button(
                onClick = { viewModel.navigateTo(com.example.deptienda.navigation.Screen.Profile) },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Icon(Icons.Default.Person, contentDescription = "Perfil")
                Text("Mi Perfil", modifier = Modifier.padding(start = 8.dp))
            }

            // BOTÓN INICIAR SESIÓN (solo si no hay usuario)
            if (currentUser == null) {
                Button(
                    onClick = { viewModel.navigateTo(com.example.deptienda.navigation.Screen.Login) },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Iniciar Sesión")
                }
            } else {
                // BOTÓN CERRAR SESIÓN (solo si hay usuario)
                Button(
                    onClick = { viewModel.logout() },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Cerrar Sesión")
                }
            }
        }
    }
}