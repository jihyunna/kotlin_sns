package com.example.snsproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.snsproject.databinding.ActivityRegisterBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
        }
        binding.signup.setOnClickListener {
            val userEmail = binding.username.text.toString()
            val password = binding.password.text.toString()
            doSignup(userEmail, password)
        }
    }

    private fun doSignup(userEmail:String, password:String){

        Firebase.auth.createUserWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener(this) { // it: Task<AuthResult!>
                if (it.isSuccessful) {
                    startActivity(
                        Intent(this, LoginActivity::class.java)
                    )
                    finish()
                } else {
                    Log.w("RegisterActivity", "signUpWithEmail", it.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}