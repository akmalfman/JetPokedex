package com.akmj.jetpokedex.ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.akmj.jetpokedex.viewmodel.LoginRegisterViewModel
import com.akmj.jetpokedex.viewmodel.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    viewModel: LoginRegisterViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // ❗️ Ambil error dari ViewModel (ini sudah benar)
    val errorMessage by viewModel.errorMessage

    // ❗️ PERUBAHAN 1: Buat 'observer' untuk event dari ViewModel
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.RegisterSuccess -> {
                    onRegisterSuccess()
                }
                // Anda bisa tambahkan 'when' lain jika ada event lain
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Register", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            // ❗️ PERUBAHAN 2: Bersihkan error saat mengetik
            onValueChange = {
                username = it
                viewModel.clearError()
            },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null // Error dari ViewModel
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                viewModel.clearError()
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                viewModel.clearError()
            },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = errorMessage != null
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // ❗️ Tombol hanya memanggil ViewModel (ini sudah benar)
                viewModel.register(username, email, password)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        TextButton(onClick = { onNavigateToLogin() }) {
            Text("Sudah punya akun? Login")
        }

        // ❗️ Tampilkan error (ini sudah benar)
        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }

    // ❗️ PERUBAHAN 3: Hapus LaunchedEffect(errorMessage) yang buggy
}