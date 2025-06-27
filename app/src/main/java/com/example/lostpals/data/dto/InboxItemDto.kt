package com.example.lostpals.data.dto

// reprezinta un rand din lista de inbox
// cu datele cele mai recente dintr-o conversatie
data class InboxItemDto(
    val conversationUserId: Long,
    val conversationUsername: String,
    val conversationUserPhoto: String?,
    val lastMessage: String?,
    val lastMessagePhoto: String?,
    val lastMessageTimestamp: Long,
    val postId: Long
)

