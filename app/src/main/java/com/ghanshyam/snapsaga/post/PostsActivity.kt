package com.ghanshyam.snapsaga.post

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ghanshyam.snapsaga.HomeActivity
import com.ghanshyam.snapsaga.databinding.ActivityPostsBinding
import com.ghanshyam.snapsaga.models.PostModel
import com.ghanshyam.snapsaga.models.UserModel
import com.ghanshyam.snapsaga.utils.POST
import com.ghanshyam.snapsaga.utils.POSTS_FOLDER
import com.ghanshyam.snapsaga.utils.USER
import com.ghanshyam.snapsaga.utils.uploadImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.toObject

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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.materialToolbar.setNavigationOnClickListener {
            navigateToHome()
        }

        binding.image.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.cancel.setOnClickListener {
            navigateToHome()
        }

        binding.post.setOnClickListener {
            val currentUser = Firebase.auth.currentUser
            if (currentUser != null && imageUrl != null && binding.caption.editText?.text != null) {
                Firebase.firestore.collection(USER).document(currentUser.uid).get()
                    .addOnSuccessListener { document ->
                        val user = document.toObject<UserModel>()
                        if (user != null) {
                            val post = PostModel(
                                imageUrl!!,
                                binding.caption.editText?.text.toString(),
                                currentUser.uid,
                                System.currentTimeMillis().toString()
                            )
                            Firebase.firestore.collection(POST).document().set(post)
                                .addOnSuccessListener {
                                    Firebase.firestore.collection(currentUser.uid).document().set(post)
                                        .addOnSuccessListener {
                                            navigateToHome()
                                        }
                                }
                        }
                    }
            } else {
                // Handle null cases
                // You can show a toast or log an error message here
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        navigateToHome()
    }

    private fun navigateToHome() {
        startActivity(Intent(applicationContext, HomeActivity::class.java))
        finish()
    }
}
