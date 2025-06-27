package com.example.lostpals.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.AndroidViewModel
import com.example.lostpals.data.dto.PostDemoDto
import com.example.lostpals.data.dto.UserDemoDto
import com.example.lostpals.network.RetrofitClient
import com.example.lostpals.data.database.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HttpDemoViewModel(application: Application) : AndroidViewModel(application) {

    private val _items = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val items: StateFlow<List<Pair<String, String>>> = _items

    // lista de postari din api
    private val _posts = MutableStateFlow<List<PostDemoDto>>(emptyList())
    val posts: StateFlow<List<PostDemoDto>> = _posts

    // lista de utilizatori din api
    private val _users = MutableStateFlow<List<UserDemoDto>>(emptyList())
    val users: StateFlow<List<UserDemoDto>> = _users

    // acces la baza locala
    private val userDao = AppDatabase.getDatabase(application).userDao()

    // cerere GET catre api extern
    fun loadData() = viewModelScope.launch {
        try {
            // obtinem lista de useri de pe server prin Retrifit
            val users = RetrofitClient.api.getUsers()
            // salvam lista
            _users.value = users
            // transformam lista in perechi pentru interfata
            _items.value = users.map { u -> "${u.id}" to u.username }
        } catch (e: Exception) {
            // eroare de retea
            _items.value = listOf("Network error" to (e.localizedMessage ?: "Unknown"))
        }
    }

    // GET local
    fun getAllLocalUsers() = viewModelScope.launch {
        try {
            // extrage userii din bd
            val localUsers = userDao.getAll()
            _items.value = localUsers.map { u -> "${u.id}" to u.username }
        } catch (e: Exception) {
            _items.value = listOf("Local DB error" to (e.localizedMessage ?: "Unknown"))
        }
    }

    // DELETE local
    fun deleteUserById(id: Long) = viewModelScope.launch {
        try {
            // incearca sa stearga un utilizator
            val rowsDeleted = userDao.deleteById(id)

            if (rowsDeleted > 0) {
                // mesaj de succes daca s-a sters cel putin un rand
                _items.value = listOf("Success" to "The user with id $id has been deleted.")
            } else {
                // mesaj daca nu s-a gasit user-ul
                _items.value = listOf("Error" to "User with id $id not found")
            }
        } catch (e: Exception) {
            _items.value = listOf("Delete error" to (e.localizedMessage ?: "Unknown error"))
        }
    }

}
