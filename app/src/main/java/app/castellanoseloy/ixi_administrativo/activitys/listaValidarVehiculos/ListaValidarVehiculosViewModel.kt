package app.castellanoseloy.ixi_administrativo.activitys.listaValidarConductores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.castellanoseloy.ixi_administrativo.modelos.CarModel
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades.eliminarAcentosTildes
import app.castellanoseloy.ixi_administrativo.provinders.CarProvider
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.QuerySnapshot

class ListaValidarVehiculosViewModel : ViewModel() {

    private val _vehiculo = MutableLiveData<ArrayList<CarModel>>()
    val vehiculos: LiveData<ArrayList<CarModel>> get() = _vehiculo

    private val listaVehiculosOriginal = ArrayList<CarModel>()

    fun getVehiculos() {
        val carProvider = CarProvider()
        carProvider.getAllCar().addOnSuccessListener(OnSuccessListener<QuerySnapshot> { query ->

            val listaVehiculos = ArrayList<CarModel>()

            if (query != null) {
                if (query.documents.size > 0) {
                    val documents = query.documents

                    for (d in documents) {
                        var conductor = d.toObject(CarModel::class.java)
                        conductor?.let { listaVehiculos.add(it) }
                    }
                }
            }

            // Guarda la lista original para futuros filtros
            listaVehiculosOriginal.clear()
            listaVehiculosOriginal.addAll(listaVehiculos)

            // Actualiza el LiveData con la nueva lista de clientes
            _vehiculo.value = listaVehiculos
        })
    }

    fun filtrar(filtro: String, estado:String) {
        val listaFiltrada = if (estado.equals("Todos", ignoreCase = true)) {
            // No aplicar filtro por estado si el estado es "Todos"
            listaVehiculosOriginal.filter { vehiculo ->
                vehiculo.placa?.eliminarAcentosTildes()?.contains(filtro, true) == true ||
                        vehiculo.tipo?.contains(filtro, true) == true ||
                        vehiculo.bland?.contains(filtro, true) == true
            }
        } else {
            // Aplicar filtro por estado
            listaVehiculosOriginal.filter { vehiculo ->
                ( vehiculo.placa?.eliminarAcentosTildes()?.contains(filtro, true) == true ||
                        vehiculo.tipo?.contains(filtro, true) == true ||
                        vehiculo.bland?.contains(filtro, true) == true) &&
                        vehiculo.estado?.contains(estado, ignoreCase = true) == true
            }
        }
        _vehiculo.value = ArrayList(listaFiltrada)
    }
}