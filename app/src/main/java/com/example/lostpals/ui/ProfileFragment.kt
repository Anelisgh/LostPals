package com.example.lostpals.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.lostpals.R
import com.example.lostpals.ui.auth.LoginActivity
import com.example.lostpals.util.Resource
import com.example.lostpals.util.SessionManager
import com.example.lostpals.viewmodel.AuthViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var globalToolbar: View? = null
    private val authViewModel: AuthViewModel by viewModels()
    val deleteAccountStatus = MutableLiveData<Resource<Unit>>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* ---------------------------------- UI refs ---------------------------------- */
        globalToolbar = requireActivity().findViewById(R.id.menu)
        globalToolbar?.visibility = View.GONE

        val logoutIcon     = view.findViewById<ImageView>(R.id.accountIcon)
        val inboxIcon      = view.findViewById<ImageView>(R.id.Inbox)
        val createPostIcon = view.findViewById<ImageView>(R.id.createPostIcon)

        /* ------------------------------ 1. INBOX click ------------------------------- */
        inboxIcon.setOnClickListener {
            findNavController().navigate(R.id.inboxFragment)
        }

        /* ------------------------------ 2. LOGOUT click ------------------------------ */
        logoutIcon.setImageResource(R.drawable.logout)
        logoutIcon.setOnClickListener {
            authViewModel.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

        /* --------------------------- 3. CREATE-POST click ---------------------------- */
        createPostIcon.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_createPostFragment)
        }

        /* ------------------------------- User info ----------------------------------- */
        val tvUsername = view.findViewById<TextView>(R.id.tvUsername)
        tvUsername.text = SessionManager(requireContext()).getUsername() ?: "–"

        /* --------------------------- Change password flow ---------------------------- */
        val etCurrentPwd = view.findViewById<TextInputEditText>(R.id.etCurrentPassword)
        val etNewPwd     = view.findViewById<TextInputEditText>(R.id.etNewPassword)
        view.findViewById<MaterialButton>(R.id.btnSavePassword).setOnClickListener {
            authViewModel.changePassword(
                etCurrentPwd.text.toString(),
                etNewPwd.text.toString()
            )
        }

        authViewModel.passwordChangeStatus.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    Toast.makeText(requireContext(),
                        "Password updated successfully", Toast.LENGTH_SHORT).show()
                    etCurrentPwd.text?.clear(); etNewPwd.text?.clear()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(),
                        result.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        /* ----------------------------- Change email flow ----------------------------- */
        val etNewEmail = view.findViewById<TextInputEditText>(R.id.etNewEmail)
        view.findViewById<MaterialButton>(R.id.btnSaveEmail).setOnClickListener {
            authViewModel.changeEmail(etNewEmail.text.toString())
        }

        authViewModel.emailChangeStatus.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    Toast.makeText(requireContext(),
                        "Email updated successfully", Toast.LENGTH_SHORT).show()
                    etNewEmail.text?.clear()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(),
                        result.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        /* ------------------------------ Delete account ------------------------------- */
        view.findViewById<MaterialButton>(R.id.btnDelete).setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Account deletion confirmation")
                .setMessage("Are you sure you want to delete your account? This action is irreversible.")
                .setPositiveButton("Delete") { _: DialogInterface, _: Int ->
                    authViewModel.deleteAccount()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        authViewModel.deleteAccountStatus.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    Toast.makeText(requireContext(),
                        "Account deleted", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(),
                        "Error: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        globalToolbar?.visibility = View.VISIBLE
        globalToolbar = null
    }
}
