package com.ghanshyam.snapsaga.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ghanshyam.snapsaga.R
import com.ghanshyam.snapsaga.databinding.ReelDesignBinding
import com.ghanshyam.snapsaga.models.ReelModel
import com.squareup.picasso.Picasso

class ReelAdapter(var context: Context, var reelList: ArrayList<ReelModel>) :
    RecyclerView.Adapter<ReelAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ReelDesignBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ReelDesignBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return reelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reel = reelList[position]

        // Ensure the profileLink is not empty or null before loading with Picasso
        if (!reel.profileLink.isNullOrEmpty()) {
            Picasso.get().load(reel.profileLink).placeholder(R.drawable.man).into(holder.binding.profilePic)
        } else {
            // Set a default image if profileLink is empty or null
            holder.binding.profilePic.setImageResource(R.drawable.man)
        }

        holder.binding.captions.text = reel.caption
        holder.binding.videoView.setVideoPath(reel.reelUrl)
        holder.binding.videoView.setOnPreparedListener {
            holder.binding.progressBar.visibility = View.GONE
            holder.binding.videoView.start()
        }
        holder.binding.videoView.setOnCompletionListener {
            // Restart the video when it reaches the end
            holder.binding.videoView.start()
        }
    }
}
