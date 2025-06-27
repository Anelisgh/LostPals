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

// activitatea pentru login
class LoginActivity : AppCompatActivity() {
    // view binding pentru a accesa elementele din layout
    private lateinit var binding: ActivityLoginBinding
    // viewmodel care gestioneaza logica de login
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // initializare sessionmanager
        // pentru a verificare daca utilizatorul este deja logat
        val sessionManager = SessionManager(this)
        super.onCreate(savedInstanceState)

        // daca userul este logat => trimis in MainActivity
        if (sessionManager.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish() // inchidere ecran de login
            return
        }

        // initializare binding pentru layout-ul de activitati
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // obtine instanta viewmodel-ului pentru autentificare
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        // listener pentru butonul login
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            // verifica daca toate campurile sunt completate
            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // trimite datele pentru login catre viewmodel
            authViewModel.loginUser(LoginRequest(username, password))
        }

        // cand linkul register este apasat => ecran de inregistrare
        binding.tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        authViewModel.loginStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val user = resource.data
                    Toast.makeText(this, "Welcome back, ${user?.username}!", Toast.LENGTH_SHORT)
                        .show()

                        // salvam datele utilizatorului in sesiune
                        user?.let {
                            sessionManager.saveUserId(it.id)
                            sessionManager.saveUsername(it.username)
                            sessionManager.setLoggedIn(true)
                        }

                    // navigam catre activitatea principala
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is Resource.Error -> {
                    // afisam un mesaj de eroare daca loginul a esuat
                    Toast.makeText(this, "Login failed: ${resource.message}", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}