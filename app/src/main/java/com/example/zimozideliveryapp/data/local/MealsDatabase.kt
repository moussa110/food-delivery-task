package com.example.zimozideliveryapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.zimozideliveryapp.data.pojo.Meal
import com.example.zimozideliveryapp.data.pojo.MealsResponse

@Database(entities = [Meal::class],version = 1)
abstract class MealsDatabase : RoomDatabase(){
    abstract fun mealsDao(): MealsDao
}