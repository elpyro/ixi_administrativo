package app.castellanoseloy.ixi_administrativo.modelos

import com.beust.klaxon.Klaxon
import java.io.Serializable
import java.util.Date

private val klaxon = Klaxon()
class TransaccionesModelo(

    val id: String? = null,
    val idDriver: String? = null,
    val banco: String? = null,
    val cuenta:String? = null,
    val codigoVerificacion:String? =null,
    val valor: String? = null,
    val estado: String? = null,
    val fecha: Date? = null

    )  : Serializable {
        public fun toJson() = klaxon.toJsonString(this)

        companion object {
            public fun fromJson(json: String) = klaxon.parse<TransaccionesModelo>(json)
        }

}