package app.castellanoseloy.ixi_administrativo.adaptadores

import android.app.Activity
import android.content.Intent
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.castellanoseloy.ixi_administrativo.R
import app.castellanoseloy.ixi_administrativo.activitys.detalleConductor.DetalleConductor
import app.castellanoseloy.ixi_administrativo.activitys.detalleReferidor.DetalleReferidor
import app.castellanoseloy.ixi_administrativo.activitys.historiesDetails.HistoriesDetailsActivity
import app.castellanoseloy.ixi_administrativo.modelos.History
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades.formatoMonenda
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades.getFechaComoString
import app.castellanoseloy.ixi_administrativo.utilidades.RelativeTime


class HistoriesCancelsAdapter(val context: Activity, val histories: ArrayList<History>): RecyclerView.Adapter<HistoriesCancelsAdapter.HistoriesAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoriesAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_history_cancels, parent, false)
        return HistoriesAdapterViewHolder(view)
    }

    // ESTABLECER LA INFORMACION
    override fun onBindViewHolder(holder: HistoriesAdapterViewHolder, position: Int) {

        val history =  histories[position] // UN SOLO HISTORIAL
        holder.textViewOrigin.text = history.origin
        holder.textViewDestination.text = history.destination
        holder.textViewPasajero.text=history.pasajero
        holder.textViewTelefono.text=history.pasajeroTelefono
        
        holder.textComentarioConductor.text = Html.fromHtml(history?.commentDriver?:"")
        holder.textComentarioConductor.movementMethod = LinkMovementMethod.getInstance()

        holder.textComentarioReferidor.text = Html.fromHtml(history?.commentClient?:"")
        holder.textComentarioReferidor.movementMethod = LinkMovementMethod.getInstance()

        if (history.timestamp != null) {
            holder.textViewDate.text = RelativeTime.getTimeAgo(history.timestamp!!, context)
        }
        val fecha = history.timestamp?.let { getFechaComoString(it) }

        // Mostrar la suma de ganancias por día
        if (fecha != null) {
            Log.d("historial","la fecha del registro es $fecha")
            holder.texview_gananciaDiaFecha.text= "Viajes del  $fecha"
            // Hacer visible solo en el primer registro del día
            holder.linearGananciaDia.visibility = if (esPrimerRegistroDelDia(position)) View.VISIBLE else View.GONE
        }

        holder.buttonVerConductor.setOnClickListener { goToDetailDriver(history?.idDriver!!) }
        holder.buttonVerReferidor.setOnClickListener { goToDetailClient(history?.idClient!!) }
    }

    private fun goToDetailClient(idClient: String) {
        val i = Intent(context, DetalleReferidor::class.java)
        i.putExtra("idCliente", idClient)
        context.startActivity(i)
    }

    private fun goToDetailDriver(idDriver: String) {
        val i = Intent(context, DetalleConductor::class.java)
        i.putExtra("idDriver", idDriver)
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
        val textComentarioConductor: TextView
        val textComentarioReferidor: TextView
        val textViewPasajero:TextView
        val textViewTelefono:TextView
        val linearGananciaDia: LinearLayout
        val texview_gananciaDiaFecha:TextView
        val buttonVerReferidor: Button
        val buttonVerConductor: Button


        init {
            textViewOrigin = view.findViewById(R.id.textViewOrigen)
            textViewDestination = view.findViewById(R.id.textViewDestination)
            textViewDate = view.findViewById(R.id.textViewDate)
            textComentarioConductor=view.findViewById(R.id.textComentarioConductor)
            textComentarioReferidor=view.findViewById(R.id.textComentarioReferidor)
            textViewPasajero=view.findViewById(R.id.textViewPasajero)
            textViewTelefono=view.findViewById(R.id.textViewTelefono)
            linearGananciaDia=view.findViewById(R.id.LinearGananciaDia)
            texview_gananciaDiaFecha=view.findViewById(R.id.texview_gananciaDiaFecha)
            buttonVerReferidor=view.findViewById(R.id.buttonVerReferidor)
            buttonVerConductor=view.findViewById(R.id.buttonVerConductor)
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