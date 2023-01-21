package nz.netvalue.codechallenge.core.chargepoint

/**
 * Retrieves Charge Point from DB.
 */
interface GetChargePointRepository {
    /**
     * Selects Charge Point and all its connectors.
     */
    fun getChargePointWithConnectors(chargePointId: String): ChargePointWithConnectorsModel?
}

/**
 * Charge point model which contains the list of connectors.
 */
data class ChargePointWithConnectorsModel(
    val id: String,
    val name: String,
    val serialNumber: String? = null,
    val ownerId: String? = null,
    val connectors: List<ConnectorModel> = listOf()
)

fun ChargePointWithConnectorsModel.toChargePointModel(): ChargePointModel {
    return ChargePointModel(
        id = this.id,
        name = this.name,
        serialNumber = this.serialNumber,
        ownerId = this.ownerId
    )
}
