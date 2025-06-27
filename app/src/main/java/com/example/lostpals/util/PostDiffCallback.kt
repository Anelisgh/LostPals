package com.example.lostpals.util

import androidx.recyclerview.widget.DiffUtil
import com.example.lostpals.data.dto.PostDisplayDto

// clasa folosita pentru a compara doua liste de postari si a identifica diferentele dintre ele
class PostDiffCallback(
    private val oldList: List<PostDisplayDto>, // lista veche de postari afisate
    private val newList: List<PostDisplayDto>  // lista noua de postari ce trebuie afisata
) : DiffUtil.Callback() {

    // returneaza marimea listei vechi
    override fun getOldListSize() = oldList.size

    // returneaza marimea listei noi
    override fun getNewListSize() = newList.size

    // verifica daca doua postari din liste sunt aceleasi pe baza id-ului
    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
        return oldList[oldPos].id == newList[newPos].id
    }

    // verifica daca continutul a doua postari este identic
    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
        return oldList[oldPos] == newList[newPos]
    }
}
