package com.example.lostpals.data.dao

import androidx.room.*
import com.example.lostpals.data.dto.PostDisplayDto
import com.example.lostpals.data.entity.*

@Dao
interface PostDao {

    /*──────────── CRUD de bază ────────────*/
    @Insert suspend fun insert(post: Post): Long
    @Update suspend fun updatePost(post: Post)
    @Delete suspend fun deletePost(post: Post)

    @Query("SELECT * FROM posts WHERE id = :id LIMIT 1")
    suspend fun getPostById(id: Long): Post?

    @Query("SELECT * FROM posts WHERE ownerId = :ownerId")
    suspend fun getPostsForUser(ownerId: Long): List<Post>

    /*──────────── Homepage fără filtre ────────────*/
    @Query("""
        SELECT p.*, u.username AS ownerUsername, u.photoUri AS ownerPhotoUri
        FROM posts p
        INNER JOIN users u ON p.ownerId = u.id
        WHERE p.postType = :lostType
        ORDER BY p.timestamp DESC
    """)
    suspend fun getLostPostsWithOwnerInfo(
        lostType: PostType = PostType.LOST
    ): List<PostDisplayDto>

    /*──────────── Homepage cu filtre ────────────*/
    @Query("""
        SELECT p.*, u.username AS ownerUsername, u.photoUri AS ownerPhotoUri
        FROM posts p
        INNER JOIN users u ON p.ownerId = u.id
        WHERE p.postType = :lostType
          AND (:location   IS NULL OR p.location   = COALESCE(:location, 'UNKNOWN'))
          AND (:objectType IS NULL OR p.objectType = :objectType)
        ORDER BY p.timestamp DESC
    """)
    suspend fun getLostPostsByFilterWithOwnerInfo(
        location: Location? = null,
        objectType: ObjectType? = null,
        lostType: PostType = PostType.LOST
    ): List<PostDisplayDto>

    /*──────────── Filtrare simplă fără owner info ────────────*/
    @Query("""
        SELECT * FROM posts
        WHERE postType = :lostType
          AND (:location   IS NULL OR location   = COALESCE(:location , 'UNKNOWN'))
          AND (:objectType IS NULL OR objectType = :objectType)
        ORDER BY timestamp DESC
    """)
    suspend fun getLostPostsByFilter(
        location: Location? = null,
        objectType: ObjectType? = null,
        lostType: PostType = PostType.LOST
    ): List<Post>

    /*──────────── Listă toate LOST fără filtre ────────────*/
    @Query("SELECT * FROM posts WHERE postType = :lostType ORDER BY timestamp DESC")
    suspend fun getLostPosts(
        lostType: PostType = PostType.LOST
    ): List<Post>
}
