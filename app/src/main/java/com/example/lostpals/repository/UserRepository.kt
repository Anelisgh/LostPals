package com.example.lostpals.repository

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.lostpals.data.dao.UserDao
import com.example.lostpals.data.dto.UserDto
import com.example.lostpals.data.mapper.toDto
import com.example.lostpals.data.mapper.toEntity

class UserRepository(
    private val userDao: UserDao
) {

    // inregistrare utilizator nou
    // verifica unicitatea si valideaza parola
    suspend fun registerUser(userDto: UserDto, rawPassword: String): Long {
        // verifica daca emailul a fost folosit deja
        if (userDao.emailExists(userDto.email) > 0) {
            throw Exception("Email already registered")
        }
        // verifica daca username-ul exista deja
        if (userDao.usernameExists(userDto.username) > 0) {
            throw Exception("Username taken")
        }
        // verifica lungimea parolei
        if (rawPassword.length < 6) {
            throw Exception("Password must be at least 6 characters")
        }
        // hash-uire parola
        val passwordHash = BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray())

        // creare si inserare utilizator in bd
        val userEntity = userDto.toEntity(existingPassword = passwordHash)
        // inserare utilizator in bd si returnare id nou
        return userDao.insert(userEntity)
    }

    // autentifica utilizatorul pe baza username-ului si parolei
    suspend fun loginUser(username: String, password: String): UserDto {
        // cauta utilizatorul dupa username
        val user = userDao.getUserByUsername(username) ?: throw Exception("User not found")
        // verifica daca parola introdusa este corecta
        val result = BCrypt.verifyer().verify(password.toCharArray(), user.password)
        // parola nu este corecta => eroare
        if (!result.verified) {
            throw Exception("Invalid credentials")
        }
        return user.toDto()
    }

    // returnare utilizator dupa id-ul sau
    suspend fun getUserById(userId: Long): UserDto =
        userDao.getUserById(userId)?.toDto() ?: throw Exception("User not found")

    // actualizare profil utilizator
    suspend fun updateUserProfile(
        userDto: UserDto, currentPassword: String? = null, newPassword: String? = null
    ) {
        // obtine datele curente ale utilizatorului din bd
        val existing = userDao.getUserById(userDto.id) ?: throw Exception("User not found")

        // daca username-ul a fost modificat, se verifica sa nu fie deja luat
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

        // construire obeict actualizat
        val updated = existing.copy(
            username = userDto.username, email = userDto.email, photoUri = userDto.photoUri
        ).apply {
            // daca exista o parola noua, se verifica si se cripteaza
            newPassword?.let {
                if (it.length < 6) throw Exception("New password must be at least 6 characters")
                password = BCrypt.withDefaults().hashToString(12, it.toCharArray())
            }
        }
        // salvare modificari in db
        userDao.updateUser(updated)
    }

    // schimbare parola utilizator
    suspend fun changePassword(
        userId: Long, oldPassword: String, newPassword: String
    ) {
        // obtine utilizatorul
        val existing = userDao.getUserById(userId) ?: throw Exception("User not found")
        // verifica daca parola veche e corecta
        val result = BCrypt.verifyer().verify(oldPassword.toCharArray(), existing.password)
        if (!result.verified) {
            throw Exception("Current password is incorrect")
        }
        if (newPassword.length < 6) {
            throw Exception("New password must be at least 6 characters")
        }
        // criptare noua parola
        val hash = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray())

        // salvare parola noua in bd
        userDao.updateUser(existing.copy(password = hash))
    }

    // sterge contul unui utilizator
    suspend fun deleteUser(userId: Long) {
        // verifica daca utilizatorul exista
        val user = userDao.getUserById(userId) ?: throw Exception("User not found")
        // sterge contul utilizatorului
        userDao.deleteUser(user)
    }

    // schimbare adresa de email al unui utilizator
    suspend fun changeEmail(userId: Long, newEmail: String) {
        // verifica daca utilizatorul exista
        val existing = userDao.getUserById(userId)
            ?: throw Exception("User not found")
        // verifica daca noul email nu este deja folosit
        if (userDao.emailExistsExcludingUser(newEmail, userId) > 0) {
            throw Exception("Email already registered")
        }
        // actualizeaza baza de date
        userDao.updateUser(existing.copy(email = newEmail))
    }

}