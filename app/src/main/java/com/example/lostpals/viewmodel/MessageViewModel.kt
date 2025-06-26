package com.example.lostpals.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostpals.data.database.AppDatabase
import com.example.lostpals.data.dto.InboxItemDto
import com.example.lostpals.data.dto.MessageDto
import com.example.lostpals.repository.MessageRepository
import com.example.lostpals.util.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MessageViewModel(application: Application) : AndroidViewModel(application) {

    val session = SessionManager(application)
    private val repo: MessageRepository

    private val _inbox = MutableStateFlow<List<InboxItemDto>>(emptyList())
    val inbox: StateFlow<List<InboxItemDto>> = _inbox

    private val _conversation = MutableStateFlow<List<MessageDto>>(emptyList())
    val conversation: StateFlow<List<MessageDto>> = _conversation

    init {
        val db = AppDatabase.getDatabase(application)
        repo = MessageRepository(db.messageDao(), session)

        viewModelScope.launch {
            repo.getInboxFlow().collect { _inbox.value = it }
        }
    }

    fun loadConversation(otherId: Long) {
        viewModelScope.launch {
            repo.getConversationFlow(otherId)
                .collect { _conversation.value = it }
        }
    }

    fun sendMessage(otherId: Long, text: String? = null, photoUri: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.sendMessage(otherId, text, photoUri)
            // reîncarcă conversația după trimitere
            loadConversation(otherId)
        }
    }

    fun resetConversation() {
        _conversation.value = emptyList()
    }


}
