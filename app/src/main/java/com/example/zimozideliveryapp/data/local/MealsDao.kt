package com.example.zimozideliveryapp.data.local

import androidx.room.*
import com.example.zimozideliveryapp.data.pojo.Meal


@Dao
interface MealsDao {
    @Query("SELECT * FROM Meal")
    fun retrieveAllMeals(): List<Meal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMeal(meal: Meal)

    @Delete
    fun deleteMeal(meal: Meal)
}