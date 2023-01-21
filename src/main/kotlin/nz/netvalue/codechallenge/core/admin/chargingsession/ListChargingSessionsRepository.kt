package nz.netvalue.codechallenge.core.admin.chargingsession

import nz.netvalue.codechallenge.core.chargepoint.ConnectorModel
import java.time.Instant

/**
 * Repository to list all charging sessions for admin.
 */
interface ListChargingSessionsRepository {
    /**
     * Returns found sessions.
     * @param from optional lower datetime (inclusive) for selection
     * @param till optional higher datetime (inclusive) for selection
     */
    fun listSessions(from: Instant? = null, till: Instant? = null): List<ChargingSessionModel>
}

/**
 * Charging session from database.
 * Contains connector and RFID tag objects.
 * Contains list of events.
 */
data class ChargingSessionModel(
    val id: String,
    val connector: ConnectorModel? = null,
    val rfidTag: RfidTagModel? = null,
    val events: List<ChargingSessionEventModel> = listOf()
)

/**
 * RFID tag model.
 */
data class RfidTagModel(
    val id: String,
    val name: String? = null,
    val number: String,
    val ownerId: String? = null,
    val vehicleId: String? = null
)

/**
 * Charging session event happened at specified moment.
 */
data class ChargingSessionEventModel(
    val id: String,
    val time: Instant,
    val type: String,
    val meterValue: Int? = null,
    val message: String? = null
)
