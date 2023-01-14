package nz.netvalue.codechallenge.core.config

import nz.netvalue.codechallenge.core.user.DatabaseFindUserRepository
import nz.netvalue.codechallenge.core.user.FindUserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration for users, where to get their credentials.
 */
@Configuration
class UserConfig {

    @Bean
    fun findUserRepository(): FindUserRepository {
        return DatabaseFindUserRepository()
    }

}
