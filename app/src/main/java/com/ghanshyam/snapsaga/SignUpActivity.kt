package com.ghanshyam.snapsaga

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ghanshyam.snapsaga.models.UserModel
import com.ghanshyam.snapsaga.utils.USER
import com.ghanshyam.snapsaga.utils.USER_PROFILE_FOLDER
import com.ghanshyam.snapsaga.utils.uploadImage
import com.ghanshyam.snapsaga.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.squareup.picasso.Picasso

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private lateinit var user: UserModel
    private var isProfilePicSet = false
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, USER_PROFILE_FOLDER) { downloadUrl ->
                if (downloadUrl == null) {
                    // Handle error
                } else {
                    user.image = downloadUrl
                    binding.profilePic.setImageURI(uri)
                    isProfilePicSet = true
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = UserModel()

        if (intent.hasExtra("MODE") && intent.getIntExtra("MODE", -1) == 1) {
            binding.signUp.text = "Update Profile"
            binding.login.text = "Sign Out"

            Firebase.firestore.collection(USER).document(Firebase.auth.currentUser!!.uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(UserModel::class.java)
                    if (user != null) {
                        this.user = user
                        if (!user.image.isNullOrEmpty()) {
                            Picasso.get().load(user.image).into(binding.profilePic)
                            isProfilePicSet = true
                        }
                        binding.name.editText?.setText(user.name)
                        binding.email.editText?.setText(user.email)
                        binding.password.editText?.setText(user.password)
                    }
                }
        }

        binding.login.setOnClickListener {
            if (intent.hasExtra("MODE") && intent.getIntExtra("MODE", -1) == 1) {
                Firebase.auth.signOut()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.addPic.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.signUp.setOnClickListener {
            val name = binding.name.editText?.text.toString()
            val email = binding.email.editText?.text.toString()
            val password = binding.password.editText?.text.toString()
            val confirmPassword = binding.confirmPassword.editText?.text.toString()

            if (intent.hasExtra("MODE") && intent.getIntExtra("MODE", -1) == 1) {
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "Please fill all necessary fields",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (password != confirmPassword) {
                    Toast.makeText(
                        applicationContext, "Passwords do not match", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    user.name = name
                    user.email = email
                    user.password = password
                    Firebase.firestore.collection(USER).document(Firebase.auth.currentUser!!.uid)
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(
                                applicationContext, "Profile Updated", Toast.LENGTH_SHORT
                            ).show()
                            startActivity(
                                Intent(
                                    applicationContext, HomeActivity::class.java
                                )
                            )
                            finish()
                        }
                }
            } else {
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "Please fill all necessary fields",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (password != confirmPassword) {
                    Toast.makeText(
                        applicationContext, "Passwords do not match", Toast.LENGTH_SHORT
                    ).show()
                } else if (!isProfilePicSet) {
                    Toast.makeText(this, "Please set a profile picture", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { result ->
                            if (result.isSuccessful) {
                                user.name = name
                                user.password = password
                                user.email = email
                                Firebase.auth.currentUser?.let { currentUser ->
                                    Firebase.firestore.collection(USER).document(currentUser.uid)
                                        .set(user)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                applicationContext,
                                                "Welcome to SnapSaga",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            startActivity(
                                                Intent(
                                                    applicationContext, HomeActivity::class.java
                                                )
                                            )
                                            finish()
                                        }
                                }
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    result.exception?.localizedMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }
}
