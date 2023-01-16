package nz.netvalue.codechallenge.core.admin.charginsession

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.sql.Array
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class H2ChargingSessionRepositoryTest {

    private lateinit var repository: H2ChargingSessionRepository

    @BeforeEach
    fun setUp() {
        repository = H2ChargingSessionRepository(mock())
    }

    @Test
    fun testMapRow() {
        val now = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        val resultSet: ResultSet = mock()
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
        whenever(resultSet.getString("tagOwnerId")).thenReturn("O1")
        whenever(resultSet.getString("tagVehicleId")).thenReturn("V1")
        val eventIds = sqlArrayOf("E1", "E2")
        whenever(resultSet.getArray("eventIds")).thenReturn(eventIds)
        val eventTimes = sqlArrayOf(now.minusSeconds(600), now)
        whenever(resultSet.getArray("eventTimes")).thenReturn(eventTimes)
        val eventTypes = sqlArrayOf("START", "END")
        whenever(resultSet.getArray("eventTypes")).thenReturn(eventTypes)
        val eventMeters = sqlArrayOf(123, 456)
        whenever(resultSet.getArray("eventMeters")).thenReturn(eventMeters)
        val eventMessages = sqlArrayOf("Message", null)
        whenever(resultSet.getArray("eventMessages")).thenReturn(eventMessages)

        val session = repository.mapRow(resultSet, 1)

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
                ownerId = "O1",
                vehicleId = "V1"
            ),
            events = listOf(
                ChargingSessionEventModel(
                    id = "E1",
                    time = ZonedDateTime.ofInstant(now.minusSeconds(600), ZoneOffset.UTC),
                    type = "START",
                    meterValue = 123,
                    message = "Message"
                ),
                ChargingSessionEventModel(
                    id = "E2",
                    time = ZonedDateTime.ofInstant(now, ZoneOffset.UTC),
                    type = "END",
                    meterValue = 456,
                    message = null
                ),
            )
        ), session)
    }

    private fun sqlArrayOf(vararg elements: String?): Array {
        val array: Array = mock()
        whenever(array.getArray()).thenReturn(elements)
        return array
    }

    private fun sqlArrayOf(vararg elements: Instant?): Array {
        val array: Array = mock()
        whenever(array.getArray()).thenReturn(elements.map { it?.let { Timestamp(it.toEpochMilli()) } }.toTypedArray())
        return array
    }

    private fun sqlArrayOf(vararg elements: Int?): Array {
        val array: Array = mock()
        whenever(array.getArray()).thenReturn(elements)
        return array
    }

}
