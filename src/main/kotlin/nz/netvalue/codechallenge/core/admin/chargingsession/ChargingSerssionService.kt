package nz.netvalue.codechallenge.core.admin.chargingsession

import java.time.LocalDateTime

/**
 * Mediator to [ListChargingSessionsRepository].
 */
interface ListChargingSessionsService {
    /**
     * Returns found sessions.
     * @param from optional lower datetime (inclusive) for selection
     * @param till optional higher datetime (inclusive) for selection
     */
    fun listSessions(from: LocalDateTime? = null, till: LocalDateTime? = null): List<ChargingSessionModel>
}
