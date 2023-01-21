package nz.netvalue.codechallenge.core.customer.chargingsession

import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionModel

/**
 * Retrieves one Charging Session from database.
 */
interface GetChargingSessionRepository {
    /**
     * Gets the Charging Session.
     * @param sessionId id of the Charging Session
     * @return the selected session
     */
    fun getSession(sessionId: String): ChargingSessionModel?
}
