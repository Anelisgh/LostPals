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

    // se apeleaza cand utilizatorul selecteaza o imagine din galerie si o afiseaza in preview
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.ivPreview.setImageURI(it)
            binding.ivPreview.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        postViewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]
        postViewModel.clearCreatePostResult()
        // adaugam un ascultator care va fi notificat de fiecare data cand se schimba dimensiunile spatiului ocupat de ferestrele de sistem (adica bare de status, nativatie)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        setupSpinners()
        setupListeners()
        observeCreatePostResult()
    }

    private fun observeCreatePostResult() {
        postViewModel.createPostResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "Post created successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    resetForm()
                    postViewModel.clearCreatePostResult()
                    findNavController().popBackStack()
                }
                is Resource.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Error: ${resource.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    postViewModel.clearCreatePostResult()
                }
            }
        }
    }

// configurarea dropdown-urilor
    private fun setupSpinners() {
        // location
        val locationDisplayNames = Location.entries.map { it.displayName }
        binding.spinnerLocation.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, locationDisplayNames
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        // objectType
        val objectTypeDisplayNames = ObjectType.entries.map { it.displayName }
        binding.spinnerObjectType.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, objectTypeDisplayNames
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }
// configurarea butoanelor
    private fun setupListeners() {
        // selectarea imaginii
        binding.btnUploadPhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
        // crearea postarii
        binding.btnCreatePost.setOnClickListener {
            createNewPost()
        }
    }
// crearea unei postari noi
    private fun createNewPost() {
    // preluam datele din formular
        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val rewardText = binding.etReward.text.toString().trim()
        val reward = rewardText.replace(',', '.').toDoubleOrNull()
    // verificam daca datele sunt valide
        if (title.isBlank() || description.isBlank()) {
            Toast.makeText(
                requireContext(), "Title and description are required", Toast.LENGTH_SHORT
            ).show()
            return
        }
    // preluam datele din dropdown-uri
        val location = Location.entries[binding.spinnerLocation.selectedItemPosition]
        val objectType = ObjectType.entries[binding.spinnerObjectType.selectedItemPosition]
        val userId = sessionManager.getUserId()
    // cream obiectul postDto
        val postDto = PostDto(
            ownerId = userId,
            title = title,
            description = description,
            location = location,
            objectType = objectType,
            photoUri = selectedImageUri?.toString(),
            reward = reward
        )
    // trimitem postarea la db
        postViewModel.createPost(postDto)
    }
// golim campurile
    private fun resetForm() {
        binding.etTitle.text?.clear()
        binding.etDescription.text?.clear()
        binding.etReward.text?.clear()
        binding.ivPreview.visibility = View.GONE
        selectedImageUri = null
        binding.spinnerLocation.setSelection(0)
        binding.spinnerObjectType.setSelection(0)
    }
// pentru evitarea memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
