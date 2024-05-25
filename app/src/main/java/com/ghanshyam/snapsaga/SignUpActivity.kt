package com.ghanshyam.snapsaga

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ghanshyam.snapsaga.Models.UserModel
import com.ghanshyam.snapsaga.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = UserModel()

        binding.login.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.signUp.setOnClickListener {
            val name = binding.name.editText?.text.toString()
            val email = binding.email.editText?.text.toString()
            val password = binding.password.editText?.text.toString()
            val confirmPassword = binding.confirmPassword.editText?.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Please fill all necessary fields",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (password != confirmPassword) {
                Toast.makeText(
                    applicationContext,
                    "Passwords do not match",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            user.name = binding.name.editText?.text.toString()
                            user.password = binding.password.editText?.text.toString()
                            user.email = binding.email.editText?.text.toString()
                            Firebase.auth.currentUser?.let { it1 ->
                                Firebase.firestore.collection("User")
                                    .document(it1.uid).set(user)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            applicationContext,
                                            "Login",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }

                            Toast.makeText(
                                applicationContext,
                                "Welcome to SnapSaga",
                                Toast.LENGTH_SHORT
                            ).show()
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
