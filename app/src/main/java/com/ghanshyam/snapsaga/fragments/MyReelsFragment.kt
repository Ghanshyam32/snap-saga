package com.ghanshyam.snapsaga.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ghanshyam.snapsaga.adapters.MyReelAdapter
import com.ghanshyam.snapsaga.databinding.FragmentMyReelsBinding
import com.ghanshyam.snapsaga.models.ReelModel
import com.ghanshyam.snapsaga.utils.REEL
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class MyReelsFragment : Fragment() {
    private lateinit var binding: FragmentMyReelsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyReelsBinding.inflate(inflater, container, false)
        var reelList = ArrayList<ReelModel>()
        var adapter = MyReelAdapter(requireContext(), reelList)
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = adapter
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + REEL).get()
            .addOnSuccessListener {
                var tempList = arrayListOf<ReelModel>()
                for (i in it.documents) {
                    var reel: ReelModel = i.toObject<ReelModel>()!!
                    tempList.add(reel)
                }
                reelList.addAll(tempList)
                adapter.notifyDataSetChanged()
            }
        return binding.root
    }

    companion object {
    }

}