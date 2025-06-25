package com.example.lostpals

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.lostpals.data.database.AppDatabase
import com.example.lostpals.data.dto.PostFilterDto
import com.example.lostpals.data.entity.Location
import com.example.lostpals.data.entity.ObjectType
import com.example.lostpals.databinding.DialogFilterBinding
import com.example.lostpals.repository.PostRepository
import com.example.lostpals.ui.auth.LoginActivity
import com.example.lostpals.util.SessionManager
import com.example.lostpals.viewmodel.PostViewModel
import com.example.lostpals.viewmodel.PostViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var postViewModel: PostViewModel
    private lateinit var postRepository: PostRepository
    private lateinit var navController: NavController

    // se executa cand se deschide aplicatia
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
//        sessionManager.logout()
        // verificam daca user-ul e autentificat, daca nu, il redirectionam catre login
        if (!sessionManager.isLoggedIn()) {
            redirectToLogin()
            return
        }
        enableEdgeToEdge()
        // setam layout-ul
        setContentView(R.layout.activity_main)
        // adaugam un ascultator care va fi notificat de fiecare data cand se schimba dimensiunile spatiului ocupat de ferestrele de sistem (adica bare de status, nativatie) si se va seta padding-ul in consecinta
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        // initiam NavController-ul
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        // cream conexiunea la baza de date si cream repositories
        val database = AppDatabase.getDatabase(this)
        postRepository = PostRepository(
            database.postDao(), database.userDao()
        )
        // cream factory-ul pentru viewmodel si viewmodel-ul pentru postari
        val factory = PostViewModelFactory(postRepository)
        postViewModel = ViewModelProvider(this, factory)[PostViewModel::class.java]

        // butonul pentru crearea unei noi postari
        findViewById<ImageView>(R.id.createPostIcon).setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_createPostFragment)
        }

        // accesam pagina principala a aplicatiei prin apasarea logo-ului/numelui aplicatiei
        findViewById<TextView>(R.id.appName).setOnClickListener {
            navController.popBackStack(R.id.homeFragment, false)
        }

        // **navigare catre ProfileFragment la click pe accountIcon**
        findViewById<ImageView>(R.id.accountIcon).setOnClickListener {
            navController.navigate(R.id.profileFragment)
        }
    }

    // functia pentru afisarea dialogului de filtrare
    internal fun showFilterDialog() {
        // folosim binding pentru a accesa elementele din layout-ul dialog_filter
        val binding = DialogFilterBinding.inflate(layoutInflater)
        // folosim dialog pentru a crea popup-ul
        val dialog = AlertDialog.Builder(this).setView(binding.root).create()
        // populam spinner-ul cu valorile din location
        val locationDisplayNames =
            listOf("All categories") + Location.entries.map { it.displayName }
        val locationAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, locationDisplayNames
        )
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerLocation.adapter = locationAdapter
        // populam spinner-ul cu valorile din objectType
        val objectTypeDisplayNames =
            listOf("All categories") + ObjectType.entries.map { it.displayName }
        val objectTypeAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, objectTypeDisplayNames
        )
        objectTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerObjectType.adapter = objectTypeAdapter

        // daca exista deja un filtru aplicat il afisam pe el in spinner
        postViewModel.filter.value?.let { currentFilter ->
            currentFilter.location?.let { selectedLocation ->
                val position = locationDisplayNames.indexOf(selectedLocation.displayName)
                if (position >= 0) {
                    binding.spinnerLocation.setSelection(position)
                }
            }
            currentFilter.objectType?.let { selectedType ->
                val position = objectTypeDisplayNames.indexOf(selectedType.displayName)
                if (position >= 0) {
                    binding.spinnerObjectType.setSelection(position)
                }
            }
        }

        // butonul apply, daca nu are pozitia 0, adica "all categories", atunci se aplica filtrul respectiv
        binding.btnApply.setOnClickListener {
            val selectedLocation = if (binding.spinnerLocation.selectedItemPosition > 0) {
                val selectedName = binding.spinnerLocation.selectedItem.toString()
                Location.entries.find { it.displayName == selectedName }
            } else null

            val selectedObjectType = if (binding.spinnerObjectType.selectedItemPosition > 0) {
                val selectedName = binding.spinnerObjectType.selectedItem.toString()
                ObjectType.entries.find { it.displayName == selectedName }
            } else null

            if (selectedLocation == null && selectedObjectType == null) {
                postViewModel.setFilter(null)
            } else {
                postViewModel.setFilter(PostFilterDto(selectedLocation, selectedObjectType))
            }
            dialog.dismiss()
        }

        // butonul clear
        binding.btnClear.setOnClickListener {
            postViewModel.setFilter(null)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun redirectToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
