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

// viewmodel pentru gestionarea mesajelor si conversatiilor utilizatorului
class MessageViewModel(application: Application) : AndroidViewModel(application) {

    // stie cine e user-ul logat si ce id are
    val session = SessionManager(application)
    // repository pentru operatiile legate de mesaje
    private val repo: MessageRepository

    // flux intern pentru lista de conversatii din inbox
    private val _inbox = MutableStateFlow<List<InboxItemDto>>(emptyList())
    // flux public pentru observarea inbox-ului
    val inbox: StateFlow<List<InboxItemDto>> = _inbox

    // flux intern pentru conversatia curenta
    private val _conversation = MutableStateFlow<List<MessageDto>>(emptyList())
    // flux public pentru observarea mesajelor din conversatia selectata
    val conversation: StateFlow<List<MessageDto>> = _conversation

    // initializeaza repository-ul si incarca inbox-ul curent
    init {
        // se intiializeaza baza de date
        val db = AppDatabase.getDatabase(application)
        // se pregateste repository-ul pentru acces la mesaje
        repo = MessageRepository(db.messageDao(), session)

        // pornire flux
        // pune toate conversatiile in inbox
        viewModelScope.launch {
            repo.getInboxFlow().collect { _inbox.value = it }
        }
    }

    // incarca toate mesajele din conversatia cu un anumit utilizator
    fun loadConversation(otherId: Long) {
        viewModelScope.launch {
            repo.getConversationFlow(otherId)
                .collect { _conversation.value = it }
        }
    }

    // trimite un mesaj (text sau poza) catre un utilizator si reincarca conversatia
    fun sendMessage(otherId: Long, text: String? = null, photoUri: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.sendMessage(otherId, text, photoUri)
            // reincarca conversatia dupa trimiterea mesajului
            loadConversation(otherId)
        }
    }

    // curata mesajele din memorie cand conversatia este parasita
    fun resetConversation() {
        _conversation.value = emptyList()
    }
}
