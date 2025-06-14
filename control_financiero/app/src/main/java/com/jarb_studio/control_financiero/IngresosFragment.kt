// app/src/main/java/com/jarb_studio/control_financiero/IngresosFragment.kt

package com.jarb_studio.control_financiero

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class IngresosFragment : Fragment() {

    private lateinit var tilSalario: TextInputLayout
    private lateinit var edtSalario: TextInputEditText
    private lateinit var btnCalcular: Button
    private lateinit var txtISSS: TextView
    private lateinit var txtAFP: TextView
    private lateinit var txtRenta: TextView
    private lateinit var txtTotal: TextView

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ingresos, container, false)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // Inicializa las vistas
        tilSalario = view.findViewById(R.id.tilSalario)
        edtSalario = view.findViewById(R.id.edtSalario)
        btnCalcular = view.findViewById(R.id.btnCalcular)
        txtISSS = view.findViewById(R.id.txtISSS)
        txtAFP = view.findViewById(R.id.txtAFP)
        txtRenta = view.findViewById(R.id.txtRenta)
        txtTotal = view.findViewById(R.id.txtTotal)

        edtSalario.addTextChangedListener {
            tilSalario.error = null // Limpia el error del TextInputLayout
        }

        // --- CORRECCIÓN IMPORTANTE AQUÍ: Cambiamos el .let para que use salarioBruto directamente ---
        sharedViewModel.salarioBruto.value?.let { ultimoSalarioBrutoGuardado -> // Renombrado para mayor claridad
            // Verificamos si el salario bruto guardado es mayor que 0.0f
            // Esto asegura que no intentemos precargar un valor inicial de 0.0
            if (ultimoSalarioBrutoGuardado > 0.0f) {
                // Establece el texto del EditText con el salario bruto guardado
                edtSalario.setText(ultimoSalarioBrutoGuardado.toString())

                // Realiza los cálculos con el salario bruto guardado, convirtiéndolo a Double
                performCalculations(ultimoSalarioBrutoGuardado.toDouble())
            }
        }
        // --- FIN CORRECCIÓN ---


        btnCalcular.setOnClickListener {
            // Ocultar el teclado
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            val salarioStr = edtSalario.text.toString()

            if (salarioStr.isEmpty()) {
                tilSalario.error = "Ingrese el salario"
                return@setOnClickListener
            }

            val salario = salarioStr.toDouble() // Este es el salario bruto ingresado
            performCalculations(salario) // Llama a la función de cálculo

            // Navegar automáticamente a GastosFragment
            findNavController().navigate(R.id.gastosFragment)
        }

        return view
    }

    // Función para realizar los cálculos y actualizar la UI
    private fun performCalculations(salarioBruto: Double) {
        var isss = salarioBruto * 0.03
        if (isss > 30.0) isss = 30.0

        val afp = salarioBruto * 0.0725
        val renta = calcularRenta(salarioBruto - isss - afp)
        val salarioNeto = salarioBruto - isss - afp - renta

        txtISSS.text = "$%.2f".format(isss)
        txtAFP.text = "$%.2f".format(afp)
        txtRenta.text = "$%.2f".format(renta)
        txtTotal.text = "$%.2f".format(salarioNeto)

        // Guardar ambos salarios en la ViewModel compartida, asegurando que sean Float
        sharedViewModel.setSalarioBruto(salarioBruto.toFloat())
        sharedViewModel.setSalarioNeto(salarioNeto.toFloat())

        Toast.makeText(requireContext(), "Salario neto calculado: $%.2f".format(salarioNeto), Toast.LENGTH_LONG).show()
    }

    private fun calcularRenta(base: Double): Double {
        return when {
            base <= 472.00 -> 0.0
            base <= 895.24 -> ((base - 472) * 0.10) + 17.67
            base <= 2038.10 -> ((base - 895.24) * 0.20) + 60.00
            else -> ((base - 2038.10) * 0.30) + 288.57
        }
    }
}