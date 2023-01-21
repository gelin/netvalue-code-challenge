package nz.netvalue.codechallenge.core.admin.chargingsession

import nz.netvalue.codechallenge.core.chargepoint.ChargePointModel
import nz.netvalue.codechallenge.core.chargepoint.ConnectorModel
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.endsWith
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.sql.Array
import java.sql.ResultSet
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

// TODO: add Spring test with H2 in-memory database
class H2ListChargingSessionRepositoryTest {

    private lateinit var repository: H2ListChargingSessionsRepository

    @BeforeEach
    fun setUp() {
        repository = H2ListChargingSessionsRepository(mock())
    }

    @Test
    fun testBuildQuery_noParams() {
        val (sql, params) = repository.buildQuery(null, null)
        assertThat(sql, endsWith("GROUP BY e.session_id ORDER BY startTime;"))
        assertEquals(listOf<Any>(), params)
    }

    @Test
    fun testBuildQuery_fromParam() {
        val time = Instant.now()
        val (sql, params) = repository.buildQuery(time, null)
        assertThat(sql, endsWith("GROUP BY e.session_id HAVING maxTime >= ? ORDER BY startTime;"))
        assertEquals(listOf(time), params)
    }

    @Test
    fun testBuildQuery_tillParam() {
        val time = Instant.now()
        val (sql, params) = repository.buildQuery(null, time)
        assertThat(sql, endsWith("GROUP BY e.session_id HAVING minTime <= ? ORDER BY startTime;"))
        assertEquals(listOf(time), params)
    }

    @Test
    fun testBuildQuery_twoParams() {
        val timeFrom = Instant.now().minusSeconds(100)
        val timeTill = Instant.now()
        val (sql, params) = repository.buildQuery(timeFrom, timeTill)
        assertThat(sql, endsWith("GROUP BY e.session_id HAVING maxTime >= ? AND minTime <= ? ORDER BY startTime;"))
        assertEquals(listOf(timeFrom, timeTill), params)
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

        val session = repository.mapRow(resultSet)

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
                    time = now.minusSeconds(600),
                    type = "START",
                    meterValue = 123,
                    message = "Message"
                ),
                ChargingSessionEventModel(
                    id = "E2",
                    time = now,
                    type = "END",
                    meterValue = 456,
                    message = null
                ),
            )
        ), session)
    }

    @Test
    fun testMapRow_emptySession() {
        val resultSet: ResultSet = mock()
        whenever(resultSet.getString("sessionId")).thenReturn("S1")
        val eventIds = sqlArrayOfNulls(1)
        whenever(resultSet.getArray("eventIds")).thenReturn(eventIds)
        val eventTimes = sqlArrayOfNulls(1)
        whenever(resultSet.getArray("eventTimes")).thenReturn(eventTimes)
        val eventTypes = sqlArrayOfNulls(1)
        whenever(resultSet.getArray("eventTypes")).thenReturn(eventTypes)
        val eventMeters = sqlArrayOfNulls(1)
        whenever(resultSet.getArray("eventMeters")).thenReturn(eventMeters)
        val eventMessages = sqlArrayOfNulls(1)
        whenever(resultSet.getArray("eventMessages")).thenReturn(eventMessages)

        val session = repository.mapRow(resultSet)

        assertEquals(ChargingSessionModel(
            id = "S1",
            events = listOf()
        ), session)
    }

    private fun sqlArrayOf(vararg elements: String?): Array {
        val array: Array = mock()
        whenever(array.getArray()).thenReturn(elements)
        return array
    }

    private fun sqlArrayOf(vararg elements: Instant?): Array {
        val array: Array = mock()
        whenever(array.getArray()).thenReturn(elements.map { OffsetDateTime.ofInstant(it, ZoneOffset.UTC) }.toTypedArray())
        // H2 provides here [java.time.OffsetDateTime] (in array), but our code expects any [java.time.TemporalAccessor] which can be converted to [java.time.Instant]
        return array
    }

    private fun sqlArrayOf(vararg elements: Int?): Array {
        val array: Array = mock()
        whenever(array.getArray()).thenReturn(elements)
        return array
    }

    private fun sqlArrayOfNulls(size: Int): Array {
        val array: Array = mock()
        val nulls: kotlin.Array<Any?> = arrayOfNulls(size)
        whenever(array.getArray()).thenReturn(nulls)
        return array
    }

}
