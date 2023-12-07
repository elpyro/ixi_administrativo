package app.castellanoseloy.ixi_administrativo.activitys.configuraciones

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import app.castellanoseloy.ixi_administrativo.databinding.ActivityConfiguracionesBinding

class Configuraciones : AppCompatActivity() {
    private lateinit var binding: ActivityConfiguracionesBinding
    private var viewModel: ConfiguracionesViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracionesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ConfiguracionesViewModel::class.java)

        viewModel?.obtenerConfiguraciones(binding)
        listenes()
    }

    private fun listenes() {
        binding.button2.setOnClickListener {
            viewModel?.guardar(binding)
            finish()
        }
    }
}