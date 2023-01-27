package nz.netvalue.codechallenge.web.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import java.util.function.Consumer

/**
 * Configures [CorsFilter] for the application.
 *
 * Then you can change allowed origins, methods and headers by defining the environment variables:
 * <dl>
 * <dt>CORS_ALLOWED_ORIGINS</dt>
 * <dd>comma separated list of allowed origins, "*" by default which means all origins are allowed (don't do it on production for non-public APIs)</dd>
 * <dt>CORS_ALLOWED_METHODS</dt>
 * <dd>comma separated list of allowed methods, "*" by default which means all methods are allowed</dd>
 * <dt>CORS_ALLOWED_HEADERS</dt>
 * <dd>comma separated list of allowed headers, "*" by default which means all headers are allowed</dd>
 * <dt>CORS_ALLOW_CREDENTIALS</dt>
 * <dd>value for Access-Control-Allow-Credentials response header, "true" by default</dd>
 * <dt>CORS_PATH</dt>
 * <dd>path pattern where to apply the CORS filter, "/ **" by default</dd>
 * </dl>
 */
@Configuration
class CorsConfig {

    @Value("#{'\${CORS_ALLOWED_ORIGINS:*}'.split('\\s*,\\s*')}")
    private val allowedOrigins: Set<String> = HashSet(setOf("*"))

    @Value("#{'\${CORS_ALLOWED_METHODS:*}'.split('\\s*,\\s*')}")
    private val allowedMethods: Set<String> = HashSet(setOf("*"))

    @Value("#{'\${CORS_ALLOWED_HEADERS:*}'.split('\\s*,\\s*')}")
    private val allowedHeaders: Set<String> = HashSet(setOf("*"))

    @Value("\${CORS_ALLOW_CREDENTIALS:true}")
    private val allowCredentials = true

    @Value("\${CORS_PATH:/**}")
    private val path = "/**"

    // https://stackoverflow.com/questions/45677048/how-do-i-enable-cors-headers-in-the-swagger-v2-api-docs-offered-by-springfox-sw
    // https://stackoverflow.com/questions/66060750/cors-error-when-using-corsfilter-and-spring-security
    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = allowCredentials
        allowedOrigins.forEach(Consumer { originPattern: String? -> config.addAllowedOriginPattern(originPattern) })
        allowedMethods.forEach(Consumer { method: String? -> config.addAllowedMethod(method!!) })
        allowedHeaders.forEach(Consumer { allowedHeader: String? -> config.addAllowedHeader(allowedHeader!!) })
        //        log.info("CORS config: {}", toString(config));
        source.registerCorsConfiguration(path, config)
        return CorsFilter(source)
    }

    fun toString(config: CorsConfiguration): String {
        return "CorsConfiguration(" +
                "allowCredentials=" + config.allowCredentials +
                ", allowedOrigins=" + config.allowedOrigins +
                ", allowedMethods=" + config.allowedMethods +
                ", allowedHeaders=" + config.allowedHeaders +
                ")"
    }
}
