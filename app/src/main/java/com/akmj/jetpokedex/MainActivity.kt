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

                Surface(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavHost(navController, loginViewModel)
                }
            }
        }
    }
}
