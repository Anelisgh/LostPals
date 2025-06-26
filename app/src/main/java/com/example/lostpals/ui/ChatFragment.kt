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

class ChatFragment : Fragment() {

    /* ---------- view refs ---------- */
    private lateinit var rv: RecyclerView
    private lateinit var input: EditText
    private lateinit var btnSend: ImageView
    private lateinit var tvUser: TextView
    private lateinit var tvDate: TextView

    /* ---------- infra ---------- */
    private val viewModel: MessageViewModel by viewModels()
    private val messages = mutableListOf<Message>()
    private lateinit var adapter: MessageAdapter

    private var otherId: Long = -1
    private var postId: Long = -1

    /* ---------- pick image ---------- */
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let { onImagePicked(it) }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) pickImageLauncher.launch(arrayOf("image/*"))
        }

    /* ---------- lifecycle ---------- */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_chat, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* ----- view bindings ----- */
        rv      = view.findViewById(R.id.recyclerViewMessages)
        input   = view.findViewById(R.id.messageInput)
        btnSend = view.findViewById(R.id.sendButton)
        tvUser  = view.findViewById(R.id.username)
        tvDate  = view.findViewById(R.id.messageDate)

        /* ----- attach image button ----- */
        view.findViewById<ImageView>(R.id.attachButton).setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pickImageLauncher.launch(arrayOf("image/*"))
            } else {
                val perm = Manifest.permission.READ_EXTERNAL_STORAGE
                if (ContextCompat.checkSelfPermission(requireContext(), perm) ==
                    android.content.pm.PackageManager.PERMISSION_GRANTED
                ) {
                    pickImageLauncher.launch(arrayOf("image/*"))
                } else {
                    requestPermissionLauncher.launch(perm)
                }
            }
        }

        /* ----- back arrow ----- */
        view.findViewById<ImageView>(R.id.backButton).setOnClickListener {
            findNavController().popBackStack()
        }

        /* ----- args ----- */
        val args   = ChatFragmentArgs.fromBundle(requireArguments())
        otherId    = args.otherId
        val partner = args.partnerName

        tvUser.text = partner
        tvDate.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

        /* ----- recycler ----- */
        adapter = MessageAdapter(messages, currentUserId = viewModel.session.getUserId())
        rv.layoutManager = LinearLayoutManager(requireContext()).apply { stackFromEnd = true }
        rv.adapter = adapter

        /* ----- flows ----- */
        viewModel.resetConversation()
        viewModel.loadConversation(otherId)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.conversation.collectLatest { list ->
                messages.clear()
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
                adapter.notifyDataSetChanged()
                rv.scrollToPosition(messages.size - 1)
            }
        }

        /* ----- send text ----- */
        btnSend.setOnClickListener {
            val txt = input.text.toString().trim()
            if (txt.isNotEmpty()) {
                viewModel.sendMessage(otherId, txt)
                input.text.clear()
            }
        }

        input.requestFocus()
    }

    /* ---------- helpers ---------- */
    private fun onImagePicked(uri: Uri) {
        // păstrăm permisiunea (necesar pentru uri persistente)
        try {
            requireContext().contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (_: SecurityException) { /* ignoră pe Android 13+ */ }

        viewModel.sendMessage(
            otherId = otherId,
            text    = null,
            photoUri = uri.toString()
        )
    }
}
