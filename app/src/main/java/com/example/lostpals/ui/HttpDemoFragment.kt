package com.example.lostpals.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.lostpals.R
import com.example.lostpals.viewmodel.HttpDemoViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HttpDemoFragment : Fragment() {

    // viewmodel care face apeluri HTTP si gestioneaza datele
    private val vm: HttpDemoViewModel by viewModels()

    // view-uri din layout
    private lateinit var txtResult: TextView // afiseaza lista itemelor
    private lateinit var btnApi: Button // buton GET API
    private lateinit var btnClear: Button // buton Clear
    private lateinit var btnGetLocal: Button // buton GET LOCAL
    private lateinit var btnDelete: Button // buton de stergere
    private lateinit var etUserId: EditText // input pentru id-ul userului de sters

    // incarcam si transformam fisierul de layout intr-un obiect view
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_http_demo, container, false)

    // dupa ce view-ul e gata, legam elementele si setam functionalitatile
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // preluam referintele la view-uri
        txtResult   = view.findViewById(R.id.txtResult)
        btnApi      = view.findViewById(R.id.btnPosts)
        btnClear    = view.findViewById(R.id.btnClear)
        btnGetLocal = view.findViewById(R.id.btnGetLocal)
        btnDelete   = view.findViewById(R.id.btnDelete)
        etUserId    = view.findViewById(R.id.etUserId)

        //"GET API" -> incarca date de pe server
        btnApi.setOnClickListener { vm.loadData() }

        // "GET LOCAL" -> ia toti userii din Room
        btnGetLocal.setOnClickListener { vm.getAllLocalUsers() }

        //"DELETE" -> incearca sa stergi user dupa id
        btnDelete.setOnClickListener {
            val id = etUserId.text.toString().toLongOrNull()  // convertim text in numar
            if (id != null) {
                vm.deleteUserById(id)  // stergem user
            } else {
                txtResult.text = "Enter a valid id for DELETE request"// mesaj de eroare
            }
        }

        // "Clear" -> golim zona de rezultate
        btnClear.setOnClickListener { txtResult.text = "" }

        // ascultam fluxul de date din viewmodel si afisam mereu ce vine
        viewLifecycleOwner.lifecycleScope.launch {
            vm.items.collectLatest { list ->
                txtResult.text = buildString {
                    list.forEach { (title, sub) ->
                        appendLine("$title: $sub")  // afiseaza titlul + subtitlul pe o linie
                    }
                }
            }
        }
    }
}