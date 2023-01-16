package nz.netvalue.codechallenge.core.admin.charginsession

import org.springframework.jdbc.core.JdbcOperations
import java.sql.ResultSet
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * Selects charging sessions from H2 database.
 * @param jdbc connection to database
 * @param timeZone timezone for input from and till dates
 */
class H2ChargingSessionRepository(
    private val jdbc: JdbcOperations,
    private val timeZone: ZoneId = ZoneOffset.UTC
) : ChargingSessionRepository {

    override fun listSessions(from: LocalDateTime?, till: LocalDateTime?): List<ChargingSessionModel> {
        val result = jdbc.query("""
            SELECT 
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
                t.vehicle_id AS tagVehicleId,
                ARRAY_AGG(e.id ORDER BY e.time) AS eventIds,
                ARRAY_AGG(e.time ORDER BY e.time) AS eventTimes,
                ARRAY_AGG(e.type ORDER BY e.time) AS eventTypes,
                ARRAY_AGG(e.meter_value ORDER BY e.time) AS eventMeters,
                ARRAY_AGG(e.message ORDER BY e.time) AS eventMessages
            FROM charging_session s
            LEFT JOIN charging_session_event e ON e.session_id = s.id
            LEFT JOIN connector c ON s.connector_id = c.id
            LEFT JOIN charge_point p ON c.charge_point_id = p.id
            LEFT JOIN rfid_tag t ON s.rfid_tag_id = t.id
        """.trimIndent()) { rs, rowNum ->
            mapRow(rs, rowNum)
        }
        return result
    }

    internal fun mapRow(rs: ResultSet, rowNum: Int): ChargingSessionModel {
        TODO("Not yet implemented")
    }

}
