package nz.netvalue.codechallenge.web.admin.chargingsession

import nz.netvalue.codechallenge.core.admin.chargingsession.*
import nz.netvalue.codechallenge.web.converter.toLocalDateTime
import nz.netvalue.codechallenge.web.security.AuthRoleRequired
import nz.netvalue.codechallenge.web.view.ConnectorView
import nz.netvalue.codechallenge.web.view.ResponseView
import nz.netvalue.codechallenge.web.view.toView
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalTime
import java.time.ZonedDateTime

/**
 * Holds methods to access Charging Sessions for Admin.
 */
@RestController
@RequestMapping("/admin/charging-sessions")
class AdminListChargingSessionsController(
    private val service: ChargingSessionService
) {

    /**
     * Returns list of all charging sessions.
     * @param from from date, parsed with [toLocalDateTime]
     * @param till till date, parsed with [toLocalDateTime]
     */
    @GetMapping
    @AuthRoleRequired("ADMIN")
    fun listChargingSessions(
        @RequestParam(required = false) from: String?,
        @RequestParam(required = false) till: String?
    ): ResponseView<List<ChargingSessionView>> {
        val sessions = service.listSessions(
            from = from?.toLocalDateTime(defaultTime = LocalTime.MIDNIGHT),
            till = till?.toLocalDateTime(defaultTime = LocalTime.MIDNIGHT.minusNanos(1))
        )
        return ResponseView(
            result = sessions.map { it.toView() }
        )
    }

}

// separated view models from database models looks redundant here, but are very useful if you need to change the view form or hide some fields
// also the database may change independently of the views/controllers while the changes are compatible
// and we may have different views for different API versions...

// TODO: move views to another files when necessary

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
fun ChargingSessionEventModel.toView(): ChargingSessionEventView {
    return ChargingSessionEventView(
        time = this.time,
        type = this.type,
        meterValue = this.meterValue,
        message = this.message
    )
}
