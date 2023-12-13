package app.castellanoseloy.ixi_administrativo.activitys.historiesDetails

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.castellanoseloy.ixi_administrativo.databinding.ActivityHistoriesDetailsBinding
import app.castellanoseloy.ixi_administrativo.modelos.CarModel
import app.castellanoseloy.ixi_administrativo.modelos.ClienteModelo
import app.castellanoseloy.ixi_administrativo.modelos.ConductorModelo
import app.castellanoseloy.ixi_administrativo.modelos.History
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades.formatoMonenda
import app.castellanoseloy.ixi_administrativo.promts.DatosConductorPromt
import app.castellanoseloy.ixi_administrativo.promts.DatosReferidorPromt
import app.castellanoseloy.ixi_administrativo.promts.MostrarTarifas
import app.castellanoseloy.ixi_administrativo.provinders.ClienteProveedor
import app.castellanoseloy.ixi_administrativo.provinders.DriverProvider
import app.castellanoseloy.ixi_administrativo.utilidades.RelativeTime



import com.optic.uberclonedriverkotlin.providers.HistoryProvider

class HistoriesDetailsActivity : AppCompatActivity() {
    private var history: History? = null
    private lateinit var binding: ActivityHistoriesDetailsBinding
    private var historyProvider = HistoryProvider()
    private var extraId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoriesDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Detalle del viaje"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setTitleTextColor(Color.WHITE)

        extraId = intent.getStringExtra("id")!!
        getHistory()

        listeners()
    }

    private fun listeners() {
        binding.buttonVerDetalle.setOnClickListener{
            var mostrarTarifas= MostrarTarifas()
            mostrarTarifas.mostrarTarifas(this, history?.tarifas!!)
        }
    }
    private fun getHistory() {
        historyProvider.getHistoryById(extraId).addOnSuccessListener { document ->

            if (document.exists()) {
                history = document.toObject(History::class.java)
                history?.id= document.id
                binding.textViewOrigen.text = history?.origin
                binding.textViewDestination.text = history?.destination
                binding.textViewDate.text =
                    RelativeTime.getTimeAgo(history?.timestamp!!, this@HistoriesDetailsActivity)

                binding.textViewPasajero.text=history?.pasajero?: "Desconocido"
                binding.textViewPasajeroTelefono.text=history?.pasajeroTelefono

                binding.textViewPrice.text =history!!.tarifas?.totalComisionApp.toString().formatoMonenda()
                binding.textViewCalificacionConductor.text = "${history?.calificationToDriver  ?: "5"}    ${history?.commentClient  ?: "No calificaste"}"
                binding.textViewClientCalification.text = "${history?.calificationToClient ?: "5"}    ${history?.commentDriver ?: "El conductor no califico"}"

                binding?.buttonVerCoductor?.setOnClickListener{
                     history?.idDriver?.let { it2 -> history?.car?.let { it1 ->
                         mostrarConductor(it2,
                             it1
                         )
                     } }
                }

                binding?.buttonVerCliente?.setOnClickListener{
                    history?.idClient?.let { it2 -> mostrarCliente( it2) }
                }

            }

        }
    }

    private fun mostrarConductor(idDriver:String, car: CarModel) {
        var driverProvideer = DriverProvider()
        driverProvideer.getDriver(idDriver).addOnSuccessListener { document ->
            if (document.exists()) {
                val driver = document.toObject(ConductorModelo::class.java)

                    var datosConductorPromt= DatosConductorPromt()
                    datosConductorPromt.conductor(this,driver!!,car)


                Log.d("Firestore", "Obteniendo datos del conductor ${driver?.validacion}")

            } else {
                Log.d("Firestore", "No se encontraron datos del conductor")
            }
        }
    }

    private fun mostrarCliente(idClient: String?) {
        var clienteProveedor = ClienteProveedor()
        clienteProveedor.getClientById(idClient!!).addOnSuccessListener { document ->
            if (document.exists()) {
                val cliente = document.toObject(ClienteModelo::class.java)

                var datosRefeidorPromt= DatosReferidorPromt()
                datosRefeidorPromt.clienteDetalle(this,cliente!!)


                Log.d("Firestore", "Obteniendo datos del cliente ${cliente}")

            } else {
                Log.e("Firestore", "No se encontraron datos del cliente")
            }
        }
    }


}