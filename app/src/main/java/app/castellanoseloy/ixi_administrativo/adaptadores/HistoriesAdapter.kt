package app.castellanoseloy.ixi_administrativo.adaptadores

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.castellanoseloy.ixi_administrativo.R
import app.castellanoseloy.ixi_administrativo.activitys.historiesDetails.HistoriesDetailsActivity
import app.castellanoseloy.ixi_administrativo.modelos.History
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades.formatoMonenda
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades.getFechaComoString
import app.castellanoseloy.ixi_administrativo.utilidades.RelativeTime


class HistoriesAdapter(val context: Activity, val histories: ArrayList<History>): RecyclerView.Adapter<HistoriesAdapter.HistoriesAdapterViewHolder>() {

    // HashMap para almacenar la suma de ganancias por día
    private val gananciaPorDia = HashMap<String, Double>()

    init {
        // Inicializar el HashMap al crear el adaptador
        calcularGananciaPorDia()
    }

    private fun calcularGananciaPorDia() {
        for (history in histories) {
            val fecha = history.timestamp?.let { getFechaComoString(it) }
            Log.d("Historial","la fecha s del registro es: $fecha")
            if (fecha != null) {
                val ganancia = history.tarifas?.totalComisionReferido ?: 0.0

                // Verificar si la clave ya existe en el mapa
                if (gananciaPorDia.containsKey(fecha)) {
                    gananciaPorDia[fecha] = gananciaPorDia[fecha]!! + ganancia
                } else {
                    gananciaPorDia[fecha] = ganancia
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoriesAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_history, parent, false)
        return HistoriesAdapterViewHolder(view)
    }

    // ESTABLECER LA INFORMACION
    override fun onBindViewHolder(holder: HistoriesAdapterViewHolder, position: Int) {

        val history =  histories[position] // UN SOLO HISTORIAL
        holder.textViewOrigin.text = history.origin
        holder.textViewDestination.text = history.destination
        holder.textViewGanancia.text=history.tarifas?.totalComisionReferido.toString().formatoMonenda()

        if (history.timestamp != null) {
            holder.textViewDate.text = RelativeTime.getTimeAgo(history.timestamp!!, context)
        }

        val fecha = history.timestamp?.let { getFechaComoString(it) }

        // Mostrar la suma de ganancias por día
        if (fecha != null) {
            Log.d("historial","la fecha del registro es $fecha")
            holder.textViewGanaciaDia.text =
                gananciaPorDia[fecha]?.toString()?.formatoMonenda() ?: "0$"
            holder.texview_gananciaDiaFecha.text= "Ganancías del $fecha"
            // Hacer visible solo en el primer registro del día
            holder.linearGananciaDia.visibility = if (esPrimerRegistroDelDia(position)) View.VISIBLE else View.GONE
        }

        holder.itemView.setOnClickListener { goToDetail(history?.id!!) }
    }

    private fun goToDetail(idHistory: String) {
        val i = Intent(context, HistoriesDetailsActivity::class.java)
        i.putExtra("id", idHistory)
        context.startActivity(i)
    }

    // EL TAMAñO DE LA LISTA QUE VAMOS A MOSTRAR
    override fun getItemCount(): Int {
        return histories.size
    }


    class HistoriesAdapterViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewOrigin: TextView
        val textViewDestination: TextView
        val textViewDate: TextView
        val textViewGanancia: TextView
        val textViewGanaciaDia : TextView
        val linearGananciaDia: LinearLayout
        val texview_gananciaDiaFecha:TextView
        init {
            textViewOrigin = view.findViewById(R.id.textViewPasajero)
            textViewDestination = view.findViewById(R.id.textViewDestination)
            textViewDate = view.findViewById(R.id.textViewDate)
            textViewGanancia = view.findViewById(R.id.textViewGanacia)
            textViewGanaciaDia=view.findViewById(R.id.textViewGanaciaDia)
            linearGananciaDia=view.findViewById(R.id.LinearGananciaDia)
            texview_gananciaDiaFecha=view.findViewById(R.id.texview_gananciaDiaFecha)
        }
    }

    // Función para verificar si es el primer registro del día
    private fun esPrimerRegistroDelDia(position: Int): Boolean {
        if (position == 0) {
            return true
        }

        val currentFecha = histories[position].timestamp?.let { getFechaComoString(it) }
        val previousFecha = histories[position - 1].timestamp?.let { getFechaComoString(it) }

        return currentFecha != previousFecha
    }

}