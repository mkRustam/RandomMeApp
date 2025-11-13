package com.mkr.randomuser.presentation.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mkr.randomuser.databinding.FragmentUserDetailsLocationBinding
import kotlinx.coroutines.launch

class UserDetailsLocationFragment : Fragment() {

    private var _binding: FragmentUserDetailsLocationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserDetailsViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailsLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect { user ->
                    user?.let {
                        val location = it.location
                        binding.streetTextView.text =
                            "Street: ${location.street.number} ${location.street.name}"
                        binding.cityTextView.text = "City: ${location.city}"
                        binding.stateTextView.text = "State: ${location.state}"
                        binding.countryTextView.text = "Country: ${location.country}"
                        binding.postcodeTextView.text = "Postcode: ${location.postcode}"
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