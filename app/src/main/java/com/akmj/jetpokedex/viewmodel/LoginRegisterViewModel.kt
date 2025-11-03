package com.akmj.jetpokedex.viewmodel

// ❗️ HAPUS: import android.content.Context
// ❗️ HAPUS: import com.akmj.jetpokedex.data.local.UserDatabase
// ❗️ HAPUS: import com.akmj.jetpokedex.data.local.UserSession
// ❗️ HAPUS: import com.akmj.jetpokedex.domain.model.User

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// ❗️ IMPORT BARU: Use Cases
import com.akmj.jetpokedex.domain.usecase.CheckLoginStatusUseCase
import com.akmj.jetpokedex.domain.usecase.GetLoggedInEmailUseCase
import com.akmj.jetpokedex.domain.usecase.LoginUseCase
import com.akmj.jetpokedex.domain.usecase.LogoutUseCase
import com.akmj.jetpokedex.domain.usecase.RegisterUseCase
// ❗️ IMPORT BARU: AuthResult Wrapper
import com.akmj.jetpokedex.domain.util.AuthResult
// ❗️ IMPORT BARU: Hilt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiEvent {
    data object RegisterSuccess : UiEvent()
    // Anda bisa tambahkan event lain di sini jika perlu, misal:
    // data class ShowSnackbar(val message: String) : UiEvent()
}

// ❗️ ANOTASI BARU
@HiltViewModel
// ❗️ CONSTRUCTOR BARU: Hilt akan 'menyuntikkan' semua Use Cases
class LoginRegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val checkLoginStatusUseCase: CheckLoginStatusUseCase,
    private val getLoggedInEmailUseCase: GetLoggedInEmailUseCase
) : ViewModel() {

    // ❗️ HAPUS: val userDb = UserDatabase(context)
    // ❗️ HAPUS: val session = UserSession(context)

    // ❗️ Inisialisasi state dari Use Case. Bersih!
    var loginState = mutableStateOf(checkLoginStatusUseCase())
        private set

    // ❗️ STATE BARU: Ambil email menggunakan Use Case
    var userEmail = mutableStateOf(getLoggedInEmailUseCase() ?: "Unknown")
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set // ❗️ Ubah ini menjadi 'private set'

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            when (val result = registerUseCase(username, email, password)) {
                is AuthResult.Success -> {
                    errorMessage.value = null
                    // ❗️ KIRIM EVENT SUKSES KE UI
                    _eventFlow.emit(UiEvent.RegisterSuccess)
                }
                is AuthResult.Error -> {
                    errorMessage.value = result.message
                }
            }
        }
    }

    // ❗️ Fungsi ini tidak perlu mengembalikan User lagi
    fun login(email: String, password: String) {
        viewModelScope.launch {
            // ❗️ Panggil Use Case, yang juga menangani 'saveSession'
            when (val result = loginUseCase(email, password)) {
                is AuthResult.Success -> {
                    loginState.value = true
                    errorMessage.value = null
                }
                is AuthResult.Error -> {
                    errorMessage.value = result.message
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            loginState.value = false
            userEmail.value = "Unknown" // Reset email state
        }
    }
    /**
     * ❗️ TAMBAHKAN FUNGSI INI
     * Untuk dipanggil dari UI saat user mulai mengetik
     */
    fun clearError() {
        errorMessage.value = null
    }
}