package com.example.zimozideliveryapp.di

import android.app.Application
import androidx.room.Room
import com.example.zimozideliveryapp.data.local.MealsDatabase
import com.example.zimozideliveryapp.data.remote.WebServices
import com.example.zimozideliveryapp.repositories.MealsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(WebServices.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideWebServices(retrofit: Retrofit): WebServices =
        retrofit.create(WebServices::class.java)

    @Provides
    @Singleton
    fun provideDataBase(application : Application) : MealsDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            MealsDatabase::class.java,
            "MEALS-DB"
        )
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideMealRepository(
        db: MealsDatabase,
        api: WebServices
    ) = MealsRepo(api,db)


}