package nz.netvalue.codechallenge.core.admin.charginsession

import java.time.LocalDateTime
import java.time.ZonedDateTime

/**
 * Repository to list all charging sessions for admin.
 */
interface ChargingSessionRepository {
    /**
     * Returns found sessions.
     * Timezone for parameters is defined externally.
     * @param from optional lower date (inclusive) for selection
     * @param till optional higher date (inclusive) for selection
     */
    fun listSessions(from: LocalDateTime?, till: LocalDateTime?): List<ChargingSessionModel>
}

/**
 * Charging session from database.
 * Contains connector and RFID tag objects.
 * Contains list of events.
 */
data class ChargingSessionModel(
    val id: String,
    val connector: ConnectorModel?,
    val rfidTag: RfidTagModel?,
    val events: List<ChargingSessionEventModel>
)

/**
 * Charge point connector model.
 * Contains charge point object.
 */
data class ConnectorModel(
    val id: String,
    val chargePoint: ChargePointModel,
    val number: String
)

/**
 * Charge point model.
 */
data class ChargePointModel(
    val id: String,
    val name: String,
    val serialNumber: String?,
    val ownerId: String?
)

/**
 * RFID tag model.
 */
data class RfidTagModel(
    val id: String,
    val name: String?,
    val number: String,
    val ownerId: String?,
    val vehicleId: String?
)

/**
 * Charging session event happened at specified moment.
 */
data class ChargingSessionEventModel(
    val id: String,
    val time: ZonedDateTime,
    val type: String,
    val meterValue: Int?,
    val message: String?
)
