package com.example.spacedimvisuel.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
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
    suspend fun getUsers():
            List<User>

    @GET("user/find/{name}")
    suspend fun findUser(@Path("name") userName: String):
            User

    @POST("user/register")
    suspend fun createUser(@Body userPost: UserPost):
            Response<UserPost>
}

object SpaceDimApi {
    val retrofitService : SpaceDimApiService by lazy {
        retrofit.create(SpaceDimApiService::class.java) }
}