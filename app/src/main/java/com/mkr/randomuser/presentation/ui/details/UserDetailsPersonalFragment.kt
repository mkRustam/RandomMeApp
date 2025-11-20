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
import com.mkr.randomuser.R
import com.mkr.randomuser.databinding.FragmentUserDetailsPersonalBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class UserDetailsPersonalFragment : Fragment() {

    private var _binding: FragmentUserDetailsPersonalBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserDetailsViewModel by viewModels({ requireParentFragment() })
    private val serverDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    private val prettyDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailsPersonalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    state.user?.let {
                        binding.emailTextView.text =
                            getString(R.string.details_email, it.email)
                        binding.phoneTextView.text =
                            getString(R.string.details_phone, it.phone)
                        binding.cellTextView.text =
                            getString(R.string.details_cell, it.cell)

                        val formattedDate = runCatching {
                            serverDateFormat.parse(it.dob.date)?.let(prettyDateFormat::format)
                        }.getOrNull() ?: it.dob.date

                        binding.dobTextView.text =
                            getString(R.string.details_birthday, formattedDate, it.dob.age)
                        binding.natTextView.text =
                            getString(R.string.details_nationality, it.nat)
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