package nz.netvalue.codechallenge.web.security

/**
 * Service to work with JWT token.
 */
interface JwtTokenService {

    /**
     * Parses the token to user credentials
     * @param token the token string to parse
     * @return authenticated data
     */
    fun parseToken(token: String): UserCredentials?

}
