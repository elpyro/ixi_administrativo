package app.castellanoseloy.ixi_administrativo.provinders

import android.util.Log
import app.castellanoseloy.ixi_administrativo.modelos.TransaccionesModelo

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import java.util.Date


class TransaccionesProvider {

    private val db = FirebaseDatabase.getInstance().reference.child("Transacciones")
    val recargasPorVerificar = mutableListOf<String>()

    fun create(recarga: TransaccionesModelo): Task<Void> {
        return db.child(recarga.id!!).setValue(recarga).addOnFailureListener {
            Log.d("REALTIME_DB", "ERROR: ${it.message}")
        }
    }

    fun obtenerTodasRecargas(): Task<DataSnapshot> {
        val taskCompletionSource = TaskCompletionSource<DataSnapshot>()

        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                taskCompletionSource.setResult(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                taskCompletionSource.setException(error.toException())
            }
        })

        return taskCompletionSource.task
    }

    fun obtenerRecargasPorBanco(bancoBuscado: String): Query {
        return db.orderByChild("banco").equalTo(bancoBuscado)
    }



    fun obtenerRecargasPorBancoYFecha(bancoBuscado: String, fechaInicio: Date, fechaFin: Date): Query {
        val queryPorBanco = db.orderByChild("banco").equalTo(bancoBuscado)

        // Filtrar por fecha en el cliente, usando startAt() y endBefore()
        val queryFiltrada = queryPorBanco.startAt(fechaInicio.time.toDouble())
            .endBefore(fechaFin.time.toDouble())

        return queryFiltrada
    }

    fun cambiarEstado(id: String, status: String): Task<Void> {
        Log.d("Pruebas Nequi", "cambio de estado a $status del $id")
        return db.child(id).child("estado").setValue(status).addOnFailureListener { exception ->
            Log.d("REALTIME_DB", "ERROR: ${exception.message}")
        }
    }

    fun deleteRecargaPorId(idRecarga: String): Task<Void> {
        val referenciaRecarga = db.child(idRecarga)
        return referenciaRecarga.removeValue()
    }





//    fun buscarTransaccionesPorIdDriver(idDriver: String, verificarPendientes: Boolean = true, callback: (List<TransaccionesModelo>) -> Unit) {
//        // Crea una consulta que filtre por el campo "idDriver"
//        val recargas = db.orderByChild("idDriver").equalTo(idDriver)
//        Log.d("Pruebas Nequi", "buscando recargas del conductor: $idDriver")
//
//        val valueEventListener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val recargasList = mutableListOf<TransaccionesModelo>()
//                var hayRecargasPendientes=false
//
//                for (recargaSnapshot in snapshot.children) {
//                    val recarga = recargaSnapshot.getValue(TransaccionesModelo::class.java)
//                    recarga?.let { recargasList.add(it) }
//
//                    if (recarga?.estado.equals("Pendiente") && recarga?.codigoVerificacion != null) {
//                        hayRecargasPendientes = true
//                        // Verificar si esta recarga ya se procesó previamente
//                        if (!recargasPorVerificar.contains(recarga.codigoVerificacion)) {
//                            recargasPorVerificar.add(recarga.codigoVerificacion)
//
//                            // Verifica la recarga con nequi
//                            if (verificarPendientes) {
//                                Log.d("Pruebas Nequi", "El estado de la recarga pendiente ${recarga.codigoVerificacion} se esta verificando ")
//                                verifcarRecargaNequi(recarga.codigoVerificacion!!, recarga.valor!!)
//                            }
//
//                        }
//                    }
//                }
//                callback(recargasList)
//                if (!hayRecargasPendientes) {
//                    recargas.ref.removeEventListener(this)
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Manejar errores si es necesario
//                callback(emptyList())
//            }
//        }
//
//        // Agrega el ValueEventListener a la consulta
//        recargas.addValueEventListener(valueEventListener)
//
//    }


    fun buscarPorCodigoVerificacion(codigo: String, callback: (TransaccionesModelo) -> Unit) {

        val consulta = db.orderByChild("codigoVerificacion").equalTo(codigo).limitToFirst(1)
        Log.d("Pruebas Nequi","codigo a buscar: $codigo")
        consulta.keepSynced(true)
        consulta.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    var recarga = childSnapshot.getValue(TransaccionesModelo::class.java)
                    callback(recarga!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


}