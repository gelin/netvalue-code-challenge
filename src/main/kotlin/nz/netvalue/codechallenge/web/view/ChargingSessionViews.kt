package nz.netvalue.codechallenge.web.view

import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionEventModel
import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionModel
import nz.netvalue.codechallenge.core.admin.chargingsession.RfidTagModel
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

/**
 * View for Charging Session.
 */
data class ChargingSessionView(
    val id: String,
    val connector: ConnectorView?,
    val rfidTag: RfidTagView?,
    val events: List<ChargingSessionEventView>
)

/**
 * Converts [ChargingSessionModel] to [ChargingSessionView].
 */
fun ChargingSessionModel.toView(): ChargingSessionView {
    return ChargingSessionView(
        id = this.id,
        connector = this.connector?.toView(),
        rfidTag = this.rfidTag?.toView(),
        events = this.events.map { it.toView() }
    )
}

/**
 * View for RFID tag.
 */
data class RfidTagView(
    val id: String,
    val name: String?,
    val number: String,
    val ownerId: String?,
    val vehicleId: String?
)

/**
 * Converts [RfidTagModel] to [RfidTagView].
 */
fun RfidTagModel.toView(): RfidTagView {
    return RfidTagView(
        id = this.id,
        name = this.name,
        number = this.number,
        ownerId = this.ownerId,
        vehicleId = this.vehicleId
    )
}

/**
 * View for charging session event.
 */
data class ChargingSessionEventView(
    // hiding id
    val time: ZonedDateTime,
    val type: String,
    val meterValue: Int?,
    val message: String?
)

/**
 * Converts [ChargingSessionEventModel] to [ChargingSessionEventView].
 */
fun ChargingSessionEventModel.toView(timezone: ZoneId = ZoneOffset.UTC): ChargingSessionEventView {
    return ChargingSessionEventView(
        time = ZonedDateTime.ofInstant(this.time, timezone),
        type = this.type,
        meterValue = this.meterValue,
        message = this.message
    )
}
