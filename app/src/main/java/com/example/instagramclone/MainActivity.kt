package com.example.instagramclone

import android.content.Context
import android.content.Intent
import android.hardware.input.InputManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagramclone.databinding.ActivityMainBinding
import com.example.instagramclone.databinding.ItemPostBinding
import androidx.core.app.ActivityCompat.startActivityForResult
import java.io.FileNotFoundException
import java.io.InputStream
import android.widget.Toast

class MainActivity : AppCompatActivity(), AuthCallback {

    private val vm: MainVewModel by viewModels()
    private val adapter = PostListAdapter(arrayListOf())
    private lateinit var binding: ActivityMainBinding
    private  val RESULT_LOAD_IMG = 1
    private  var imageStream: InputStream? = null

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
        binding.loginButton.setOnClickListener{
            val dialog = LoginDialog(this)
            dialog.show(supportFragmentManager, "LoginDialog")
        }

        binding.signupButton.setOnClickListener {
            val dialog = SignupDialog(this)
            dialog.show(supportFragmentManager, "SignupDialog")
        }

        binding.logoutButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes") {dialog, which -> vm.onLogout()}
                .setNegativeButton("Cansel") {dialog, which -> dialog.dismiss()}
                .show()
        }

        binding.selectImageButton.setOnClickListener{
            val photoPockerIntent = Intent(Intent.ACTION_PICK)
            photoPockerIntent.type = "image/*"
            startActivityForResult(this, photoPockerIntent, RESULT_LOAD_IMG, null)
        }

        binding.performUpload.setOnClickListener{
            if (imageStream == null) {
                showToast("Please select image")
            } else {
                vm.onPostUpload(imageStream!!, binding.caption.text.toString())
                imageStream = null
                binding.caption.setText("")
            }

            closeKeyboard()
        }

    }

    private fun closeKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.performUpload.windowToken, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_LOAD_IMG) {
            try {
                data?.data?.let{
                    imageStream = contentResolver.openInputStream(it)
                    showToast("Image selected")
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                showToast("Something went wrong")
            }
        }
    }


    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun setupObservables() {
        vm.posts.observe(this, Observer { posts -> adapter.updatePosts(posts) })
        vm.loggedIn.observe(this, {loggedIn ->
            binding.loginLayout.visibility = if (loggedIn) View.GONE else View.VISIBLE
            binding.logoutLayout.visibility = if (loggedIn) View.VISIBLE else View.GONE
            binding.uploadUnavailableMessage.visibility = if (loggedIn) View.GONE else View.VISIBLE
            binding.uploadLayout.visibility = if (loggedIn) View.VISIBLE else View.GONE
            adapter.onAuth(loggedIn)
        })
    }

    override fun onLogin(username: String, password: String) {
        vm.onLogin(username, password)
    }

    override fun onSignup(username: String, email: String, password: String) {
        vm.onSignup(username, email, password)
    }

}