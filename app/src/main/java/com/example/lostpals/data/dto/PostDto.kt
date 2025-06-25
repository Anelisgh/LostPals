package com.example.lostpals.data.dto

import com.example.lostpals.data.entity.Location
import com.example.lostpals.data.entity.ObjectType
import com.example.lostpals.data.entity.PostType

// creare, editare post
data class PostDto(
    val id: Long = 0,
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

