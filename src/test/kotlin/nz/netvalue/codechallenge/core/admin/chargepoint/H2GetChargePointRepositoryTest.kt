package nz.netvalue.codechallenge.core.admin.chargepoint

import nz.netvalue.codechallenge.core.model.ConnectorModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.sql.ResultSet

// TODO: add Spring test with H2 in-memory database
class H2GetChargePointRepositoryTest {

    private lateinit var repository: H2GetChargePointRepository

    @BeforeEach
    fun setUp() {
        repository = H2GetChargePointRepository(mock())
    }

    @Test
    fun testExtractResult() {
        val resultSet: ResultSet = mock()
        // two rows
        whenever(resultSet.next()).thenReturn(true, true, false)

        whenever(resultSet.getString("connectorId")).thenReturn("C1", "C2")
        whenever(resultSet.getString("connectorNumber")).thenReturn("1", "2")
        whenever(resultSet.getString("pointId")).thenReturn("P1")
        whenever(resultSet.getString("pointName")).thenReturn("Point")
        whenever(resultSet.getString("pointSerial")).thenReturn("Serial")
        whenever(resultSet.getString("pointOwnerId")).thenReturn("O1")

        val point = repository.extractResult(resultSet)

        assertEquals(ChargePointWithConnectorsModel(
            id = "P1",
            name = "Point",
            serialNumber = "Serial",
            ownerId = "O1",
            connectors = listOf(
                ConnectorModel(
                    id = "C1",
                    number = "1",
                ),
                ConnectorModel(
                    id = "C2",
                    number = "2",
                )
            )
        ), point)
    }

}
