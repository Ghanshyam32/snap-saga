package com.ghanshyam.snapsaga.fragments

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghanshyam.snapsaga.adapters.SearchAdapter
import com.ghanshyam.snapsaga.databinding.FragmentSearchBinding
import com.ghanshyam.snapsaga.models.UserModel
import com.ghanshyam.snapsaga.utils.USER
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SearchAdapter
    private var userList = ArrayList<UserModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        setupRecyclerView()

        // Load initial data
        loadUserData()

        // Set click listener for the search button
        binding.searchButton.setOnClickListener {
            performSearch()
        }

        // Set EditorActionListener for the EditText
        binding.searchView.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                performSearch()
                true
            } else {
                false
            }
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.searchRes.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(requireContext(), userList)
        binding.searchRes.adapter = adapter
    }

    private fun loadUserData() {
        Firebase.firestore.collection(USER).get().addOnSuccessListener {
            val tempList = ArrayList<UserModel>()
            userList.clear()
            for (i in it.documents) {
                if (i.id != Firebase.auth.currentUser!!.uid) {
                    val user: UserModel? = i.toObject(UserModel::class.java)
                    user?.let { tempList.add(it) }
                }
            }
            userList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to load user data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performSearch() {
        val searchText = binding.searchView.text.toString().trim().toLowerCase()
        if (searchText.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a search term", Toast.LENGTH_SHORT).show()
            return
        }

        // Create the end text for the search query
        val endText = searchText + '\uf8ff'

        Firebase.firestore.collection(USER)
            .whereGreaterThanOrEqualTo("name", searchText)
            .whereLessThanOrEqualTo("name", endText)
            .get()
            .addOnSuccessListener { result ->
                val tempList = ArrayList<UserModel>()
                userList.clear()
                if (result.isEmpty) {
                    Toast.makeText(requireContext(), "No results found", Toast.LENGTH_SHORT).show()
                } else {
                    for (i in result.documents) {
                        if (i.id != Firebase.auth.currentUser!!.uid) {
                            val user: UserModel? = i.toObject(UserModel::class.java)
                            user?.let { tempList.add(it) }
                        }
                    }
                    userList.addAll(tempList)
                    adapter.notifyDataSetChanged()
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Search failed", Toast.LENGTH_SHORT).show()
            }
    }
}
