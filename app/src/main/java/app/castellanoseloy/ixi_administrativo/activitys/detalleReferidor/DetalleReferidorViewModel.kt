package app.castellanoseloy.ixi_administrativo.activitys.detalleReferidor

import android.content.Context
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.castellanoseloy.ixi_administrativo.R
import app.castellanoseloy.ixi_administrativo.databinding.ActivityDetalleReferidorBinding
import app.castellanoseloy.ixi_administrativo.modelos.ClienteModelo
import app.castellanoseloy.ixi_administrativo.provinders.ClienteProveedor
import com.bumptech.glide.Glide
import java.io.File

class DetalleReferidorViewModel:ViewModel() {

    val mensaje = MutableLiveData<String>()
    val cliente = MutableLiveData<ClienteModelo>()

    fun validarDatos(
        binding: ActivityDetalleReferidorBinding
    ): Boolean {

        var datosValidos = true

        if (binding.textFieldName.text.toString().isEmpty()) {
            datosValidos = false
            binding.textFieldName.setError("Obligatorio")
        }
        if (binding.textFieldLastname.text.toString().isEmpty()) {
            datosValidos = false
            binding.textFieldLastname.setError("Obligatorio")
        }
        if (binding.textFieldDocumento.text.toString().isEmpty()) {
            datosValidos = false
            binding.textFieldDocumento.setError("Obligatorio")
        }

        return datosValidos

    }

    fun cargarCliente(idCliente:String) {

            val clienteProveedor=ClienteProveedor()
            clienteProveedor.getClient(idCliente)
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        cliente.value= document.toObject(ClienteModelo::class.java)
                    }
                }
    }

    fun mostrarDatosCliente(cliente: ClienteModelo?, binding: ActivityDetalleReferidorBinding, context: Context) {

                    binding.textFieldName.setText(cliente?.nombre)
                    binding.textFieldLastname.setText(cliente?.apellido)
                    binding.textFieldPhone.setText(cliente?.telefono)
                    binding.textFieldDocumento.setText(cliente?.numeroDocumento)
                    //estblecer el valor del spinner
                    val opcionesArray =
                        context.resources.getStringArray(R.array.tipoDocumento)
                    val indiceValorPredeterminado = opcionesArray.indexOf(cliente?.tipoDocumento)
                    if (indiceValorPredeterminado != -1) {
                        // Establecer el valor predeterminado en el Spinner
                        binding?.spinner?.setSelection(indiceValorPredeterminado)
                    }

                    if (cliente?.validacion?.contains("Valida tu usuario")!!) {
                        binding.imageEstado.setImageResource(
                            R.drawable.ic_valida_datos
                        )
                        binding.textViewEstado.text = cliente?.validacion
                    }
                    if (cliente?.validacion?.contains("Verificando Datos")!!) {
                        binding.imageEstado.setImageResource(
                            R.drawable.ic_pendiente_validar
                        )
                        binding.textViewEstado.text = cliente?.validacion
                        
                    }
                    if (cliente?.validacion?.contains("Validado")!!) {
                        binding.imageEstado.setImageResource(
                            R.drawable.ic_validado
                        )
                        binding.textViewEstado.text = cliente?.validacion
                        
                    }
                    if (cliente?.validacion?.contains("Bloqueado")!!) {
                        binding.imageEstado.setImageResource(
                            R.drawable.ic_prohibido
                        )
                        binding.textViewEstado.text = cliente?.validacion
                    }


                    if (cliente?.imagenPerfil != null) {
                        if (cliente!!.imagenPerfil != "") {
                            Glide.with(context).load(cliente!!.imagenPerfil)
                                .into(binding.circleImageProfile)
                        }
                    }

                    if (cliente?.imagenDocumento != null) {
                        if (cliente!!.imagenDocumento != "") {
                            Glide.with(context).load(cliente!!.imagenDocumento)
                                .into(binding.imageViewDocumento)
                        }
                    }

                    if (cliente?.imagenDocumentoTrasero != null) {
                        if (cliente!!.imagenDocumentoTrasero != "") {
                            Glide.with(context).load(cliente!!.imagenDocumentoTrasero)
                                .into(binding.imageViewDocumentoTrasero)
                        }
                    }

                    if (cliente?.imagenFotoValidacion != null) {
                        if (cliente!!.imagenFotoValidacion != "") {
                            binding.circleImageValidacion.visibility = View.VISIBLE
                            Glide.with(context).load(cliente!!.imagenFotoValidacion)
                                .into(binding.circleImageValidacion)
                        }
                    }

                }


//    fun verificarDocumento(
//        clienteActual: ClienteModelo,
//        nuevoNumeroDocumento: String,
//        tipoDocumento: String,
//        callback: (Boolean) -> Unit
//    ) {
//        val clienteProvider = ClienteProveedor()
//        clienteProvider.getCClientePorDocumento(nuevoNumeroDocumento)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val querySnapshot = task.result
//                    if (querySnapshot != null) {
//                        val conductores =
//                            querySnapshot.documents.mapNotNull { it.toObject(ClienteModelo::class.java) }
//                        // 'conductores' ahora contiene una lista de objetos ConductorModelo que coinciden con el número de documento
//                        for (conductor in conductores) {
//                            if (clienteActual.id.toString() != conductor.id.toString() && tipoDocumento == conductor.tipoDocumento ){
//                                // Realiza las acciones que necesites con cada objeto ConductorModelo
//                                Log.d("Firebase", "Conductor encontrado: ${conductor.id.toString()}")
//                                callback(true)
//                                return@addOnCompleteListener
//                            }
//                        }
//                    }
//                    callback(false)
//                } else {
//                    Log.e("Firebase", "Error al obtener el resultado de la consulta")
//                }
//            }
//    }


}