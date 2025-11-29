package com.mkr.randomuser.presentation.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mkr.randomuser.databinding.FragmentUserListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserListViewModel by viewModels()
    private lateinit var userAdapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        binding.fabAddUser.setOnClickListener {
            findNavController().navigate(UserListFragmentDirections.actionUserListFragmentToMainFragment())
        }
    }

    private fun setupRecyclerView() {
        userAdapter = UserListAdapter { user ->
            val action = UserListFragmentDirections.actionUserListFragmentToUserDetailsFragment(user.id)
            findNavController().navigate(action)
        }
        binding.usersRecyclerView.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state.isLoading
                    binding.emptyListTextView.isVisible = state.isEmpty && !state.isLoading
                    binding.usersRecyclerView.isVisible = state.users.isNotEmpty()
                    userAdapter.submitList(state.users)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}