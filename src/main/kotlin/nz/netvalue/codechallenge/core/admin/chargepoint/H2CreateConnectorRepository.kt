package nz.netvalue.codechallenge.core.admin.chargepoint

import io.micrometer.common.util.internal.logging.Slf4JLoggerFactory
import org.springframework.jdbc.core.JdbcOperations
import java.util.*

/**
 * Creates new connector in H2 database.
 * @param jdbc connection to database
 */
class H2CreateConnectorRepository(
    private val jdbc: JdbcOperations,
) : CreateConnectorRepository {
    private val logger = Slf4JLoggerFactory.getInstance(this::class.java)

    override fun createConnector(chargePointId: String, connectorNumber: String): String {
        val id = UUID.randomUUID().toString()
        val (sql, params) = buildQuery(id, chargePointId, connectorNumber)
        logger.debug("Creating new connector with sql:\n{}\nparams: {}", sql, params)
        jdbc.update(sql, *params.toTypedArray())
        return id
    }

    internal fun buildQuery(id: String, chargePointId: String, connectorNumber: String): Pair<String, List<Any>> {
        val sql = """
            INSERT INTO connector (id, charge_point_id, number)
            VALUES (?, ?, ?);
        """.trimIndent()

        return sql to listOf(id, chargePointId, connectorNumber)
    }

}
