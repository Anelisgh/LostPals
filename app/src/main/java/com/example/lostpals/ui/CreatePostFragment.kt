package com.example.lostpals.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.lostpals.data.dto.PostDto
import com.example.lostpals.data.entity.Location
import com.example.lostpals.data.entity.ObjectType
import com.example.lostpals.databinding.FragmentCreatePostBinding
import com.example.lostpals.util.SessionManager
import com.example.lostpals.viewmodel.PostViewModel
import com.example.lostpals.util.Resource

class CreatePostFragment : Fragment() {
    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private lateinit var postViewModel: PostViewModel
    private var selectedImageUri: Uri? = null

    // inregistreaza activitatea pentru a alege o poza
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it  // salvam uri-ul
            binding.ivPreview.setImageURI(it)  // punem poza in preview
            binding.ivPreview.visibility = View.VISIBLE  // facem preview-ul vizibil
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return binding.root  // returnam view-ul fragmentului
    }

    // dupa ce view-ul e gata
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())  // initializam sesiunea
        postViewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]  // luam viewmodel

        // ajusteaza marginile dupa inaltimea barelor de sistem
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, 0, bars.right, bars.bottom)  // aplica padding
            insets
        }

        setupSpinners()  // pregatim dropdown-urile
        setupListeners()  // setam ascultatorii de click
        observeCreatePostResult()  // vedem daca postarea a fost salvata cu succes sau nu
    }

    // e atent al schimbari in rezultat (success sau error)
    private fun observeCreatePostResult() {
        postViewModel.createPostResult.observe(viewLifecycleOwner) { resource ->
            if (resource == null) return@observe

            when (resource) {
                is Resource.Success -> {
                    Toast.makeText(requireContext(), "Post created successfully!", Toast.LENGTH_SHORT).show()  // confirmare
                    resetForm()  // golim formularul
                    postViewModel.clearCreatePostResult()  // curatam starea
                    findNavController().popBackStack()  // ne intoarcem
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Error: ${resource.message}", Toast.LENGTH_SHORT).show()  // afisam eroare
                    postViewModel.clearCreatePostResult()  // curatam starea
                }
            }
        }
    }

    // seteaza optiunile din spinners pentru locatie si tip obiect
    private fun setupSpinners() {
        // spinner locatie
        val locations = listOf("Select location") + Location.entries.map { it.displayName }
        binding.spinnerLocation.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, locations
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        // spinner de tip obiect
        val types = ObjectType.entries.map { it.displayName }
        binding.spinnerObjectType.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, types
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
    }

    // seteaza click listeners pentru butoane
    private fun setupListeners() {
        binding.btnUploadPhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")  // deschide galerie
        }
        binding.btnCreatePost.setOnClickListener {
            createNewPost()  // porneste crearea postarii
        }
    }

    // colecteaza datele din formular si trimite postarea
    private fun createNewPost() {
        val title = binding.etTitle.text.toString().trim()  // titlul
        val desc = binding.etDescription.text.toString().trim()  // descrierea

        // validare
        if (title.isBlank() || desc.isBlank()) {
            Toast.makeText(requireContext(), "Title and description are required", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.spinnerLocation.selectedItemPosition == 0) {
            Toast.makeText(requireContext(), "Please select a location", Toast.LENGTH_SHORT).show()
            return
        }

        // obtinem obiectele selectate
        val location = Location.entries[binding.spinnerLocation.selectedItemPosition - 1]
        val objType = ObjectType.entries[binding.spinnerObjectType.selectedItemPosition]

        // convertim textul reward-ului in double
        val rewardText = binding.etReward.text.toString().trim()
        val reward = rewardText.replace(',', '.').toDoubleOrNull()  // convertim la double
        val userId = sessionManager.getUserId()  // id-ul user-ului curent

        // construim dto-ul cu toate datele
        val postDto = PostDto(
            ownerId = userId,
            title = title,
            description = desc,
            location = location,
            objectType = objType,
            photoUri = selectedImageUri?.toString(),
            reward = reward
        )

        postViewModel.createPost(postDto)  // trimitem catre viewmodel
    }

    // reseteaza tot formularul la starea initiala
    private fun resetForm() {
        // curatam campurile
        binding.etTitle.text?.clear()
        binding.etDescription.text?.clear()
        binding.etReward.text?.clear()
        binding.ivPreview.visibility = View.GONE  // ascundem preview
        selectedImageUri = null
        binding.spinnerLocation.setSelection(0)  // reset spinner locatie
        binding.spinnerObjectType.setSelection(0)  // reset spinner tip obiect
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // evitam memory leak
    }
}