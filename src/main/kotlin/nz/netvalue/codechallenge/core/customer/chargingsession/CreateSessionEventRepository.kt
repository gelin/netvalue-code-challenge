package nz.netvalue.codechallenge.core.customer.chargingsession

import java.time.Instant

/**
 * Creates Charging Session Event in database
 */
interface CreateSessionEventRepository {
    /**
     * Creates Charging Session event.
     * @param sessionId id of the Charging Session
     * @param time timestamp of the Event
     * @param type event type
     * @param meterValue optional meter value at the moment of the event
     * @param message optional event message
     * @return id of the created Event
     */
    fun createSessionEvent(sessionId: String, time: Instant, type: String, meterValue: Int? = null, message: String? = null): String

}
