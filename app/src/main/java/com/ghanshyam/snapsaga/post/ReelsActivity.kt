package com.ghanshyam.snapsaga.post

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ghanshyam.snapsaga.HomeActivity
import com.ghanshyam.snapsaga.databinding.ActivityReelsBinding
import com.ghanshyam.snapsaga.models.ReelModel
import com.ghanshyam.snapsaga.models.UserModel
import com.ghanshyam.snapsaga.utils.REEL
import com.ghanshyam.snapsaga.utils.REELS_FOLDER
import com.ghanshyam.snapsaga.utils.USER
import com.ghanshyam.snapsaga.utils.uploadVideo
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class ReelsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReelsBinding

    private var videoUrl: String? = null
    lateinit var progressDialog: ProgressDialog
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadVideo(uri, REELS_FOLDER, progressDialog) { url ->
                if (url != null) {
                    videoUrl = url
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityReelsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        progressDialog = ProgressDialog(this)

        binding.selectReel.setOnClickListener {
            launcher.launch("video/*")
        }

        binding.cancel.setOnClickListener {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }
        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }
        binding.post.setOnClickListener {
            Firebase.firestore.collection(USER).document(Firebase.auth.currentUser!!.uid).get()
                .addOnSuccessListener {
                    var user: UserModel = it.toObject<UserModel>()!!
                    val reel: ReelModel =
                        ReelModel(videoUrl!!, binding.caption.editText?.text.toString(), user.image!!)
                    Firebase.firestore.collection(REEL).document().set(reel).addOnSuccessListener {
                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + REEL)
                            .document().set(reel)
                            .addOnSuccessListener {
                                startActivity(Intent(applicationContext, HomeActivity::class.java))
                                finish()
                            }.addOnSuccessListener {
                                finish()
                            }
                    }
                }
        }
    }
}