package nz.netvalue.codechallenge.core.config

import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionRepository
import nz.netvalue.codechallenge.core.admin.chargingsession.H2ChargingSessionRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcOperations

/**
 * Configuration for Admin Charging Point component.
 */
@Configuration
class AdminChargingSessionConfig {

    @Bean
    fun chargingSessionRepository(
        jdbc: JdbcOperations
    ): ChargingSessionRepository {
        return H2ChargingSessionRepository(jdbc = jdbc)
    }

}
