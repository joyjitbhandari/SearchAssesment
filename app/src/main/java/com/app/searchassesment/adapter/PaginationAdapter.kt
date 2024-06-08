package com.app.searchassesment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.searchassesment.databinding.RecyclerItemBinding
import com.app.searchassesment.model.Search
import com.bumptech.glide.Glide

class PaginationAdapter(
    private val list: List<Search>,
    private var clickListener:() -> Unit, ): RecyclerView.Adapter<PaginationAdapter.ViewHolder>() {

    class ViewHolder(private val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(search: Search) {
            Glide.with(binding.root.context).load(search.Poster).into(binding.movieImage)
            binding.movieName.text = search.Title
            binding.movieYear.text = search.Year
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position == list.size - 1){
            clickListener()
        }
        holder.bind(list[position])
    }
}