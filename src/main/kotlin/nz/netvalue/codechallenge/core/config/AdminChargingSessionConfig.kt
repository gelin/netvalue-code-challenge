package nz.netvalue.codechallenge.core.config

import nz.netvalue.codechallenge.core.admin.chargingsession.H2ListChargingSessionsRepository
import nz.netvalue.codechallenge.core.admin.chargingsession.ListChargingSessionsRepository
import nz.netvalue.codechallenge.core.admin.chargingsession.ListChargingSessionsService
import nz.netvalue.codechallenge.core.admin.chargingsession.SimpleListChargingSessionsService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcOperations
import java.time.ZoneId

/**
 * Configuration for Admin Charging Session component.
 */
@Configuration
class AdminChargingSessionConfig {

    @Bean
    fun chargingSessionRepository(
        jdbc: JdbcOperations
    ): ListChargingSessionsRepository {
        return H2ListChargingSessionsRepository(jdbc = jdbc)
    }

    @Bean
    fun chargingSessionService(
        repository: ListChargingSessionsRepository,
        @Value("\${system.timezone:UTC}") timezone: ZoneId
    ): ListChargingSessionsService {
        return SimpleListChargingSessionsService(repository, timezone)
    }

}
