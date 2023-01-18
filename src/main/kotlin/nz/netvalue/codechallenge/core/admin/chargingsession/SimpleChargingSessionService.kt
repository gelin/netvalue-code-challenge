package nz.netvalue.codechallenge.core.admin.chargingsession

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * This mediator just adds timezone to the from and till params.
 * @param repository repository to retrieve data from
 * @param timezone timezone for input from and till dates
 */
class SimpleChargingSessionService(
    private val repository: ChargingSessionRepository,
    private val timezone: ZoneId = ZoneOffset.UTC
) : ChargingSessionService {
    override fun listSessions(from: LocalDateTime?, till: LocalDateTime?): List<ChargingSessionModel> {
        return repository.listSessions(
            from?.atZone(timezone)?.toInstant(),
            till?.atZone(timezone)?.toInstant()
        )
    }
}
