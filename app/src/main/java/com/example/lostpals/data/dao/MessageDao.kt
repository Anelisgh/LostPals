package com.example.lostpals.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lostpals.data.dto.InboxConversationData
import com.example.lostpals.data.entity.Message

@Dao
interface MessageDao {
    @Insert
    suspend fun insert(message: Message): Long

    @Query("SELECT * FROM messages WHERE postId = :postId")
    suspend fun getMessagesForPost(postId: Long): List<Message>

    @Query(
        """
    SELECT * FROM messages 
    WHERE ((senderId = :meId AND receiverId = :otherId) 
    OR (senderId = :otherId AND receiverId = :meId)) 
    AND postId = :postId 
    ORDER BY timestamp ASC
"""
    )
    suspend fun getMessagesForConversation(meId: Long, otherId: Long, postId: Long): List<Message>

    // pentru inbox
    @Query(
        """SELECT m.*, otherUser.username, otherUser.photoUri AS userPhotoUri
    FROM messages m
    INNER JOIN users otherUser ON
        (CASE 
            WHEN m.senderId = :userId THEN otherUser.id = m.receiverId
            ELSE otherUser.id = m.senderId
        END)
    WHERE (m.senderId = :userId OR m.receiverId = :userId)
      AND m.timestamp = (
        SELECT MAX(latestMessage.timestamp)
        FROM messages latestMessage
        WHERE (
            (latestMessage.senderId = :userId AND latestMessage.receiverId = 
                CASE WHEN m.senderId = :userId THEN m.receiverId ELSE m.senderId END)
            OR
            (latestMessage.receiverId = :userId AND latestMessage.senderId = 
                CASE WHEN m.senderId = :userId THEN m.receiverId ELSE m.senderId END)
        )
    )
    ORDER BY m.timestamp DESC"""
    )
    suspend fun getInboxConversations(userId: Long): List<InboxConversationData>
}