package com.example.zimozideliveryapp.ui.homescreen.adapters

import android.content.Context
import android.os.Handler
import android.os.Looper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zimozideliveryapp.R
import com.example.zimozideliveryapp.data.pojo.Meal
import com.example.zimozideliveryapp.databinding.ItemMealRvBinding


class MealsRecyclerAdapter(private val listener: (Meal) -> Unit) :
    ListAdapter<Meal, MealsRecyclerAdapter.ViewHolder>(DiffCallback) {
    private lateinit var context: Context

    companion object DiffCallback : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.id == newItem.id
        }
    }

    inner class ViewHolder(private val binding: ItemMealRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(position: Int) {
            binding.apply {
                val item = getItem(position)
                Glide.with(context).load(item.imageUrl)
                    .placeholder(R.drawable.mega_sale_background)
                    .into(foodIv)
                titleTv.text = item.title
                descriptionTv.text = item.details
                sizeTv.text = item.size
                addToCartBtn.text = "${item.price} USD"
                addToCartBtn.setOnClickListener {
                    addToCartBtn.apply {
                        background =
                            ContextCompat.getDrawable(context, R.drawable.green_curved_shpae)
                        text = context.resources.getText(R.string.added)
                        Handler(Looper.getMainLooper()).postDelayed({
                            background =
                                ContextCompat.getDrawable(context, R.drawable.black_curved_shpae)
                            text = "${item.price} USD"
                        }, 1000)
                    }
                    listener.invoke(item)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_meal_rv,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindItem(position)


}