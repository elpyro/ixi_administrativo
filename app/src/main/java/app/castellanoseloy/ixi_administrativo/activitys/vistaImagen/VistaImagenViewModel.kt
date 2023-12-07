package app.castellanoseloy.ixi_administrativo.activitys.vistaImagen

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.castellanoseloy.ixi_administrativo.databinding.ActivityVistaImagenBinding
import com.bumptech.glide.Glide

class VistaImagenViewModel: ViewModel() {

    private val _mostrarDatos = MutableLiveData<VistaImagen.vistaDatos>()
    val mostrarDatos: LiveData<VistaImagen.vistaDatos> get() = _mostrarDatos

    fun setMostrarDatos(datos: VistaImagen.vistaDatos) {
        _mostrarDatos.value = datos
    }

    fun actualizarActivity(
        vistaDatos: VistaImagen.vistaDatos,
        binding: ActivityVistaImagenBinding,
        context: Context
    ) {
        if (vistaDatos?.urlImagen != "") {
            Glide.with(context).load(vistaDatos?.urlImagen)
                .into(binding.photoView)
        }
        binding.textViewNombre.text=vistaDatos.nombre
        binding.textViewApellido.text=vistaDatos.apellido
        binding.textViewTipoDocumento.text=vistaDatos.tipoDocumento+":"
        binding.textViewNumeroDocumento.text=vistaDatos.numeroDocumento
    }

}