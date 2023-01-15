package nz.netvalue.codechallenge.web.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.lang.NonNull
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import java.lang.reflect.Method

const val USER_CREDENTIALS_ATTR = "userCredentialsAttr"

/**
 * Spring interceptor for JWT based authentication and authorization
 */
class JwtAuthInterceptor(
    private val jwtService: JwtTokenService
) : HandlerInterceptor {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod) {
            val method = handler.method
            return checkAuthorization(method, request, response)
        }
        return true
    }

    private fun checkAuthorization(
        method: Method,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Boolean {
        return try {
            val userCredentials = getUserCredentials(request)
            if (method.isAnnotationPresent(AuthRoleRequired::class.java)) {
                if (userCredentials == null) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                    return false
                }
                val annotation = method.getAnnotation(AuthRoleRequired::class.java)
                val requiredRole = annotation.value
                val userRoles: MutableSet<String> = HashSet()
                userRoles.addAll(userCredentials.roles)
                val isAuthorized = userRoles.contains(requiredRole)
                if (!isAuthorized) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Not enough permissions")
                    return false
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun getUserCredentials(request: HttpServletRequest): UserCredentials? {
        return try {
            val header: String = request.getHeader(HttpHeaders.AUTHORIZATION)
            if (!header.startsWith("Bearer ")) {
                return null
            }
            val token = header.substring(7)
            val credentials = jwtService.parseToken(token)
            logger.debug("Found credentials in Authorization header: {}", credentials?.username)
            request.setAttribute(USER_CREDENTIALS_ATTR, credentials)
            credentials
        } catch (e: Exception) {
            logger.debug("Failed to get credentials: {}", e.message, e)
            null
        }
    }

}
