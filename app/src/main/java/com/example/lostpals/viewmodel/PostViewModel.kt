package com.example.lostpals.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostpals.data.dto.PostDisplayDto
import com.example.lostpals.data.dto.PostDto
import com.example.lostpals.data.dto.PostFilterDto
import com.example.lostpals.repository.PostRepository
import com.example.lostpals.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// clasa care pastreaza datele postarilor si decide ce se afiseaza in aplicatie
class PostViewModel(private val postRepository: PostRepository) : ViewModel() {

    // tine minte ce filtru a fost aplicat la postari (oras, tip obiect)
    private val _filter = MutableLiveData<PostFilterDto?>()
    val filter: LiveData<PostFilterDto?> get() = _filter

    // lista postarilor incarcate, cu statusul de incarcare sau eroare
    private val _posts = MutableLiveData<Resource<List<PostDisplayDto>>>()
    val posts: LiveData<Resource<List<PostDisplayDto>>> get() = _posts

    // rezultatul crearii unei postari noi (id sau eroare)
    private val _createPostResult = MutableLiveData<Resource<Long>?>()
    val createPostResult: LiveData<Resource<Long>?> get() = _createPostResult

    // la initializare se incarca automat toate postarile fara filtru
    init {
        loadPosts(null)
    }

    // seteaza filtrul si reincarca postarile in functie de noul filtru
    fun setFilter(filter: PostFilterDto?) {
        _filter.value = filter
        loadPosts(filter)
    }

    // incarca postarile, optional dupa un filtru; trateaza eventuale erori
    fun loadPosts(filter: PostFilterDto?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val posts = if (filter == null) {
                    postRepository.getLostPosts()
                } else {
                    postRepository.getLostPostsWithFilter(filter)
                }
                // daca merge, salveaza postarile in _posts
                _posts.postValue(Resource.Success(posts))
            } catch (e: Exception) {
                // daca apare o eroare, se salveaza eroare in _posts
                _posts.postValue(Resource.Error(e.message ?: "Failed to load posts", emptyList()))
            }
        }
    }

    // creeaza o postare noua si reincarca postarile dupa succes
    fun createPost(postDto: PostDto) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // trimite posatrea in postrepository
                val postId = postRepository.createPost(postDto)
                // daca merge, seteaza rezultatul
                _createPostResult.postValue(Resource.Success(postId))
                // reincarca lista de postari actualizata
                loadPosts(filter.value)
            } catch (e: Exception) {
                _createPostResult.postValue(Resource.Error(e.message ?: "Failed to create post", null))
            }
        }
    }

    // reseteaza rezultatul crearii unei postari
    fun clearCreatePostResult() {
        _createPostResult.value = null
    }
}
