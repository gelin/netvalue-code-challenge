package nz.netvalue.codechallenge.core.model

/**
 * Charge point model.
 */
data class ChargePointModel(
    val id: String,
    val name: String,
    val serialNumber: String? = null,
    val ownerId: String? = null
)

/**
 * Charge point connector model.
 * Contains charge point object.
 */
data class ConnectorModel(
    val id: String,
    val chargePoint: ChargePointModel? = null,
    val number: String
)

