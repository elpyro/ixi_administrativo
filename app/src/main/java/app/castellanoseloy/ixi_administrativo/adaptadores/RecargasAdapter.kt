package app.castellanoseloy.ixi_administrativo.adaptadores

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.castellanoseloy.ixi_administrativo.R
import app.castellanoseloy.ixi_administrativo.activitys.detalleConductor.DetalleConductor
import app.castellanoseloy.ixi_administrativo.activitys.historiesDetails.HistoriesDetailsActivity
import app.castellanoseloy.ixi_administrativo.modelos.TransaccionesModelo
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades.formatoMonenda
import app.castellanoseloy.ixi_administrativo.utilidades.RelativeTime


class RecargasAdapter(val context: Activity, recargas: ArrayList<TransaccionesModelo>): RecyclerView.Adapter<RecargasAdapter.HistoriesAdapterViewHolder>() {
    val recargasOrdenadas = recargas.sortedByDescending  { it.fecha }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoriesAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recarga, parent, false)
        return HistoriesAdapterViewHolder(view)
    }

    // ESTABLECER LA INFORMACION
    override fun onBindViewHolder(holder: HistoriesAdapterViewHolder, position: Int) {


        val transaccion =  recargasOrdenadas[position] // UN SOLO HISTORIAL

        val valorMostrado = if (transaccion.valor?.toDouble() ?: 0.0 < 0) {
            // Si el valor es negativo, conviértelo a positivo
            (-transaccion.valor?.toDouble()!!).toString()
        } else {
            // Si el valor es positivo o nulo, simplemente formatea como moneda
            transaccion.valor
        }
        // Cambia el signo del valor si el banco es "ixi-cobros"
         if (transaccion.banco == "ixi-cobros") {
             holder.itemView.setOnClickListener { goToDetail(transaccion?.id!!) }
        } else {
             holder.itemView.setOnClickListener { goToDetailCliente(transaccion?.idDriver!!) }
        }

        holder.textviewValor.text = valorMostrado?.formatoMonenda() +" | "+ transaccion.cuenta
        holder.textViewEstado.text = transaccion.estado

        when (transaccion.banco) {
            "ixi-cobros" -> holder.imageView_logo.setImageResource(R.drawable.ic_ixi_logo)
            "Nequi" -> holder.imageView_logo.setImageResource(R.drawable.nequi_logo)
            // Agrega más casos según sea necesario para otros bancos
            else -> holder.imageView_logo.setImageResource(R.drawable.ic_ixi_logo)
        }

        if (transaccion.estado.equals("Realizado", ignoreCase = true)) {
            holder.textViewEstado.setTextColor(ContextCompat.getColor(context, R.color.verde))
        } else if(transaccion.estado.equals("Pendiente")) {
            holder.textViewEstado.setTextColor(ContextCompat.getColor(context, R.color.rojo))
        }
        else {
            // Restaurar el color predeterminado en caso contrario
            holder.textViewEstado.setTextColor(ContextCompat.getColor(context, R.color.black))
        }

        if (transaccion.fecha != null) {
            holder.textViewFecha.text = RelativeTime.getTimeAgo(transaccion.fecha.time, context)
        }

    }

    private fun goToDetail(idHistory: String) {
        val i = Intent(context, HistoriesDetailsActivity::class.java)
        i.putExtra("id", idHistory)
        context.startActivity(i)
    }

    private fun goToDetailCliente(idDriver: String) {
        val i = Intent(context, DetalleConductor::class.java)
        i.putExtra("idDriver", idDriver)
        context.startActivity(i)
    }

    // EL TAMAñO DE LA LISTA QUE VAMOS A MOSTRAR
    override fun getItemCount(): Int {
        return recargasOrdenadas.size
    }


    class HistoriesAdapterViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textviewValor: TextView
        val textViewFecha: TextView
        val textViewEstado: TextView
        val  imageView_logo:ImageView
        init {
            textviewValor = view.findViewById(R.id.textview_valor)
            textViewFecha = view.findViewById(R.id.textViewFecha)
            textViewEstado = view.findViewById(R.id.textViewEstado)
            imageView_logo= view.findViewById(R.id.imageView_logo)
        }

    }

}