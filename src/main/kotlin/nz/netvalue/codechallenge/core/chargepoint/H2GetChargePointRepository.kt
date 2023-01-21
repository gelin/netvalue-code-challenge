package nz.netvalue.codechallenge.core.chargepoint

import io.micrometer.common.util.internal.logging.Slf4JLoggerFactory
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.ResultSetExtractor
import java.sql.ResultSet

/**
 * Selects one Charge Point from H2 database.
 * @param jdbc connection to database
 */
class H2GetChargePointRepository(
    private val jdbc: JdbcOperations,
) : GetChargePointRepository {
    private val logger = Slf4JLoggerFactory.getInstance(this::class.java)

    override fun getChargePointWithConnectors(chargePointId: String): ChargePointWithConnectorsModel? {
        val (sql, params) = buildQuery(chargePointId)
        logger.debug("Getting charge point with sql:\n{}\nparams: {}", sql, params)
        val result = jdbc.query(sql, ResultSetExtractor { rs ->
            extractResult(rs)
        }, *params.toTypedArray())
        return result
    }

    internal fun buildQuery(chargePointId: String): Pair<String, List<Any>> {
        val sql = """
            SELECT
                c.id AS connectorId,
                c.number AS connectorNumber,
                p.id AS pointId,
                p.name AS pointName,
                p.serial_number AS pointSerial,
                p.owner_id AS pointOwnerId
            FROM connector c
                JOIN charge_point p ON c.charge_point_id = p.id
            WHERE p.id = ?
            ORDER BY c.number;
        """.trimIndent()

        return sql to listOf(chargePointId)
    }

    internal fun extractResult(rs: ResultSet): ChargePointWithConnectorsModel? {
        var point: ChargePointWithConnectorsModel? = null
        val connectors = mutableListOf<ConnectorModel>()
        while (rs.next()) {
            if (point == null) {
                point = ChargePointWithConnectorsModel(
                    id = rs.getString("pointId"),
                    name = rs.getString("pointName"),
                    serialNumber = rs.getString("pointSerial"),
                    ownerId = rs.getString("pointOwnerId")
                )
            }
            connectors.add(ConnectorModel(
                id = rs.getString("connectorId"),
                number = rs.getString("connectorNumber")
            ))
        }
        return point?.copy(connectors = connectors)
    }

}
