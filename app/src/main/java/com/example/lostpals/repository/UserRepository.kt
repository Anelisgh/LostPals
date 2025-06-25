package com.example.lostpals.repository

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.lostpals.data.dao.UserDao
import com.example.lostpals.data.dto.UserDto
import com.example.lostpals.data.mapper.toDto
import com.example.lostpals.data.mapper.toEntity

class UserRepository(
    private val userDao: UserDao
) {

    suspend fun registerUser(userDto: UserDto, rawPassword: String): Long {
        if (userDao.emailExists(userDto.email) > 0) {
            throw Exception("Email already registered")
        }
        if (userDao.usernameExists(userDto.username) > 0) {
            throw Exception("Username taken")
        }
        if (rawPassword.length < 6) {
            throw Exception("Password must be at least 6 characters")
        }
        val passwordHash = BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray())

        val userEntity = userDto.toEntity(existingPassword = passwordHash)
        return userDao.insert(userEntity)
    }

    suspend fun loginUser(username: String, password: String): UserDto {
        val user = userDao.getUserByUsername(username) ?: throw Exception("User not found")
        val result = BCrypt.verifyer().verify(password.toCharArray(), user.password)
        if (!result.verified) {
            throw Exception("Invalid credentials")
        }
        return user.toDto()
    }

    suspend fun getUserById(userId: Long): UserDto =
        userDao.getUserById(userId)?.toDto() ?: throw Exception("User not found")

    suspend fun updateUserProfile(
        userDto: UserDto, currentPassword: String? = null, newPassword: String? = null
    ) {
        val existing = userDao.getUserById(userDto.id) ?: throw Exception("User not found")

        if (userDto.username != existing.username && userDao.usernameExistsExcludingUser(
                userDto.username,
                userDto.id
            ) > 0
        ) {
            throw Exception("Username already taken")
        }
        if (userDto.email != existing.email && userDao.emailExistsExcludingUser(
                userDto.email,
                userDto.id
            ) > 0
        ) {
            throw Exception("Email already registered")
        }

        val updated = existing.copy(
            username = userDto.username, email = userDto.email, photoUri = userDto.photoUri
        ).apply {
            newPassword?.let {
                if (it.length < 6) throw Exception("New password must be at least 6 characters")
                password = BCrypt.withDefaults().hashToString(12, it.toCharArray())
            }
        }
        userDao.updateUser(updated)
    }

    suspend fun changePassword(
        userId: Long, oldPassword: String, newPassword: String
    ) {
        val existing = userDao.getUserById(userId) ?: throw Exception("User not found")
        val result = BCrypt.verifyer().verify(oldPassword.toCharArray(), existing.password)
        if (!result.verified) {
            throw Exception("Current password is incorrect")
        }
        if (newPassword.length < 6) {
            throw Exception("New password must be at least 6 characters")
        }
        val hash = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray())
        userDao.updateUser(existing.copy(password = hash))
    }

    suspend fun deleteUser(userId: Long) {
        val user = userDao.getUserById(userId) ?: throw Exception("User not found")
        userDao.deleteUser(user)
    }

    suspend fun changeEmail(userId: Long, newEmail: String) {
        val existing = userDao.getUserById(userId)
            ?: throw Exception("User not found")
        if (userDao.emailExistsExcludingUser(newEmail, userId) > 0) {
            throw Exception("Email already registered")
        }
        userDao.updateUser(existing.copy(email = newEmail))
    }

}