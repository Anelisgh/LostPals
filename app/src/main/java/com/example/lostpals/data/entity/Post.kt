package com.example.lostpals.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "posts", foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["ownerId"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index("ownerId"), Index("postType"), Index("location"), Index("timestamp")]
)
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ownerId: Long,
    val title: String,
    val description: String,
    val location: Location,
    val objectType: ObjectType = ObjectType.OTHER,
    val postType: PostType = PostType.LOST,
    val photoUri: String? = null,
    val reward: Double? = null,
    val timestamp: Long = System.currentTimeMillis()
)