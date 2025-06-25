package com.example.lostpals.repository

import com.example.lostpals.data.dao.MessageDao
import com.example.lostpals.data.dto.InboxItemDto
import com.example.lostpals.data.dto.MessageDto
import com.example.lostpals.data.mapper.toDto
import com.example.lostpals.data.mapper.toEntity
import com.example.lostpals.data.mapper.toInboxItemDto

class  MessageRepository(private val messageDao: MessageDao) {

    suspend fun sendMessage(dto: MessageDto) {
        messageDao.insert(dto.toEntity())
    }

    suspend fun getMessagesForPost(postId: Long): List<MessageDto> {
        return messageDao.getMessagesForPost(postId).map { it.toDto() }
    }

    suspend fun getConversation(
        meId: Long, otherId: Long, postId: Long
    ): List<MessageDto> {
        return messageDao.getMessagesForConversation(meId, otherId, postId).map { it.toDto() }
    }

    suspend fun getInbox(userId: Long): List<InboxItemDto> {
        return messageDao.getInboxConversations(userId).map { it.toInboxItemDto(userId) }
    }
}