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

// gestioneaza datele pentru postari
class PostViewModel(private val postRepository: PostRepository) : ViewModel() {

    private val _filter = MutableLiveData<PostFilterDto?>()
    val filter: LiveData<PostFilterDto?> get() = _filter

    private val _posts = MutableLiveData<Resource<List<PostDisplayDto>>>()
    val posts: LiveData<Resource<List<PostDisplayDto>>> get() = _posts

    private val _createPostResult = MutableLiveData<Resource<Long>>()
    val createPostResult: LiveData<Resource<Long>> get() = _createPostResult
// incarca postarile automat cand se creeaza viewModel
    init {
        loadPosts(null)
    }

    fun setFilter(filter: PostFilterDto?) {
        _filter.value = filter
        loadPosts(filter) // reincarca automat cand se schimba filtrul
    }

    // incarca postarile cu gestionarea erorilor consistenta
    fun loadPosts(filter: PostFilterDto?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val posts = if (filter == null) {
                    postRepository.getLostPosts()
                } else {
                    postRepository.getLostPostsWithFilter(filter)
                }
                _posts.postValue(Resource.Success(posts))
            } catch (e: Exception) {
                _posts.postValue(Resource.Error(e.message ?: "Failed to load posts", emptyList()))
            }
        }
    }

    fun createPost(postDto: PostDto) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val postId = postRepository.createPost(postDto)
                _createPostResult.postValue(Resource.Success(postId))
                loadPosts(filter.value) // incarca postarile din nou
            } catch (e: Exception) {
                _createPostResult.postValue(Resource.Error(e.message ?: "Failed to create post", null))
            }
        }
    }
    fun clearCreatePostResult() {
        _createPostResult.value = null
    }
}