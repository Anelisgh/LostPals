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
    // primeste DAO-ul pentru accesul la bd
    private val messageDao: MessageDao,
    private val session: SessionManager? = null // session manager ofera ID-ul userului logat
) {

    // trimitere mesaj nou de la sender catre receiver
    suspend fun sendMessage(
        otherId: Long,
        text: String? = null,
        photoUri: String? = null
    ) {
        // obtine id-ul expeditorului din sesiune
        val senderId = session?.getUserId()
            ?: error("SessionManager is not injected")
        // creare obiect message
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

    // primeste in timp real toate mesajele legate de o anumita postare
    private fun getMessagesForPostFlow(postId: Long): Flow<List<MessageDto>> =
        messageDao.getMessagesForPost(postId)
            .map { list -> list.map { it.toDto() } } // transforma in dto-uri

    // primeste in timp real conversatia dintre userul curent si alt user
    fun getConversationFlow(otherId: Long): Flow<List<MessageDto>> {
        // primeste id-ul utilizatorului curent din sesiune
        val meId = session?.getUserId()
            ?: error("SessionManager is not injected. Use the suspended overload.")

        // returneaza conversatia si converteste mesajele in dto-uri
        return messageDao.getMessagesForConversation(meId, otherId)
            .map { list -> list.map { it.toDto() } }
    }

    // primeste ultimele conversatii ale userului curent (inbox-ul)
    fun getInboxFlow(): Flow<List<InboxItemDto>> {
        // daca user-ul nu e logat, retunreaza un flow gol
        val meId = session?.getUserId() ?: return flowOf(emptyList())

        return messageDao.getInboxConversations(meId)
            .map { list -> list.map { it.toInboxItemDto(meId) } }
            .catch { emit(emptyList()) } // daca exista eroare la bd => returneaza lista goala
    }
}
