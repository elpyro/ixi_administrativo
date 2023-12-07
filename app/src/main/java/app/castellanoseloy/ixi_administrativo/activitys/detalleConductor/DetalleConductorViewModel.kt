package app.castellanoseloy.ixi_administrativo.activitys.detalleConductor

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.castellanoseloy.ixi_administrativo.R
import app.castellanoseloy.ixi_administrativo.databinding.ActivityDetalleConductorBinding
import app.castellanoseloy.ixi_administrativo.modelos.ConductorModelo
import app.castellanoseloy.ixi_administrativo.provinders.DriverProvider
import com.bumptech.glide.Glide

class DetalleConductorViewModel : ViewModel() {

    val mensaje = MutableLiveData<String>()
    val conductor = MutableLiveData<ConductorModelo>()

    fun validarDatos(
        binding: ActivityDetalleConductorBinding
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

    fun cargarConductor(idConductor: String) {

        val conductorProveedor = DriverProvider()
        conductorProveedor.getDriver(idConductor)
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    conductor.value = document.toObject(ConductorModelo::class.java)
                }
            }
    }

    fun mostrarDatosConductor(
        conductor: ConductorModelo?,
        binding: ActivityDetalleConductorBinding,
        context: Context
    ) {

        binding.textFieldName.setText(conductor?.nombre)
        binding.textFieldLastname.setText(conductor?.apellido)
        binding.textFieldPhone.setText(conductor?.telefono)
        binding.textFieldDocumento.setText(conductor?.numeroDocumento)
        //estblecer el valor del spinner
        val opcionesArray =
            context.resources.getStringArray(R.array.tipoDocumento)
        val indiceValorPredeterminado = opcionesArray.indexOf(conductor?.tipoDocumento)
        if (indiceValorPredeterminado != -1) {
            // Establecer el valor predeterminado en el Spinner
            binding?.spinner?.setSelection(indiceValorPredeterminado)
        }

        if (conductor?.validacion?.contains("Valida tu usuario")!!) {
            binding.imageEstado.setImageResource(
                R.drawable.ic_valida_datos
            )
            binding.textViewEstado.text = conductor?.validacion
        }
        if (conductor?.validacion?.contains("Verificando Datos")!!) {
            binding.imageEstado.setImageResource(
                R.drawable.ic_pendiente_validar
            )
            binding.textViewEstado.text = conductor?.validacion

        }
        if (conductor?.validacion?.contains("Validado")!!) {
            binding.imageEstado.setImageResource(
                R.drawable.ic_validado
            )
            binding.textViewEstado.text = conductor?.validacion

        }
        if (conductor?.validacion?.contains("Bloqueado")!!) {
            binding.imageEstado.setImageResource(
                R.drawable.ic_prohibido
            )
            binding.textViewEstado.text = conductor?.validacion
        }


        if (conductor?.imagenPerfil != null) {
            if (conductor!!.imagenPerfil != "") {
                Glide.with(context).load(conductor!!.imagenPerfil)
                    .into(binding.circleImageProfile)
            }
        }

        if (conductor?.imagenDocumento != null) {
            if (conductor!!.imagenDocumento != "") {
                Glide.with(context).load(conductor!!.imagenDocumento)
                    .into(binding.imageViewDocumento)
            }
        }

        if (conductor?.imagenDocumentoTrasero != null) {
            if (conductor!!.imagenDocumentoTrasero != "") {
                Glide.with(context).load(conductor!!.imagenDocumentoTrasero)
                    .into(binding.imageViewDocumentoTrasero)
            }
        }

        if (conductor?.imagenFotoValidacion != null) {
            if (conductor!!.imagenFotoValidacion != "") {
                binding.circleImageValidacion.visibility = View.VISIBLE
                Glide.with(context).load(conductor!!.imagenFotoValidacion)
                    .into(binding.circleImageValidacion)
            }
        }

        if (conductor?.imagenLicenciaDelantera != null) {
            if (conductor!!.imagenLicenciaDelantera != "") {
                binding.imageViewLicenciaFrontal.visibility = View.VISIBLE
                Glide.with(context).load(conductor!!.imagenLicenciaDelantera)
                    .into(binding.imageViewLicenciaFrontal)
            }
        }
        if (conductor?.imagenLicenciaTrasera != null) {
            if (conductor!!.imagenLicenciaTrasera != "") {
                binding.imageViewLicenciaTrasera.visibility = View.VISIBLE
                Glide.with(context).load(conductor!!.imagenLicenciaTrasera)
                    .into(binding.imageViewLicenciaTrasera)
            }
        }


    }


}