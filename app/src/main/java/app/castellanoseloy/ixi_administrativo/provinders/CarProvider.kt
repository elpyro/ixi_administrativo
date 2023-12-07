package app.castellanoseloy.ixi_administrativo.provinders

import android.net.Uri
import android.util.Log
import app.castellanoseloy.ixi_administrativo.modelos.CarModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class CarProvider {

    val bd =Firebase.firestore.collection("Cars")

    fun getCarByDriverId(driverId: String): Task<QuerySnapshot> {
        // Realizar una consulta para encontrar carros con el campo "idDriver" igual a driverId
        return bd.whereEqualTo("idDriver", driverId).get()
    }

    fun getCartById(id: String): Task<DocumentSnapshot> {
        return bd.document(id).get()
    }

    fun getAllCar(): Task<QuerySnapshot> {
        return bd.get()
            .addOnFailureListener { e ->
                Log.e("Firebase informacion", "Error buscando clientes", e)
            }
    }



    fun createCarId(carID: String): Task<Void> {
        val carData: MutableMap<String, Any> = HashMap()
        carData["id"] = carID
    return bd.document(carID).set(carData)
    }
    fun updateStatus(id: String, status: String): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()
        map["estado"] =status
        return bd.document(id).update(map)
            .addOnFailureListener { e ->
                Log.e("Firebase informacion", "Error buscando clientes", e)
            }
    }

    fun updateCar(car: CarModel): Task<Void> {
        val carData: MutableMap<String, Any> = HashMap()
        carData["id"] = car.id!!
        carData["bland"] = car.bland!!
        carData["tipo"] = car.tipo!!
        carData["placa"] = car.placa!!
        carData["pasajeros"] = car.pasajeros!!
        carData["malero"] = car.malero!!
        carData["tecnomecanica"] = car.tecnomecanica!!
        carData["SOAT"] = car.SOAT!!

        return bd.document(car.id!!).update(carData)
    }



}