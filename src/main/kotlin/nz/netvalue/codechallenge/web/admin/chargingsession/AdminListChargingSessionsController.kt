package nz.netvalue.codechallenge.web.admin.chargingsession

import nz.netvalue.codechallenge.core.admin.chargingsession.*
import nz.netvalue.codechallenge.web.converter.toLocalDateTime
import nz.netvalue.codechallenge.web.security.AuthRoleRequired
import nz.netvalue.codechallenge.web.view.ChargingSessionView
import nz.netvalue.codechallenge.web.view.ResponseView
import nz.netvalue.codechallenge.web.view.toView
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalTime

/**
 * Holds methods to access Charging Sessions for Admin.
 */
@RestController
@RequestMapping("/admin/charging-sessions")
class AdminListChargingSessionsController(
    private val service: ListChargingSessionsService
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
