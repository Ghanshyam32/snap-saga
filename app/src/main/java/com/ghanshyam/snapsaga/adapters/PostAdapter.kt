package com.ghanshyam.snapsaga.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ghanshyam.snapsaga.R
import com.ghanshyam.snapsaga.databinding.PostsHomeBinding
import com.ghanshyam.snapsaga.models.PostModel
import com.ghanshyam.snapsaga.models.UserModel
import com.ghanshyam.snapsaga.utils.USER
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import java.lang.Exception

class PostAdapter(var context: Context, var postList: ArrayList<PostModel>) :
    RecyclerView.Adapter<PostAdapter.MyHolder>() {

    inner class MyHolder(var binding: PostsHomeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var binding = PostsHomeBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        try {
            Firebase.firestore.collection(USER).document(postList.get(position).uid).get()
                .addOnSuccessListener {

                    var user = it.toObject<UserModel>()
                    Glide.with(context).load(user!!.image).placeholder(R.drawable.man)
                        .into(holder.binding.profilePic)
                    holder.binding.name.text = user.name
                }
        } catch (e: Exception) {

        }


        Glide.with(context).load(postList.get(position).postUrl).placeholder(R.drawable.img_2)
            .into(holder.binding.userPost)

        try {
            val text = TimeAgo.using(postList.get(position).time.toLong())
            holder.binding.time.text = text
        } catch (e: Exception) {
            holder.binding.time.text = ""
        }
        holder.binding.caption.text = postList.get(position).caption

        holder.binding.like.setOnClickListener {
            holder.binding.like.setImageResource(R.drawable.heart)
        }
    }

}