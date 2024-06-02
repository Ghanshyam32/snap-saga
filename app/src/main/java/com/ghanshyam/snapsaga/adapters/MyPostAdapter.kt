package com.ghanshyam.snapsaga.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ghanshyam.snapsaga.databinding.MyPostDesignBinding
import com.ghanshyam.snapsaga.models.PostModel
import com.squareup.picasso.Picasso

class MyPostAdapter(var context: Context, var postList: ArrayList<PostModel>) :
    RecyclerView.Adapter<MyPostAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: MyPostDesignBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = MyPostDesignBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.imageView2
        Picasso.get().load(postList.get(position).postUrl).into(holder.binding.imageView2)

    }

}