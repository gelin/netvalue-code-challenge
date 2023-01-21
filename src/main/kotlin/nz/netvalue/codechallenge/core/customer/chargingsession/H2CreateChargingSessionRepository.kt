package nz.netvalue.codechallenge.core.customer.chargingsession

import io.micrometer.common.util.internal.logging.Slf4JLoggerFactory
import org.springframework.jdbc.core.JdbcOperations
import java.util.*

/**
 * Creates new Charging Session in H2 database.
 * @param jdbc connection to database
 */
class H2CreateChargingSessionRepository(
    private val jdbc: JdbcOperations,
) : CreateChargingSessionRepository {
    private val logger = Slf4JLoggerFactory.getInstance(this::class.java)

    override fun createNewChargingSession(connectorId: String, rfidTagId: String): String {
        val id = UUID.randomUUID().toString()
        val (sql, params) = buildQuery(id, connectorId, rfidTagId)
        logger.debug("Creating new session with sql:\n{}\nparams: {}", sql, params)
        jdbc.update(sql, *params.toTypedArray())
        return id
    }

    internal fun buildQuery(id: String, connectorId: String, rfidTagId: String): Pair<String, List<Any>> {
        val sql = """
            INSERT INTO charging_session (id, connector_id, rfid_tag_id)
            VALUES (?, ?, ?);
        """.trimIndent()

        return sql to listOf(id, connectorId, rfidTagId)
    }

}
