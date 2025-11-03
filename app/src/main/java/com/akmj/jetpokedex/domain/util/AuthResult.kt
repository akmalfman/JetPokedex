package com.akmj.jetpokedex.domain.util

/**
 * Sebuah sealed class untuk merepresentasikan hasil dari sebuah operasi
 * (seperti registrasi atau login)
 */
sealed class AuthResult {
    data object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}