package com.example.lostpals.data.dto

// obiect folosit pentru trimiterea unui mesaj intre doi utilizatori
data class MessageDto(
    val id: Long = 0,
    val postId: Long, // id-ul postarii de la care a pornit conversatia
    val senderId: Long,
    val receiverId: Long,
    val text: String? = null,
    val photoUri: String? = null,
    val timestamp: Long = System.currentTimeMillis() // momentul in care este creat mesajul
)
