package com.example.instagramclone

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.instagramclone.api.InstagramApiService.BASE_URL
import com.example.instagramclone.api.Post
import com.example.instagramclone.databinding.ItemPostBinding


class PostListAdapter(private var posts: List<Post>):
    RecyclerView.Adapter<PostListAdapter.PostViewHolder>() {

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): PostViewHolder {

        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount() = posts.size

    class PostViewHolder(
        val binding: ItemPostBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.postUsername.text = post.user.username
            binding.captionUsername.text = post.user.username + ":"
            binding.captionText.text = post.caption

            val imgContext = binding.postImage.context
            val circularProgressDrawable = CircularProgressDrawable(imgContext)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            val imageUrl = if (post.imageUrl == "absolute")
                post.imageUrl else BASE_URL + post.imageUrl

            Glide.with(imgContext)
                .load(imageUrl)
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(binding.postImage)

            binding.commentLayout.removeAllViews()
            for (comment in post.comments) {
                val commentView = TextView(binding.captionLayout.context)
                commentView.text = "${comment.username} : ${comment.text}"
                binding.commentLayout.addView(commentView)

            }
        }
    }

}