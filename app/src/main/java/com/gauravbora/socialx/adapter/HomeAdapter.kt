package com.gauravbora.socialx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.gauravbora.socialx.Modal.NewsData
import com.gauravbora.socialx.R

class HomeAdapter : RecyclerView.Adapter<HomeViewHolder>() {

    private var items = ArrayList<NewsData>()

    fun setfilteredlist(filteredlist : ArrayList<NewsData>){
        this.items = filteredlist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.news_item,parent,false)
        return HomeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentItem = items[position]
        holder.publishedAt.text = currentItem.publishedAt
        holder.source.text = currentItem.source
        holder.title.text = currentItem.title
        holder.desc.text = currentItem.desc
        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.newsimage)
    }
    fun updatedNews(updatedNewsData: ArrayList<NewsData>){
        items.clear()
        items.addAll(updatedNewsData)

        notifyDataSetChanged()
    }
}

class HomeViewHolder(itemView: View) : ViewHolder(itemView) {

    val publishedAt : TextView = itemView.findViewById(R.id.published_at)
    val source : TextView = itemView.findViewById(R.id.source)
    val title : TextView = itemView.findViewById(R.id.title)
    val desc : TextView = itemView.findViewById(R.id.desc)
    val newsimage : ImageView = itemView.findViewById(R.id.image_view)
}