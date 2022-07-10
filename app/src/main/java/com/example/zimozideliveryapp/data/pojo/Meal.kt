package com.example.zimozideliveryapp.data.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Meal(
    @PrimaryKey
    val id: Int,
    val category: String,
    val details: String,
    val imageUrl: String,
    val price: Int,
    val size: String,
    val title: String
)