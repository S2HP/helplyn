package com.example.helplyn.ui.sites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.helplyn.databinding.FragmentReportIssueBinding

class ReportIssueFragment : Fragment() {
    private var _binding: FragmentReportIssueBinding? = null
    private val binding get() = _binding!!
    private val args: ReportIssueFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportIssueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.companyName.text = args.companyName
        binding.submitButton.setOnClickListener {
            val issue = binding.editIssue.text.toString()
            val email = binding.editEmail.text.toString()
            val phone = binding.editPhone.text.toString()
            // Here you would send/store the report
            Toast.makeText(requireContext(), "Issue reported. Thank you!", Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
        }
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

