package com.example.secondhandmarket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.secondhandmarket.databinding.ItemBinding
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class ItemAdapter(private var itemList: List<ItemModel>): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }
    fun updateList(newList: List<ItemModel>) {
        itemList = newList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(clickListener: onItemClickListener) {
        mListener = clickListener
    }

    inner class ViewHolder(var binding : ItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]

        val storageReference = FirebaseStorage.getInstance().reference
        val imageReference = storageReference.child("image/item1.jpg")

        imageReference.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get()
                .load(uri)
                .into(holder.binding.itemImg)
        }.addOnFailureListener { e ->

        }

        holder.binding.itemTitle.text = currentItem.title

        //리사이클러 뷰의 아이템 클릭리스너
        holder.binding.root.setOnClickListener {
            mListener?.onItemClick(position)
        }

        // Check the status and set the appropriate text
        val statusText = if (currentItem.status == "판매 중") {
            "판매 중"
        } else {
            "판매 완료"
        }

        holder.binding.itemStatus.text = statusText
        holder.binding.itemPrice.text = currentItem.price
    }
}




