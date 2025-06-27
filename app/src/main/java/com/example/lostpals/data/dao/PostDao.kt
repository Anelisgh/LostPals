package com.example.lostpals.data.dao

import androidx.room.*
import com.example.lostpals.data.dto.PostDisplayDto
import com.example.lostpals.data.entity.*

@Dao
interface PostDao {

    //insert, adauga o postare noua in baza de date
    @Insert suspend fun insert(post: Post): Long

    //actualizeaza o postare existenta
    @Update suspend fun updatePost(post: Post)

    //sterge o postare existenta
    @Delete suspend fun deletePost(post: Post)

    // retunreaza o singura postare cu un anumit id
    @Query("SELECT * FROM posts WHERE id = :id LIMIT 1")
    suspend fun getPostById(id: Long): Post?

    // returneaza toate postarile facute de un anumit utilizator
    @Query("SELECT * FROM posts WHERE ownerId = :ownerId")
    suspend fun getPostsForUser(ownerId: Long): List<Post>

    // returneaza cele mai recente postari de tip LOST/FOUND,
    // impreuna cu username-ul si poza utilizatorului, prin JOIN cu tabela users.
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

    // returnează postarile filtrate dupa locație și tip obiect, impreună cu username-ul si poza utilizatorului.
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

    // returneaza postari filtrate optional dupa locatie si tip obiect,
    // fara informatii despre utilizator (doar campurile din tabela posts).
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

    // returneaza toate postatile de tip LOST/FOUND fara niciun filtru,
    // ordonate descrescator dupa data.
    @Query("SELECT * FROM posts WHERE postType = :lostType ORDER BY timestamp DESC")
    suspend fun getLostPosts(
        lostType: PostType = PostType.LOST
    ): List<Post>
}
