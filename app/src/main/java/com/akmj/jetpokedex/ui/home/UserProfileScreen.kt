package com.akmj.jetpokedex.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.akmj.jetpokedex.viewmodel.LoginRegisterViewModel

@Composable
fun UserProfileScreen(viewModel: LoginRegisterViewModel) {
    val context = viewModel.getApplicationContext()
    val sharedPref = context.getSharedPreferences("user_session", android.content.Context.MODE_PRIVATE)
    val email = sharedPref.getString("email", null)

    val user = remember(email) {
        if (email != null) viewModel.getUserByEmail(email) else null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Profile", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (user != null) {
            Text("Username: ${user.username}")
            Text("Email: ${user.email}")
        } else {
            Text("No user data found.")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            sharedPref.edit().clear().apply()
        }) {
            Text("Logout")
        }
    }
}
