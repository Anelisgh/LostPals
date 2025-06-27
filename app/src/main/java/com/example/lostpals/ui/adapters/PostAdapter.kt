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

class PostAdapter(private var posts: List<PostDisplayDto>, private val currentUserId: Long, private val listener: OnChatClickListener) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    // interfata pentru notificarea fragmentului ca s-a apasat pe Chat
    interface OnChatClickListener {
        fun onChatClicked(post: PostDisplayDto)
    }

    // formator pentru afisarea recompensei in EURO
    private val currencyFormat = NumberFormat.getCurrencyInstance().apply {
        maximumFractionDigits = 2
        currency = Currency.getInstance("EUR")
    }

    // viewholder
    class PostViewHolder(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root)

    // creeaza un viewholder atunci cand recyclerview are nevoie de rand
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    // leaga datele unei postari de elementele din layout
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position] // postarea curenta din lista

        holder.binding.apply {
            // incarca poza de profil
            Glide.with(holder.itemView.context)
                .load(post.ownerPhotoUri)
                .placeholder(R.drawable.placeholder) // daca nu exista poza => placeholder
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

            // seteaza textul in campurile corespunzatoare
            username.text = post.ownerUsername // numele userului care creeaza anuntul
            title.text = post.title // titlu anunt
            description.text = post.description // descriere
            location.text = post.location.displayName // locatie

            // recompensa > 0, se afiseaza formatat
            if (post.reward != null && post.reward > 0) {
                reward.text = "Reward: ${currencyFormat.format(post.reward)}"
                reward.visibility = View.VISIBLE
            } else {
                reward.visibility = View.GONE // daca nu exista recompensa, este ascunsa
            }

            // formatare si afisare data postarii
            timestamp.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                .format(Date(post.timestamp))

            // ascunde butonul de chat
            // daca postarea apartine utilizatorului logat
            btnChat.visibility =
                if (post.ownerId == currentUserId) View.GONE else View.VISIBLE

            // cand se apasa pe "Chat", notificam fragmentul folosind listenerul
            btnChat.setOnClickListener { listener.onChatClicked(post) }
        }
    }

    // returneaza cate postari sunt in lista
    override fun getItemCount() = posts.size

    // actualizeaza lista de postari
    fun updatePosts(newPosts: List<PostDisplayDto>) {
        val diffResult = DiffUtil.calculateDiff(PostDiffCallback(posts, newPosts))
        posts = newPosts // inlocuim referinta listei
        diffResult.dispatchUpdatesTo(this) // animam modificarile detectate
    }

    //metoda apelata cand un viewholder iese de pe ecran si poate fi refolosit
    override fun onViewRecycled(holder: PostViewHolder) {
        super.onViewRecycled(holder)
        // eliberam memoria ocupata de imagini
        Glide.with(holder.itemView).clear(holder.binding.profileImage)
        Glide.with(holder.itemView).clear(holder.binding.postImage)
        holder.binding.profileImage.setImageDrawable(null)
        holder.binding.postImage.setImageDrawable(null)
    }
}