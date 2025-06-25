package com.example.lostpals.data.mapper

import com.example.lostpals.data.dto.UserDto
import com.example.lostpals.data.entity.User

fun User.toDto() = UserDto(
    id = id, username = username, email = email, photoUri = photoUri
)

fun UserDto.toEntity(existingPassword: String) = User(
    id = id, username = username, email = email, password = existingPassword, photoUri = photoUri
)