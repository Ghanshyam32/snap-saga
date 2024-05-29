package com.ghanshyam.snapsaga.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ghanshyam.snapsaga.Models.UserModel
import com.ghanshyam.snapsaga.SignUpActivity
import com.ghanshyam.snapsaga.Utils.USER
import com.ghanshyam.snapsaga.Utils.USER_PROFILE_FOLDER
import com.ghanshyam.snapsaga.databinding.FragmentProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.editProfile.setOnClickListener {
            val intent = Intent(activity, SignUpActivity::class.java)
            intent.putExtra("MODE", 1)
            activity?.startActivity(intent)
            activity?.finish()
        }

        return binding.root
    }

    companion object {

    }

    override fun onStart() {
        super.onStart()
        Firebase.firestore.collection(USER).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                val user: UserModel = it.toObject<UserModel>()!!
                binding.name.text = user.name
                binding.email.text = user.email
                if (!user.image.isNullOrEmpty()) {
                    Picasso.get().load(user.image).into(binding.profilePic)
                }
            }
    }

}