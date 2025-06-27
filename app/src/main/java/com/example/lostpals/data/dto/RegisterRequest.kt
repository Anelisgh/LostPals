package com.example.lostpals.data.dto

// contine informatiile introduse de utilizator
// atunci cand se inregistreaza in aplicatie
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)
