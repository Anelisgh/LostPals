package com.example.lostpals.data.dto

// inbox -> lista cu mesaje rezultata din query-ul SQL
data class InboxConversationData(
    val id: Long,
    val postId: Long,
    val senderId: Long,
    val receiverId: Long,
    val text: String?,
    val photoUri: String?,
    val timestamp: Long,
    val username: String,
    val userPhotoUri: String?
)
