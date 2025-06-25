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
import com.example.lostpals.R
import com.example.lostpals.ui.auth.LoginActivity
import com.example.lostpals.util.Resource
import com.example.lostpals.util.SessionManager
import com.example.lostpals.viewmodel.AuthViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import androidx.lifecycle.MutableLiveData

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    // Resursa initiala a iconitei (va fi pusa inapoi la iesirea din fragment)
    private var originalResId: Int = R.drawable.account

    // Referinta catre iconita din toolbar
    private lateinit var toolbarIcon: ImageView

    // ViewModel-ul care gestioneaza actiunile de autentificare
    private val authViewModel: AuthViewModel by viewModels()

    val deleteAccountStatus = MutableLiveData<Resource<Unit>>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1) Username
        val tvUsername = view.findViewById<TextView>(R.id.tvUsername)
        val sessionManager = SessionManager(requireContext())
        tvUsername.text = sessionManager.getUsername() ?: "–"

        // 2) Logout icon în toolbar
        toolbarIcon = requireActivity().findViewById(R.id.accountIcon)
        toolbarIcon.setImageResource(R.drawable.logout)
        toolbarIcon.setOnClickListener {
            authViewModel.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

        // 3) Schimbare parolă
        val etCurrentPwd = view.findViewById<TextInputEditText>(R.id.etCurrentPassword)
        val etNewPwd     = view.findViewById<TextInputEditText>(R.id.etNewPassword)
        view.findViewById<MaterialButton>(R.id.btnSavePassword).setOnClickListener {
            authViewModel.changePassword(
                etCurrentPwd.text.toString(),
                etNewPwd.text.toString()
            )
        }

        // 4) Observare rezultat schimbare parolă
        authViewModel.passwordChangeStatus.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    Toast.makeText(requireContext(), "Password updated successfully", Toast.LENGTH_SHORT).show()
                    etCurrentPwd.text?.clear()
                    etNewPwd.text?.clear()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        // 5) Schimbare email
        val etNewEmail = view.findViewById<TextInputEditText>(R.id.etNewEmail)
        view.findViewById<MaterialButton>(R.id.btnSaveEmail).setOnClickListener {
            authViewModel.changeEmail(etNewEmail.text.toString())
        }

        // 6) Observare rezultat schimbare email
        authViewModel.emailChangeStatus.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    Toast.makeText(requireContext(), "Email updated successfully", Toast.LENGTH_SHORT).show()
                    etNewEmail.text?.clear()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        // 7) Delete account + confirmare dialog
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

        // 8) Observare rezultat delete
        authViewModel.deleteAccountStatus.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    Toast.makeText(requireContext(), "Account deleted", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Error: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toolbarIcon.setImageResource(originalResId)
        toolbarIcon.setOnClickListener(null)
    }
}
