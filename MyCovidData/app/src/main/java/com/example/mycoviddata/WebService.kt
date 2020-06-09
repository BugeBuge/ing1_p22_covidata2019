package com.example.mycoviddata

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WebService {
    @GET("summary")
    fun summary(): Call<GlobalSummary>

    @GET("total/dayone/country/{country}/status/{status}")
    fun evolution(@Path("country") country: String, @Path("status") status: String):
            Call<List<DatedSituation>>

    @GET("countries")
    fun countries(): Call<List<CountryName>>
}