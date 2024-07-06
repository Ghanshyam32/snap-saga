package com.ghanshyam.snapsaga.adapters

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ghanshyam.snapsaga.R
import com.ghanshyam.snapsaga.databinding.PostsHomeBinding
import com.ghanshyam.snapsaga.models.PostModel
import com.ghanshyam.snapsaga.models.UserModel
import com.ghanshyam.snapsaga.utils.USER
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.lang.Exception

class PostAdapter(var context: Context, var postList: ArrayList<PostModel>) :
    RecyclerView.Adapter<PostAdapter.MyHolder>() {

    inner class MyHolder(var binding: PostsHomeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = PostsHomeBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val post = postList[position]

        // Fetch user data once
        FirebaseFirestore.getInstance().collection(USER).document(post.uid).get()
            .addOnSuccessListener { document ->
                val user = document.toObject<UserModel>()
                if (user != null) {
                    Glide.with(context).load(user.image).placeholder(R.drawable.man)
                        .into(holder.binding.profilePic)
                    holder.binding.name.text = user.name

                    // Combine username and caption
                    val username = user.name
                    val caption = post.caption
                    val captionText = "$username $caption"
                    val spannableString = SpannableString(captionText)
                    if (username != null) {
                        spannableString.setSpan(
                            StyleSpan(android.graphics.Typeface.BOLD),
                            0,
                            username.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    holder.binding.caption.text = spannableString
                }
            }
            .addOnFailureListener {
                holder.binding.name.text = ""
                holder.binding.caption.text = post.caption
            }

        // Load post image
        Glide.with(context).load(post.postUrl).placeholder(R.drawable.img_2)
            .into(holder.binding.userPost)

        // Set post time
        try {
            val text = TimeAgo.using(post.time.toLong())
            holder.binding.time.text = text
        } catch (e: Exception) {
            holder.binding.time.text = ""
        }

        // Share button click listener
        holder.binding.share.setOnClickListener {
            holder.binding.share.setImageResource(R.drawable.send_t)
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                holder.binding.share.setImageResource(R.drawable.send_icon)
            }, 600)

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, post.postUrl)
            }
            context.startActivity(Intent.createChooser(intent, "Share via"))
        }

        // Like button click listener
        var isLiked = false
        holder.binding.like.setOnClickListener {
            isLiked = !isLiked
            holder.binding.like.setImageResource(if (isLiked) R.drawable.heart else R.drawable.like)
        }

        // Save button click listener
        var isSaved = false
        holder.binding.save.setOnClickListener {
            isSaved = !isSaved
            holder.binding.save.setImageResource(if (isSaved) R.drawable.save_icon_white else R.drawable.save_icon)
        }
    }
}
