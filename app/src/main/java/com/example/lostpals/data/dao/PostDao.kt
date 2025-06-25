package com.example.lostpals.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lostpals.data.dto.PostDisplayDto
import com.example.lostpals.data.entity.Location
import com.example.lostpals.data.entity.ObjectType
import com.example.lostpals.data.entity.Post
import com.example.lostpals.data.entity.PostType

@Dao
interface PostDao {
    @Insert
    suspend fun insert(post: Post): Long

    @Query("SELECT * FROM posts WHERE id = :id LIMIT 1")
    suspend fun getPostById(id: Long): Post?

    // pentru profil -> edit posts
    @Query("SELECT * FROM posts WHERE ownerId = :ownerId")
    suspend fun getPostsForUser(ownerId: Long): List<Post>

    // pentru homepage
    @Query(
        """
            SELECT p.*, u.username as ownerUsername, u.photoUri as ownerPhotoUri
            FROM posts p
            INNER JOIN users u ON p.ownerId = u.id
            WHERE p.postType = :lostType 
            ORDER BY p.timestamp DESC
        """
    )
    suspend fun getLostPostsWithOwnerInfo(lostType: PostType = PostType.LOST): List<PostDisplayDto>

    @Query("SELECT * FROM posts WHERE postType = :lostType ORDER BY timestamp DESC")
    suspend fun getLostPosts(lostType: PostType = PostType.LOST): List<Post>

    // pentru filtre:
    @Query("SELECT * FROM posts WHERE postType = :lostType AND (:location IS NULL OR location = :location) AND (:objectType IS NULL OR objectType = :objectType) ORDER BY timestamp DESC")
    suspend fun getLostPostsByFilter(
        location: Location? = null,
        objectType: ObjectType? = null,
        lostType: PostType = PostType.LOST
    ): List<Post>

    @Query(
        """
        SELECT p.*, u.username as ownerUsername, u.photoUri as ownerPhotoUri
        FROM posts p
        INNER JOIN users u ON p.ownerId = u.id
        WHERE p.postType = :lostType 
        AND (:location IS NULL OR p.location = :location) 
        AND (:objectType IS NULL OR p.objectType = :objectType) 
        ORDER BY p.timestamp DESC
    """
    )
    suspend fun getLostPostsByFilterWithOwnerInfo(
        location: Location? = null,
        objectType: ObjectType? = null,
        lostType: PostType = PostType.LOST
    ): List<PostDisplayDto>

    @Update
    suspend fun updatePost(post: Post)

    @Delete
    suspend fun deletePost(post: Post)
}