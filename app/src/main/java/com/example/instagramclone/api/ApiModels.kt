package com.example.instagramclone.api

import com.google.gson.annotations.SerializedName

data class Post(
    val id: Int,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("image_url_type") val imageUrlType: String,
    val caption: String,
    val timestamp: String,
    val user: PostUser,
    val comments: List<Comment>
)

data class PostUser(
    val username: String
)

data class Comment(
    val text: String,
    val username: String,
    val timestamp: String
)

data class UserLoginResponse(
    @SerializedName("user_id") val userId: Int,
    val username: String,
    @SerializedName("access_token") val access_token: String,
    @SerializedName("token_type") val tokenType: String
)
