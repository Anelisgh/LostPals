package com.example.lostpals.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lostpals.data.dto.InboxConversationData
import com.example.lostpals.data.entity.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    // insereaza/ salveaza un mesaj nou in baza de date (in tabela messages)
    @Insert
    suspend fun insert(message: Message): Long

    // obtine toate mesajele dintr-un anumit post
    // ordonate dupa timp
    @Query("SELECT * FROM messages WHERE postId = :postId ORDER BY timestamp")
    fun getMessagesForPost(postId: Long): Flow<List<Message>> // returnaeaza un Flow => Ui se actualizeaza automat daca apare un mesaj nou in bd

    // obtine mesajele intre doi utilizatori
    @Query(
        """
        SELECT * FROM messages
        WHERE (senderId = :meId AND receiverId = :otherId) -- verifica daca meId este sender si otherId este receiver sau invers
            OR (senderId = :otherId AND receiverId = :meId)
        ORDER BY timestamp
        """
    )
    fun getMessagesForConversation(
        meId: Long,
        otherId: Long,
    ): Flow<List<Message>> // Flow => Ui se actualizeaza automat la mesajele noi

    // obtine inboxul unui utilizator (ultimele conversatii)
    // returneaza ultimul mesaj din fiecare conversatie
    @Query(
        """
    SELECT m.*, 
           otherUser.username, 
           otherUser.photoUri AS userPhotoUri
    FROM messages m
    INNER JOIN users otherUser 
           ON otherUser.id = 
                CASE 
                    WHEN m.senderId = :userId THEN m.receiverId 
                    ELSE m.senderId 
                END
    WHERE (m.senderId = :userId OR m.receiverId = :userId)
      AND m.timestamp = (
            SELECT MAX(timestamp)
            FROM messages
            WHERE (
                     (senderId = m.senderId AND receiverId = m.receiverId)
                  OR (senderId = m.receiverId AND receiverId = m.senderId)
                  )
      )
    ORDER BY m.timestamp DESC
    """
    )
    fun getInboxConversations(userId: Long): Flow<List<InboxConversationData>>

}
