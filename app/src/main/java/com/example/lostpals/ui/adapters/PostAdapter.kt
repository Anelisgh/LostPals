package com.example.lostpals.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lostpals.R
import com.example.lostpals.data.dto.PostDisplayDto
import com.example.lostpals.databinding.ItemPostBinding
import com.example.lostpals.util.PostDiffCallback
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Date
import java.util.Locale

class PostAdapter(private var posts: List<PostDisplayDto>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    // formator pentru currency
    private val currencyFormat = NumberFormat.getCurrencyInstance().apply {
        maximumFractionDigits = 2
        currency = Currency.getInstance("EUR")
    }

    class PostViewHolder(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        holder.binding.apply {
            // incarca poza de profil
            Glide.with(holder.itemView.context)
                .load(post.ownerPhotoUri)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .circleCrop()
                .into(profileImage)
            // incarca poza postarii
            Glide.with(holder.itemView.context)
                .load(post.photoUri)
                .placeholder(R.drawable.post_placeholder)
                .error(R.drawable.post_placeholder)
                .centerCrop()
                .into(postImage)

            username.text = post.ownerUsername
            title.text = post.title
            description.text = post.description
            location.text = post.location.displayName

            if (post.reward != null && post.reward > 0) {
                reward.text = "Reward: ${currencyFormat.format(post.reward)}"
                reward.visibility = View.VISIBLE
            } else {
                reward.visibility = View.GONE
            }

            timestamp.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                .format(Date(post.timestamp))
        }
    }

    override fun getItemCount() = posts.size
// actualizeaza doar ce s-a schimbat
    fun updatePosts(newPosts: List<PostDisplayDto>) {
        val diffResult = DiffUtil.calculateDiff(PostDiffCallback(posts, newPosts))
        posts = newPosts
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onViewRecycled(holder: PostViewHolder) {
        super.onViewRecycled(holder)
        Glide.with(holder.itemView).clear(holder.binding.profileImage)
        Glide.with(holder.itemView).clear(holder.binding.postImage)
        holder.binding.profileImage.setImageDrawable(null)
        holder.binding.postImage.setImageDrawable(null)
    }
}