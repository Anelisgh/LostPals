package com.example.lostpals.ui

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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

// ecranul de profil al utilizatorului

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    // viewmodel care se ocupa de autentificare si user
    private val authViewModel: AuthViewModel by viewModels()
    // starea stergerii contului
    val deleteAccountStatus = MutableLiveData<Resource<Unit>>()
    // toolbar-ul global pe care il ascundem aici
    private var globalToolbar: View? = null

    // launcher ca sa alegem poza din galerie
    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                // punem poza aleasa in avatar
                view?.findViewById<ImageView>(R.id.imgAvatar)?.setImageURI(it)
                // salvam path-ul pozei in sesiune
                SessionManager(requireContext()).saveProfileImage(it.toString())
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ascundem toolbar-ul comun
        globalToolbar = requireActivity().findViewById(R.id.menu)
        globalToolbar?.visibility = View.GONE

        // butoane de navigare
        view.findViewById<TextView>(R.id.appName)?.setOnClickListener {
            findNavController().popBackStack(R.id.homeFragment, false)  // inapoi la home
        }
        view.findViewById<ImageView>(R.id.Inbox).setOnClickListener {
            findNavController().navigate(R.id.inboxFragment)  // deschide inbox
        }
        view.findViewById<ImageView>(R.id.createPostIcon).setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_createPostFragment)  // deschide creare post
        }

        // logout: stergem sesiunea si mergem la login
        view.findViewById<ImageView>(R.id.accountIcon).apply {
            setImageResource(R.drawable.logout)
            setOnClickListener {
                authViewModel.logout()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish() // inchide activiatea curenta
            }
        }

        // afisam username-ul curent
        view.findViewById<TextView>(R.id.tvUsername).text =
            SessionManager(requireContext()).getUsername() ?: "–"

        // incarcam poza de profil salvata, daca exista
        view.findViewById<ImageView>(R.id.imgAvatar).apply {
            SessionManager(requireContext()).getProfileImage()?.let { path ->
                try {
                    setImageURI(Uri.parse(path))  // incearca sa incarce poza
                } catch (_: Exception) {
                    setImageResource(R.drawable.avatar)  // fallback la avatar default
                }
            }
            // click pe poza: deschide galerie
            setOnClickListener { pickImage.launch("image/*") }
        }

        // change password: citim parolele si apelam viewmodel
        val etCurrent = view.findViewById<TextInputEditText>(R.id.etCurrentPassword)
        val etNew     = view.findViewById<TextInputEditText>(R.id.etNewPassword)
        view.findViewById<MaterialButton>(R.id.btnSavePassword).setOnClickListener {
            authViewModel.changePassword(
                etCurrent.text.toString(),
                etNew.text.toString()
            )
        }
        authViewModel.passwordChangeStatus.observe(viewLifecycleOwner) { result ->
            if (result is Resource.Success) {
                Toast.makeText(requireContext(), "password updated successfully", Toast.LENGTH_SHORT).show()
                etCurrent.text?.clear(); etNew.text?.clear()
            } else if (result is Resource.Error) {
                Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
            }
        }

        // change email: citim email-ul si apelam viewmodel
        val etEmail = view.findViewById<TextInputEditText>(R.id.etNewEmail)
        view.findViewById<MaterialButton>(R.id.btnSaveEmail).setOnClickListener {
            authViewModel.changeEmail(etEmail.text.toString())
        }
        authViewModel.emailChangeStatus.observe(viewLifecycleOwner) { result ->
            if (result is Resource.Success) {
                Toast.makeText(requireContext(), "email updated successfully", Toast.LENGTH_SHORT).show()
                etEmail.text?.clear()
            } else if (result is Resource.Error) {
                Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
            }
        }

        // delete account: confirmam cu dialog
        view.findViewById<MaterialButton>(R.id.btnDelete).setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("account deletion confirmation")
                .setMessage("are you sure you want to delete your account? this action is irreversible.")
                .setPositiveButton("delete") { _: DialogInterface, _: Int ->
                    authViewModel.deleteAccount()  // stergem contul
                }
                .setNegativeButton("cancel", null)
                .show()
        }
        authViewModel.deleteAccountStatus.observe(viewLifecycleOwner) { result ->
            if (result is Resource.Success) {
                Toast.makeText(requireContext(), "account deleted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            } else if (result is Resource.Error) {
                Toast.makeText(requireContext(), "error: ${result.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // readucem toolbar-ul si evitam memory leak
        globalToolbar?.visibility = View.VISIBLE
        globalToolbar = null
    }
}