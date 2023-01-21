package nz.netvalue.codechallenge.core.customer.chargingsession

import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionEventModel
import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionModel
import nz.netvalue.codechallenge.core.chargepoint.GetChargePointRepository
import nz.netvalue.codechallenge.core.chargepoint.toChargePointModel
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

/**
 * Creates new Charging Session.
 * This implementation doesn't check the ownership of the Charge Point or RFID Tag.
 * Any existing Charge Point or Tag are allowed to start Charging Session.
 * This implementation doesn't velidates the meter value to match with the previous session.
 */
open class SimpleCreateChargingSessionService(
    private val chargePointRepository: GetChargePointRepository,
    private val rfidTagRepository: GetRfidTagRepository,
    private val sessionRepository: CreateChargingSessionRepository,
    private val sessionEventRepository: CreateSessionEventRepository
): CreateChargingSessionService {

    @Transactional
    override fun createNewSession(
        chargePointId: String,
        connectorNumber: String,
        rfidTagNumber: String,
        meterValue: Int,
    ): ChargingSessionModel {
        val now = Instant.now()

        val point = chargePointRepository.getChargePointWithConnectors(chargePointId = chargePointId)
            ?: throw NoSuchEntityException()
        val connector = point.connectors.find { it.number == connectorNumber }
            ?: throw NoSuchEntityException()
        val tag = rfidTagRepository.getRfidTagByNumber(rfidTagNumber)
            ?: throw NoSuchEntityException()

        val sessionId = sessionRepository.createNewChargingSession(
            connectorId = connector.id,
            rfidTagId = tag.id
        )
        val eventId = sessionEventRepository.createSessionEvent(
            sessionId = sessionId,
            time = now,
            type = "START",
            meterValue = meterValue,
        )

        return ChargingSessionModel(
            id = sessionId,
            connector = connector.copy(chargePoint = point.toChargePointModel()),
            rfidTag = tag,
            events = listOf(
                ChargingSessionEventModel(
                    id = eventId,
                    time = now,
                    type = "START",
                    meterValue = meterValue,
                )
            )
        )
    }

}
