package app.castellanoseloy.ixi_administrativo.activitys.listaValidarRereridores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.castellanoseloy.ixi_administrativo.modelos.ClienteModelo
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades.eliminarAcentosTildes
import app.castellanoseloy.ixi_administrativo.provinders.ClienteProveedor
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.QuerySnapshot

class ListaValidarReferidoresViewModel : ViewModel() {

    private val _clientes = MutableLiveData<ArrayList<ClienteModelo>>()
    val clientes: LiveData<ArrayList<ClienteModelo>> get() = _clientes

    private val listaClientesOriginal = ArrayList<ClienteModelo>()

    fun getClientes() {
        val clientProveedor = ClienteProveedor()
        clientProveedor.getAllClients().addOnSuccessListener(OnSuccessListener<QuerySnapshot> { query ->

            val listaClientes = ArrayList<ClienteModelo>()

            if (query != null) {
                if (query.documents.size > 0) {
                    val documents = query.documents

                    for (d in documents) {
                        var cliente = d.toObject(ClienteModelo::class.java)
                        cliente?.let { listaClientes.add(it) }
                    }
                }
            }

            // Guarda la lista original para futuros filtros
            listaClientesOriginal.clear()
            listaClientesOriginal.addAll(listaClientes)

            // Actualiza el LiveData con la nueva lista de clientes
            _clientes.value = listaClientes
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
        _clientes.value = ArrayList(listaFiltrada)
    }
}