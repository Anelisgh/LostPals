package com.example.lostpals.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lostpals.R
import com.example.lostpals.data.entity.Message
import com.bumptech.glide.Glide

class MessageAdapter(
    private val messages: List<Message>,
    private val currentUserId: Long
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_SENT = 1
        private const val TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == currentUserId) TYPE_SENT else TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : RecyclerView.ViewHolder {
        val layoutRes = if (viewType == TYPE_SENT)
            R.layout.item_message_sent
        else
            R.layout.item_message_received

        val view = LayoutInflater.from(parent.context)
            .inflate(layoutRes, parent, false)

        return if (viewType == TYPE_SENT)
            SentViewHolder(view)
        else
            ReceivedViewHolder(view)
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = messages[position]

        when (holder) {
            is SentViewHolder     -> holder.bind(msg)
            is ReceivedViewHolder -> holder.bind(msg)
        }

        // asigură înălțime wrap-content per mesaj
        holder.itemView.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
    inner class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txt: TextView = itemView.findViewById(R.id.textMessage)
        private val img: ImageView = itemView.findViewById(R.id.imageMessage)

        fun bind(m: Message) {
            if (!m.text.isNullOrEmpty()) {
                txt.text = m.text
                txt.visibility = View.VISIBLE
            } else {
                txt.visibility = View.GONE
            }
            if (!m.photoUri.isNullOrEmpty()) {
                img.visibility = View.VISIBLE
                Glide.with(itemView).load(m.photoUri).into(img)
            } else {
                img.visibility = View.GONE
            }
        }
    }

    inner class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txt: TextView = itemView.findViewById(R.id.textMessage)
        private val img: ImageView = itemView.findViewById(R.id.imageMessage)

        fun bind(m: Message) {
            if (!m.text.isNullOrEmpty()) {
                txt.text = m.text
                txt.visibility = View.VISIBLE
            } else {
                txt.visibility = View.GONE
            }
            if (!m.photoUri.isNullOrEmpty()) {
                img.visibility = View.VISIBLE
                    Glide.with(itemView).load(m.photoUri).into(img)
            } else {
                img.visibility = View.GONE
            }
        }
    }
}
