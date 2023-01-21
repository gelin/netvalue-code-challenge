package nz.netvalue.codechallenge.core.admin.chargepoint

import nz.netvalue.codechallenge.core.chargepoint.ConnectorModel
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Creates new Connector for the Charge Point.
 */
interface CreateChargePointConnectorService {
    /**
     * Checks the Charge Point exists.
     * Finds correct number for the new Connector.
     * Creates the Connector in the database.
     * Returns the new created Connector model.
     * @param chargePointId id of the Charge Point, throws exception if Charge Point with such id does not exist
     * @return new Connector
     * @throws NoSuchChargePointException if Charge Point id is invalid
     */
    fun createNewConnector(chargePointId: String): ConnectorModel
}

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Charge Point not found")
class NoSuchChargePointException: Exception()
