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
import com.mkr.randomuser.databinding.FragmentUserDetailsPersonalBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class UserDetailsPersonalFragment : Fragment() {

    private var _binding: FragmentUserDetailsPersonalBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserDetailsViewModel by viewModels({ requireParentFragment() })

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
                viewModel.user.collect { user ->
                    user?.let {
                        binding.emailTextView.text = "Email: ${it.email}"
                        binding.phoneTextView.text = "Phone: ${it.phone}"
                        binding.cellTextView.text = "Cell: ${it.cell}"

                        val formattedDate = SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                            Locale.getDefault()
                        )
                            .parse(it.dob.date)
                            ?.let { date ->
                                SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(date)
                            } ?: it.dob.date

                        binding.dobTextView.text = "Birthday: $formattedDate (${it.dob.age} years old)"
                        binding.natTextView.text = "Nationality: ${it.nat}"
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