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
        Glide.with(context).load(followList[position].image).placeholder(R.drawable.man)
            .into(holder.binding.userPic)

        val name = followList[position].name
        if (name != null) {
            if (name.length > 10) {
                // Trim the name to 15 characters and add ellipsis
                if (name != null) {
                    holder.binding.name.text = name.substring(0, 10) + "..."
                }
            } else {
                // Set the name as is
                holder.binding.name.text = name
            }
        }
    }

}