package nz.netvalue.codechallenge.web.config

import nz.netvalue.codechallenge.web.security.JsonWebTokenService
import nz.netvalue.codechallenge.web.security.JwtTokenService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration for security and authorization.
 */
@Configuration
class SecurityConfig {

    @Bean
    fun jwtTokenService(
        @Value("\${jwt.signingKey}") jwtSigningKey: String
    ): JwtTokenService {
        return JsonWebTokenService(jwtSigningKey)
    }

}
