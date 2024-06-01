package com.ghanshyam

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ghanshyam.snapsaga.R
import com.ghanshyam.snapsaga.databinding.ActivityPostsBinding
import com.ghanshyam.snapsaga.utils.POSTS_FOLDER
import com.ghanshyam.snapsaga.utils.uploadImage

class PostsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostsBinding

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, POSTS_FOLDER) {
                if (it != null) {
                    binding.image.setImageURI(uri)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.image.setOnClickListener {
            launcher.launch("image/*")
        }
    }
}
