package com.example.lostpals.ui.adapters

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lostpals.R
import com.example.lostpals.data.dto.InboxItemDto
import com.example.lostpals.databinding.ItemInboxBinding

class InboxAdapter(
    private val listener: (InboxItemDto) -> Unit
) : RecyclerView.Adapter<InboxAdapter.InboxVH>() {

    // listă internă – se porneşte goală
    private val items = mutableListOf<InboxItemDto>()

    inner class InboxVH(val b: ItemInboxBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InboxVH =
        InboxVH(
            ItemInboxBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: InboxVH, position: Int) = with(holder.b) {
        val item = items[position]

        Glide.with(root)
            .load(item.conversationUserPhoto)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .circleCrop()
            .into(profileImg)

        username.text    = item.conversationUsername
        lastMessage.text = item.lastMessage ?: "photo"
        timestamp.text   = DateFormat.format("dd MMM\nHH:mm", item.lastMessageTimestamp)

        root.setOnClickListener { listener(item) }
    }

    /** Actualizează lista din adapter folosind DiffUtil (performant & animat) */
    fun submit(new: List<InboxItemDto>) {
        // Construim callback-ul de comparație
        val diffCallback = object : androidx.recyclerview.widget.DiffUtil.Callback() {
            override fun getOldListSize() = items.size
            override fun getNewListSize() = new.size

            // Două item-uri sunt aceleași dacă au același utilizator din conversație
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].conversationUserId == new[newPos].conversationUserId

            // Conținutul e același dacă toate câmpurile coincid
            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos] == new[newPos]
        }

        // Calculăm diferențele
        val diff = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffCallback)

        // Înlocuim lista internă
        items.clear()
        items.addAll(new)

        // Notificăm doar schimbările minime
        diff.dispatchUpdatesTo(this)
    }

}
