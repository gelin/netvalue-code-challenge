package nz.netvalue.codechallenge.core.customer.chargingsession

import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionEventModel
import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionModel
import nz.netvalue.codechallenge.core.admin.chargingsession.RfidTagModel
import nz.netvalue.codechallenge.core.chargepoint.ChargePointModel
import nz.netvalue.codechallenge.core.chargepoint.ConnectorModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.Instant

class H2GetChargingSessionRepositoryTest {

    private lateinit var repository: H2GetChargingSessionRepository

    @BeforeEach
    fun setUp() {
        repository = H2GetChargingSessionRepository(mock())
    }

    @Test
    fun testExtractResult() {
        val now = Instant.now()
        val resultSet: ResultSet = mock()
        // two rows
        whenever(resultSet.next()).thenReturn(true, true, false)

        whenever(resultSet.getString("eventId")).thenReturn("E1", "E2")
        whenever(resultSet.getTimestamp("eventTime")).thenReturn(Timestamp.from(now.minusSeconds(100)), Timestamp.from(now))
        whenever(resultSet.getString("eventType")).thenReturn("START", "END")
        whenever(resultSet.getInt("eventMeterValue")).thenReturn(123, 456)
        whenever(resultSet.getString("eventMessage")).thenReturn("Message 1", "Message 2")
        whenever(resultSet.getString("sessionId")).thenReturn("S1")
        whenever(resultSet.getString("connectorId")).thenReturn("C1")
        whenever(resultSet.getString("connectorNumber")).thenReturn("1")
        whenever(resultSet.getString("pointId")).thenReturn("P1")
        whenever(resultSet.getString("pointName")).thenReturn("Point")
        whenever(resultSet.getString("pointSerial")).thenReturn("Serial")
        whenever(resultSet.getString("pointOwnerId")).thenReturn("O1")
        whenever(resultSet.getString("tagId")).thenReturn("T1")
        whenever(resultSet.getString("tagName")).thenReturn("Tag")
        whenever(resultSet.getString("tagNumber")).thenReturn("NUMBER")
        whenever(resultSet.getString("tagOwnerId")).thenReturn("O2")
        whenever(resultSet.getString("tagVehicleId")).thenReturn("V1")

        val point = repository.extractResult(resultSet)

        assertEquals(ChargingSessionModel(
            id = "S1",
            connector = ConnectorModel(
                id = "C1",
                number = "1",
                chargePoint = ChargePointModel(
                    id = "P1",
                    name = "Point",
                    serialNumber = "Serial",
                    ownerId = "O1"
                )
            ),
            rfidTag = RfidTagModel(
                id = "T1",
                name = "Tag",
                number = "NUMBER",
                ownerId = "O2",
                vehicleId = "V1"
            ),
            events = listOf(
                ChargingSessionEventModel(
                    id = "E1",
                    time = now.minusSeconds(100),
                    type = "START",
                    meterValue = 123,
                    message = "Message 1",
                ),
                ChargingSessionEventModel(
                    id = "E2",
                    time = now,
                    type = "END",
                    meterValue = 456,
                    message = "Message 2",
                )
            )
        ), point)
    }

}
