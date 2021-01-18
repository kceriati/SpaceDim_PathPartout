package com.example.spacedimvisuel.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL =
    "https://spacedim.async-agency.com/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface SpaceDimApiService {
    @GET("users")
    suspend fun getPlayers():
            List<Player>

    @GET("user/find/{name}")
    suspend fun findPlayer(@Path("name") userName: String):
            Player
}

object SpaceDimApi {
    val retrofitService : SpaceDimApiService by lazy {
        retrofit.create(SpaceDimApiService::class.java) }
}