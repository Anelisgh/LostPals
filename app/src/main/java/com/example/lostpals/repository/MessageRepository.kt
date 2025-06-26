package com.example.lostpals.repository

import com.example.lostpals.data.dao.MessageDao
import com.example.lostpals.data.dto.InboxItemDto
import com.example.lostpals.data.dto.MessageDto
import com.example.lostpals.data.entity.Message
import com.example.lostpals.data.mapper.toDto
import com.example.lostpals.data.mapper.toEntity
import com.example.lostpals.data.mapper.toInboxItemDto
import com.example.lostpals.util.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf

class MessageRepository(
    private val messageDao: MessageDao,
    private val session: SessionManager? = null
) {

    /*---------------------------  SEND  -------------------------------*/
    suspend fun sendMessage(
        otherId: Long,
        text: String? = null,
        photoUri: String? = null
    ) {
        val senderId = session?.getUserId()
            ?: error("SessionManager is not injected")

        val msg = Message(
            senderId   = senderId,
            receiverId = otherId,
            postId     = 0L,
            text       = text,
            photoUri   = photoUri,
            timestamp  = System.currentTimeMillis()
        )
        messageDao.insert(msg)
    }

    /** OLD: păstrat pentru compatibilitate – îl poţi şterge când nu-ţi mai trebuie. */
    suspend fun sendMessage(dto: MessageDto) =
        messageDao.insert(dto.toEntity())

    /*------------------------------------------------------------------*/
    /*--------------------------  READ (Flow)  -------------------------*/
    /*------------------------------------------------------------------*/

    fun getMessagesForPostFlow(postId: Long): Flow<List<MessageDto>> =
        messageDao.getMessagesForPost(postId)                // Flow<List<MessageEntity>>
            .map { list -> list.map { it.toDto() } }

    fun getConversationFlow(otherId: Long): Flow<List<MessageDto>> {
        val meId = session?.getUserId()
            ?: error("SessionManager is not injected. Use the suspended overload.")

        return messageDao.getMessagesForConversation(meId, otherId)
            .map { list -> list.map { it.toDto() } }
    }

    fun getInboxFlow(): Flow<List<InboxItemDto>> {
        // Dacă user-ul nu e logat, întoarcem un flow gol
        val meId = session?.getUserId() ?: return flowOf(emptyList())

        return messageDao.getInboxConversations(meId)
            .map { list -> list.map { it.toInboxItemDto(meId) } }
            .catch { emit(emptyList()) }          // ← prevenim crash-uri pe DB
    }

    /*--------------------  READ (semnăturile vechi)  ------------------*/

    suspend fun getMessagesForPost(postId: Long): List<MessageDto> =
        getMessagesForPostFlow(postId).first()

    suspend fun getConversation(
        meId: Long,
        otherId: Long,
    ): List<MessageDto> =
        messageDao.getMessagesForConversation(meId, otherId)
            .first()
            .map { it.toDto() }

    suspend fun getInbox(userId: Long): List<InboxItemDto> =
        messageDao.getInboxConversations(userId)
            .first()
            .map { it.toInboxItemDto(userId) }


}
