package nz.netvalue.codechallenge.core.customer.chargingsession

/**
 * Creates new Charging Session in database.
 */
interface CreateChargingSessionRepository {
    /**
     * Creates new Charging Session.
     * @param connectorId id of the Charge Point Connector
     * @param rfidTagId id of the RFID tag
     * @return id of the new created session
     */
    fun createNewChargingSession(connectorId: String, rfidTagId: String): String
}
