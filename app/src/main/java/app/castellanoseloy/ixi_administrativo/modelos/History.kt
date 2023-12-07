package app.castellanoseloy.ixi_administrativo.modelos

import com.beust.klaxon.Klaxon

private val klaxon = Klaxon()
data class History (
    var id: String? = null,
    val idClient: String? = null,
    val idDriver: String? = null,
    val pasajero:String?=null,
    val pasajeroTelefono:String?=null,
    val origin: String? = null,
    val destination: String? = null,
    var commentClient: String? = null,
    val commentDriver: String? = null,
    val calificationToClient: Double? = null,
    val calificationToDriver: Double? = null,
    val time: Int? = null,
    val km: Double? = null,
    val originLat: Double? = null,
    val originLng: Double? = null,
    val destinationLat: Double? = null,
    val destinationLng: Double? = null,
    val price: Double? = null,
    val tarifas: TarifasModelo? = null,
    val timestamp: Long? = null,
    val car: CarModel?=null
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<History>(json)
    }
}