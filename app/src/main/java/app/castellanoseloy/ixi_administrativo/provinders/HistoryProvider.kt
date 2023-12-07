package com.optic.uberclonedriverkotlin.providers

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HistoryProvider {

    val db = Firebase.firestore.collection("Histories")

    fun getHistoryById(id: String): Task<DocumentSnapshot> {
        return db.document(id).get()
    }

    fun getHistoriesClient(idCliente:String): Query { // CONSULTA COMPUESTA - INDICE
        return db.whereEqualTo("idClient", idCliente).orderBy("timestamp", Query.Direction.DESCENDING)
    }

    fun getHistoriesConductor(idConductor:String): Query { // CONSULTA COMPUESTA - INDICE
        return db.whereEqualTo("idDriver", idConductor)
    }


    }