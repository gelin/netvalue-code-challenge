package nz.netvalue.codechallenge.core.customer.chargingsession

import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionModel
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Ends the Charging Session
 */
interface EndChargingSessionService {
    /**
     * Creates and starts new session for specified Charge Point, Connector and RFID Tag.
     * @param sessionId id of the Charging Session
     * @param meterValue watts meter value at the session end
     * @return updated session
     * @throws NoSuchSessionException if provided session id is invalid
     */
    fun endSession(
        sessionId: String,
        meterValue: Int,
    ): ChargingSessionModel
}

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Charging session not found")
class NoSuchSessionException: Exception()
