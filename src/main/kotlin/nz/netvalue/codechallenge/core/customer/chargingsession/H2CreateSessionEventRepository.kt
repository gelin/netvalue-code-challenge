package nz.netvalue.codechallenge.core.customer.chargingsession

import io.micrometer.common.util.internal.logging.Slf4JLoggerFactory
import org.springframework.jdbc.core.JdbcOperations
import java.time.Instant
import java.util.*

/**
 * Creates new Event in the Charging Session in H2 database.
 * @param jdbc connection to database
 */
class H2CreateSessionEventRepository(
    private val jdbc: JdbcOperations,
) : CreateSessionEventRepository {
    private val logger = Slf4JLoggerFactory.getInstance(this::class.java)

    override fun createSessionEvent(
        sessionId: String,
        time: Instant,
        type: String,
        meterValue: Int?,
        message: String?
    ): String {
        val id = UUID.randomUUID().toString()
        val (sql, params) = buildQuery(id, sessionId, time, type, meterValue, message)
        logger.debug("Creating new session event with sql:\n{}\nparams: {}", sql, params)
        jdbc.update(sql, *params.toTypedArray())
        return id
    }

    internal fun buildQuery(
        id: String,
        sessionId: String,
        time: Instant,
        type: String,
        meterValue: Int?,
        message: String?
    ): Pair<String, List<Any?>> {
        val sql = """
            INSERT INTO charging_session_event (id, session_id, time, type, meter_value, message)
            VALUES (?, ?, ?, ?, ?, ?);
        """.trimIndent()

        return sql to listOf<Any?>(id, sessionId, time, type, meterValue, message)
    }

}
