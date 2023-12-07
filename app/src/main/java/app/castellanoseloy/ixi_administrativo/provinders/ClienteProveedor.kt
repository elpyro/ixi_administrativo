package app.castellanoseloy.ixi_administrativo.provinders

import android.util.Log
import app.castellanoseloy.ixi_administrativo.modelos.ClienteModelo
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ClienteProveedor {

    val bd =Firebase.firestore.collection("Clientes")


    fun getAllClients(): Task<QuerySnapshot> {
        return bd.get()
            .addOnFailureListener { e ->
                Log.e("Firebase informacion", "Error buscando clientes", e)
            }
    }

    fun update(cliente: ClienteModelo): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()
        map["nombre"] = cliente?.nombre!!
        map["apellido"] = cliente?.apellido!!
        map["tipoDocumento"] = cliente?.tipoDocumento!!
        map["numeroDocumento"] = cliente?.numeroDocumento!!
        return bd.document(cliente?.id!!).update(map)
            .addOnFailureListener { e ->
                Log.e("Firebase informacion", "Error buscando clientes", e)
            }
    }


    fun updateStatus(clienteId: String, status: String): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()
        map["validacion"] =status
        return bd.document(clienteId).update(map)
            .addOnFailureListener { e ->
                Log.e("Firebase informacion", "Error buscando clientes", e)
            }
    }
    fun getClientById(id: String): Task<DocumentSnapshot> {
        return bd.document(id).get()
    }

    fun getClientsByStatus(status: String): Task<QuerySnapshot> {
        return bd.whereEqualTo("estado", status).get()
    }
    fun getClient(idClient: String): Task<DocumentSnapshot> {
        return bd.document(idClient).get()
            .addOnFailureListener { e ->
                Log.e("Firebase informacion", "Error buscando cliente", e)
            }
    }


}