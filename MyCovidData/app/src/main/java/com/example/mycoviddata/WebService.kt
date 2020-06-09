package com.example.mycoviddata

import retrofit2.Call
import retrofit2.http.GET

interface WebService {
    @GET("summary")
    fun summary(): Call<GlobalSummary>
}