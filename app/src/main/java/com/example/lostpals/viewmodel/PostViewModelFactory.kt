package com.example.lostpals.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lostpals.repository.PostRepository

// clasa folosita pentru a crea instante de PostViewModel cu repository-ul necesar
class PostViewModelFactory(private val postRepository: PostRepository) : ViewModelProvider.Factory {
    // creeaza si returneaza ViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // verifica daca modelClass este de tip PostViewModel
        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostViewModel(postRepository) as T
        }
        // aruncare eroare
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
