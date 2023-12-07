package app.castellanoseloy.ixi_administrativo.activitys.addCar

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import app.castellanoseloy.ixi_administrativo.R
import app.castellanoseloy.ixi_administrativo.activitys.vistaImagen.VistaImagen
import app.castellanoseloy.ixi_administrativo.databinding.ActivityAddCarBinding
import app.castellanoseloy.ixi_administrativo.modelos.CarModel
import app.castellanoseloy.ixi_administrativo.modelos.ConductorModelo
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades
import app.castellanoseloy.ixi_administrativo.provinders.DriverProvider


class DetalleVehiculo : AppCompatActivity() {

    private var conductor: ConductorModelo? = null
    private var extraId: String? = null
    private lateinit var idCar: String
    private var carModel: CarModel? = null
    var colorSeleccionado = Color.BLACK

    private lateinit var binding: ActivityAddCarBinding
    private lateinit var viewModel: DetalleVehiculoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Detalle Vehiculo"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setTitleTextColor(Color.WHITE)

        viewModel = DetalleVehiculoViewModel()


        extraId = intent.getStringExtra("idVehiculo")
        if (extraId != null) {
            viewModel.cargarDatos(extraId!!, binding, applicationContext) { vehiculo ->
                if (vehiculo != null) {
                    carModel=vehiculo
                    val driverProvider=DriverProvider()
                    carModel?.idDriver?.let {
                        driverProvider.getDriver(it)
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    conductor = document.toObject(ConductorModelo::class.java)
                                }
                            }
                    }
                }
            }
        }

        binding.imageViewCarColor.setColorFilter(colorSeleccionado)//color negro por defecto para el carro

        observador()
        listeners()
    }

    private fun observador() {
        viewModel?.mensaje?.observe(this) { mensaje ->
            Utilidades.mostrarToast(mensaje, this)
        }
        viewModel?.elementosActualizar?.observe(this, Observer { it ->
            if (it == 0) {
                progressDialog!!.dismiss()
                Utilidades.mostrarToast("Datos Guardados", this)
            }
        })
    }

    val progressDialog: ProgressDialog by lazy {
        ProgressDialog(this).apply {
            setMessage("Guardando...")
            setCancelable(false)
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
        }
    }


    private fun listeners() {
        binding.circleImageFotoFrontal.setOnClickListener{mostraImagen(carModel?.imagenFrontal)}
        binding.circleImageFotoLateral.setOnClickListener{mostraImagen(carModel?.imagenLateral)}
        binding.imageViewDocumentoVehiculoFontal.setOnClickListener{mostraImagen(carModel?.imagenDocumento)}
        binding.imageViewDocumentoVehiculoTrasero.setOnClickListener{mostraImagen(carModel?.imagenDocumentoTrasero)}
        binding.btnUpdate.setOnClickListener { actualizarCarro() }
        binding.buttonCambioEstado2.setOnClickListener { abrirCambioEstado() }

    }
    private fun abrirCambioEstado() {
        val builder = AlertDialog.Builder(this)

        // Obtener la vista inflada desde el archivo XML del diseño del diálogo
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.promt_cambio_estado_vehiculo, null)
        val button_cambiar_estado=dialogView.findViewById<Button>(R.id.button_cambiar_estado)
        val textFieldComentario=dialogView.findViewById<EditText>(R.id.textFieldComentario)
        val spinner_cambiar_estado=dialogView.findViewById<Spinner>(R.id.spinner_cambiar_estado)
        // Configurar el contenido del diálogo
        builder.setView(dialogView)


        button_cambiar_estado.setOnClickListener {
            var nuevoEstado=spinner_cambiar_estado.selectedItem.toString()
            var comentario=textFieldComentario.text.toString()
            if (comentario.isNotEmpty()) nuevoEstado+="\n $comentario"

            extraId?.let { it1 -> viewModel.actualizarEstado(it1, nuevoEstado,this ) }
            finish()
            finish()}



        // Configurar cualquier otra propiedad del cuadro de diálogo según sea necesario
        builder.setTitle("Cambiar Estado")
        builder.setPositiveButton("Cerrar") { dialog, _ -> dialog.dismiss() }

        // Crear y mostrar el cuadro de diálogo
        val alertDialog = builder.create()
        alertDialog.show()
    }
    private fun mostraImagen(urlImagen: String?) {
        val i = Intent(this, VistaImagen::class.java)
        i.putExtra("urlImagen", urlImagen)
        i.putExtra("nombre", conductor?.nombre)
        i.putExtra("apellido", conductor?.apellido)
        i.putExtra("tipoDocumento", conductor?.tipoDocumento)
        i.putExtra("numeroDocumento", conductor?.numeroDocumento)
        startActivity(i)
    }

    private fun actualizarCarro() {


        val datosValidados = viewModel.validarDatos(binding)

        if (datosValidados) {
            extraId?.let { viewModel.salveData(it, binding) }
        }
    }


}