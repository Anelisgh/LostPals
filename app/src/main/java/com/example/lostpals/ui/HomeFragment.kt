package com.example.lostpals.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lostpals.MainActivity
import com.example.lostpals.data.dto.PostFilterDto
import com.example.lostpals.databinding.FragmentHomeBinding
import com.example.lostpals.ui.adapters.PostAdapter
import com.example.lostpals.viewmodel.PostViewModel
import com.example.lostpals.util.Resource
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var postViewModel: PostViewModel
    private lateinit var postAdapter: PostAdapter
    private lateinit var filterFab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterFab = binding.filterIcon
        filterFab.setOnClickListener {
            (activity as? MainActivity)?.showFilterDialog()
        }

        binding.recyclerViewPosts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && filterFab.isShown) {
                    filterFab.hide()
                } else if (dy < 0 && !filterFab.isShown) {
                    filterFab.show()
                }
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        postViewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]
        postAdapter = PostAdapter(emptyList())

        binding.recyclerViewPosts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postAdapter
        }

        postViewModel.posts.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val posts = resource.data ?: emptyList()
                    if (posts.isEmpty()) {
                        binding.emptyState.visibility = View.VISIBLE
                        binding.recyclerViewPosts.visibility = View.GONE
                    } else {
                        binding.emptyState.visibility = View.GONE
                        binding.recyclerViewPosts.visibility = View.VISIBLE
                        postAdapter.updatePosts(posts)
                    }
                }
                is Resource.Error -> {
                    binding.emptyState.visibility = View.VISIBLE
                    binding.recyclerViewPosts.visibility = View.GONE
                    Toast.makeText(context, "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
// este atent la modificarile filtrului si actualizeaza fab-ul in consecinta
        postViewModel.filter.observe(viewLifecycleOwner) { filter ->
            updateFilterFabState(filter)
        }
        postViewModel.filter.value?.let {
            updateFilterFabState(it)
        }
        if (postViewModel.posts.value == null) {
            postViewModel.loadPosts(postViewModel.filter.value)
        }
    }

    private fun updateFilterFabState(filter: PostFilterDto?) {
        val hasFilters = filter != null &&
                (filter.location != null || filter.objectType != null)
        filterFab.isSelected = hasFilters
    }

    override fun onResume() {
        super.onResume()
        filterFab.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}