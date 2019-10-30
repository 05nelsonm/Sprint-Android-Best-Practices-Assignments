package com.lambdaschool.mortgagecalculator.retrofit

import com.google.gson.Gson
import com.lambdaschool.mortgagecalculator.model.RandomNumbers
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomNumberAPI {

    @GET("jsonI.php")
    fun getRandomNumber(@Query("length") length: Int, @Query("type") type: String)
            : Single<RandomNumbers>

    class Factory {
        companion object {
            private val BASE_URL = "http://qrng.anu.edu.au/API/"
            private val gson = Gson()
        }

        fun create(): RandomNumberAPI {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RandomNumberAPI::class.java)
        }
    }
}