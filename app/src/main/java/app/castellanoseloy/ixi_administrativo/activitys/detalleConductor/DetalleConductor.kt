package app.castellanoseloy.ixi_administrativo.activitys.detalleConductor

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import app.castellanoseloy.ixi_administrativo.R
import app.castellanoseloy.ixi_administrativo.activitys.historiesActivity.HistoriesActivity
import app.castellanoseloy.ixi_administrativo.activitys.vistaImagen.VistaImagen
import app.castellanoseloy.ixi_administrativo.databinding.ActivityDetalleConductorBinding
import app.castellanoseloy.ixi_administrativo.modelos.ConductorModelo
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades
import app.castellanoseloy.ixi_administrativo.provinders.DriverProvider


class DetalleConductor : AppCompatActivity() {

    private var datosConductor: ConductorModelo? = null
    private var extraIdConductor: String? = null
    private lateinit var binding: ActivityDetalleConductorBinding
    val driverProvider = DriverProvider()

    private var viewModel: DetalleConductorViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleConductorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Detalle Conductor"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setTitleTextColor(Color.WHITE)

        viewModel = ViewModelProvider(this).get(DetalleConductorViewModel::class.java)

        extraIdConductor = intent.getStringExtra("idDriver")
        Log.d("Detalle conductor", "El id del conductor es $extraIdConductor")
        extraIdConductor?.let { viewModel?.cargarConductor(it) }

        observador()
        listeners()
    }

    private fun observador() {
        viewModel?.mensaje?.observe(this) { mensaje ->
            Utilidades.mostrarToast(mensaje, this)
        }
        viewModel?.conductor?.observe(this) {
            datosConductor = it
            viewModel?.mostrarDatosConductor(it, binding, this)
        }
    }

    private fun listeners() {

        binding.btnUpdate.setOnClickListener { verificacionDatos() }
        binding.circleImageProfile.setOnClickListener { ampliarImagen(datosConductor?.imagenPerfil) }
        binding.imageViewDocumento.setOnClickListener { ampliarImagen(datosConductor?.imagenDocumento) }
        binding.imageViewDocumentoTrasero.setOnClickListener { ampliarImagen(datosConductor?.imagenDocumentoTrasero) }
        binding.circleImageValidacion.setOnClickListener { ampliarImagen(datosConductor?.imagenFotoValidacion) }
        binding.imageViewLicenciaFrontal.setOnClickListener { ampliarImagen(datosConductor?.imagenLicenciaDelantera) }
        binding.imageViewLicenciaTrasera.setOnClickListener { ampliarImagen(datosConductor?.imagenLicenciaTrasera) }
        binding.buttonCambioEstado.setOnClickListener { abrirCambioEstado() }
        binding.buttonHistorial.setOnClickListener { mostrarHistorial() }
    }

    private fun mostrarHistorial() {
        val i = Intent(this, HistoriesActivity::class.java)
        i.putExtra("id", extraIdConductor)
        startActivity(i)
    }

    private fun abrirCambioEstado() {
        val builder = AlertDialog.Builder(this)

        // Obtener la vista inflada desde el archivo XML del diseño del diálogo
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.promt_cambio_estado, null)
        val button_cambiar_estado=dialogView.findViewById<Button>(R.id.button_cambiar_estado)
        val textFieldComentario=dialogView.findViewById<EditText>(R.id.textFieldComentario)
        val spinner_cambiar_estado=dialogView.findViewById<Spinner>(R.id.spinner_cambiar_estado)
        // Configurar el contenido del diálogo
        builder.setView(dialogView)


        button_cambiar_estado.setOnClickListener {
            var nuevoEstado=spinner_cambiar_estado.selectedItem.toString()
            var comentario=textFieldComentario.text.toString()
            if (comentario.isNotEmpty()) nuevoEstado+="\n $comentario"

            actualizarEstado(nuevoEstado )
        finish()
        finish()}



        // Configurar cualquier otra propiedad del cuadro de diálogo según sea necesario
        builder.setTitle("Cambiar Estado")
        builder.setPositiveButton("Cerrar") { dialog, _ -> dialog.dismiss() }

        // Crear y mostrar el cuadro de diálogo
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun actualizarEstado(nuevoEstado: String) {
        Log.d("Detalle referidor","Cambio de estado $nuevoEstado")
        extraIdConductor?.let { driverProvider.updateStatus(it,nuevoEstado)
            .addOnCompleteListener { Utilidades.mostrarToast("Estado actualizado a $nuevoEstado", this) }}
    }

    private fun ampliarImagen(urlImagen: String?) {
        val i = Intent(this, VistaImagen::class.java)
        i.putExtra("urlImagen", urlImagen)
        i.putExtra("nombre", datosConductor?.nombre)
        i.putExtra("apellido", datosConductor?.apellido)
        i.putExtra("tipoDocumento", datosConductor?.tipoDocumento)
        i.putExtra("numeroDocumento", datosConductor?.numeroDocumento)
        startActivity(i)
    }


    private fun verificacionDatos() {


        val datosValidos = viewModel?.validarDatos(binding)

        if (datosValidos == true) {
            updateInfo()
        }
    }


    val progressDialog: ProgressDialog by lazy {
        ProgressDialog(this).apply {
            setMessage("Guardando...")
            setCancelable(true)
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
        }
    }


    private fun updateInfo() {
        progressDialog!!.show()

        binding.btnUpdate.isEnabled = false

        val name = binding.textFieldName.text.toString()
        val lastname = binding.textFieldLastname.text.toString()
        val numeroDocumento = binding.textFieldDocumento.text.toString()
        val tipoDocumento = binding.spinner.selectedItem.toString()

        val client = ConductorModelo(
            id = extraIdConductor,
            nombre = name,
            apellido = lastname,
            tipoDocumento = tipoDocumento,
            numeroDocumento = numeroDocumento,
        )

        driverProvider.update(client).addOnCompleteListener {
            if (!it.isSuccessful) {
                Utilidades.mostrarToast("No se pudo actualizar la información", this)
            }else{
                Utilidades.mostrarToast("Datos Actualizados", this)
            }
            progressDialog.dismiss()
        }

    }


}

