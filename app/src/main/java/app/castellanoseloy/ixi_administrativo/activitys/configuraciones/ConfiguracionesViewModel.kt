package app.castellanoseloy.ixi_administrativo.activitys.configuraciones

import android.view.View
import androidx.lifecycle.ViewModel
import app.castellanoseloy.ixi_administrativo.databinding.ActivityConfiguracionesBinding
import app.castellanoseloy.ixi_administrativo.modelos.PreciosModelo
import com.optic.uberclonekotlin.providers.ConfigProvider

class ConfiguracionesViewModel : ViewModel() {
    fun obtenerConfiguraciones(binding: ActivityConfiguracionesBinding) {
        val configProvider = ConfigProvider()
        configProvider.getPrices()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val precios = document.toObject(PreciosModelo::class.java)
                    binding.textFieldPrecioMin.setText(precios?.min.toString())
                    binding.textFieldValorKm.setText(precios?.km.toString())
                    binding.textFieldPrecioMinimo.setText(precios?.minValue.toString())
                    binding.textFieldTarifa.setText(precios?.tarifaApp.toString())
                    binding.textFieldPorcentaje.setText(precios?.porcentajeTarifaApp.toString())

                    binding.button2.visibility = View.VISIBLE
                }
            }

        configProvider.getWhastAppInformacion()
            .addOnSuccessListener { documentSnapshot ->
                // Verifica si el documento existe y contiene el campo "ayudaNumero"
                if (documentSnapshot.exists() ) {
                    val numeroWhatsApp = documentSnapshot.getString("ayudaNumero")
                    val grupoWhatsApp = documentSnapshot.getString("grupoUrl")
                    binding.textFieldTelefonoAyuda.setText(numeroWhatsApp.toString())
                    binding.textFieldGrupo.setText(grupoWhatsApp.toString())
                }
            }

    }

    fun guardar(binding: ActivityConfiguracionesBinding) {
        val numeroAyuda=binding.textFieldTelefonoAyuda.text.toString()
        val grupoWhastapp=binding.textFieldGrupo.text.toString()

        val minutoInput = binding.textFieldPrecioMin.text.toString()
        val kmInput = binding.textFieldValorKm.text.toString()
        val precioMinimoInput = binding.textFieldPrecioMinimo.text.toString()
        val tarifaAppInput = binding.textFieldTarifa.text.toString()
        val porcentajeInput = binding.textFieldPorcentaje.text.toString()

        val minuto = obtenerValor(minutoInput)
        val km = obtenerValor(kmInput)
        val precioMinimo = obtenerValor(precioMinimoInput)
        val tarifaApp = obtenerValor(tarifaAppInput)
        val porcentaje = obtenerValor(porcentajeInput)

        val preciosModelo = PreciosModelo(
            min = minuto,
            km = km,
            minValue = precioMinimo,
            tarifaApp = tarifaApp,
            porcentajeTarifaApp = porcentaje
        )

        val configProvider = ConfigProvider()
        configProvider.actualizarPrices(preciosModelo)
        configProvider.actualizarWhastapps(numeroAyuda,grupoWhastapp)

    }

    fun obtenerValor(texto: String): Double {
        return if (texto.isBlank()) {
            0.0
        } else {
            try {
                texto.toDouble()
            } catch (e: NumberFormatException) {
                // Manejar la conversión a double si es necesario
                0.0
            }
        }
    }

}