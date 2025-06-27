package com.example.lostpals.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lostpals.R
import com.example.lostpals.data.adapter.MessageAdapter
import com.example.lostpals.data.entity.Message
import com.example.lostpals.viewmodel.MessageViewModel
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
// afiseaza ecranul de chat
class ChatFragment : Fragment() {
    private lateinit var rv: RecyclerView
    private lateinit var input: EditText
    private lateinit var btnSend: ImageView
    private lateinit var tvUser: TextView
    private lateinit var tvDate: TextView

    // viewmodel care tine legatura cu baza de date si logica
    private val viewModel: MessageViewModel by viewModels()
    private val messages = mutableListOf<Message>()
    private lateinit var adapter: MessageAdapter

    // id-ul celuilalt utilizator cu care discuti
    private var otherId: Long = -1
    // id-ul postarii de la care s-a pornit discutia
    private var postId: Long = -1

    // deschide galeria ca sa alegi o poza
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let { onImagePicked(it) }
        }

    // cere permisiunea de citit din stocare
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) pickImageLauncher.launch(arrayOf("image/*"))  // daca primeste permisiunea, deschide galeria
        }

    // initializare interfata
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_chat, container, false)

    // dupa ce layout-ul e gata legam tot
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // legam view-urile din xml
        rv      = view.findViewById(R.id.recyclerViewMessages)
        input   = view.findViewById(R.id.messageInput)
        btnSend = view.findViewById(R.id.sendButton)
        tvUser  = view.findViewById(R.id.username)
        tvDate  = view.findViewById(R.id.messageDate)

        // buton sa atasezi poza
        view.findViewById<ImageView>(R.id.attachButton).setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pickImageLauncher.launch(arrayOf("image/*"))
            } else {
                val perm = Manifest.permission.READ_EXTERNAL_STORAGE
                if (ContextCompat.checkSelfPermission(requireContext(), perm) ==
                    android.content.pm.PackageManager.PERMISSION_GRANTED
                ) {
                    pickImageLauncher.launch(arrayOf("image/*"))  // ai deja permisiunea
                } else {
                    requestPermissionLauncher.launch(perm)  // cere permisiunea
                }
            }
        }

        // butonul inapoi (back)
        view.findViewById<ImageView>(R.id.backButton).setOnClickListener {
            findNavController().popBackStack()
        }

        // luam date din bundle
        val args   = ChatFragmentArgs.fromBundle(requireArguments())
        otherId    = args.otherId
        val partner = args.partnerName

        tvUser.text = partner  // afisam numele
        // data
        tvDate.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

        // pregatim adapterul si recyclerview-ul
        adapter = MessageAdapter(messages, currentUserId = viewModel.session.getUserId())
        rv.layoutManager = LinearLayoutManager(requireContext()).apply { stackFromEnd = true }  // ultimele mesaje
        rv.adapter = adapter

        // incarcam conversatia din viewmodel
        viewModel.resetConversation()  // stergem ce era vechi
        viewModel.loadConversation(otherId)  // aducem mesajele

        // asteptam schimbari in lista de mesaje
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.conversation.collectLatest { list ->
                messages.clear()  // golim lista
                messages.addAll(list.map {
                    Message(
                        id         = it.id,
                        postId     = it.postId,
                        senderId   = it.senderId,
                        receiverId = it.receiverId,
                        text       = it.text,
                        photoUri   = it.photoUri,
                        timestamp  = it.timestamp
                    )
                })
                adapter.notifyDataSetChanged()  // transmitem adapterului ca s-au schimbat datele
                rv.scrollToPosition(messages.size - 1)  // mergem la ultimul mesaj
            }
        }

        // cand apasam send
        btnSend.setOnClickListener {
            val txt = input.text.toString().trim()  // luam textul
            if (txt.isNotEmpty()) {
                viewModel.sendMessage(otherId, txt)  // trimitem mesaj
                input.text.clear()  // golim campul
            }
        }
        input.requestFocus()
    }

    // cand alegem poza, pastram permisiunea si o trimitem
    private fun onImagePicked(uri: Uri) {
        try {
            // pastreaza acces pe termen lung la uri
            requireContext().contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (_: SecurityException) { }

        // trimite poza ca mesaj
        viewModel.sendMessage(
            otherId = otherId,
            text    = null,
            photoUri = uri.toString()
        )
    }
}