package app.castellanoseloy.ixi_administrativo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.castellanoseloy.ixi_administrativo.activitys.configuraciones.Configuraciones
import app.castellanoseloy.ixi_administrativo.activitys.listaValidarConductores.ListaValidarConductores
import app.castellanoseloy.ixi_administrativo.activitys.listaValidarVehiculos.ListaValidarVehiculos
import app.castellanoseloy.ixi_administrativo.activitys.listaValidarRereridores.ListaValidarReferidores
import app.castellanoseloy.ixi_administrativo.activitys.reportes.Reportes
import app.castellanoseloy.ixi_administrativo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listeners()
    }

    private fun listeners() {
       binding.buttonValidarReferidores.setOnClickListener {
           val i = Intent(this, ListaValidarReferidores::class.java)
           startActivity(i)
       }
        binding.buttonValidarConductores.setOnClickListener {
            val i = Intent(this, ListaValidarConductores::class.java)
            startActivity(i)
        }
        binding.buttonValidarVehiculos.setOnClickListener {
            val i = Intent(this, ListaValidarVehiculos::class.java)
            startActivity(i)
        }
        binding.buttonSaldos.setOnClickListener {
            val i = Intent(this, Reportes::class.java)
            startActivity(i)
        }
        binding.buttonConfiguraciones.setOnClickListener {
            val i = Intent(this, Configuraciones::class.java)
            startActivity(i)
        }
    }
}