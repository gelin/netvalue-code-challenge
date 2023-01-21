package nz.netvalue.codechallenge.core.admin.chargepoint

/**
 * Creates new Connector for the Charge Point.
 */
// TODO: use DTOs instead of primitive types
interface CreateConnectorRepository {
    /**
     * Creates new Connector.
     * @param chargePointId ID of the Charge Point where to create the connector
     * @param connectorNumber new Connector number
     * @return id of the created connector
     */
    fun createConnector(chargePointId: String, connectorNumber: String): String
}
