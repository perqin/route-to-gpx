package com.perqin.routetogpx.views.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baidu.mapapi.search.core.PoiInfo
import com.perqin.routetogpx.databinding.SearchPoiItemBinding

class SearchPoiRecyclerAdapter : RecyclerView.Adapter<SearchPoiRecyclerAdapter.ViewHolder>() {
    var dataSet = listOf<PoiInfo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onItemClickListener: ((data: PoiInfo, index: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        SearchPoiItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val poi = dataSet[position]
        holder.binding.nameTextView.text = poi.name
        holder.binding.addressTextView.text = poi.address
        holder.binding.root.setOnClickListener {
            onItemClickListener?.invoke(poi, position)
        }
    }

    class ViewHolder(val binding: SearchPoiItemBinding) : RecyclerView.ViewHolder(binding.root)
}
