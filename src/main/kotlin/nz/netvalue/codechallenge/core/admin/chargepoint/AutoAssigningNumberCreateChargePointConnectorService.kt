package nz.netvalue.codechallenge.core.admin.chargepoint

import nz.netvalue.codechallenge.core.chargepoint.ChargePointModel
import nz.netvalue.codechallenge.core.chargepoint.ChargePointWithConnectorsModel
import nz.netvalue.codechallenge.core.chargepoint.ConnectorModel
import nz.netvalue.codechallenge.core.chargepoint.GetChargePointRepository
import org.springframework.transaction.annotation.Transactional

/**
 * Creates new Connector for Charge Point.
 * Assigns the number for Connector automatically.
 */
// TODO: rename to something shorter?
open class AutoAssigningNumberCreateChargePointConnectorService(
    private val chargePointRepository: GetChargePointRepository,
    private val connectorRepository: CreateConnectorRepository
): CreateChargePointConnectorService {

    @Transactional
    override fun createNewConnector(chargePointId: String): ConnectorModel {
        val chargePoint = chargePointRepository.getChargePointWithConnectors(chargePointId)
            ?: throw NoSuchChargePointException()  // TODO: provide details in exception?

        val connectorNumber = findConnectorNumber(chargePoint)
        val connectorId = connectorRepository.createConnector(chargePoint.id, connectorNumber)

        return ConnectorModel(
            id = connectorId,
            number = connectorNumber,
            chargePoint = ChargePointModel(
                id = chargePoint.id,
                name = chargePoint.name,
                serialNumber = chargePoint.serialNumber,
                ownerId = chargePoint.ownerId,
            )
        )
    }

    /**
     * Finds the next connector number assuming the numbers are integer.
     */
    private fun findConnectorNumber(chargePoint: ChargePointWithConnectorsModel): String {
        val maxNumber = chargePoint.connectors.mapNotNull { it.number.toIntOrNull() }.maxOrNull() ?: 0
        return (maxNumber + 1).toString()
    }

}
