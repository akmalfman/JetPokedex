package com.akmj.jetpokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize // ❗️ Import ini
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.akmj.jetpokedex.ui.navigation.AppNavHost
import com.akmj.jetpokedex.ui.theme.JetPokedexTheme
// ❗️ HAPUS: import com.akmj.jetpokedex.viewmodel.LoginRegisterViewModel
// ❗️ HAPUS: import com.akmj.jetpokedex.viewmodel.LoginRegisterViewModelFactory

// ❗️ IMPORT BARU: Wajib untuk Hilt
import dagger.hilt.android.AndroidEntryPoint

// ❗️ ANOTASI BARU: Memberi tahu Hilt ini adalah entry point
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPokedexTheme {
                // ❗️ SEMUA LOGIC LAMA DIHAPUS DARI SINI.
                // ViewModel dan SharedPreferences akan diurus di dalam NavHost.

                Surface(
                    modifier = Modifier.fillMaxSize(), // ❗️ Pastikan modifier ini ada
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    // ❗️ AppNavHost Anda SEKARANG PASTI ERROR
                    // ❗️ Ini adalah hal yang kita inginkan.
                    AppNavHost(
                        navController = navController
                        // Kita hapus 'loginViewModel' dan 'startDestination'
                        // karena kita akan menanganinya di langkah berikutnya.
                    )
                }
            }
        }
    }
}