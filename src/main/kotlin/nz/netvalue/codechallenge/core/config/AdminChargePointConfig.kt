package nz.netvalue.codechallenge.core.config

import nz.netvalue.codechallenge.core.admin.chargepoint.AutoAssigningNumberCreateChargePointConnectorService
import nz.netvalue.codechallenge.core.admin.chargepoint.CreateChargePointConnectorService
import nz.netvalue.codechallenge.core.admin.chargepoint.CreateConnectorRepository
import nz.netvalue.codechallenge.core.admin.chargepoint.H2CreateConnectorRepository
import nz.netvalue.codechallenge.core.chargepoint.GetChargePointRepository
import nz.netvalue.codechallenge.core.chargepoint.H2GetChargePointRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcOperations

/**
 * Configuration for Admin Charge Point component.
 */
@Configuration
class AdminChargePointConfig {

    @Bean
    fun getChargePointRepository(
        jdbc: JdbcOperations
    ): GetChargePointRepository {
        return H2GetChargePointRepository(jdbc = jdbc)
    }

    @Bean
    fun createConnectorRepository(
        jdbc: JdbcOperations
    ): CreateConnectorRepository {
        return H2CreateConnectorRepository(jdbc = jdbc)
    }

    @Bean
    fun createChargePointConnectorService(
        pointRepository: GetChargePointRepository,
        connectorRepository: CreateConnectorRepository
    ): CreateChargePointConnectorService {
        return AutoAssigningNumberCreateChargePointConnectorService(
            chargePointRepository = pointRepository,
            connectorRepository = connectorRepository
        )
    }

}
