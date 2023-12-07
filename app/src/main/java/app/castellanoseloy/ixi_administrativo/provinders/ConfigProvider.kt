package com.optic.uberclonekotlin.providers

import android.util.Log
import app.castellanoseloy.ixi_administrativo.modelos.PreciosModelo
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ConfigProvider {
    val db = Firebase.firestore.collection("Config")

    fun getPrices(): Task<DocumentSnapshot> {
        return db.document("prices").get().addOnFailureListener { exception ->
            Log.d("FIREBASE", "Error: ${exception.message}")
        }
    }

    fun getWhastAppInformacion(): Task<DocumentSnapshot> {
        return db.document("numerosWhatsApp").get().addOnFailureListener { exception ->
            Log.d("FIREBASE", "Error: ${exception.message}")
        }
    }

    fun actualizarPrices(precios: PreciosModelo): Task<Void> {
        val preciosMap = mapOf(
            "km" to precios.km,
            "tarifaApp" to precios.tarifaApp,
            "minValue" to precios.minValue,
            "min" to precios.min,
            "porcentajeTarifaApp" to precios.porcentajeTarifaApp,
        )

        return db.document("prices").update(preciosMap).addOnFailureListener { exception ->
            Log.d("FIREBASE", "Error al actualizar los precios: ${exception.message}")
        }
    }

    fun actualizarWhastapps(numeroAyuda:String, grupo:String): Task<Void> {
        val preciosMap = mapOf(
            "ayudaNumero" to numeroAyuda,
            "grupoUrl" to grupo,
                )

        return db.document("numerosWhatsApp").update(preciosMap).addOnFailureListener { exception ->
            Log.d("FIREBASE", "Error al actualizar los precios: ${exception.message}")
        }
    }

}