package app.castellanoseloy.ixi_administrativo.modelos

import android.net.Uri
import com.beust.klaxon.Klaxon


private val klaxon = Klaxon()
data class UsuarioModelo (
    var id: String? = null,
    val nombre: String? = null,
    var imagen: Uri? = null,
    var correo:String?=null
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<ClienteModelo>(json)
    }
}