package com.example.lostpals.data.dto

// inbox -> lista cu mesaje, potrivit pentru UI
data class InboxItemDto(
    val conversationUserId: Long,
    val conversationUsername: String,
    val conversationUserPhoto: String?,
    val lastMessage: String?,
    val lastMessagePhoto: String?,
    val lastMessageTimestamp: Long,
    val postId: Long
)

