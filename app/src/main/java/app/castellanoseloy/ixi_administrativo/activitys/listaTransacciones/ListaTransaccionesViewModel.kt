package app.castellanoseloy.ixi_administrativo.activitys.listaTransacciones

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.castellanoseloy.ixi_administrativo.modelos.TransaccionesModelo

import app.castellanoseloy.ixi_administrativo.objetos.Utilidades.eliminarAcentosTildes
import kotlin.math.absoluteValue

class ListaTransaccionesViewModel:ViewModel() {

    private val _transaccion = MutableLiveData<ArrayList<TransaccionesModelo>>()
    val transacciones: LiveData<ArrayList<TransaccionesModelo>> get() = _transaccion

    private val listaTransaccionesOriginal = ArrayList<TransaccionesModelo>()

    private val _sumasTransacciones = MutableLiveData<SumasTransaccionesModelo>()
    val sumasTransacciones: LiveData<SumasTransaccionesModelo> get() = _sumasTransacciones

    fun filtrar(filtro: String, estado:String) {
        val listaFiltrada = if (estado.equals("Todos", ignoreCase = true)) {
            // No aplicar filtro por estado si el estado es "Todos"
            listaTransaccionesOriginal.filter { transaccion ->
                transaccion.cuenta?.eliminarAcentosTildes()?.contains(filtro, true) == true
            }
        } else {
            // Aplicar filtro por estado
            listaTransaccionesOriginal.filter { transaccion ->
                (transaccion.cuenta?.eliminarAcentosTildes()?.contains(filtro, true) == true ) &&
                        transaccion.estado?.contains(estado, ignoreCase = true) == true
            }
        }

        val sumas = sumarEstados(listaFiltrada)

        _transaccion.value = ArrayList(listaFiltrada)
        _sumasTransacciones.value = sumas
    }


    fun sumarEstados(listaEstados: List<TransaccionesModelo>): SumasTransaccionesModelo {
        val sumaRealizado = listaEstados.filter { it.estado.equals("Realizado", ignoreCase = true) }
            .sumByDouble { transaccion -> transaccion.valor?.toDouble() ?: 0.0 }
            .absoluteValue

        val sumaCancelado = listaEstados.filter { it.estado.equals("Cancelado", ignoreCase = true) }
            .sumByDouble { transaccion -> transaccion.valor?.toDouble() ?: 0.0 }
            .absoluteValue

        val sumaPendiente = listaEstados.filter { it.estado.equals("Pendiente", ignoreCase = true) }
            .sumByDouble { transaccion -> transaccion.valor?.toDouble() ?: 0.0 }
            .absoluteValue

        Log.d("Transacciones", "Realizados: $sumaRealizado Cancelados: $sumaCancelado Pendientes: $sumaPendiente")

        return SumasTransaccionesModelo(sumaRealizado, sumaCancelado, sumaPendiente)
    }


    fun setRecargas(listaRecargas: ArrayList<TransaccionesModelo>?) {
        listaTransaccionesOriginal.clear()
        listaRecargas?.let { listaTransaccionesOriginal.addAll(it) }

        // Actualiza el LiveData con la nueva lista de clientes
        _transaccion.value = listaRecargas!!
    }

    data class SumasTransaccionesModelo(
        val sumaRealizado: Double,
        val sumaCancelado: Double,
        val sumaPendiente: Double
    )
}