package app.castellanoseloy.ixi_administrativo.activitys.vistaImagen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.castellanoseloy.ixi_administrativo.databinding.ActivityVistaImagenBinding

class VistaImagen : AppCompatActivity() {

    private lateinit var binding: ActivityVistaImagenBinding
    private var viewModel: VistaImagenViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVistaImagenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val extraUrlImagen = intent.getStringExtra("urlImagen")
        val extraNombre= intent.getStringExtra("nombre")
        val extraApellido= intent.getStringExtra("apellido")
        val extraTipoDocumento= intent.getStringExtra("tipoDocumento")
        val extraNumeroDocumento= intent.getStringExtra("numeroDocumento")

        viewModel = ViewModelProvider(this)[VistaImagenViewModel::class.java]

        val mostrarDatos = vistaDatos(
            nombre = extraNombre.orEmpty(),
            apellido = extraApellido.orEmpty(),
            tipoDocumento = extraTipoDocumento.orEmpty(),
            numeroDocumento = extraNumeroDocumento.orEmpty(),
            urlImagen = extraUrlImagen.orEmpty()
        )
        viewModel?.setMostrarDatos(mostrarDatos)

        observadores()
    }

    private fun observadores() {
        viewModel?.mostrarDatos?.observe(this, Observer {
            it?.let { datos ->
                viewModel?.actualizarActivity(datos, binding, this)
            }
        })
    }

    data class vistaDatos(
        val nombre: String,
        val apellido: String,
        val tipoDocumento: String,
        val numeroDocumento: String,
        val urlImagen: String
    )
}