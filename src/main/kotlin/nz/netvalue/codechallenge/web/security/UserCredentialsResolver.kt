package nz.netvalue.codechallenge.web.security

import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.lang.NonNull
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

/**
 * Resolves [UserCredentials] method arguments.
 */
class UserCredentialsResolver : HandlerMethodArgumentResolver {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType.isAssignableFrom(UserCredentials::class.java)
    }

    override fun resolveArgument(
        @NonNull parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        @NonNull webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): UserCredentials? {
        return try {
            logger.debug("Getting UserCredentials from request attribute")
            webRequest.getAttribute(
                USER_CREDENTIALS_ATTR, RequestAttributes.SCOPE_REQUEST
            ) as UserCredentials?
        } catch (e: Exception) {
            null
        }
    }
}
