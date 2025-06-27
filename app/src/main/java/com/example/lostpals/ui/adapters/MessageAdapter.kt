package com.example.lostpals.ui.adapters

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
    private val currentUserId: Long // id-ul utilizatorului logat
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // tipuri de mesaje: 1-> trimis; 2-> primit
    companion object {
        private const val TYPE_SENT = 1
        private const val TYPE_RECEIVED = 2
    }

    // daca senderId este ID-ul utilziatorului logat, atunci e un mesaj trimis
    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == currentUserId) TYPE_SENT else TYPE_RECEIVED
    }

    // crearea baloanelor vizuale pentru fiecare mesaj
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : RecyclerView.ViewHolder {
        val layoutRes = if (viewType == TYPE_SENT)
            R.layout.item_message_sent
        else
            R.layout.item_message_received

        // incarcarea xml-ului
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutRes, parent, false)

        // returneaza ViewHolderul in functie de tipul mesajului
        return if (viewType == TYPE_SENT)
            SentViewHolder(view) // sent message view
        else
            ReceivedViewHolder(view) // received message view
    }

    // numara cate mesaje se afiseaza
    override fun getItemCount(): Int = messages.size

    // leaga mesajul cu balonul sau vizual
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // obtinem mesajul care trebuie afisat la pozitia respectiva
        val msg = messages[position]

        // lipim mesajul pe balonul vizual potrivit
        when (holder) {
            is SentViewHolder -> holder.bind(msg) // balon alb
            is ReceivedViewHolder -> holder.bind(msg) // balon albastru
        }

        // asigura inaltimea wrap-content per mesaj
        holder.itemView.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, // latime -> cat tot ecranul
            ViewGroup.LayoutParams.WRAP_CONTENT // inaltime -> cat text/poza
        )
    }
    inner class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txt: TextView = itemView.findViewById(R.id.textMessage)
        private val img: ImageView = itemView.findViewById(R.id.imageMessage)

        // leaga mesajele de elementele vizuale
        fun bind(m: Message) {
            if (!m.text.isNullOrEmpty()) { // daca mesajul contine text
                txt.text = m.text // il afiseaza in TextView
                txt.visibility = View.VISIBLE // se asigura ca TextView-ul este vizibil
            } else {
                txt.visibility = View.GONE // altfel il ascunde complet
            }
            if (!m.photoUri.isNullOrEmpty()) { // daca mesajul are o fotografie
                img.visibility = View.VISIBLE // face ImageView-ul vizibil
                Glide.with(itemView).load(m.photoUri).into(img) // incarca poza cu Glide
            } else {
                img.visibility = View.GONE // daca poza nu exista poza, ImageView-ul este ascuns
            }
        }
    }

    inner class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txt: TextView = itemView.findViewById(R.id.textMessage)
        private val img: ImageView = itemView.findViewById(R.id.imageMessage)

        // lipeste continutul mesajului pe UI
        fun bind(m: Message) {
            if (!m.text.isNullOrEmpty()) { // verifica daca mesajul are text
                txt.text = m.text // il afiseaza
                txt.visibility = View.VISIBLE
            } else {
                txt.visibility = View.GONE // nu are text -> se ascunde TextView-ul
            }
            if (!m.photoUri.isNullOrEmpty()) { // verifica daca mesajul are fotografie
                img.visibility = View.VISIBLE
                    Glide.with(itemView).load(m.photoUri).into(img) // se foloseste Glide pentru incarcarea imaginii
            } else {
                img.visibility = View.GONE // daca nu are poza -> ImageView-ul este ascuns
            }
        }
    }
}
