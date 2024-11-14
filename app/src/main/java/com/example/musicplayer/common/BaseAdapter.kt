package com.example.musicplayer.common

import android.view.Display.Mode
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


abstract class BaseAdapter<Model,VB : ViewBinding>(val inflate : (LayoutInflater, ViewGroup?, Boolean)-> VB) : RecyclerView.Adapter<BaseAdapter.BaseViewHolder<VB>>() {
    class BaseViewHolder<VB :ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
    var itemData : List<Model> = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        return BaseViewHolder(inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return itemData.size
    }

    fun updateAdapter(newData : List<Model>){
        itemData = newData
        notifyDataSetChanged()
    }

    abstract override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int)

}
