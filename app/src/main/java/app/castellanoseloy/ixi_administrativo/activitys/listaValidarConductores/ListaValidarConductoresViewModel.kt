package app.castellanoseloy.ixi_administrativo.activitys.listaValidarConductores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.castellanoseloy.ixi_administrativo.modelos.ConductorModelo
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades.eliminarAcentosTildes
import app.castellanoseloy.ixi_administrativo.provinders.DriverProvider
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.QuerySnapshot

class ListaValidarConductoresViewModel : ViewModel() {

    private val _conductor = MutableLiveData<ArrayList<ConductorModelo>>()
    val conductores: LiveData<ArrayList<ConductorModelo>> get() = _conductor

    private val listaClientesOriginal = ArrayList<ConductorModelo>()

    fun getConductores() {
        val driverProveedor = DriverProvider()
        driverProveedor.getAllDrivers().addOnSuccessListener(OnSuccessListener<QuerySnapshot> { query ->

            val listaConductores = ArrayList<ConductorModelo>()

            if (query != null) {
                if (query.documents.size > 0) {
                    val documents = query.documents

                    for (d in documents) {
                        var conductor = d.toObject(ConductorModelo::class.java)
                        conductor?.let { listaConductores.add(it) }
                    }
                }
            }

            // Guarda la lista original para futuros filtros
            listaClientesOriginal.clear()
            listaClientesOriginal.addAll(listaConductores)

            // Actualiza el LiveData con la nueva lista de clientes
            _conductor.value = listaConductores
        })
    }

    fun filtrar(filtro: String, estado:String) {
        val listaFiltrada = if (estado.equals("Todos", ignoreCase = true)) {
            // No aplicar filtro por estado si el estado es "Todos"
            listaClientesOriginal.filter { cliente ->
                cliente.nombre?.eliminarAcentosTildes()?.contains(filtro, true) == true ||
                        cliente.apellido?.contains(filtro, true) == true ||
                        cliente.telefono?.contains(filtro, true) == true ||
                        cliente.numeroDocumento?.contains(filtro, true) == true
            }
        } else {
            // Aplicar filtro por estado
            listaClientesOriginal.filter { cliente ->
                (cliente.nombre?.eliminarAcentosTildes()?.contains(filtro, true) == true ||
                        cliente.apellido?.contains(filtro, true) == true ||
                        cliente.telefono?.contains(filtro, true) == true ||
                        cliente.numeroDocumento?.contains(filtro, true) == true) &&
                        cliente.validacion?.contains(estado, ignoreCase = true) == true
            }
        }
        _conductor.value = ArrayList(listaFiltrada)
    }
}