package app.castellanoseloy.ixi_administrativo.promts

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import app.castellanoseloy.ixi_administrativo.R
import app.castellanoseloy.ixi_administrativo.activitys.detalleConductor.DetalleConductor
import app.castellanoseloy.ixi_administrativo.activitys.detalleReferidor.DetalleReferidor
import app.castellanoseloy.ixi_administrativo.modelos.CarModel
import app.castellanoseloy.ixi_administrativo.modelos.ConductorModelo
import app.castellanoseloy.ixi_administrativo.modelos.History
import app.castellanoseloy.ixi_administrativo.objetos.Utilidades
import com.bumptech.glide.Glide



import com.optic.uberclonedriverkotlin.providers.HistoryProvider
import de.hdodenhof.circleimageview.CircleImageView
import java.text.DecimalFormat

class DatosConductorPromt {

    fun conductor(
        context: Context,
        conductor: ConductorModelo,
        car: CarModel?
    ) {
        val dialogBuilder = AlertDialog.Builder(context)

        // Inflar el layout para el diálogo
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.promt_informacion_conductor, null)
        dialogBuilder.setView(dialogView)

        val circleImageProfile=dialogView.findViewById<CircleImageView>(R.id.circleImageProfile)
        val textViewConductor = dialogView.findViewById<TextView>(R.id.textViewConductor)
        val TextView_telefono= dialogView.findViewById<TextView>(R.id.TextView_telefono)
        val imageViewWhatsappConductor=dialogView.findViewById<ImageView>(R.id.imageViewWhatsappConductor)
        val imageViewPhoneReferidor=dialogView.findViewById<ImageView>(R.id.imageViewPhoneReferidor)
        val textViewClientCalification=dialogView.findViewById<TextView>(R.id.textViewClientCalification)
        val textViewViajes=dialogView.findViewById<TextView>(R.id.textViewViajes)
        val circleImageCar_color=dialogView.findViewById<CircleImageView>(R.id.circleImageCar_color)
        val textView_datosVehiculo=dialogView.findViewById<TextView>(R.id.TextView_datosVehiculo)
        val imagenView_car1=dialogView.findViewById<ImageView>(R.id.imagenView_car1)
        val imagenView_car2=dialogView.findViewById<ImageView>(R.id.imagenView_car2)


        // Llamada a la función y manejo de los resultados
        obtenerCalificacionYViajes(conductor.id!!) { promedio, cantidadRegistros ->

            val df = DecimalFormat("#.00")
            textViewClientCalification.text= df.format(promedio)
            textViewViajes.text= cantidadRegistros.toString()
        }
        val nombre= conductor.nombre?:""
        val apellido=conductor.apellido?:""

        textViewConductor.text=nombre+apellido
        TextView_telefono.text=conductor.telefono ?: ""

        val placa= car?.placa
        val marca=car?.bland
        val tipo=car?.tipo
        textView_datosVehiculo?.text= "$placa $marca ($tipo)"


        if (car?.color != null) {
            circleImageCar_color?.setColorFilter(car?.color!!)
        }

        if (conductor?.imagenPerfil != null) {
            if (conductor?.imagenPerfil != "") {
                try {
                    Glide.with(context).load(conductor?.imagenPerfil).into(circleImageProfile!!)
                }catch (e: Exception){  }

            }
        }

        if (car?.imagenFrontal !=null) {
            if (car?.imagenFrontal  != "") {
                try {
                    Glide.with(context).load(car?.imagenFrontal ).into(imagenView_car1!!)
                }catch (e: Exception){  }
            }
        }

        if (car?.imagenLateral != null) {
            if (car?.imagenLateral != "") {
                try {
                    Glide.with(context).load(car?.imagenLateral).into(imagenView_car2!!)
                }catch (e: Exception){  }

            }
        }
        dialogBuilder.setNegativeButton("Ver detalle") { dialogInterface, i ->
            val i = Intent(context, DetalleConductor::class.java)
            i.putExtra("idDriver", conductor.id)
            context.startActivity(i)
        }
        dialogBuilder.setPositiveButton("Aceptar") { dialogInterface, i ->
            // No hacer nada
        }
// Mostrar el diálogo
        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        imageViewPhoneReferidor.setOnClickListener { Utilidades.llamar(conductor?.telefono!!, context) }
        imageViewWhatsappConductor.setOnClickListener { Utilidades.enviarWhatsapp(conductor?.telefono!!, context) }
    }

    private fun obtenerCalificacionYViajes(idConductor: String, onComplete: (promedio: Double, cantidadRegistros: Int) -> Unit) {
        val histories = HistoryProvider()
        histories.getHistoriesConductor(idConductor).get().addOnSuccessListener { query ->

            if (query != null) {
                if (query.documents.size > 0) {
                    val documents = query.documents
                    var totalStar = 0.0
                    for (d in documents) {
                        val history = d.toObject(History::class.java)
                        totalStar += history?.calificationToDriver ?: 5.0
                    }

                    val cantidadRegistros = documents.size
                    val promedio = if (cantidadRegistros > 0) totalStar / cantidadRegistros else 0.0

                    // Llamar al callback con los resultados
                    onComplete(promedio, cantidadRegistros)
                } else {
                    // No hay registros
                    onComplete(0.0, 0)
                }
            } else {
                // Error en la consulta
                onComplete(0.0, 0)
            }
        }
    }
}