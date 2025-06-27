package com.example.lostpals.ui.adapters

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lostpals.R
import com.example.lostpals.data.dto.InboxItemDto
import com.example.lostpals.databinding.ItemInboxBinding

// adapter care afiseaza lista conversatiilor din inbox intr-un recyclerview
class InboxAdapter(
    private val listener: (InboxItemDto) -> Unit
) : RecyclerView.Adapter<InboxAdapter.InboxVH>() {

    // listă internă care retine elementele din inbox
    private val items = mutableListOf<InboxItemDto>()

    // viewholder pentru fiecare item din lista
    inner class InboxVH(val b: ItemInboxBinding) : RecyclerView.ViewHolder(b.root)

    // creeaza un viewholder nou atunci cand este envoie de un nou rand pe ecran
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InboxVH =
        InboxVH(
            ItemInboxBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    // spune cate conversatii sunt in lista
    override fun getItemCount(): Int = items.size

    // leaga datele unei conversatii (poza, nume, mesaj) cu elementele de pe ecran
    override fun onBindViewHolder(holder: InboxVH, position: Int) = with(holder.b) {
        val item = items[position] // obtine conversatia curenta

        // incarca poza utilizatorului cu care e conversatia
        Glide.with(root)
            .load(item.conversationUserPhoto) // adresa poza
            .placeholder(R.drawable.placeholder) // poza afisata daca se incarca
            .error(R.drawable.placeholder) // poza afisata dace este o eroare
            .circleCrop()
            .into(profileImg)

        // afiseaza numele utilizatorului cu care e conversatia
        username.text    = item.conversationUsername
        lastMessage.text = item.lastMessage ?: "photo" // ultimul mesaj
        timestamp.text   = DateFormat.format("dd MMM\nHH:mm", item.lastMessageTimestamp) // data si ora cand s-a trimis ultimul mesaj

        // functia de sus se apeleaza cand utilizatorul da click pe o conversatie
        root.setOnClickListener { listener(item) }
    }

    // actualizeaza lista de conversatii afisate in recyclerview
    fun submit(new: List<InboxItemDto>) {
        // compara lista veche cu cea noua
        val diffCallback = object : androidx.recyclerview.widget.DiffUtil.Callback() {
            // numarul de conversatii din lista veche
            override fun getOldListSize() = items.size
            // numarul de conversatii din lista noua
            override fun getNewListSize() = new.size

            // verifica daca doua conversatii sunt aceleasi (acelasi userId)
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].conversationUserId == new[newPos].conversationUserId

            // verifica daca s-a schimbat continutul conversatiei
            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos] == new[newPos]
        }

        // calculeaza diferentele dintre lista veche si cea noua
        val diff = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffCallback)

        // inlocuieste lista veche cu cea noua
        items.clear()
        items.addAll(new)

        // notifica recyclerview ca s-au schimbat doar anumite randuri
        diff.dispatchUpdatesTo(this)
    }

}
