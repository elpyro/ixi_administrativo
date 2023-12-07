package app.castellanoseloy.ixi_administrativo.activitys.addCar

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.castellanoseloy.ixi_administrativo.R
import app.castellanoseloy.ixi_administrativo.databinding.ActivityAddCarBinding
import app.castellanoseloy.ixi_administrativo.modelos.CarModel
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades
import app.castellanoseloy.ixi_administrativo.provinders.CarProvider
import com.bumptech.glide.Glide

class DetalleVehiculoViewModel : ViewModel() {


    private val _elementosActualizar = MutableLiveData<Int>()
    val mensaje=MutableLiveData<String> ()

    val elementosActualizar: LiveData<Int>
        get() = _elementosActualizar

    init {
        _elementosActualizar.value = 1
    }

    fun incrementarElementos() {
        val nuevoValor = _elementosActualizar.value?.plus(1)
        _elementosActualizar.value = nuevoValor!!
    }
    fun decrementarElementos() {
        val nuevoValor = _elementosActualizar.value?.minus(1)
        _elementosActualizar.value = nuevoValor!!
    }







    fun salveData(
        idCar: String,
        binding: ActivityAddCarBinding,
    ) {

        val placa = binding.textFieldPlaca.text.toString()
        val marca = binding.textFielMarca.text.toString()
        val tipo = binding.spinner.selectedItem.toString()
        val pasajeros = binding.textFielAsisentos.text.toString().toInt()
        val malero=binding.switchMaletero.isChecked
        val tecnomecanica=binding.checkBoxTecnomecanica.isChecked
        val SOAT=binding.checkBoxSoat.isChecked

        val car = CarModel(
            id= idCar,
            bland = marca,
            tipo =tipo,
            placa = placa,
            pasajeros= pasajeros,
            malero= malero,
            tecnomecanica= tecnomecanica,
            SOAT=SOAT
        )

        val carProvider=CarProvider()
        carProvider.updateCar(car).addOnCompleteListener {
            if(it.isSuccessful){
                decrementarElementos()
            }else{
                decrementarElementos()
                mensaje.value="Error guardando datos"
                Log.e("Firebase", "ERROR GUARDANDO EN LA NUBE: ${it.exception}")
            }
        }

    }

    fun validarDatos(
        binding: ActivityAddCarBinding,
    ): Boolean {

        var datosValidos=true


        if(binding.textFieldPlaca.text.toString().isEmpty()){
            datosValidos=false
            binding.textFieldPlaca.setError("Obligatorio")
        }
        if(binding.textFielMarca.text.toString().isEmpty()){
            datosValidos=false
            binding.textFielMarca.setError("Obligatorio")
        }
        if(binding.textFielAsisentos.text.toString().isEmpty()){
            datosValidos=false
            binding.textFielAsisentos.setError("Obligatorio")
        }

        return datosValidos

    }

    fun cargarDatos(idCar: String, binding: ActivityAddCarBinding, applicationContext: Context,  callback: (CarModel?) -> Unit) {

        val carProvider=CarProvider()

        carProvider.getCartById(idCar).addOnSuccessListener { document ->
            if (document.exists()) {

                val carModel = document.toObject(CarModel::class.java)
                callback(carModel)
                if (carModel?.imagenFrontal != null) {
                    try {
                        Glide.with(applicationContext).load(carModel?.imagenFrontal).into(binding.circleImageFotoFrontal!!)
                    }catch (e: Exception){  }
                }
                if (carModel?.imagenLateral != null) {
                    try {
                        Glide.with(applicationContext).load(carModel?.imagenLateral).into(binding.circleImageFotoLateral!!)
                    }catch (e: Exception){  }
                }
                if (carModel?.imagenDocumento != null) {
                    try {
                        Glide.with(applicationContext).load(carModel?.imagenDocumento).into(binding.imageViewDocumentoVehiculoFontal!!)
                    }catch (e: Exception){  }
                }
                if (carModel?.imagenDocumentoTrasero != null) {
                    try {
                        Glide.with(applicationContext).load(carModel?.imagenDocumentoTrasero).into(binding.imageViewDocumentoVehiculoTrasero!!)
                    }catch (e: Exception){  }
                }

                binding.textFieldPlaca.setText(carModel?.placa)
                binding.textFielMarca.setText(carModel?.bland)
                binding.textFielAsisentos.setText(carModel?.pasajeros!!.toString())
                binding.switchMaletero.setChecked(carModel.malero!!)
                binding.checkBoxTecnomecanica.isChecked=carModel.tecnomecanica!!
                binding.checkBoxSoat.isChecked=carModel.SOAT!!

                //estblecer el valor del spinner
                val opcionesArray = applicationContext.resources.getStringArray(R.array.tipoVehiculos)
                val indiceValorPredeterminado = opcionesArray.indexOf(carModel.tipo)
                if (indiceValorPredeterminado != -1) {
                    // Establecer el valor predeterminado en el Spinner
                    binding?.spinner?.setSelection(indiceValorPredeterminado)
                }
                binding.imageViewCarColor.setColorFilter(carModel?.color!!)

                if (carModel?.estado?.contains("Valida tu vehiculo")!!) {
                    binding.imageEstado.setImageResource(
                        R.drawable.ic_valida_datos
                    )

                }
                if (carModel?.estado?.contains("Verificando Datos")!!) {
                    binding.imageEstado.setImageResource(
                        R.drawable.ic_pendiente_validar
                    )


                }
                if (carModel?.estado?.contains("Validado")!!) {
                    binding.imageEstado.setImageResource(
                        R.drawable.ic_validado
                    )


                }
                if (carModel?.estado?.contains("Prohibido")!!) {
                    binding.imageEstado.setImageResource(
                        R.drawable.ic_prohibido
                    )

                }
                binding.textViewEstado.text = carModel?.estado



            } else {
                Log.e("Firestore", "No se encontraron datos del cliente")
            }

    }
    }

     fun actualizarEstado(id:String,nuevoEstado: String, context: Context) {
        Log.d("Detalle referidor","Cambio de estado $nuevoEstado")
        val carProvider=CarProvider()
        id?.let { carProvider.updateStatus(it,nuevoEstado)
            .addOnCompleteListener { Utilidades.mostrarToast("Estado actualizado a $nuevoEstado", context) }}
    }


}