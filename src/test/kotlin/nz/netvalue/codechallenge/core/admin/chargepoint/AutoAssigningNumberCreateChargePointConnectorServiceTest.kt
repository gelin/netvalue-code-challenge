package nz.netvalue.codechallenge.core.admin.chargepoint

import nz.netvalue.codechallenge.core.chargepoint.ChargePointModel
import nz.netvalue.codechallenge.core.chargepoint.ChargePointWithConnectorsModel
import nz.netvalue.codechallenge.core.chargepoint.ConnectorModel
import nz.netvalue.codechallenge.core.chargepoint.GetChargePointRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever

class AutoAssigningNumberCreateChargePointConnectorServiceTest {

    private lateinit var chargePointRepository: GetChargePointRepository
    private lateinit var connectorRepository: CreateConnectorRepository
    private lateinit var service: AutoAssigningNumberCreateChargePointConnectorService

    @BeforeEach
    fun setUp() {
        chargePointRepository = mock()
        connectorRepository = mock()
        service = AutoAssigningNumberCreateChargePointConnectorService(
            chargePointRepository = chargePointRepository,
            connectorRepository = connectorRepository,
        )
    }

    @Test
    fun testCreateNewConnector_invalidChargePointId() {
        whenever(chargePointRepository.getChargePointWithConnectors(any())).thenReturn(null)

        try {
            service.createNewConnector("INVALID_ID")
            fail()
        } catch (e: NoSuchChargePointException) {
            // pass
        }

        verifyNoInteractions(connectorRepository)
    }

    @Test
    fun testCreateNewConnector_noConnectors() {
        whenever(chargePointRepository.getChargePointWithConnectors(any())).thenReturn(
            ChargePointWithConnectorsModel(
                id = "CHARGE_POINT_1",
                name = "Charge Point",
                serialNumber = "SERIAL",
                ownerId = "OWNER",
                connectors = listOf()
            )
        )
        whenever(connectorRepository.createConnector(any(), any())).thenReturn("CONNECTOR_1")

        val result = service.createNewConnector("CHARGE_POINT_1")

        verify(connectorRepository).createConnector("CHARGE_POINT_1", "1")

        assertEquals(
            ConnectorModel(
                id = "CONNECTOR_1",
                number = "1",
                chargePoint = ChargePointModel(
                    id = "CHARGE_POINT_1",
                    name = "Charge Point",
                    serialNumber = "SERIAL",
                    ownerId = "OWNER",
                )
            ), result
        )
    }

    @Test
    fun testCreateNewConnector_twoConnectors() {
        whenever(chargePointRepository.getChargePointWithConnectors(any())).thenReturn(
            ChargePointWithConnectorsModel(
                id = "CHARGE_POINT_1",
                name = "Charge Point",
                serialNumber = "SERIAL",
                ownerId = "OWNER",
                connectors = listOf(
                    ConnectorModel(id = "CONNECTOR_1", number = "1"),
                    ConnectorModel(id = "CONNECTOR_2", number = "2"),
                )
            )
        )
        whenever(connectorRepository.createConnector(any(), any())).thenReturn("CONNECTOR_3")

        val result = service.createNewConnector("CHARGE_POINT_1")

        verify(connectorRepository).createConnector("CHARGE_POINT_1", "3")

        assertEquals(
            ConnectorModel(
                id = "CONNECTOR_3",
                number = "3",
                chargePoint = ChargePointModel(
                    id = "CHARGE_POINT_1",
                    name = "Charge Point",
                    serialNumber = "SERIAL",
                    ownerId = "OWNER",
                )
            ), result
        )
    }

}
