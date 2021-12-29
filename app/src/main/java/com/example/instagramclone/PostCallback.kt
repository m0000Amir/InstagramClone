package com.example.instagramclone

interface PostCallback {
    fun onDeletePost(postId: Int)
    fun onComment(text: String, postId: Int)
}