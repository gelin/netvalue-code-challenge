package nz.netvalue.codechallenge.core.config

import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionRepository
import nz.netvalue.codechallenge.core.admin.chargingsession.ChargingSessionService
import nz.netvalue.codechallenge.core.admin.chargingsession.H2ChargingSessionRepository
import nz.netvalue.codechallenge.core.admin.chargingsession.SimpleChargingSessionService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcOperations
import java.time.ZoneId

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

    @Bean
    fun chargingSessionService(
        repository: ChargingSessionRepository,
        @Value("\${system.timezone:UTC}") timezone: ZoneId
    ): ChargingSessionService {
        return SimpleChargingSessionService(repository, timezone)
    }

}
