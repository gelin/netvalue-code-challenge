package nz.netvalue.codechallenge.core.config

import nz.netvalue.codechallenge.core.chargepoint.GetChargePointRepository
import nz.netvalue.codechallenge.core.customer.chargingsession.CreateChargingSessionRepository
import nz.netvalue.codechallenge.core.customer.chargingsession.CreateChargingSessionService
import nz.netvalue.codechallenge.core.customer.chargingsession.CreateSessionEventRepository
import nz.netvalue.codechallenge.core.customer.chargingsession.EndChargingSessionService
import nz.netvalue.codechallenge.core.customer.chargingsession.GetChargingSessionRepository
import nz.netvalue.codechallenge.core.customer.chargingsession.GetRfidTagRepository
import nz.netvalue.codechallenge.core.customer.chargingsession.H2CreateChargingSessionRepository
import nz.netvalue.codechallenge.core.customer.chargingsession.H2CreateSessionEventRepository
import nz.netvalue.codechallenge.core.customer.chargingsession.H2GetChargingSessionRepository
import nz.netvalue.codechallenge.core.customer.chargingsession.H2GetRfidTagRepository
import nz.netvalue.codechallenge.core.customer.chargingsession.SimpleCreateChargingSessionService
import nz.netvalue.codechallenge.core.customer.chargingsession.SimpleEndChargingSessionService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcOperations

/**
 * Configuration for Admin Charging Session component.
 */
@Configuration
class CustomerChargingSessionConfig {

    @Bean
    fun createChargingSessionService(
        chargePointRepository: GetChargePointRepository,
        rfidTagRepository: GetRfidTagRepository,
        sessionRepository: CreateChargingSessionRepository,
        sessionEventRepository: CreateSessionEventRepository
    ): CreateChargingSessionService {
        return SimpleCreateChargingSessionService(
            chargePointRepository = chargePointRepository,
            rfidTagRepository = rfidTagRepository,
            sessionRepository = sessionRepository,
            sessionEventRepository = sessionEventRepository
        )
    }

    @Bean
    fun endChargingSessionService(
        sessionRepository: GetChargingSessionRepository,
        sessionEventRepository: CreateSessionEventRepository
    ): EndChargingSessionService {
        return SimpleEndChargingSessionService(
            sessionRepository = sessionRepository,
            sessionEventRepository = sessionEventRepository,
        )
    }

    @Bean
    fun getChargingSessionRepository(
        jdbc: JdbcOperations
    ): GetChargingSessionRepository {
        return H2GetChargingSessionRepository(jdbc = jdbc)
    }

    @Bean
    fun getRfidTagRepository(
        jdbc: JdbcOperations
    ): GetRfidTagRepository {
        return H2GetRfidTagRepository(jdbc = jdbc)
    }

    @Bean
    fun createChargingSessionRepository(
        jdbc: JdbcOperations
    ): CreateChargingSessionRepository {
        return H2CreateChargingSessionRepository(jdbc = jdbc)
    }

    @Bean
    fun createSessionEventRepository(
        jdbc: JdbcOperations
    ): CreateSessionEventRepository {
        return H2CreateSessionEventRepository(jdbc = jdbc)
    }

}
