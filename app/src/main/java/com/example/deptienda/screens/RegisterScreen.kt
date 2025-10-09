package com.example.deptienda.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.deptienda.viewmodel.DepViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: DepViewModel = viewModel()
) {
    val registerForm by viewModel.registerForm.collectAsState()
    val errors by viewModel.registerErrors.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta DEP") },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Únete a DEP Store",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                "Donde tu estilo encuentra su voz",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Campo Nombre
            OutlinedTextField(
                value = registerForm.name,
                onValueChange = { viewModel.updateRegisterForm(name = it) },
                label = { Text("Nombre completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Nombre") },
                modifier = Modifier.fillMaxWidth(),
                isError = errors.containsKey("name")
            )
            if (errors.containsKey("name")) {
                Text(
                    errors["name"] ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Campo Email
            OutlinedTextField(
                value = registerForm.email,
                onValueChange = { viewModel.updateRegisterForm(email = it) },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = errors.containsKey("email")
            )
            if (errors.containsKey("email")) {
                Text(
                    errors["email"] ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Campo Teléfono
            OutlinedTextField(
                value = registerForm.phone,
                onValueChange = { viewModel.updateRegisterForm(phone = it) },
                label = { Text("Teléfono") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                isError = errors.containsKey("phone")
            )
            if (errors.containsKey("phone")) {
                Text(
                    errors["phone"] ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Campo Contraseña
            OutlinedTextField(
                value = registerForm.password,
                onValueChange = { viewModel.updateRegisterForm(password = it) },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                isError = errors.containsKey("password")
            )
            if (errors.containsKey("password")) {
                Text(
                    errors["password"] ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Campo Confirmar Contraseña
            OutlinedTextField(
                value = registerForm.confirmPassword,
                onValueChange = { viewModel.updateRegisterForm(confirmPassword = it) },
                label = { Text("Confirmar Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                isError = errors.containsKey("confirmPassword")
            )
            if (errors.containsKey("confirmPassword")) {
                Text(
                    errors["confirmPassword"] ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Campo Dirección
            OutlinedTextField(
                value = registerForm.address,
                onValueChange = { viewModel.updateRegisterForm(address = it) },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                minLines = 3,
                isError = errors.containsKey("address")
            )
            if (errors.containsKey("address")) {
                Text(
                    errors["address"] ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.submitRegister() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = errors.isEmpty() && registerForm.name.isNotBlank()
            ) {
                Text("Crear Cuenta DEP", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}