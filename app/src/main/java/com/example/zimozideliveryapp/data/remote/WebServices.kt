package com.example.zimozideliveryapp.data.remote

import com.example.zimozideliveryapp.data.pojo.MealsResponse
import retrofit2.Response
import retrofit2.http.GET


interface WebServices {
    companion object {
        const val BASE_URL = "https://developer.android.com"
    }

    @GET("airlines")
    suspend fun getAllAirlines(): Response<MealsResponse>

}