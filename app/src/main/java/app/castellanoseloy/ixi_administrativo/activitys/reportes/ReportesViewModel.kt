package app.castellanoseloy.ixi_administrativo.activitys.reportes

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModel
import app.castellanoseloy.ixi_administrativo.activitys.historiesActivity.HistoriesActivity
import app.castellanoseloy.ixi_administrativo.activitys.historiesActivity.HistoriesViajesCanceladosActivity
import app.castellanoseloy.ixi_administrativo.activitys.listaTransacciones.ListaTransacciones

import app.castellanoseloy.ixi_administrativo.databinding.ActivitySaldosBinding
import app.castellanoseloy.ixi_administrativo.modelos.History
import app.castellanoseloy.ixi_administrativo.modelos.TransaccionesModelo
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades.convertirStringADate
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades.convertirStringAUnix
import app.castellanoseloy.ixi_administrativo.provinders.TransaccionesProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.optic.uberclonedriverkotlin.providers.HistoryCancelsProvider
import com.optic.uberclonedriverkotlin.providers.HistoryProvider
import java.util.Calendar

class ReportesViewModel : ViewModel() {


    fun mostrarDatePickerDialog(textView: TextView, context: Reportes) {
        val c = Calendar.getInstance()
        val dia = c[Calendar.DAY_OF_MONTH]
        val mes = c[Calendar.MONTH]
        val ano = c[Calendar.YEAR]

        val datepickerDialogo = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formattedMonth = month + 1
                val formattedDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                val formattedDate = "$formattedDayOfMonth/$formattedMonth/$year"
                textView.text = formattedDate
            }, ano, mes, dia
        )
        datepickerDialogo.show()
    }

    fun mostrarReporte(binding: ActivitySaldosBinding, tipo: String, context: Context) {
        var fechaInicio = "01/01/2000"
        var fechaFin = "01/01/2050"

        if (binding?.textViewDesde?.text.toString() != "Desde") fechaInicio =
            binding?.textViewDesde?.text.toString()
        if (binding?.textViewHasta?.text.toString() != "Hasta") fechaFin =
            binding?.textViewHasta?.text.toString()

        if (tipo == "Movimientos Nequi") obtenerMovimientos("Nequi", fechaInicio, fechaFin, context)
        if (tipo == "Comision de viajes") obtenerMovimientos("ixi-cobros", fechaInicio, fechaFin, context)
        if (tipo == "Viajes Realizados") obtenerViajes(fechaInicio.convertirStringAUnix(), fechaFin.convertirStringAUnix(),context)
        if (tipo == "Viajes Cancelados") obtenerViajesCancelados(fechaInicio.convertirStringAUnix(), fechaFin.convertirStringAUnix(),context)
    }

    private fun obtenerViajesCancelados(fechaInicio: Long, fechaFin: Long, context: Context) {
        val historyCancelsProvider=HistoryCancelsProvider()
        val query = historyCancelsProvider.getHistoriesBetweenDates(fechaInicio, fechaFin)

        query.get().addOnSuccessListener { querySnapshot ->
            val viajesCancelados = mutableListOf<History>()
            Log.d("ReportesHistorial","total historicos:  ${querySnapshot.size()}")
            for (document in querySnapshot.documents) {
                val history = document.toObject(History::class.java)
                history?.let { viajesCancelados.add(it) }
            }
            mortrarViajesCancelados(viajesCancelados,context)
        }.addOnFailureListener { exception ->
            Log.e("ReportesHistorial", "Error al obtener historias entre fechas: ${exception.message}")
        }
    }

    private fun mortrarViajesCancelados(viajesCancelados: MutableList<History>, context: Context) {
        val intent = Intent(context, HistoriesViajesCanceladosActivity::class.java)
        intent.putExtra("viajesCanceladosPorFecha", ArrayList(viajesCancelados))
        context.startActivity(intent)
    }

    private fun obtenerViajes(fechaInicio: Long, fechaFin: Long,context: Context) {
        val historyProvider=HistoryProvider()
        val query = historyProvider.getHistoriesBetweenDates(fechaInicio, fechaFin)

        query.get().addOnSuccessListener { querySnapshot ->
            val viajesRealizados = mutableListOf<History>()
            Log.d("ReportesHistorial","total historicos:  ${querySnapshot.size()}")
            for (document in querySnapshot.documents) {
                val history = document.toObject(History::class.java)
                history?.let { viajesRealizados.add(it) }
            }
            mortrarViajesRealizados(viajesRealizados,context)
        }.addOnFailureListener { exception ->
            Log.e("ReportesHistorial", "Error al obtener historias entre fechas: ${exception.message}")
        }
    }

    private fun mortrarViajesRealizados(viajesRealizados: MutableList<History>, context: Context) {
        val intent = Intent(context, HistoriesActivity::class.java)
        intent.putExtra("viajesRealizadosPorFecha", ArrayList(viajesRealizados))
        context.startActivity(intent)
    }

    private fun obtenerMovimientos(
        banco: String,
        fechaInicio: String,
        fechaFin: String,
        context: Context
    ) {
        val transaccionesProvider = TransaccionesProvider()
        // Llamada a obtenerTodasRecargas
        transaccionesProvider.obtenerRecargasPorBanco(banco)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Manejar los datos obtenidos en "snapshot"
                    val recargasFiltradas = mutableListOf<TransaccionesModelo>()

                    for (recargaSnapshot in snapshot.children) {
                        val recarga = recargaSnapshot.getValue(TransaccionesModelo::class.java)
                        recarga?.let {
                            if (recarga.fecha!! >= fechaInicio.convertirStringADate() && recarga.fecha!! <= fechaFin.convertirStringADate()) recargasFiltradas.add(
                                it
                            )
                        }
                    }

                    mostrarRecargas(recargasFiltradas, context)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar errores de lectura de datos
                    Log.d("REALTIME_DB", "Error al obtener recargas: ${error.message}")
                }
            })
    }

    private fun mostrarRecargas(recargas: MutableList<TransaccionesModelo>, context: Context) {

        val intent = Intent(context, ListaTransacciones::class.java)
        intent.putExtra("listaRecargas", ArrayList(recargas))
        context.startActivity(intent)
    }



}