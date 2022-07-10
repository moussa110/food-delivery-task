package com.example.zimozideliveryapp.ui.homescreen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.zimozideliveryapp.R
import com.example.zimozideliveryapp.ui.homescreen.adapters.SalePagerAdapter.*
import com.example.zimozideliveryapp.databinding.ItemPagerBinding

class SalePagerAdapter() : RecyclerView.Adapter<ViewHolder>() {
    private var data: List<Int> = listOf()
    inner class ViewHolder(private val binding: ItemPagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(position: Int) {
            binding.image.setImageResource(data[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_pager,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindItem(position)

    override fun getItemCount() = data.size

    fun updateAllData(list: List<Int>){
        data = list
        notifyDataSetChanged()
    }
}