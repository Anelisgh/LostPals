package com.example.lostpals.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// creare tabela "mesagges"
@Entity(
    tableName = "messages", foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["senderId"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["receiverId"],
        onDelete = ForeignKey.CASCADE // daca un utilizator este sters => se sterg automat si mesajele pe care le-a trimis
    ), ForeignKey(
        entity = Post::class,
        parentColumns = ["id"],
        childColumns = ["postId"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index("senderId"), Index("receiverId"), Index("postId"), Index("timestamp")]
)
data class Message(
    // atributele clasei messages
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val postId: Long,
    val senderId: Long,
    val receiverId: Long,
    val text: String? = null,
    val photoUri: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)