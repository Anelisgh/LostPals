package com.example.lostpals.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Button
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lostpals.MainActivity
import com.example.lostpals.R
import com.example.lostpals.data.dto.PostDisplayDto
import com.example.lostpals.data.dto.PostFilterDto
import com.example.lostpals.databinding.FragmentHomeBinding
import com.example.lostpals.ui.adapters.PostAdapter
import com.example.lostpals.util.Resource
import com.example.lostpals.util.SessionManager
import com.example.lostpals.viewmodel.PostViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {
    // acces la elementele din fragment_home.xml
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // viewmodel care aduce lista de postari
    private lateinit var postViewModel: PostViewModel
    // adapter pentru afisarea postarilor
    private lateinit var postAdapter: PostAdapter
    // buton plutitor pentru deschiderea filtrului
    private lateinit var filterFab: FloatingActionButton

    // creeaza view-ul fragmentului
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // creeaza binding-ul si pregateste layout-ul (xml)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    // dupa ce view-ul e gata
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // navigare catre pagina http demo
        view.findViewById<Button>(R.id.btnHttpDemo).setOnClickListener {
            findNavController().navigate(R.id.action_home_to_http_demo)
        }

        filterFab = binding.filterIcon  // luam referinta la butonul de filter

        // deschide dialogul de filter din MainActivity
        filterFab.setOnClickListener {
            (activity as? MainActivity)?.showFilterDialog()
        }

        // arata sau ascunde fab cand scroll-uiesti
        binding.recyclerViewPosts.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0 && filterFab.isShown) filterFab.hide()  // scroll jos, ascunde
                    else if (dy < 0 && !filterFab.isShown) filterFab.show()  // scroll sus, arata
                }
            }
        )

        // ajusteaza paddingul dupa barele sistemului
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, 0, bars.right, bars.bottom)
            insets
        }

        // pregatim viewmodel si adapter
        postViewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]
        val currentUserId = SessionManager(requireContext()).getUserId()  // id-ul curent

        postAdapter = PostAdapter(
            emptyList(),
            currentUserId,
            object : PostAdapter.OnChatClickListener {
                override fun onChatClicked(post: PostDisplayDto) {
                    // nu avem chat la propria postare
                    if (post.ownerId == currentUserId) {
                        Toast.makeText(requireContext(), "you can't send messages to yourself", Toast.LENGTH_SHORT).show()
                        return
                    }
                    // navigam la chat-ul owner-ului postarii
                    val dir = HomeFragmentDirections.actionHomeFragmentToChatFragment(
                        otherId = post.ownerId,
                        partnerName = post.ownerUsername
                    )
                    findNavController().navigate(dir)
                }
            }
        )

        // setam recyclerview
        binding.recyclerViewPosts.apply {
            layoutManager = LinearLayoutManager(context)  // lista verticala
            adapter = postAdapter  // legam adapterul
        }

        // observam lista de postari din viewmodel
        postViewModel.posts.observe(viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Success -> {
                    val posts = res.data.orEmpty()  // lista de postari
                    if (posts.isEmpty()) {
                        binding.emptyState.visibility = View.VISIBLE  // arata mesaj empty
                        binding.recyclerViewPosts.visibility = View.GONE
                    } else {
                        binding.emptyState.visibility = View.GONE
                        binding.recyclerViewPosts.visibility = View.VISIBLE
                        postAdapter.updatePosts(posts)  // actualizeaza adapter
                    }
                }
                is Resource.Error -> {
                    binding.emptyState.visibility = View.VISIBLE  // arata eroare
                    binding.recyclerViewPosts.visibility = View.GONE
                    Toast.makeText(requireContext(), "error: ${res.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // schimbarea starii butonului de filter
        postViewModel.filter.observe(viewLifecycleOwner, ::updateFilterFabState)

        // incarcare initiala daca nu s-a incarcat nimic
        if (postViewModel.posts.value == null) {
            postViewModel.loadPosts(postViewModel.filter.value)
        }
    }

    // seteaza starea fab-ului in functie de filtru
    private fun updateFilterFabState(filter: PostFilterDto?) {
        val active = filter?.let { it.location != null || it.objectType != null } ?: false
        filterFab.isSelected = active
    }

    override fun onResume() {
        super.onResume()
        // butonul demo devine vizibil cand revii pe Home
        (requireActivity() as MainActivity)
            .findViewById<Button>(R.id.btnOpenHttpDemo).visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        // ascunde-l cand pleci din Home
        (requireActivity() as MainActivity)
            .findViewById<Button>(R.id.btnOpenHttpDemo).visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // eliberam binding
    }
}