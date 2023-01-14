package nz.netvalue.codechallenge.core.config

import nz.netvalue.codechallenge.core.version.PropertiesVersionRepository
import nz.netvalue.codechallenge.core.version.VersionRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

/**
 * Configuration for version subsystem.
 */
@Configuration
class VersionConfig {

    @Bean
    fun versionRepository(environment: Environment): VersionRepository {
        return PropertiesVersionRepository(environment)
    }

}
