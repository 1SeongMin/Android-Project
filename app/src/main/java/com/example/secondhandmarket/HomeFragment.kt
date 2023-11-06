package com.example.secondhandmarket

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.secondhandmarket.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var storageRef: DatabaseReference
    private lateinit var firebaseFireStore : FirebaseFirestore
    private lateinit var adapter : ItemAdapter
    private lateinit var itemList: MutableList<ItemModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        getItemData()

    }

    private fun init(){
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        itemList = mutableListOf()
        adapter = ItemAdapter(itemList)
        binding.recyclerView.adapter = adapter
    }

    fun getItemData() {
        val itemRecyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)
        itemRecyclerView?.visibility = View.GONE

        storageRef = FirebaseDatabase.getInstance().reference.child("Items")
        firebaseFireStore = FirebaseFirestore.getInstance()

        storageRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                itemList.clear()

                if (snapshot.exists()) {
                    for (itemSnap in snapshot.children) {
                        val itemData = itemSnap.getValue(ItemModel::class.java)
                        itemList.add(itemData!!)
                    }
                    val mAdapter = ItemAdapter(itemList)
                    itemRecyclerView?.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : ItemAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(requireContext(), MainActivity::class.java)

                            intent.putExtra("itemImg", itemList[position].imgUri)
                            intent.putExtra("itemTitle", itemList[position].title)
                            intent.putExtra("itemStatus", itemList[position].status)
                            intent.putExtra("itemPrice", itemList[position].price)

                            startActivity(intent)
                        }
                    })
                    binding.recyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}