package com.example.zimozideliveryapp.repositories

import android.content.Context
import com.example.zimozideliveryapp.data.local.MealsDatabase
import com.example.zimozideliveryapp.data.pojo.Meal
import com.example.zimozideliveryapp.data.pojo.MealsResponse
import com.example.zimozideliveryapp.data.remote.WebServices
import com.example.zimozideliveryapp.utils.MEALS_JSON_NAME
import com.example.zimozideliveryapp.utils.getJsonDataFromAsset
import com.google.gson.Gson


class MealsRepo(api: WebServices, private val db: MealsDatabase) {
    private fun getAllMeals(context: Context): MealsResponse? {
        val jsonText = getJsonDataFromAsset(MEALS_JSON_NAME, context) ?: return null
        val gson = Gson()
        return gson.fromJson(jsonText, MealsResponse::class.java)
    }

    fun getMealsByCategoryName(name: String,context: Context): MealsResponse? {
        val mealsResponse = getAllMeals(context) ?: return null
        val list: List<Meal> = mealsResponse.meals.filter { item -> item.category == name }
        return MealsResponse(list)
    }

    fun insertMealToCart(meal:Meal){
        db.mealsDao().insertMeal(meal)
    }

    fun getAllMealsInCart() = db.mealsDao().retrieveAllMeals()

    fun deleteMealFromCart(meal:Meal){
        db.mealsDao().deleteMeal(meal)
    }

}