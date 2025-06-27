package com.example.lostpals.data.dto

import com.example.lostpals.data.entity.Location
import com.example.lostpals.data.entity.ObjectType

// dto folosit pe homepage pentru afisarea postarilor cu toate informatiile necesare
data class PostDisplayDto(
    val id: Long,
    val ownerId: Long,
    val ownerUsername: String,
    val ownerPhotoUri: String?,
    val title: String,
    val description: String,
    val location: Location,
    val objectType: ObjectType,
    val photoUri: String?,
    val reward: Double?,
    val timestamp: Long
)
