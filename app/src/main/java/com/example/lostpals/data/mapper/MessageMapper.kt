package com.example.lostpals.data.mapper

import com.example.lostpals.data.dto.InboxConversationData
import com.example.lostpals.data.dto.InboxItemDto
import com.example.lostpals.data.dto.MessageDto
import com.example.lostpals.data.entity.Message

// functii de mapare intre obiecte DTO si entitati Room

// MessageDto => Message
fun MessageDto.toEntity() = Message(
    id = id,
    postId = postId,
    senderId = senderId,
    receiverId = receiverId,
    text = text,
    photoUri = photoUri,
    timestamp = timestamp
)

fun Message.toDto() = MessageDto(
    id = id,
    postId = postId,
    senderId = senderId,
    receiverId = receiverId,
    text = text,
    photoUri = photoUri,
    timestamp = timestamp
)

// creeaza un obiect InboxItemDto pe baza ultimei conversatii
fun InboxConversationData.toInboxItemDto(forUserId: Long) = InboxItemDto(
    conversationUserId = if (senderId == forUserId) receiverId else senderId,
    conversationUsername = username,
    conversationUserPhoto = userPhotoUri,
    lastMessage = text,
    lastMessagePhoto = photoUri,
    lastMessageTimestamp = timestamp,
    postId = postId
)