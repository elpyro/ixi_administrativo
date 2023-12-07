package app.castellanoseloy.ixi_administrativo.modelos

import com.beust.klaxon.Klaxon

private val klaxon = Klaxon()
data class PreciosModelo (
    val km: Double? = null,
    val min: Double? = null,
    val minValue: Double? = null,
    val difference: Double? = null,
    val impuestoNacional: Double? =null,
    val porcentajeTarifaApp:Double? =null,
    val tarifaApp: Double? =null
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<PreciosModelo>(json)
    }
}