package app.castellanoseloy.ixi_administrativo.activitys.historiesActivity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import app.castellanoseloy.ixi_administrativo.adaptadores.HistoriesCancelsAdapter
import app.castellanoseloy.ixi_administrativo.databinding.ActivityHistoriesBinding
import app.castellanoseloy.ixi_administrativo.modelos.History
import com.optic.uberclonedriverkotlin.providers.HistoryProvider
import java.text.DecimalFormat

class HistoriesViajesCanceladosActivity : AppCompatActivity() {

    private var extraId: String? = null
    private lateinit var binding: ActivityHistoriesBinding
    private var historyProvider = HistoryProvider()
    private var histories = ArrayList<History>()
    private lateinit var adapter: HistoriesCancelsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerViewHistories.layoutManager = linearLayoutManager

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Historial de viajes cancelados"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setTitleTextColor(Color.WHITE)

        extraId = intent.getStringExtra("id")
        if(extraId!=null)  getHistories()

        val extraViajesRealizadosPorFecha = intent.getSerializableExtra("viajesCanceladosPorFecha") as? ArrayList<History>
        if(extraViajesRealizadosPorFecha!=null) mostrarViajesPorFecha(extraViajesRealizadosPorFecha)
    }

    private fun mostrarViajesPorFecha(extraViajesRealizadosPorFecha: ArrayList<History>) {
        adapter = HistoriesCancelsAdapter(this@HistoriesViajesCanceladosActivity, extraViajesRealizadosPorFecha)
        binding.recyclerViewHistories.adapter = adapter
        binding.linearLayoutCalificacion.visibility=View.GONE
        binding.textViewViajes.text = extraViajesRealizadosPorFecha.size.toString()
    }

    private fun getHistories() {
        histories.clear()

        extraId?.let {
            historyProvider.getHistoriesClient(it).get().addOnSuccessListener { query ->

                if (query != null) {
                    if (query.documents.size > 0) {
                        val documents = query.documents
                        var totalStrar=0.00
                        for (d in documents) {
                            var history = d.toObject(History::class.java)
                            history?.id = d.id
                            histories.add(history!!)
                            totalStrar += history?.calificationToClient ?: 5.00
                        }

                        val cantidadRegistros = documents.size
                        val promedio = if (cantidadRegistros > 0) totalStrar / cantidadRegistros else 0.0
                        val df = DecimalFormat("#.00")
                        binding.textViewClientCalification.text= df.format(promedio)
                        binding.textViewViajes.text=cantidadRegistros.toString()
                    }

                    adapter = HistoriesCancelsAdapter(this@HistoriesViajesCanceladosActivity, histories)
                    binding.recyclerViewHistories.adapter = adapter
                }
            }
        }
    }
}