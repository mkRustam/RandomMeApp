package com.mkr.randomuser.presentation.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mkr.randomuser.R
import com.mkr.randomuser.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinners()
        observeViewModel()

        binding.generateUserButton.setOnClickListener {
            val gender = binding.genderSpinner.selectedItem.toString().lowercase()
            val nat = binding.nationalitySpinner.selectedItem.toString().uppercase()
            viewModel.onGenerateUserClicked(gender, nat)
        }
    }

    private fun setupSpinners() {
        val genderAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.genders,
            android.R.layout.simple_spinner_item
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genderSpinner.adapter = genderAdapter

        val nationalityAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.nationalities,
            android.R.layout.simple_spinner_item
        )
        nationalityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.nationalitySpinner.adapter = nationalityAdapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        binding.progressBar.isVisible = state.isLoading
                        binding.generateUserButton.isEnabled = !state.isLoading
                        state.errorMessage?.let { message ->
                            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
                            viewModel.consumeError()
                        }
                    }
                }
                launch {
                    viewModel.events.collect { event ->
                        when (event) {
                            MainEvent.NavigateToUserList -> {
                                findNavController().navigate(MainFragmentDirections.actionMainFragmentToUserListFragment())
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}