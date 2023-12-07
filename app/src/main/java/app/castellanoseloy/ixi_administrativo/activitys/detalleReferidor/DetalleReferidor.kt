package app.castellanoseloy.ixi_administrativo.activitys.detalleReferidor

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
import app.castellanoseloy.ixi_administrativo.databinding.ActivityDetalleReferidorBinding
import app.castellanoseloy.ixi_administrativo.modelos.ClienteModelo
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades
import app.castellanoseloy.ixi_administrativo.provinders.ClienteProveedor


class DetalleReferidor : AppCompatActivity() {

    private var datosCliente: ClienteModelo? = null
    private var extraIdCliente: String? = null
    private lateinit var binding: ActivityDetalleReferidorBinding
    val clientProvider = ClienteProveedor()

    private var viewModel: DetalleReferidorViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleReferidorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Detalle Referidor"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setTitleTextColor(Color.WHITE)

        viewModel = ViewModelProvider(this).get(DetalleReferidorViewModel::class.java)

        extraIdCliente = intent.getStringExtra("idCliente")
        Log.d("Detalle referidor", "El id del cliente es $extraIdCliente")
        extraIdCliente?.let { viewModel?.cargarCliente(it) }

        observador()
        listeners()
    }

    private fun observador() {
        viewModel?.mensaje?.observe(this) { mensaje ->
            Utilidades.mostrarToast(mensaje, this)
        }
        viewModel?.cliente?.observe(this) {
            datosCliente = it
            viewModel?.mostrarDatosCliente(it, binding, this)
        }
    }

    private fun listeners() {

        binding.btnUpdate.setOnClickListener { verificacionDatos() }
        binding.circleImageProfile.setOnClickListener { ampliarImagen(datosCliente?.imagenPerfil) }
        binding.imageViewDocumento.setOnClickListener { ampliarImagen(datosCliente?.imagenDocumento) }
        binding.imageViewDocumentoTrasero.setOnClickListener { ampliarImagen(datosCliente?.imagenDocumentoTrasero) }
        binding.circleImageValidacion.setOnClickListener { ampliarImagen(datosCliente?.imagenFotoValidacion) }
        binding.buttonCambioEstado.setOnClickListener { abrirCambioEstado() }
        binding.buttonHistorial.setOnClickListener { mostrarHistorial() }
    }

    private fun mostrarHistorial() {
        val i = Intent(this, HistoriesActivity::class.java)
        i.putExtra("id", extraIdCliente)
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
        extraIdCliente?.let { clientProvider.updateStatus(it,nuevoEstado)
            .addOnCompleteListener { Utilidades.mostrarToast("Estado actualizado a $nuevoEstado", this) }}
    }

    private fun ampliarImagen(urlImagen: String?) {
        val i = Intent(this, VistaImagen::class.java)
        i.putExtra("urlImagen", urlImagen)
        i.putExtra("nombre", datosCliente?.nombre)
        i.putExtra("apellido", datosCliente?.apellido)
        i.putExtra("tipoDocumento", datosCliente?.tipoDocumento)
        i.putExtra("numeroDocumento", datosCliente?.numeroDocumento)
        startActivity(i)
    }


    private fun verificacionDatos() {


        val datosValidos = viewModel?.validarDatos(binding)

        if (datosValidos == true) {
            updateInfo()
        }
    }


//    private fun alertCuentaExistente() {
//        val alertDialogBuilder = AlertDialog.Builder(this)
//        alertDialogBuilder.setTitle("Validar Datos")
//        alertDialogBuilder.setMessage("Detectamos que ya tienes una cuenta asociada con estas credenciales. \nPara realizar un cambio de cuenta al nuevo número ${cliente?.telefono}, por favor sigue estos pasos:\n\n1. Tómate una foto de tu documento de identidad.\n2. Actualiza tus datos para validar la solicitud de cambio de cuenta.")
//
//        // Botón Aceptar
//        alertDialogBuilder.setPositiveButton("Aceptar") { dialog, which ->
//
//        }
//        val alertDialog: AlertDialog = alertDialogBuilder.create()
//        alertDialog.show()
//    }

//    private fun alertaConfirmacionDatos() {
//
//        var editarDatos = false
//        if (cliente?.numeroDocumento != null) editarDatos = true
//        val datosValidos = viewModel?.validarDatos(
//            binding,
//            fotoPerfil,
//            documento,
//            documentoTrasero,
//            fotoValidacion,
//            editarDatos
//        )
//
//        if (datosValidos == true) {
//            val alertDialogBuilder = AlertDialog.Builder(this)
//            alertDialogBuilder.setTitle("Validar Datos")
//            alertDialogBuilder.setMessage("¿Desea enviar a verificar sus datos?, una vez enviado sus datos no podran ser cambiados hasta ser verificados")
//
//            // Botón Aceptar
//            alertDialogBuilder.setPositiveButton("Si") { dialog, which ->
//                updateInfo()
//            }
//            // Botón Cancelar
//            alertDialogBuilder.setNegativeButton("No") { dialog, which ->
//            }
//            val alertDialog: AlertDialog = alertDialogBuilder.create()
//            alertDialog.show()
//        }
//    }

//
//    private val startImageForResult =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//
//            val resultCode = result.resultCode
//            val data = result.data
//
//            if (resultCode == Activity.RESULT_OK) {
//                val fileUri = data?.data
//
//                if (imagenSeleccionada.equals("fotoPerfil")) {
//                    fotoPerfil = File(fileUri?.path)
//                    binding.circleImageProfile?.setImageURI(fileUri)
//                }
//                if (imagenSeleccionada.equals("documentoIdentidad")) {
//                    documento = File(fileUri?.path)
//                    binding.imageViewDocumento?.setImageURI(fileUri)
//                }
//                if (imagenSeleccionada.equals("documentoIdentidadTrasero")) {
//                    documentoTrasero = File(fileUri?.path)
//                    binding.imageViewDocumentoTrasero?.setImageURI(fileUri)
//                }
//                if (imagenSeleccionada.equals("fotoValidacion")) {
//                    fotoValidacion = File(fileUri?.path)
//                    binding.circleImageValidacion?.setImageURI(fileUri)
//                }
//            } else if (resultCode == ImagePicker.RESULT_ERROR) {
//                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(this, "Tarea cancelada", Toast.LENGTH_LONG).show()
//            }
//
//        }

//    private fun selectImage() {
//        imagenSeleccionada = "fotoPerfil"
//        ImagePicker.with(this)
//            .crop()
//            .cameraOnly()
//            .compress(1024)
//            .maxResultSize(1080, 1080)
//            .createIntent { intent ->
//                startImageForResult.launch(intent)
//            }
//    }
//
//    private fun selectDocument() {
//        imagenSeleccionada = "documentoIdentidad"
//        ImagePicker.with(this)
//            .crop(16.0F, 9.0F)
//            .compress(1024)
//            .maxResultSize(1080, 1080)
//            .createIntent { intent ->
//                startImageForResult.launch(intent)
//            }
//    }
//
//    private fun selectLicence() {
//        imagenSeleccionada = "documentoIdentidadTrasero"
//        ImagePicker.with(this)
//            .crop(16.0F, 9.0F)
//            .compress(1024)
//            .maxResultSize(1080, 1080)
//            .createIntent { intent ->
//                startImageForResult.launch(intent)
//            }
//    }
//
//    private fun selectFotoValidacion() {
//        imagenSeleccionada = "fotoValidacion"
//        ImagePicker.with(this)
//            .crop()
//            .cameraOnly()
//            .compress(1024)
//            .maxResultSize(1080, 1080)
//            .createIntent { intent ->
//                startImageForResult.launch(intent)
//            }
//    }

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

        val client = ClienteModelo(
            id = extraIdCliente,
            nombre = name,
            apellido = lastname,
            tipoDocumento = tipoDocumento,
            numeroDocumento = numeroDocumento,
        )

        clientProvider.update(client).addOnCompleteListener {
            if (!it.isSuccessful) {
                Utilidades.mostrarToast("No se pudo actualizar la información", this)
            }else{
                Utilidades.mostrarToast("Datos Actualizados", this)
            }
            progressDialog.dismiss()
        }

    }


}

