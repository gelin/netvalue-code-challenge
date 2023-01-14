package nz.netvalue.codechallenge.core.version

import io.micrometer.common.util.internal.logging.Slf4JLoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment

/**
 * Reads versions from application.properties.
 */
// TODO: add another implementation which adds Git hash to application version and takes database version from migrations
class PropertiesVersionRepository(
    environment: Environment
): VersionRepository {
    private val log = Slf4JLoggerFactory.getInstance(this.javaClass)

    private val applicationVersion = environment.getProperty("version.application", "")
    private val databaseVersion = environment.getProperty("version.database", "")
    init {
        log.info("Application version: {}", applicationVersion)
        log.info("Database version: {}", databaseVersion)
    }

    override fun getVersion(): VersionModel {
        return VersionModel(
            application = applicationVersion,
            database = databaseVersion
        )
    }

}
