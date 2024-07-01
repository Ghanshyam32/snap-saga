package com.ghanshyam.snapsaga.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghanshyam.snapsaga.R
import com.ghanshyam.snapsaga.adapters.SearchAdapter
import com.ghanshyam.snapsaga.databinding.FragmentSearchBinding
import com.ghanshyam.snapsaga.models.UserModel
import com.ghanshyam.snapsaga.utils.USER
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    lateinit var adapter: SearchAdapter
    var userList = ArrayList<UserModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.searchRes.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(requireContext(), userList)
        binding.searchRes.adapter = adapter

        Firebase.firestore.collection(USER).get().addOnSuccessListener {
            var tempList = ArrayList<UserModel>()
            userList.clear()
            for (i in it.documents) {
                if (i.id.toString().equals(Firebase.auth.currentUser!!.uid.toString())) {

                } else {
                    var user: UserModel? = i.toObject<UserModel>()!!
                    user?.let { tempList.add(it) }
                }
            }
            userList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }

        binding.searchButton.setOnClickListener {

            var text = binding.searchView.text.toString()
            Firebase.firestore.collection(USER).whereEqualTo("name", text).get()
                .addOnSuccessListener {
                    Firebase.firestore.collection(USER).get().addOnSuccessListener {
                        var tempList = ArrayList<UserModel>()
                        userList.clear()
                        if (it.isEmpty) {

                        } else {
                            for (i in it.documents) {
                                if (i.id.toString()
                                        .equals(Firebase.auth.currentUser!!.uid.toString())
                                ) {

                                } else {
                                    var user: UserModel? = i.toObject<UserModel>()!!
                                    user?.let { tempList.add(it) }
                                }
                            }
                            userList.addAll(tempList)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }

        }

        return binding.root
    }

    companion object {
    }
}