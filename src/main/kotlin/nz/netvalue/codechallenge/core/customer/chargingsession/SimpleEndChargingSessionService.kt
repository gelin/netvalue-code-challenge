package nz.netvalue.codechallenge.core.customer.chargingsession

import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionEventModel
import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionModel
import java.time.Instant

/**
 * Ends the Charging Session.
 * This simple implementation doesn't verify the input data, the meter value can be any.
 * Also, it doesn't verify if this session is already ended.
 * Also, it doesn't verify permissions of this customer to access the session.
 */
class SimpleEndChargingSessionService(
    private val sessionRepository: GetChargingSessionRepository,
    private val sessionEventRepository: CreateSessionEventRepository,
) : EndChargingSessionService {

    override fun endSession(sessionId: String, meterValue: Int): ChargingSessionModel {
        val now = Instant.now()

        val session = sessionRepository.getSession(sessionId = sessionId)
            ?: throw NoSuchSessionException()

        // TODO: check the session is already ended

        val eventId = sessionEventRepository.createSessionEvent(
            sessionId = sessionId,
            time = now,
            type = "END",
            meterValue = meterValue,
        )

        return session.copy(
            events = session.events + ChargingSessionEventModel(
                id = eventId,
                time = now,
                type = "END",
                meterValue = meterValue,
            )
        )
    }
}
