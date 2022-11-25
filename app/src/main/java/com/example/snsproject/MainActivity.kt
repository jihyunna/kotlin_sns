package com.example.snsproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.snsproject.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Firebase.auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java))
            finish()
        }

        //binding.textUID.text = Firebase.auth.currentUser?.uid ?: "No User"

        binding.signout.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(
                Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.storage -> startActivity(
                Intent(this, StorageActivity::class.java))
            R.id.remote_config -> startActivity(
                Intent(this, RemoteConfigActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}