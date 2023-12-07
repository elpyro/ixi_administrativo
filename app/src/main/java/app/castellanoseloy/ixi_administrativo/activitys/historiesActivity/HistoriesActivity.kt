package app.castellanoseloy.ixi_administrativo.activitys.historiesActivity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import app.castellanoseloy.ixi_administrativo.adaptadores.HistoriesAdapter
import app.castellanoseloy.ixi_administrativo.databinding.ActivityHistoriesBinding
import app.castellanoseloy.ixi_administrativo.modelos.History
import com.optic.uberclonedriverkotlin.providers.HistoryProvider
import java.text.DecimalFormat

class HistoriesActivity : AppCompatActivity() {

    private var extraId: String? = null
    private lateinit var binding: ActivityHistoriesBinding
    private var historyProvider = HistoryProvider()
    private var histories = ArrayList<History>()
    private lateinit var adapter: HistoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerViewHistories.layoutManager = linearLayoutManager

        extraId = intent.getStringExtra("id")

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Historial de viajes"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setTitleTextColor(Color.WHITE)

        getHistories()
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

                    adapter = HistoriesAdapter(this@HistoriesActivity, histories)
                    binding.recyclerViewHistories.adapter = adapter
                }
            }
        }
    }
}