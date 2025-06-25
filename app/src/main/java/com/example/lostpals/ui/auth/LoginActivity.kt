package com.example.lostpals.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lostpals.MainActivity
import com.example.lostpals.data.dto.LoginRequest
import com.example.lostpals.databinding.ActivityLoginBinding
import com.example.lostpals.util.Resource
import com.example.lostpals.util.SessionManager
import com.example.lostpals.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val sessionManager = SessionManager(this)
        super.onCreate(savedInstanceState)
        if (sessionManager.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authViewModel.loginUser(LoginRequest(username, password))
        }

        binding.tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Observe login status
        authViewModel.loginStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val user = resource.data
                    Toast.makeText(this, "Welcome back, ${user?.username}!", Toast.LENGTH_SHORT)
                        .show()

                    //val sessionManager = SessionManager(this)
                        user?.let {
                            sessionManager.saveUserId(it.id)
                            sessionManager.saveUsername(it.username)
                            sessionManager.setLoggedIn(true)
                        }

                    // Navigate to main activity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is Resource.Error -> {
                    Toast.makeText(this, "Login failed: ${resource.message}", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}