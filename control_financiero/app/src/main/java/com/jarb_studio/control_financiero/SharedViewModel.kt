// app/src/main/java/com/jarb_studio/control_financiero/SharedViewModel.kt

package com.jarb_studio.control_financiero

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _salarioNeto = MutableLiveData<Float>()
    val salarioNeto: LiveData<Float> = _salarioNeto

    // --- NUEVO: MutableLiveData para el salario bruto original ---
    private val _salarioBruto = MutableLiveData<Float>()
    // LiveData pública para que los observadores solo puedan leerla
    val salarioBruto: LiveData<Float> = _salarioBruto
    // --- FIN NUEVO ---

    init {
        _salarioNeto.value = 0.0f
        _salarioBruto.value = 0.0f // Inicializar también el salario bruto
    }

    fun setSalarioNeto(salario: Float) {
        _salarioNeto.value = salario
    }

    // --- NUEVO: Función para actualizar el salario bruto ---
    fun setSalarioBruto(salario: Float) {
        _salarioBruto.value = salario
    }

    // --- NUEVO: Función para obtener el salario bruto (para IngresosFragment) ---
    fun getSalarioBruto(): Float {
        return _salarioBruto.value ?: 0.0f
    }
}