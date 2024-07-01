package com.ghanshyam.snapsaga.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghanshyam.snapsaga.R
import com.ghanshyam.snapsaga.adapters.FollowAdapter
import com.ghanshyam.snapsaga.adapters.PostAdapter
import com.ghanshyam.snapsaga.databinding.FragmentHomeBinding
import com.ghanshyam.snapsaga.models.PostModel
import com.ghanshyam.snapsaga.models.UserModel
import com.ghanshyam.snapsaga.utils.FOLLOW
import com.ghanshyam.snapsaga.utils.POST
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var postList = ArrayList<PostModel>()
    private lateinit var adapter: PostAdapter
    private var followList = ArrayList<UserModel>()
    private lateinit var followAdapter: FollowAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        adapter = PostAdapter(requireContext(), postList)
        binding.postsRecView.layoutManager = LinearLayoutManager(requireContext())
        binding.postsRecView.adapter = adapter
        followAdapter = FollowAdapter(requireContext(), followList)
        binding.followList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.followList.adapter = followAdapter
        setHasOptionsMenu(true)
        (requireContext() as AppCompatActivity).setSupportActionBar(binding.materialToolbar)

        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW).get()
            .addOnSuccessListener {
                var tempList = ArrayList<UserModel>()
                followList.clear()
                for (i in it.documents) {
                    var user: UserModel = i.toObject<UserModel>()!!
                    tempList.add(user)
                }
                followList.addAll(tempList)
                followAdapter.notifyDataSetChanged()
            }

        Firebase.firestore.collection(POST).get().addOnSuccessListener {
            val tempList = ArrayList<PostModel>()
            postList.clear()
            for (i in it.documents) {
                val post: PostModel = i.toObject(PostModel::class.java)!!
                tempList.add(post)
            }
            postList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
