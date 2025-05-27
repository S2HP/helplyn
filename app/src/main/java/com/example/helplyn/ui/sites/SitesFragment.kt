package com.example.helplyn.ui.sites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.helplyn.databinding.FragmentSitesBinding
import android.app.AlertDialog
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import android.view.Gravity
import com.example.helplyn.R
import androidx.navigation.fragment.findNavController
import android.text.Editable
import android.text.TextWatcher
import android.widget.ScrollView
import androidx.fragment.app.setFragmentResultListener

class SitesFragment : Fragment() {
    private var _binding: FragmentSitesBinding? = null
    private val binding get() = _binding!!

    data class Company(
        val name: String,
        val chatOperator: String?,
        val email: String?,
        val phone: String?,
        val details: String
    )

    private val companies = mutableListOf(
        Company(
            name = "Nintendo",
            chatOperator = "Mario Support",
            email = "support@nintendo.com",
            phone = "+1-800-255-3700",
            details = "Nintendo: Gaming and entertainment company."
        ),
        Company(
            name = "Sega",
            chatOperator = "Sonic Support",
            email = "help@sega.com",
            phone = "+1-800-872-7342",
            details = "Sega: Video game developer and publisher."
        ),
        Company(
            name = "Ubisoft",
            chatOperator = "Assassin Support",
            email = "support@ubisoft.com",
            phone = "+1-919-460-9778",
            details = "Ubisoft: International game developer and publisher."
        ),
        Company(
            name = "Apple",
            chatOperator = "Apple Genius",
            email = "support@apple.com",
            phone = "+1-800-275-2273",
            details = "Apple: Consumer electronics and software."
        ),
        Company(
            name = "Samsung",
            chatOperator = "Galaxy Support",
            email = "support@samsung.com",
            phone = "+1-800-726-7864",
            details = "Samsung: Electronics and appliances."
        ),
        Company(
            name = "Google",
            chatOperator = "Google Helper",
            email = "support@google.com",
            phone = "+1-650-253-0000",
            details = "Google: Search, cloud, and technology services."
        ),
        Company(
            name = "Sony",
            chatOperator = "PlayStation Support",
            email = "support@sony.com",
            phone = "+1-800-345-7669",
            details = "Sony: Electronics, gaming, and entertainment."
        ),
        Company(
            name = "Microsoft",
            chatOperator = "Xbox Support",
            email = "support@microsoft.com",
            phone = "+1-800-642-7676",
            details = "Microsoft: Software, hardware, and cloud services."
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSitesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val container = binding.sitesButtonsContainer
        val searchEditText = view.findViewById<android.widget.EditText>(R.id.search_edit_text)
        // Prevent keyboard from showing on startup
        searchEditText.clearFocus()
        var filteredCompanies: MutableList<Company> = companies

        fun updateCompanyButtons(filter: String = "") {
            container.removeAllViews()
            filteredCompanies = if (filter.isBlank()) companies else companies.filter {
                it.name.contains(filter, ignoreCase = true)
            }.toMutableList()
            for (company in filteredCompanies) {
                val btn = Button(requireContext())
                btn.text = company.name
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 12, 0, 12)
                btn.layoutParams = params
                btn.setPadding(0, 32, 0, 32)
                btn.textSize = 18f
                btn.setOnClickListener { showCompanyOptions(company) }
                container.addView(btn)
            }
        }

        updateCompanyButtons()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateCompanyButtons(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        setFragmentResultListener("add_company_result") { _, bundle ->
            val name = bundle.getString("name") ?: return@setFragmentResultListener
            val email = bundle.getString("email")
            val phone = bundle.getString("phone")
            val details = bundle.getString("details")
            companies.add(Company(name, null, email, phone, details ?: ""))
            updateCompanyButtons(searchEditText.text.toString())
        }
    }

    private fun showCompanyOptions(company: Company) {
        val options = mutableListOf<String>()
        val actions = mutableListOf<() -> Unit>()
        // Hide Chat button by default (do not add to options)
        // If you want to enable for a specific company, add logic here
        // Example: if (company.name == "Google") { ... }
        // options.add("Contact via Chat")
        // actions.add { openChatWeb(company) }

        if (company.email != null) {
            options.add("Contact via Email")
            actions.add { showEmailDialog(company) }
        }
        if (company.phone != null) {
            options.add("Contact via Phone")
            actions.add { showPhoneDialog(company) }
        }
        options.add("Details")
        actions.add { showDetailsDialog(company) }
        options.add("Report Issue")
        actions.add {
            val intent = android.content.Intent(android.content.Intent.ACTION_SENDTO).apply {
                data = android.net.Uri.parse("mailto:webrooks76@gmail.com")
                putExtra(android.content.Intent.EXTRA_SUBJECT, "Issue Report: ${company.name}")
                putExtra(android.content.Intent.EXTRA_TEXT, "Please describe your issue with ${company.name} here.")
            }
            startActivity(intent)
        }

        AlertDialog.Builder(requireContext())
            .setTitle(company.name)
            .setItems(options.toTypedArray(), null) // We'll handle click below
            .setNegativeButton("Back", null)
            .create().apply {
                setOnShowListener {
                    // Set up the click listeners for the list items
                    val listView = this.listView
                    listView.setOnItemClickListener { _, _, which, _ ->
                        actions[which]()
                        dismiss()
                    }
                    // Align the list items to the left
                    for (i in 0 until listView.childCount) {
                        val v = listView.getChildAt(i)
                        if (v is android.widget.TextView) {
                            v.textAlignment = android.view.View.TEXT_ALIGNMENT_VIEW_START
                            v.gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                        }
                    }
                }
            }.show()
    }

    // If you want to enable chat for a company, use this function
    private fun openChatWeb(company: Company) {
        val url = when (company.name) {
            "Google" -> "https://support.google.com/"
            "Apple" -> "https://support.apple.com/"
            "Microsoft" -> "https://support.microsoft.com/"
            "Sony" -> "https://support.playstation.com/"
            "Nintendo" -> "https://en-americas-support.nintendo.com/"
            "Samsung" -> "https://www.samsung.com/us/support/"
            "Ubisoft" -> "https://support.ubisoft.com/"
            "Sega" -> "https://support.sega.com/"
            else -> "https://www.google.com/"
        }
        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
        startActivity(intent)
    }

    private fun showEmailDialog(company: Company) {
        val messageView = android.widget.TextView(requireContext())
        messageView.text = company.email
        messageView.textAlignment = android.view.View.TEXT_ALIGNMENT_CENTER
        messageView.setPadding(32, 32, 32, 32)
        messageView.setTextColor(resources.getColor(android.R.color.holo_blue_dark, null))
        messageView.isClickable = true
        messageView.setOnClickListener {
            val intent = android.content.Intent(android.content.Intent.ACTION_SENDTO).apply {
                data = android.net.Uri.parse("mailto:${company.email}")
            }
            startActivity(intent)
        }
        AlertDialog.Builder(requireContext())
            .setTitle(company.name)
            .setCustomTitle(centeredTitle(company.name))
            .setView(messageView)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                showCompanyOptions(company)
            }
            .show()
    }

    private fun showPhoneDialog(company: Company) {
        val messageView = android.widget.TextView(requireContext())
        messageView.text = company.phone
        messageView.textAlignment = android.view.View.TEXT_ALIGNMENT_CENTER
        messageView.setPadding(32, 32, 32, 32)
        messageView.setTextColor(resources.getColor(android.R.color.holo_blue_dark, null))
        messageView.isClickable = true
        messageView.setOnClickListener {
            val intent = android.content.Intent(android.content.Intent.ACTION_DIAL).apply {
                data = android.net.Uri.parse("tel:${company.phone}")
            }
            startActivity(intent)
        }
        AlertDialog.Builder(requireContext())
            .setTitle(company.name)
            .setCustomTitle(centeredTitle(company.name))
            .setView(messageView)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                showCompanyOptions(company)
            }
            .show()
    }

    private fun showDetailsDialog(company: Company) {
        val messageView = android.widget.TextView(requireContext())
        messageView.text = company.details
        messageView.textAlignment = android.view.View.TEXT_ALIGNMENT_CENTER
        messageView.setPadding(32, 32, 32, 32)
        AlertDialog.Builder(requireContext())
            .setTitle(company.name)
            .setCustomTitle(centeredTitle(company.name))
            .setView(messageView)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                showCompanyOptions(company)
            }
            .show()
    }

    private fun centeredTitle(title: String): android.widget.TextView {
        val titleView = android.widget.TextView(requireContext())
        titleView.text = title
        titleView.textAlignment = android.view.View.TEXT_ALIGNMENT_CENTER
        titleView.textSize = 20f
        titleView.setPadding(32, 32, 32, 32)
        return titleView
    }

    private fun showReportIssueDialog(company: Company) {
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog_report_issue, null)
        val issueEdit = dialogView.findViewById<android.widget.EditText>(R.id.edit_issue)
        val emailEdit = dialogView.findViewById<android.widget.EditText>(R.id.edit_email)
        val phoneEdit = dialogView.findViewById<android.widget.EditText>(R.id.edit_phone)
        AlertDialog.Builder(requireContext())
            .setTitle("Report Issue")
            .setView(dialogView)
            .setPositiveButton("Submit") { _, _ ->
                val issue = issueEdit.text.toString()
                val email = emailEdit.text.toString()
                val phone = phoneEdit.text.toString()
                // Here you would send/store the report
                Toast.makeText(requireContext(), "Issue reported. Thank you!", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
