package app.castellanoseloy.ixi_administrativo.modelos

import com.beust.klaxon.Klaxon

private val klaxon = Klaxon()
data class ClienteModelo (
    val id: String? = null,
    val nombre: String? = null,
    val apellido: String? = null,
    val tipoDocumento: String? = null,
    val numeroDocumento: String? = null,
    val correo: String? = null,
    var clave: String? = null,
    val telefono: String? = null,
    var imagenPerfil: String? = null,
    var imagenDocumento: String? = null,
    var imagenDocumentoTrasero: String? = null,
    var imagenFotoValidacion: String? = null,
    var areaTrabajo: String? = null,
    val validacion: String? =null
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<ClienteModelo>(json)
    }
}