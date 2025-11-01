package com.akmj.jetpokedex

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.akmj.jetpokedex.data.local.UserDatabase
import com.akmj.jetpokedex.domain.model.User
import com.akmj.jetpokedex.ui.home.PokemonListScreen
import com.akmj.jetpokedex.ui.login.LoginScreen
import com.akmj.jetpokedex.ui.navigation.AppNavHost
import com.akmj.jetpokedex.ui.register.RegisterScreen
import com.akmj.jetpokedex.ui.theme.JetPokedexTheme
import com.akmj.jetpokedex.ui.viewmodel.PokemonViewModel
import com.akmj.jetpokedex.viewmodel.LoginRegisterViewModel
import com.akmj.jetpokedex.viewmodel.LoginRegisterViewModelFactory
import java.util.UUID


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPokedexTheme {
                Surface(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavHost(navController)
                }
            }
        }
    }
}