package com.example.lostpals.repository

import com.example.lostpals.data.dao.PostDao
import com.example.lostpals.data.dao.UserDao
import com.example.lostpals.data.dto.PostDisplayDto
import com.example.lostpals.data.dto.PostDto
import com.example.lostpals.data.dto.PostFilterDto
import com.example.lostpals.data.entity.PostType
import com.example.lostpals.data.mapper.toDto
import com.example.lostpals.data.mapper.toEntity
import com.example.lostpals.data.entity.Location

class PostRepository(
    private val postDao: PostDao, private val userDao: UserDao
) {
    suspend fun createPost(postDto: PostDto): Long {
        println("DEBUG: PostDto location before insert = ${postDto.location}") // Loghează valoarea
        return try {
            val entity = postDto.toEntity()
            println("DEBUG: Post entity location = ${entity.location}") // Verifică înainte de insert
            postDao.insert(entity)
        } catch (e: Exception) {
            println("ERROR: Failed to insert post: ${e.message}") // Log detaliat
            throw RuntimeException("Failed to insert post. Location: ${postDto.location}", e)
        }
    }

    suspend fun updatePost(postDto: PostDto) {
        postDao.updatePost(postDto.toEntity())
    }

    suspend fun getPostsForUser(userId: Long): List<PostDto> =
        postDao.getPostsForUser(userId).map { it.toDto() }

    suspend fun getLostPosts(): List<PostDisplayDto> =
        postDao.getLostPostsWithOwnerInfo(PostType.LOST)

    suspend fun getLostPostsWithFilter(filterDto: PostFilterDto): List<PostDisplayDto> =
        postDao.getLostPostsByFilterWithOwnerInfo(
            location = filterDto.location, objectType = filterDto.objectType
        )

    suspend fun getPostById(postId: Long): PostDto? = postDao.getPostById(postId)?.toDto()

    suspend fun deletePost(postId: Long) {
        val post = postDao.getPostById(postId)
            ?: throw IllegalArgumentException("There's no post with id $postId")
        postDao.deletePost(post)
    }
}