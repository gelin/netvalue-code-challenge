package nz.netvalue.codechallenge.web.security

import io.jsonwebtoken.Jwts
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Service to parse JWT tokens.
 */
class JsonWebTokenService(
    tokenSigningKey: String
) : JwtTokenService {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val parser = Jwts.parserBuilder().setSigningKey(tokenSigningKey.toByteArray(Charsets.UTF_8)).build()

    override fun parseToken(token: String): UserCredentials {
        logger.debug("Parsing token {}", token)
        val claims = parser.parseClaimsJws(token)
        val subject = claims.body.subject
        val roles = claims.body.get("roles", List::class.java)
        logger.debug("Parsed subject={} roles={}", subject, roles)
        return UserCredentialsImpl(subject, roles.map { it.toString() }.toSet())
    }

}
