package app.castellanoseloy.ixi_administrativo.adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.castellanoseloy.ixi_administrativo.R
import app.castellanoseloy.ixi_administrativo.activitys.detalleConductor.DetalleConductor
import app.castellanoseloy.ixi_administrativo.modelos.ConductorModelo
import com.bumptech.glide.Glide



class ConductoresListaAdaptador(val context: Context, val conductores: ArrayList<ConductorModelo>): RecyclerView.Adapter<ConductoresListaAdaptador.HistoriesAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoriesAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cliente_conductor_vehiculo, parent, false)
        return HistoriesAdapterViewHolder(view)
    }

    // ESTABLECER LA INFORMACION
    override fun onBindViewHolder(holder: HistoriesAdapterViewHolder, position: Int) {

        val conductor =  conductores[position] // UN SOLO HISTORIAL

        if (conductor?.imagenPerfil != null) {
            if (conductor!!.imagenPerfil != "") {
                Glide.with(context).load(conductor!!.imagenPerfil)
                    .into(holder.imageView_perfil)
            }
        }
        holder.textview_nombre.text = conductor.nombre +" "+ conductor.apellido
        holder.textViewTelefono.text = conductor.telefono
        holder.textViewTipoDocumento.text=conductor.tipoDocumento
        holder.textViewDocumento.text=conductor.numeroDocumento
        holder.textView_estado.text=conductor.validacion


        holder.itemView.setOnClickListener { goToDetail(conductor?.id!!) }
    }

    private fun goToDetail(idCliente: String) {
        val i = Intent(context, DetalleConductor::class.java)
        i.putExtra("idDriver", idCliente)
        context.startActivity(i)
    }

    // EL TAMAñO DE LA LISTA QUE VAMOS A MOSTRAR
    override fun getItemCount(): Int {
        return conductores.size
    }


    class HistoriesAdapterViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imageView_perfil: ImageView
        val textview_nombre: TextView
        val textViewTelefono: TextView
        val textViewTipoDocumento: TextView
        val textViewDocumento : TextView
        val textView_estado:TextView

        init {
            imageView_perfil = view.findViewById(R.id.imageView_perfil)
            textview_nombre = view.findViewById(R.id.textview_nombre)
            textViewTelefono = view.findViewById(R.id.textViewTelefono)
            textViewTipoDocumento = view.findViewById(R.id.textViewTipoDocumento)
            textViewDocumento=view.findViewById(R.id.textViewDocumento)
            textView_estado=view.findViewById(R.id.textView_estado)
        }
    }

}