package nz.netvalue.codechallenge.core.admin.chargingsession

import java.time.Instant
import java.time.LocalDateTime

/**
 * Mediator to [ChargingSessionRepository].
 */
interface ChargingSessionService {
    /**
     * Returns found sessions.
     * @param from optional lower datetime (inclusive) for selection
     * @param till optional higher datetime (inclusive) for selection
     */
    fun listSessions(from: LocalDateTime? = null, till: LocalDateTime? = null): List<ChargingSessionModel>
}
