package com.akmj.jetpokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.akmj.jetpokedex.ui.navigation.AppNavHost
import com.akmj.jetpokedex.ui.theme.JetPokedexTheme
import com.akmj.jetpokedex.viewmodel.LoginRegisterViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPokedexTheme {
                val loginViewModel: LoginRegisterViewModel = viewModel(
                    factory = LoginRegisterViewModelFactory(this)
                )
                val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
                val loggedInEmail = sharedPref.getString("email", null)

                val startDestination = if (loggedInEmail != null) {
                    "home" // langsung ke home
                } else {
                    "login" // ke login
                }

                Surface(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavHost(
                        navController = navController,
                        loginViewModel = loginViewModel,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}
