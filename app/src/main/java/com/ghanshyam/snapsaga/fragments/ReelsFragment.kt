package com.ghanshyam.snapsaga.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ghanshyam.snapsaga.R
import com.ghanshyam.snapsaga.adapters.ReelAdapter
import com.ghanshyam.snapsaga.databinding.FragmentReelsBinding
import com.ghanshyam.snapsaga.models.ReelModel
import com.ghanshyam.snapsaga.utils.REEL
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class ReelsFragment : Fragment() {
    lateinit var adapter: ReelAdapter
    var reelList = ArrayList<ReelModel>()
    private lateinit var binding: FragmentReelsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReelsBinding.inflate(inflater, container, false)
        adapter = ReelAdapter(requireContext(), reelList)
        binding.viewPager.adapter = adapter
        Firebase.firestore.collection(REEL).get().addOnSuccessListener {
            var tempList = ArrayList<ReelModel>()
            reelList.clear()

            for (i in it.documents) {
                var reel = i.toObject<ReelModel>()!!
                tempList.add(reel)
            }
            reelList.addAll(tempList)
            reelList.reverse()
            adapter.notifyDataSetChanged()
        }
        return binding.root
    }

    companion object {

    }
}