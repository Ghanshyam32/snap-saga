package com.ghanshyam.snapsaga.post

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ghanshyam.snapsaga.HomeActivity
import com.ghanshyam.snapsaga.R
import com.ghanshyam.snapsaga.databinding.ActivityPostsBinding
import com.ghanshyam.snapsaga.models.PostModel
import com.ghanshyam.snapsaga.utils.POST
import com.ghanshyam.snapsaga.utils.POSTS_FOLDER
import com.ghanshyam.snapsaga.utils.USER_PROFILE_FOLDER
import com.ghanshyam.snapsaga.utils.uploadImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class PostsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostsBinding

    private var imageUrl: String? = null
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, POSTS_FOLDER) { url ->
                if (url != null) {
                    binding.image.setImageURI(uri)
                    imageUrl = url
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.image.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.post.setOnClickListener {
            val post: PostModel = PostModel(imageUrl!!, binding.caption.editText?.text.toString())
            Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document().set(post)
                .addOnSuccessListener {
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    finish()
                }
                .addOnSuccessListener {
                    finish()
                }
        }

    }
}