package com.example.instagramclone.api

import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Field

interface InstagramApi {
    @GET("post/all")
    fun getAllPosts(): Call<List<Post>>

    @FormUrlEncoded
    @POST("login")
    fun login (@Field("username") username: String,
               @Field("password") password: String): Call<UserLoginResponse>
}
