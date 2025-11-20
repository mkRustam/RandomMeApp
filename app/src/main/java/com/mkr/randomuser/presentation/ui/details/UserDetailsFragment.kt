package com.mkr.randomuser.presentation.ui.details

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
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.mkr.randomuser.R
import com.mkr.randomuser.domain.model.User
import com.mkr.randomuser.databinding.FragmentUserDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailsFragment : Fragment() {

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserDetailsViewModel by viewModels()
    private var tabMediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        setupViewPager()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state.isLoading
                    val user = state.user
                    binding.userName.isVisible = user != null
                    binding.tabLayout.isVisible = user != null
                    binding.viewPager.isVisible = user != null
                    if (user != null) {
                        bindUserData(user)
                    }
                    state.errorMessage?.let { message ->
                        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
                        viewModel.consumeError()
                    }
                }
            }
        }
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = UserDetailsPagerAdapter(this)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_personal)
                1 -> getString(R.string.tab_location)
                2 -> getString(R.string.tab_login)
                else -> null
            }
        }.apply { attach() }
    }

    private fun bindUserData(user: User) {
        Glide.with(this)
            .load(user.picture.large)
            .circleCrop()
            .into(binding.userImage)
        binding.userName.text = getString(R.string.details_full_name, user.name.first, user.name.last)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator?.detach()
        tabMediator = null
        _binding = null
    }
}