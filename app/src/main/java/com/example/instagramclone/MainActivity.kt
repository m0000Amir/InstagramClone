package com.example.instagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagramclone.databinding.ActivityMainBinding
import com.example.instagramclone.databinding.ItemPostBinding

class MainActivity : AppCompatActivity() {

    private val vm: MainVewModel by viewModels()
    private val adapter = PostListAdapter(arrayListOf())
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_main)
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()
        setupUI()
        setupObservables()
    }

    fun setupUI() {
        binding.postsRV.adapter = adapter
        binding.postsRV.layoutManager = LinearLayoutManager(this)

    }

    fun setupObservables() {
        vm.posts.observe(this, Observer { posts -> adapter.updatePosts(posts) })
    }
}