package com.akmj.jetpokedex

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.akmj.jetpokedex.data.local.UserDatabase
import com.akmj.jetpokedex.domain.model.User
import com.akmj.jetpokedex.ui.login.LoginScreen
import com.akmj.jetpokedex.ui.register.RegisterScreen
import com.akmj.jetpokedex.ui.theme.JetPokedexTheme
import com.akmj.jetpokedex.viewmodel.LoginRegisterViewModel
import com.akmj.jetpokedex.viewmodel.LoginRegisterViewModelFactory
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val viewModel: LoginRegisterViewModel = viewModel(factory = LoginRegisterViewModelFactory(this))

            NavHost(navController = navController, startDestination = "login") {
                composable("login") {
                    LoginScreen(
                        viewModel = viewModel,
                        onLoginSuccess = { /* nanti navigasi ke home */ },
                        onNavigateToRegister = { navController.navigate("register") }
                    )
                }
                composable("register") {
                    RegisterScreen(
                        viewModel = viewModel,
                        onRegisterSuccess = { navController.popBackStack() },
                        onNavigateToLogin = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetPokedexTheme {
        Greeting("Android")
    }
}