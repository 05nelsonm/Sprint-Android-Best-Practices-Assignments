package com.lambdaschool.rx2javagplive

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ForexService {

    @GET("{currency}")
    fun getRatesFor(@Path("currency") currency: String): Single<Rates>

    companion object {
        const val BASE_URL = "https://api.exchangerate-api.com/v4/latest/"
    }
}