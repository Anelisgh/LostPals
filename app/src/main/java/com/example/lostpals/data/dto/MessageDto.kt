package com.example.lostpals.data.dto

// trimitere mesaj
data class MessageDto(
    val id: Long = 0,
    val postId: Long,
    val senderId: Long,
    val receiverId: Long,
    val text: String? = null,
    val photoUri: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
