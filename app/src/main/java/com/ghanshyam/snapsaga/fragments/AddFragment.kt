package com.ghanshyam.snapsaga.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ghanshyam.snapsaga.R
import com.ghanshyam.snapsaga.databinding.FragmentAddBinding
import com.ghanshyam.snapsaga.post.PostsActivity
import com.ghanshyam.snapsaga.post.ReelsActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private lateinit var binding: FragmentAddBinding

class AddFragment : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false)

        binding.post.setOnClickListener {
            activity?.startActivity(Intent(requireContext(), PostsActivity::class.java))
            activity?.finish()
        }

        binding.reels.setOnClickListener {
            activity?.startActivity(Intent(requireContext(), ReelsActivity::class.java))

        }

        return binding.root
    }

}