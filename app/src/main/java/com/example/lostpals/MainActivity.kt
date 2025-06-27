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
import android.widget.Button

class MainActivity : AppCompatActivity() {

    // manager pentru login/logout
    private lateinit var sessionManager: SessionManager
    // date + logica postarii
    private lateinit var postViewModel: PostViewModel
    // acces la datele postarilor
    private lateinit var postRepository: PostRepository
    // controleaza schimbarea intre ecrane
    private lateinit var navController: NavController
    // buton pentru demo http
    private lateinit var btnHttpDemo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // verificam daca userul este logat
        sessionManager = SessionManager(this)

        // daca userul nu e logat, du-te la login
        if (!sessionManager.isLoggedIn()) {
            redirectToLogin()
            return // optim executia
        }

        // incarcam layout-ul principal
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        btnHttpDemo = findViewById(R.id.btnOpenHttpDemo)

        // ajusteaza marginile ecranului
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, 0)
            insets
        }

        // seteaza sistemul de navigare
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.navController

        // bara de meniu
        val toolbarRoot = findViewById<View>(R.id.menu)

        // asculta cand se schimba ecranul
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // ascunde meniul pe ecranul de chat
            toolbarRoot.visibility = if (destination.id == R.id.chatFragment) View.GONE else View.VISIBLE

            // arata butonul http doar pe ecranul principal
            btnHttpDemo.visibility = if (destination.id == R.id.homeFragment) View.VISIBLE else View.GONE
        }

        // navigheaza la demo http
        btnHttpDemo.setOnClickListener {
            navController.navigate(R.id.httpDemoFragment)
        }

        // initializeaza baza de date si view model
        val db = AppDatabase.getDatabase(this)
        postRepository = PostRepository(db.postDao(), db.userDao())
        postViewModel = ViewModelProvider(this, PostViewModelFactory(postRepository))[PostViewModel::class.java]

        // buton pentru creare postare noua
        findViewById<ImageView>(R.id.createPostIcon).setOnClickListener {
            navController.navigate(R.id.createPostFragment)
        }

        // buton pentru intoarcere la ecranul principal
        findViewById<TextView>(R.id.appName).setOnClickListener {
            navController.popBackStack(R.id.homeFragment, false)
        }

        // buton pentru profilul utilizatorului
        findViewById<ImageView>(R.id.accountIcon).setOnClickListener {
            navController.navigate(R.id.profileFragment)
        }

        // buton pentru mesaje
        findViewById<ImageView>(R.id.Inbox).setOnClickListener {
            navController.navigate(R.id.inboxFragment)
        }
    }

    // afiseaza popup-ul de filtrare
    internal fun showFilterDialog() {
        val binding = DialogFilterBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this).setView(binding.root).create()

        // liste pentru selectie locatii
        val locationDisplayNames = listOf("All categories") + Location.entries.map { it.displayName }
        binding.spinnerLocation.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, locationDisplayNames
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // liste pentru selectie tipuri obiecte
        val objectTypeDisplayNames = listOf("All categories") + ObjectType.entries.map { it.displayName }
        binding.spinnerObjectType.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, objectTypeDisplayNames
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // preincarca filtrele existente
        postViewModel.filter.value?.let { currentFilter ->
            currentFilter.location?.let {
                binding.spinnerLocation.setSelection(locationDisplayNames.indexOf(it.displayName).coerceAtLeast(0))
            }
            currentFilter.objectType?.let {
                binding.spinnerObjectType.setSelection(objectTypeDisplayNames.indexOf(it.displayName).coerceAtLeast(0))
            }
        }

        // buton apply -> aplica filtrele selectate
        binding.btnApply.setOnClickListener {
            val selectedLocation = binding.spinnerLocation.selectedItemPosition.takeIf { it > 0 }
                ?.let { Location.entries[it - 1] }

            val selectedObjectType = binding.spinnerObjectType.selectedItemPosition.takeIf { it > 0 }
                ?.let { ObjectType.entries[it - 1] }

            postViewModel.setFilter(
                if (selectedLocation == null && selectedObjectType == null) null
                else PostFilterDto(selectedLocation, selectedObjectType)
            )
            dialog.dismiss()
        }

        // buton clear -> reseteaza filtrele
        binding.btnClear.setOnClickListener {
            postViewModel.setFilter(null)
            dialog.dismiss()
        }

        dialog.show()
    }

    // redirectioneaza catre login
    private fun redirectToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}