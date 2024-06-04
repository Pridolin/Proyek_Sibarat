package com.example.sibarat

import android.content.ClipData.Item
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sibarat.data.Alphabet
import com.example.sibarat.databinding.ItemDictionaryBinding

class DictionaryAdapter(private val listAlphabet: ArrayList<Alphabet>): RecyclerView.Adapter<DictionaryAdapter.MyViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class MyViewHolder(var binding: ItemDictionaryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemDictionaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listAlphabet.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val (alphabet, photo) = listAlphabet[position]
        holder.binding.itemPhoto.setImageResource(photo)
        holder.binding.tvAlphabet.text = alphabet
        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailDictionaryActivity::class.java)
            intentDetail.putExtra("key", listAlphabet[holder.adapterPosition])
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Alphabet)
    }
}