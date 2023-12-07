package app.castellanoseloy.ixi_administrativo.provinders

import android.util.Log
import app.castellanoseloy.ixi_administrativo.modelos.ClienteModelo
import app.castellanoseloy.ixi_administrativo.modelos.ConductorModelo
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DriverProvider {

    val db = Firebase.firestore.collection("Conductores")

    fun getAllDrivers(): Task<QuerySnapshot> {
        return db.get()
            .addOnFailureListener { e ->
                Log.e("Firebase informacion", "Error buscando clientes", e)
            }
    }
    fun getDriver(idDriver: String): Task<DocumentSnapshot> {
        return db.document(idDriver).get()
    }

    fun updateStatus(conductorId: String, status: String): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()
        map["validacion"] =status
        return db.document(conductorId).update(map)
            .addOnFailureListener { e ->
                Log.e("Firebase informacion", "Error buscando clientes", e)
            }
    }

    fun update(conductor: ConductorModelo): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()
        map["nombre"] = conductor?.nombre!!
        map["apellido"] = conductor?.apellido!!
        map["tipoDocumento"] = conductor?.tipoDocumento!!
        map["numeroDocumento"] = conductor?.numeroDocumento!!
        return db.document(conductor?.id!!).update(map)
            .addOnFailureListener { e ->
                Log.e("Firebase informacion", "Error buscando clientes", e)
            }
    }
}