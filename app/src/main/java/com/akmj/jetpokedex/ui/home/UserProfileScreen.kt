package com.akmj.jetpokedex.ui.home

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.akmj.jetpokedex.viewmodel.LoginRegisterViewModel

@Composable
fun UserProfileScreen(
    viewModel: LoginRegisterViewModel,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    val email = sharedPref.getString("email", "Unknown")

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
