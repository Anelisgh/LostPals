package com.example.lostpals.util

import androidx.recyclerview.widget.DiffUtil
import com.example.lostpals.data.dto.PostDisplayDto

class PostDiffCallback(
    private val oldList: List<PostDisplayDto>,
    private val newList: List<PostDisplayDto>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
        return oldList[oldPos].id == newList[newPos].id
    }

    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
        return oldList[oldPos] == newList[newPos]
    }
}