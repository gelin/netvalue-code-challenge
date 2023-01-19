package nz.netvalue.codechallenge.web.view

import nz.netvalue.codechallenge.core.model.ChargePointModel
import nz.netvalue.codechallenge.core.model.ConnectorModel

/**
 * View for charge point.
 */
data class ChargePointView(
    val id: String,
    val name: String,
    val serialNumber: String?,
    val ownerId: String?
)

/**
 * Converts [ChargePointModel] to [ChargePointView].
 */
fun ChargePointModel.toView(): ChargePointView {
    return ChargePointView(
        id = this.id,
        name = this.name,
        serialNumber = this.serialNumber,
        ownerId = this.ownerId
    )
}

/**
 * View for charge point connector.
 */
data class ConnectorView(
    val id: String,
    val chargePoint: ChargePointView?,
    val number: Int     // while we store connector number as String, we present it as Int
)

/**
 * Converts [ConnectorModel] to [ConnectorView].
 */
fun ConnectorModel.toView(): ConnectorView {
    return ConnectorView(
        id = this.id,
        chargePoint = this.chargePoint?.toView(),
        number = this.number.toIntOrNull() ?: -1
    )
}



