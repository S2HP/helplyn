package com.example.helplyn.ui.addcompany

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.helplyn.databinding.FragmentAddCompanyBinding
import androidx.fragment.app.setFragmentResult

class AddCompanyFragment : Fragment() {
    private var _binding: FragmentAddCompanyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCompanyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.submitCompanyButton.setOnClickListener {
            val name = binding.editCompanyName.text.toString()
            val email = binding.editCompanyEmail.text.toString()
            val phone = binding.editCompanyPhone.text.toString()
            val details = binding.editCompanyDetails.text.toString()
            // Send result to SitesFragment
            val result = Bundle().apply {
                putString("name", name)
                putString("email", email)
                putString("phone", phone)
                putString("details", details)
            }
            parentFragmentManager.setFragmentResult("add_company_result", result)
            Toast.makeText(requireContext(), "Company added!", Toast.LENGTH_SHORT).show()
            binding.editCompanyName.text?.clear()
            binding.editCompanyEmail.text?.clear()
            binding.editCompanyPhone.text?.clear()
            binding.editCompanyDetails.text?.clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

