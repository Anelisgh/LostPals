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
    suspend fun createPost(postDto: PostDto): Long = postDao.insert(postDto.toEntity())

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