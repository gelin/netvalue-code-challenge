package nz.netvalue.codechallenge.core.customer.chargingsession

import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionModel
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Creates new Charging Session
 */
interface CreateChargingSessionService {
    /**
     * Creates and starts new session for specified Charge Point, Connector and RFID Tag.
     * @param chargePointId id of the Charging Point
     * @param connectorNumber number of the connector
     * @param rfidTagNumber unique number of the RFID tag
     * @param meterValue watts meter value at the session start
     * @return created session
     * @throws NoSuchEntityException if provided ids and numbers are invalid
     */
    // TODO: use DTO for parameters?
    fun createNewSession(
        chargePointId: String,
        connectorNumber: String,
        rfidTagNumber: String,
        meterValue: Int,
    ): ChargingSessionModel
}

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Charge point or RFID tag not found")
class NoSuchEntityException: Exception()
