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

        // Inițializare SessionManager
        sessionManager = SessionManager(requireContext())

        // Inițializare ViewModel
        postViewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]

        // Gestionare WindowInsets (adaptare la bara de sistem)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        // Inițializare componente UI
        setupSpinners()
        setupListeners()

        // Observare rezultat creare post
        observeCreatePostResult()
    }

    private fun observeCreatePostResult() {
        postViewModel.createPostResult.observe(viewLifecycleOwner) { resource ->
            // Adaugă această verificare pentru null
            if (resource == null) return@observe

            when (resource) {
                is Resource.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "Post created successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    resetForm()
                    postViewModel.clearCreatePostResult()
                    // Mută navigația după clear
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

    private fun setupSpinners() {
        // Location spinner with "Select location" as first item
        val locations = listOf("Select location") + Location.entries.map { it.displayName }
        binding.spinnerLocation.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            locations
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // ObjectType spinner
        val objectTypes = ObjectType.entries.map { it.displayName }
        binding.spinnerObjectType.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            objectTypes
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun setupListeners() {
        binding.btnUploadPhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnCreatePost.setOnClickListener {
            createNewPost()
        }
    }

    private fun createNewPost() {
        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        if (title.isBlank() || description.isBlank()) {
            Toast.makeText(
                requireContext(),
                "Title and description are required",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Verify location selection
        if (binding.spinnerLocation.selectedItemPosition == 0) {
            Toast.makeText(
                requireContext(),
                "Please select a location",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Get selected location (subtract 1 to account for the "Select location" item)
        val location = Location.entries[binding.spinnerLocation.selectedItemPosition - 1]
        val objectType = ObjectType.entries[binding.spinnerObjectType.selectedItemPosition]

        val rewardText = binding.etReward.text.toString().trim()
        val reward = rewardText.replace(',', '.').toDoubleOrNull()
        val userId = sessionManager.getUserId()

        val postDto = PostDto(
            ownerId = userId,
            title = title,
            description = description,
            location = location,
            objectType = objectType,
            photoUri = selectedImageUri?.toString(),
            reward = reward
        )

        postViewModel.createPost(postDto)
    }

    private fun resetForm() {
        binding.etTitle.text?.clear()
        binding.etDescription.text?.clear()
        binding.etReward.text?.clear()
        binding.ivPreview.visibility = View.GONE
        selectedImageUri = null
        binding.spinnerLocation.setSelection(0) // Reset to "Select location"
        binding.spinnerObjectType.setSelection(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}