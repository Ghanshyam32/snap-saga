package com.ghanshyam.snapsaga.post

import android.app.ProgressDialog
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ghanshyam.snapsaga.HomeActivity
import com.ghanshyam.snapsaga.databinding.ActivityReelsBinding
import com.ghanshyam.snapsaga.models.ReelModel
import com.ghanshyam.snapsaga.models.UserModel
import com.ghanshyam.snapsaga.utils.REEL
import com.ghanshyam.snapsaga.utils.REELS_FOLDER
import com.ghanshyam.snapsaga.utils.USER
import com.ghanshyam.snapsaga.utils.uploadVideo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.toObject

class ReelsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReelsBinding
    private var videoUrl: String? = null
    private lateinit var progressDialog: ProgressDialog

    // Activity result launcher for picking a video
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(this, uri)

            // Get the duration of the selected video
            val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val duration = durationStr?.toLong() ?: 0

            if (duration > 60000) {
                // Show a toast if the video duration exceeds 60 seconds
                Toast.makeText(this, "Video must be less than 60 seconds", Toast.LENGTH_SHORT).show()
            } else {
                // Upload the video to Firebase Storage
                uploadVideo(uri, REELS_FOLDER, progressDialog) { url ->
                    if (url != null) {
                        // If upload successful, store the video URL
                        videoUrl = url

                        // Load the thumbnail of the video into the ImageView (adjust ID as per your layout)
                        Glide.with(this)
                            .load(videoUrl)
                            .thumbnail(0.1f) // Load a thumbnail for the video
                            .into(binding.selectReel)

                        // Optionally, show a success message or handle UI state change
                        binding.successMsg.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReelsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        progressDialog = ProgressDialog(this).apply {
            setMessage("Uploading video...")
            setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            max = 100
        }

        binding.selectReel.setOnClickListener {
            // Launch the video picker
            launcher.launch("video/*")
        }

        binding.cancel.setOnClickListener {
            // Navigate back to HomeActivity when cancel button is clicked
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }

        binding.materialToolbar.setNavigationOnClickListener {
            // Navigate back to HomeActivity when toolbar back button is clicked
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }

        binding.post.setOnClickListener {
            // When post button is clicked, upload reel to Firestore
            if (videoUrl.isNullOrEmpty()) {
                // Handle case where no video is selected
                Toast.makeText(this, "Please select a video", Toast.LENGTH_SHORT).show()
            } else {
                // Retrieve current user details from Firestore
                Firebase.firestore.collection(USER)
                    .document(Firebase.auth.currentUser!!.uid)
                    .get()
                    .addOnSuccessListener { document ->
                        // Convert Firestore document to UserModel
                        val user: UserModel = document.toObject<UserModel>()!!

                        // Create ReelModel object with selected video URL, caption, and user image
                        val reel = ReelModel(
                            videoUrl!!,
                            binding.caption.editText?.text.toString(),
                            user.image!!
                        )

                        // Save reel to Firestore
                        Firebase.firestore.collection(REEL)
                            .document()
                            .set(reel)
                            .addOnSuccessListener {
                                // Optionally, handle success (navigate to HomeActivity or show message)
                                Toast.makeText(this, "Reel uploaded successfully", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(applicationContext, HomeActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                // Handle failure to upload reel to Firestore
                                Toast.makeText(this, "Failed to upload reel: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .addOnFailureListener { e ->
                        // Handle failure to retrieve user details from Firestore
                        Toast.makeText(this, "Failed to get user details: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
