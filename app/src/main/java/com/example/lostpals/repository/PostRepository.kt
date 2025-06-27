package com.example.lostpals.repository

import com.example.lostpals.data.dao.PostDao
import com.example.lostpals.data.dao.UserDao
import com.example.lostpals.data.dto.PostDisplayDto
import com.example.lostpals.data.dto.PostDto
import com.example.lostpals.data.dto.PostFilterDto
import com.example.lostpals.data.entity.PostType
import com.example.lostpals.data.mapper.toDto
import com.example.lostpals.data.mapper.toEntity

class PostRepository(
    private val postDao: PostDao, private val userDao: UserDao
) {
    // creeaza o postare noua si o insereaza in bd
    suspend fun createPost(postDto: PostDto): Long {
        println("DEBUG: PostDto location before insert = ${postDto.location}") // log pentru debugging
        return try {
            val entity = postDto.toEntity() // insereaza postarea
            println("DEBUG: Post entity location = ${entity.location}") // verifica locatia transformata
            postDao.insert(entity)
        } catch (e: Exception) {
            println("ERROR: Failed to insert post: ${e.message}") // Log detaliat
            // arunca o exceptie in caz de eroare
            throw RuntimeException("Failed to insert post. Location: ${postDto.location}", e)
        }
    }

    // actualizeaza o postare existenta in bd
    suspend fun updatePost(postDto: PostDto) {
        postDao.updatePost(postDto.toEntity())
    }

    // returneaza toate postarile realizate de un anumit utilizator
    suspend fun getPostsForUser(userId: Long): List<PostDto> =
        postDao.getPostsForUser(userId).map { it.toDto() }

    // retunreaza toate postarile de tip LOST, impreuna cu informatii despre utilizator
    suspend fun getLostPosts(): List<PostDisplayDto> =
        postDao.getLostPostsWithOwnerInfo(PostType.LOST)

    // returneaza postarile de tip LOST care corespund filtrului specificat
    suspend fun getLostPostsWithFilter(filterDto: PostFilterDto): List<PostDisplayDto> =
        postDao.getLostPostsByFilterWithOwnerInfo(
            location = filterDto.location, objectType = filterDto.objectType
        )

    // cautare postare dupa id
    suspend fun getPostById(postId: Long): PostDto? = postDao.getPostById(postId)?.toDto()

    // stergere postare dupa id, daca aceasta exista
    suspend fun deletePost(postId: Long) {
        val post = postDao.getPostById(postId)
            ?: throw IllegalArgumentException("There's no post with id $postId")
        postDao.deletePost(post)
    }
}