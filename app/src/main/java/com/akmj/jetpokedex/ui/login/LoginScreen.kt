package com.akmj.jetpokedex.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.akmj.jetpokedex.viewmodel.LoginRegisterViewModel

@Composable
fun LoginScreen(
    viewModel: LoginRegisterViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // ❗️ HAPUS: localError. Kita HANYA percaya pada ViewModel
    // val localError by remember { mutableStateOf<String?>(null) }

    // ❗️ Ambil state dari ViewModel
    val loginState by remember { viewModel.loginState }
    val errorMessage by remember { viewModel.errorMessage }

    // ❗️ INI BAGIAN PENTINGNYA
    // Kita 'observe' loginState. Jika berubah jadi true, kita navigasi.
    LaunchedEffect(loginState) {
        if (loginState) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                viewModel.clearError() // ❗️ Bersihkan error
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            // ❗️ Error hanya dari ViewModel
            isError = errorMessage != null
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                viewModel.clearError() // ❗️ Bersihkan error
            },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            // ❗️ Error hanya dari ViewModel
            isError = errorMessage != null
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // ❗️ PERUBAHAN LOGIKA
                // Tombol ini HANYA memberi tahu ViewModel
                // Kita tidak cek 'loginState' di sini lagi
                viewModel.login(email, password)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        TextButton(onClick = { onNavigateToRegister() }) {
            Text("Belum punya akun? Daftar")
        }

        // ❗️ Tampilkan pesan error HANYA dari ViewModel
        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}