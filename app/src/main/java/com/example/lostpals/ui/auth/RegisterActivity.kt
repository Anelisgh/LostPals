package com.example.lostpals.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lostpals.databinding.ActivityRegisterBinding
import com.example.lostpals.util.Resource
import com.example.lostpals.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {
    // viewbinding pentru a avea acces la componentele din activity_register
    private lateinit var binding: ActivityRegisterBinding
    //viewmodel
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // legam XML-ul de activity
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        // listener pentru butonul sign up
        binding.btnSignUp.setOnClickListener {
            // colecteaza inputurile cand butonul este apasat
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            // verifica daca toate campurile sunt completate
            if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // validare daca carola este identica
            if (password != confirmPassword) {
                binding.etConfirmPassword.error = "Passwords don't match"
                return@setOnClickListener
            }

            // inregistrare utilizator
            authViewModel.registerUser(username, email, password)
        }

        // link care trimite utilizatorul de pe pagina de register la pagina de login
        binding.tvLoginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // inchidere register
        }

        authViewModel.registrationStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    // inregistrare reusita
                    Toast.makeText(
                        this,
                        "Registration successful! Please log in.",
                        Toast.LENGTH_LONG
                    ).show()
                    // navigare la pagina de login
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }

                is Resource.Error -> {
                    // afisare mesaj de eroare
                    Toast.makeText(this, "Error: ${resource.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}