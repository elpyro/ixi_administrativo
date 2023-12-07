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
import app.castellanoseloy.ixi_administrativo.activitys.detalleReferidor.DetalleReferidor
import app.castellanoseloy.ixi_administrativo.modelos.ClienteModelo
import com.bumptech.glide.Glide



class ClientesListaAdaptador(val context: Context, val clientes: ArrayList<ClienteModelo>): RecyclerView.Adapter<ClientesListaAdaptador.HistoriesAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoriesAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cliente_conductor_vehiculo, parent, false)
        return HistoriesAdapterViewHolder(view)
    }

    // ESTABLECER LA INFORMACION
    override fun onBindViewHolder(holder: HistoriesAdapterViewHolder, position: Int) {

        val cliente =  clientes[position] // UN SOLO HISTORIAL

        if (cliente?.imagenPerfil != null) {
            if (cliente!!.imagenPerfil != "") {
                Glide.with(context).load(cliente!!.imagenPerfil)
                    .into(holder.imageView_perfil)
            }
        }
        holder.textview_nombre.text = cliente.nombre +" "+ cliente.apellido
        holder.textViewTelefono.text = cliente.telefono
        holder.textViewTipoDocumento.text=cliente.tipoDocumento
        holder.textViewDocumento.text=cliente.numeroDocumento
        holder.textView_estado.text=cliente.validacion


        holder.itemView.setOnClickListener { goToDetail(cliente?.id!!) }
    }

    private fun goToDetail(idCliente: String) {
        val i = Intent(context, DetalleReferidor::class.java)
        i.putExtra("idCliente", idCliente)
        context.startActivity(i)
    }

    // EL TAMAñO DE LA LISTA QUE VAMOS A MOSTRAR
    override fun getItemCount(): Int {
        return clientes.size
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