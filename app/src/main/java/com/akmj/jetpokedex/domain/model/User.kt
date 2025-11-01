package com.akmj.jetpokedex.domain.model

data class User(
    val id: String,
    val username: String,
    val email: String,
    val password: String
)