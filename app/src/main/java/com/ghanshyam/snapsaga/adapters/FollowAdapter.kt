package com.ghanshyam.snapsaga.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ghanshyam.snapsaga.R
import com.ghanshyam.snapsaga.databinding.FollowListBinding
import com.ghanshyam.snapsaga.models.UserModel
import com.google.firebase.firestore.auth.User

class FollowAdapter(var context: Context, var followList: ArrayList<UserModel>) :
    RecyclerView.Adapter<FollowAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: FollowListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = FollowListBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return followList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(followList.get(position).image).placeholder(R.drawable.man)
            .into(holder.binding.userPic)
        holder.binding.name.text = followList.get(position).name
    }
}