package com.example.lostpals.data.mapper

import com.example.lostpals.data.dto.PostDto
import com.example.lostpals.data.entity.Post

fun PostDto.toEntity() = Post(
    id = id,
    ownerId = ownerId,
    title = title,
    description = description,
    location = location,
    objectType = objectType,
    postType = postType,
    photoUri = photoUri,
    reward = reward,
    timestamp = timestamp
)

fun Post.toDto() = PostDto(
    id = id,
    ownerId = ownerId,
    title = title,
    description = description,
    location = location,
    objectType = objectType,
    postType = postType,
    photoUri = photoUri,
    reward = reward,
    timestamp = timestamp
)