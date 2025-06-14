// app/src/main/java/com/jarb_studio/control_financiero/GastosFragment.kt

package com.jarb_studio.control_financiero

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer // Importar Observer
import androidx.lifecycle.ViewModelProvider // Importar ViewModelProvider

class GastosFragment : Fragment() {

    private lateinit var tvSalarioNetoDisplay: TextView
    private lateinit var sharedViewModel: SharedViewModel // Declarar la SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gastos, container, false)

        tvSalarioNetoDisplay = view.findViewById(R.id.tvSalarioNetoDisplay)

        // Inicializar la SharedViewModel, vinculada a la actividad (requireActivity())
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // Observar cambios en el salarioNeto de la ViewModel
        sharedViewModel.salarioNeto.observe(viewLifecycleOwner, Observer { salarioNeto ->
            if (salarioNeto > 0.0f) {
                // Si hay un salario válido, mostrarlo y NO mostrar la advertencia
                tvSalarioNetoDisplay.text = "$%.2f".format(salarioNeto)
            } else {
                // Si el salario es 0.0f (no calculado o reiniciado)
                tvSalarioNetoDisplay.text = "$0.00"
                Toast.makeText(
                    requireContext(),
                    "Para ver cálculos de gastos, primero ingrese y calcule su salario en la sección de Ingresos.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        return view
    }
}