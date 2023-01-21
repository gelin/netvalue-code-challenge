package nz.netvalue.codechallenge.core.customer.chargingsession

import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionEventModel
import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionModel
import nz.netvalue.codechallenge.core.admin.chargingsession.RfidTagModel
import nz.netvalue.codechallenge.core.chargepoint.ChargePointModel
import nz.netvalue.codechallenge.core.chargepoint.ChargePointWithConnectorsModel
import nz.netvalue.codechallenge.core.chargepoint.ConnectorModel
import nz.netvalue.codechallenge.core.chargepoint.GetChargePointRepository
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.argThat
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import java.time.Instant

class SimpleCreateChargingSessionServiceTest {

    private lateinit var chargePointRepository: GetChargePointRepository
    private lateinit var rfidTagRepository: GetRfidTagRepository
    private lateinit var sessionRepository: CreateChargingSessionRepository
    private lateinit var sessionEventRepository: CreateSessionEventRepository
    private lateinit var service: SimpleCreateChargingSessionService

    @BeforeEach
    fun setUp() {
        chargePointRepository = mock()
        whenever(chargePointRepository.getChargePointWithConnectors(any())).thenReturn(
            ChargePointWithConnectorsModel(
                id = "POINT_1",
                name = "Charge Point",
                serialNumber = "SERIAL",
                ownerId = "OWNER_1",
                connectors = listOf(
                    ConnectorModel(id = "CONNECTOR_1", number = "1"),
                    ConnectorModel(id = "CONNECTOR_2", number = "2"),
                )
            )
        )

        rfidTagRepository = mock()
        whenever(rfidTagRepository.getRfidTagByNumber(any())).thenReturn(
            RfidTagModel(
                id = "TAG_1",
                number = "TAG_NUMBER",
                name = "Tag",
                ownerId = "OWNER_2",
                vehicleId = "VEHICLE_1",
            )
        )

        sessionRepository = mock()
        whenever(sessionRepository.createNewChargingSession(any(), any())).thenReturn("SESSION_1")

        sessionEventRepository = mock()
        whenever(sessionEventRepository.createSessionEvent(any(), any(), any(), anyOrNull(), anyOrNull())).thenReturn("EVENT_1")

        service = SimpleCreateChargingSessionService(
            chargePointRepository = chargePointRepository,
            rfidTagRepository = rfidTagRepository,
            sessionRepository = sessionRepository,
            sessionEventRepository = sessionEventRepository,
        )
    }

    @Test
    fun testCreateNewSession_noSuchChargePoint() {
        whenever(chargePointRepository.getChargePointWithConnectors(any())).thenReturn(null)

        try {
            service.createNewSession("POINT_X", "1", "TAG_NUMBER", 123)
            fail()
        } catch (e: NoSuchEntityException) {
            // pass
        }

        verify(chargePointRepository).getChargePointWithConnectors(eq("POINT_X"))

        verifyNoInteractions(rfidTagRepository)
        verifyNoInteractions(sessionRepository)
        verifyNoInteractions(sessionEventRepository)
    }

    @Test
    fun testCreateNewSession_noSuchConnector() {
        try {
            service.createNewSession("POINT_1", "3", "TAG_NUMBER", 123)
            fail()
        } catch (e: NoSuchEntityException) {
            // pass
        }

        verify(chargePointRepository).getChargePointWithConnectors(eq("POINT_1"))

        verifyNoInteractions(rfidTagRepository)
        verifyNoInteractions(sessionRepository)
        verifyNoInteractions(sessionEventRepository)
    }

    @Test
    fun testCreateNewSession_noSuchTag() {
        whenever(rfidTagRepository.getRfidTagByNumber(any())).thenReturn(null)

        try {
            service.createNewSession("POINT_1", "1", "TAG_X", 123)
            fail()
        } catch (e: NoSuchEntityException) {
            // pass
        }

        verify(chargePointRepository).getChargePointWithConnectors(eq("POINT_1"))
        verify(rfidTagRepository).getRfidTagByNumber(eq("TAG_X"))

        verifyNoInteractions(sessionRepository)
        verifyNoInteractions(sessionEventRepository)
    }

    @Test
    fun testCreateNewSession_shouldCreateSessionAndEvent() {
        val now = Instant.now()

        val result = service.createNewSession("POINT_1", "1", "TAG_NUMBER", 123)

        verify(chargePointRepository).getChargePointWithConnectors(eq("POINT_1"))
        verify(rfidTagRepository).getRfidTagByNumber(eq("TAG_NUMBER"))

        verify(sessionRepository).createNewChargingSession(eq("CONNECTOR_1"), eq("TAG_1"))
        // TODO: meter value
        verify(sessionEventRepository).createSessionEvent(eq("SESSION_1"), argThat { !isBefore(now) }, eq("START"), eq(123), anyOrNull())

        assertEquals(ChargingSessionModel(
            id = "SESSION_1",
            connector = ConnectorModel(
                id = "CONNECTOR_1",
                number = "1",
                chargePoint = ChargePointModel(
                    id = "POINT_1",
                    name = "Charge Point",
                    serialNumber = "SERIAL",
                    ownerId = "OWNER_1",
                )
            ),
            rfidTag = RfidTagModel(
                id = "TAG_1",
                number = "TAG_NUMBER",
                name = "Tag",
                ownerId = "OWNER_2",
                vehicleId = "VEHICLE_1",
            ),
            events = listOf(
                ChargingSessionEventModel(
                    id = "EVENT_1",
                    time = result.events[0].time,
                    type = "START",
                    meterValue = 123,
                )
            )
        ), result)
        assertThat(result.events[0].time, greaterThanOrEqualTo(now))
    }

}
