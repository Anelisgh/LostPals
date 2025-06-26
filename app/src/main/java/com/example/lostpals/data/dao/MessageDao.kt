package com.example.lostpals.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lostpals.data.dto.InboxConversationData
import com.example.lostpals.data.entity.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert
    suspend fun insert(message: Message): Long

    /*-----------------------------  POST -----------------------------*/

    @Query("SELECT * FROM messages WHERE postId = :postId ORDER BY timestamp")
    fun getMessagesForPost(postId: Long): Flow<List<Message>>

    /*--------------------------  CONVERSATION ------------------------*/

    @Query(
        """
        SELECT * FROM messages
        WHERE (senderId = :meId AND receiverId = :otherId)
            OR (senderId = :otherId AND receiverId = :meId)
        ORDER BY timestamp
        """
    )
    fun getMessagesForConversation(
        meId: Long,
        otherId: Long,
    ): Flow<List<Message>>

    /*-----------------------------  INBOX ----------------------------*/

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
