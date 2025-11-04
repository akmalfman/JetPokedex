package com.akmj.jetpokedex.ui.home

// ❗️ HAPUS: import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.akmj.jetpokedex.viewmodel.LoginRegisterViewModel

@Composable
fun UserProfileScreen(
    viewModel: LoginRegisterViewModel,
    onLogout: () -> Unit
) {
    val email by remember { viewModel.userEmail }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Logged in as: $email",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.logout()
                onLogout()
            }
        ) {
            Text("Logout")
        }
    }
}