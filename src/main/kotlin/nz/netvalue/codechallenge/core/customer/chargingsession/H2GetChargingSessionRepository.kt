package nz.netvalue.codechallenge.core.customer.chargingsession

import io.micrometer.common.util.internal.logging.Slf4JLoggerFactory
import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionEventModel
import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionModel
import nz.netvalue.codechallenge.core.admin.chargingsession.RfidTagModel
import nz.netvalue.codechallenge.core.chargepoint.ChargePointModel
import nz.netvalue.codechallenge.core.chargepoint.ConnectorModel
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.ResultSetExtractor
import java.sql.ResultSet

/**
 * Selects one Charging Session with events from H2 database.
 * @param jdbc connection to database
 */
class H2GetChargingSessionRepository(
    private val jdbc: JdbcOperations,
) : GetChargingSessionRepository {
    private val logger = Slf4JLoggerFactory.getInstance(this::class.java)

    override fun getSession(sessionId: String): ChargingSessionModel? {
        val (sql, params) = buildQuery(sessionId)
        logger.debug("Getting session with sql:\n{}\nparams: {}", sql, params)
        val result = jdbc.query(sql, ResultSetExtractor { rs ->
            extractResult(rs)
        }, *params.toTypedArray())
        return result
    }

    internal fun buildQuery(chargePointId: String): Pair<String, List<Any>> {
        val sql = """
            SELECT
                e.id AS eventId,
                e.time AS eventTime,
                e.type AS eventType,
                e.meter_value AS eventMeterValue,
                e.message AS eventMessage,
                s.id AS sessionId,
                c.id AS connectorId,
                c.number AS connectorNumber,
                p.id AS pointId,
                p.name AS pointName,
                p.serial_number AS pointSerial,
                p.owner_id AS pointOwnerId,
                t.id AS tagId,
                t.name AS tagName,
                t.number AS tagNumber,
                t.owner_id AS tagOwnerId,
                t.vehicle_id AS tagVehicleId
            FROM charging_session_event e
            JOIN charging_session s ON s.id = e.session_id
            LEFT JOIN connector c ON c.id = s.connector_id
            LEFT JOIN charge_point p ON p.id = c.charge_point_id
            LEFT JOIN rfid_tag t ON t.id = s.rfid_tag_id
            WHERE s.id = ?
            ORDER BY e.time;
        """.trimIndent()

        return sql to listOf(chargePointId)
    }

    internal fun extractResult(rs: ResultSet): ChargingSessionModel? {
        var session: ChargingSessionModel? = null
        val events = mutableListOf<ChargingSessionEventModel>()
        while (rs.next()) {
            if (session == null) {
                session = ChargingSessionModel(
                    id = rs.getString("sessionId"),
                    connector = ConnectorModel(
                        id = rs.getString("connectorId"),
                        number = rs.getString("connectorNumber"),
                        chargePoint = ChargePointModel(
                            id = rs.getString("pointId"),
                            name = rs.getString("pointName"),
                            serialNumber = rs.getString("pointSerial"),
                            ownerId = rs.getString("pointOwnerId")
                        )
                    ),
                    rfidTag = RfidTagModel(
                        id = rs.getString("tagId"),
                        name = rs.getString("tagName"),
                        number = rs.getString("tagNumber"),
                        ownerId = rs.getString("tagOwnerId"),
                        vehicleId = rs.getString("tagVehicleId")
                    )
                )
            }
            events.add(ChargingSessionEventModel(
                id = rs.getString("eventId"),
                time = rs.getTimestamp("eventTime").toInstant(),
                type = rs.getString("eventType"),
                meterValue = rs.getInt("eventMeterValue"),
                message = rs.getString("eventMessage")
            ))
        }
        return session?.copy(events = events)
    }

}
