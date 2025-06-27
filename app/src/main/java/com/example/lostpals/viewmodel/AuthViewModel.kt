package com.example.lostpals.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lostpals.data.dto.LoginRequest
import com.example.lostpals.data.dto.UserDto
import com.example.lostpals.data.database.AppDatabase
import com.example.lostpals.repository.UserRepository
import com.example.lostpals.util.Resource
import com.example.lostpals.util.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// viewmodel pentru autentificare si gestionarea contului de utilizator
class AuthViewModel(application: Application) : AndroidViewModel(application) {
    // repository pentru operatii legate de utilizator
    private val repository: UserRepository

    // statusul pentru inregistrare (success sau error)
    val registrationStatus = MutableLiveData<Resource<Long>>()
    // statusul pentru login (success sau error)
    val loginStatus = MutableLiveData<Resource<UserDto>>()
    // statusul pentru schimbarea parolei
    val passwordChangeStatus = MutableLiveData<Resource<Unit>>()
    // statusul pentru schimbarea emailului
    val emailChangeStatus = MutableLiveData<Resource<Unit>>()
    // statusul pentru stergerea contului
    val deleteAccountStatus = MutableLiveData<Resource<Unit>>()

    // gestioneaza datele de sesiune pentru utilizatorul curent
    private val sessionManager = SessionManager(application)

    // initializare repository cu userDao din baza de date
    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    // inregistreaza un utilizator nou in baza de date
    fun registerUser(username: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // creeaza dto si trimite la repository
                val userDto = UserDto(username = username, email = email)
                val userId = repository.registerUser(userDto, password)
                registrationStatus.postValue(Resource.Success(userId))
            } catch (e: Exception) {
                registrationStatus.postValue(
                    Resource.Error(e.message ?: "Registration failed", null)
                )
            }
        }
    }

    // autentifica un utilizator folosind username si parola
    fun loginUser(request: LoginRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userDto = repository.loginUser(request.username, request.password)
                // salvare locala
                sessionManager.saveUserId(userDto.id)
                sessionManager.setLoggedIn(true)
                loginStatus.postValue(Resource.Success(userDto))
            } catch (e: Exception) {
                loginStatus.postValue(Resource.Error(e.message ?: "Login failed", null))
            }
        }
    }

    // schimba parola utilizatorului curent
    fun changePassword(current: String, newPwd: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.changePassword(
                sessionManager.getUserId(),
                current,
                newPwd
            )
            passwordChangeStatus.postValue(Resource.Success(Unit))
        } catch (e: Exception) {
            passwordChangeStatus.postValue(Resource.Error(e.message ?: "Password change failed", null))
        }
    }

    // schimba emailul utilizatorului curent
    fun changeEmail(newEmail: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.changeEmail(
                sessionManager.getUserId(),
                newEmail
            )
            emailChangeStatus.postValue(Resource.Success(Unit))
        } catch (e: Exception) {
            emailChangeStatus.postValue(Resource.Error(e.message ?: "Email change failed", null))
        }
    }

    // sterge contul utilizatorului curent si face logout
    fun deleteAccount() = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.deleteUser(sessionManager.getUserId())
            sessionManager.logout() // sterge valorile salvate local
            deleteAccountStatus.postValue(Resource.Success(Unit))
        } catch (e: Exception) {
            deleteAccountStatus.postValue(Resource.Error(e.message ?: "Delete failed", null))
        }
    }

    // sterge datele de sesiune (logout)
    fun logout() {
        sessionManager.logout()
    }
}
