package com.ghanshyam.snapsaga

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ghanshyam.snapsaga.databinding.ActivityPostDetailBinding
import com.squareup.picasso.Picasso

class PostDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postImage = intent.getStringExtra("POST_IMAGE")
        val profileImage = intent.getStringExtra("PROFILE_IMAGE")
        val name = intent.getStringExtra("NAME")
        val time = intent.getStringExtra("TIME")
        val caption = intent.getStringExtra("CAPTION")

        binding.name.text = name
        binding.time.text = time
        binding.caption.text = caption

        if (!profileImage.isNullOrEmpty()) {
            Picasso.get().load(profileImage).into(binding.profilePic)
        }

        if (!postImage.isNullOrEmpty()) {
            Picasso.get().load(postImage).into(binding.userPost)
        }
    }
}
