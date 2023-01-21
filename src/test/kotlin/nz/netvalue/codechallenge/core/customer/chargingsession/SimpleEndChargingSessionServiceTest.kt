package nz.netvalue.codechallenge.core.customer.chargingsession

import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionEventModel
import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionModel
import nz.netvalue.codechallenge.core.admin.chargingsession.RfidTagModel
import nz.netvalue.codechallenge.core.chargepoint.ConnectorModel
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
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

class SimpleEndChargingSessionServiceTest {

    private lateinit var now: Instant
    private lateinit var sessionRepository: GetChargingSessionRepository
    private lateinit var sessionEventRepository: CreateSessionEventRepository
    private lateinit var service: SimpleEndChargingSessionService

    @BeforeEach
    fun setUp() {
        now = Instant.now()

        sessionRepository = mock()
        whenever(sessionRepository.getSession(any())).thenReturn(ChargingSessionModel(
            id = "SESSION_1",
            connector = ConnectorModel(id = "CONNECTOR_1", number = "1"),
            rfidTag = RfidTagModel(id = "TAG_1", number = "TAG_NUMBER"),
            events = listOf(
                ChargingSessionEventModel(id = "EVENT_1", time = now, type = "START")
            )
        ))

        sessionEventRepository = mock()
        whenever(sessionEventRepository.createSessionEvent(any(), any(), any(), anyOrNull(), anyOrNull())).thenReturn("EVENT_NEW")

        service = SimpleEndChargingSessionService(
            sessionRepository = sessionRepository,
            sessionEventRepository = sessionEventRepository,
        )
    }

    @Test
    fun testEndSession_noSuchSession() {
        whenever(sessionRepository.getSession(any())).thenReturn(null)

        try {
            service.endSession("SESSION_X", 42)
            fail()
        } catch (e: NoSuchSessionException) {
            // pass
        }

        verify(sessionRepository).getSession(eq("SESSION_X"))

        verifyNoInteractions(sessionEventRepository)
    }

    @Test
    fun testEndSession_shouldCreateSessionEvent() {
        val result = service.endSession("SESSION_1", 42)

        verify(sessionRepository).getSession(eq("SESSION_1"))
        verify(sessionEventRepository).createSessionEvent(eq("SESSION_1"), argThat { !isBefore(now) }, eq("END"), eq(42), anyOrNull())

        assertEquals(ChargingSessionModel(
            id = "SESSION_1",
            connector = ConnectorModel(
                id = "CONNECTOR_1",
                number = "1",
            ),
            rfidTag = RfidTagModel(
                id = "TAG_1",
                number = "TAG_NUMBER",
            ),
            events = listOf(
                ChargingSessionEventModel(
                    id = "EVENT_1",
                    time = now,
                    type = "START",
                ),
                ChargingSessionEventModel(
                    id = "EVENT_NEW",
                    time = result.events[1].time,
                    type = "END",
                    meterValue = 42,
                ),
            )
        ), result)
        MatcherAssert.assertThat(result.events[1].time, Matchers.greaterThanOrEqualTo(now))
    }

}
