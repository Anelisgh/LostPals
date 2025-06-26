package com.example.lostpals.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lostpals.databinding.FragmentInboxBinding
import com.example.lostpals.ui.adapters.InboxAdapter
import com.example.lostpals.viewmodel.MessageViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class InboxFragment : Fragment() {

    private var _binding: FragmentInboxBinding? = null
    private val binding get() = _binding!!

    private val vm: MessageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInboxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = InboxAdapter { item ->
            val action = InboxFragmentDirections
                .actionInboxFragmentToChatFragment(
                    otherId      = item.conversationUserId,
                    partnerName  = item.conversationUsername
                )
            findNavController().navigate(action)
        }

        binding.recyclerInbox.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerInbox.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.inbox.collectLatest { lista ->
                    adapter.submit(lista)
                    binding.emptyState.visibility =
                        if (lista.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
