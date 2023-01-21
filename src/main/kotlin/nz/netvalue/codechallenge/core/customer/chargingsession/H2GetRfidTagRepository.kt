package nz.netvalue.codechallenge.core.customer.chargingsession

import io.micrometer.common.util.internal.logging.Slf4JLoggerFactory
import nz.netvalue.codechallenge.core.admin.chargingsession.RfidTagModel
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.ResultSetExtractor
import java.sql.ResultSet

/**
 * Selects one RFID Tag from H2 database.
 * @param jdbc connection to database
 */
class H2GetRfidTagRepository(
    private val jdbc: JdbcOperations,
) : GetRfidTagRepository {
    private val logger = Slf4JLoggerFactory.getInstance(this::class.java)

    override fun getRfidTagByNumber(tagNumber: String): RfidTagModel? {
        val (sql, params) = buildQuery(tagNumber)
        logger.debug("Getting tag with sql:\n{}\nparams: {}", sql, params)
        val result = jdbc.query(sql, ResultSetExtractor { rs ->
            extractResult(rs)
        }, *params.toTypedArray())
        return result
    }

    internal fun buildQuery(tagNumber: String): Pair<String, List<Any>> {
        val sql = """
            SELECT
                t.id AS tagId,
                t.name AS tagName,
                t.number AS tagNumber,
                t.owner_id AS tagOwnerId,
                t.vehicle_id AS tagVehicleId
            FROM rfid_tag t
            WHERE t.number = ?;
        """.trimIndent()

        return sql to listOf(tagNumber)
    }

    internal fun extractResult(rs: ResultSet): RfidTagModel? {
        if (rs.next()) {
            return RfidTagModel(
                id = rs.getString("tagId"),
                name = rs.getString("tagName"),
                number = rs.getString("tagNumber"),
                ownerId = rs.getString("tagOwnerId"),
                vehicleId = rs.getString("tagVehicleId")
            )
        }
        return null
    }

}
