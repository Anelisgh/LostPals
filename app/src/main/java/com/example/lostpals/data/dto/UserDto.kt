package com.example.lostpals.data.dto

// pentru actualizarea user-ului
data class UserDto(
    val id: Long = 0,
    val username: String,
    val email: String,
    val photoUri: String? = null
)

