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
import app.castellanoseloy.ixi_administrativo.activitys.addCar.DetalleVehiculo
import app.castellanoseloy.ixi_administrativo.activitys.detalleReferidor.DetalleReferidor
import app.castellanoseloy.ixi_administrativo.modelos.CarModel
import com.bumptech.glide.Glide


class VehiculosListaAdaptador(val context: Context, val vehiculo: ArrayList<CarModel>): RecyclerView.Adapter<VehiculosListaAdaptador.HistoriesAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoriesAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cliente_conductor_vehiculo, parent, false)
        return HistoriesAdapterViewHolder(view)
    }

    // ESTABLECER LA INFORMACION
    override fun onBindViewHolder(holder: HistoriesAdapterViewHolder, position: Int) {

        val vehiculo =  vehiculo[position] // UN SOLO HISTORIAL

        if (vehiculo?.imagenFrontal != null) {
            if (vehiculo!!.imagenFrontal != "") {
                Glide.with(context).load(vehiculo!!.imagenFrontal)
                    .into(holder.imageView_perfil)
            }
        }
        holder.textview_nombre.text = vehiculo.tipo +" "+ vehiculo.bland
        holder.textViewTelefono.text = vehiculo.placa
        holder.textView_estado.text=vehiculo.estado

        holder.itemView.setOnClickListener { goToDetail(vehiculo?.id!!) }
    }

    private fun goToDetail(id: String) {
        val i = Intent(context, DetalleVehiculo::class.java)
        i.putExtra("idVehiculo", id)
        context.startActivity(i)
    }

    // EL TAMAñO DE LA LISTA QUE VAMOS A MOSTRAR
    override fun getItemCount(): Int {
        return vehiculo.size
    }


    class HistoriesAdapterViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imageView_perfil: ImageView
        val textview_nombre: TextView
        val textViewTelefono: TextView
        val textView_estado:TextView

        init {
            imageView_perfil = view.findViewById(R.id.imageView_perfil)
            textview_nombre = view.findViewById(R.id.textview_nombre)
            textViewTelefono = view.findViewById(R.id.textViewTelefono)
            textView_estado=view.findViewById(R.id.textView_estado)
        }
    }

}