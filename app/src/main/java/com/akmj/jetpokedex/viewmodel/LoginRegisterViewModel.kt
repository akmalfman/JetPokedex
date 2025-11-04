package com.akmj.jetpokedex.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akmj.jetpokedex.domain.usecase.CheckLoginStatusUseCase
import com.akmj.jetpokedex.domain.usecase.GetUserDetailsUseCase
import com.akmj.jetpokedex.domain.usecase.LoginUseCase
import com.akmj.jetpokedex.domain.usecase.LogoutUseCase
import com.akmj.jetpokedex.domain.usecase.RegisterUseCase
import com.akmj.jetpokedex.domain.util.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.akmj.jetpokedex.domain.model.User


sealed class UiEvent {
    data object RegisterSuccess : UiEvent()
}

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val checkLoginStatusUseCase: CheckLoginStatusUseCase,
    private val getUserDetailsUseCase: GetUserDetailsUseCase
) : ViewModel() {

    var loginState = mutableStateOf(checkLoginStatusUseCase())
        private set

    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    var errorMessage = mutableStateOf<String?>(null)
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        if (loginState.value) {
            loadUserDetails()
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            when (val result = registerUseCase(username, email, password)) {
                is AuthResult.Success -> {
                    errorMessage.value = null
                    _eventFlow.emit(UiEvent.RegisterSuccess)
                }
                is AuthResult.Error -> {
                    errorMessage.value = result.message
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            when (val result = loginUseCase(email, password)) {
                is AuthResult.Success -> {
                    loginState.value = true
                    errorMessage.value = null
                    loadUserDetails()
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
            _user.value = null
        }
    }

    private fun loadUserDetails() {
        viewModelScope.launch {
            _user.value = getUserDetailsUseCase()
        }
    }

    fun clearError() {
        errorMessage.value = null
    }
}