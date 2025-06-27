package com.example.lostpals.data.dto

// DTO pentru ecranul Inbox
// reprezinta ultimul mesaj dintr-o conversatie,
data class InboxConversationData(
    val id: Long, // id-ul mesajului cel mai recent
    val postId: Long, // daca discutia s-a creat pe baza unei postari
    val senderId: Long, // cine a trimis ultimul mesaj
    val receiverId: Long, // cine a primit mesajul
    val text: String?, // continutul ultimului mesaj
    val photoUri: String?, // poza trimisa
    val timestamp: Long, // momentul in care a fost trimis ultimul mesaj
    val username: String, // username-ul receiver-ului
    val userPhotoUri: String? // avatarul receiver-ului
)
