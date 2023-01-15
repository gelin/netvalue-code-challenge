package nz.netvalue.codechallenge.web.config

import nz.netvalue.codechallenge.web.security.JwtAuthInterceptor
import nz.netvalue.codechallenge.web.security.JwtTokenService
import nz.netvalue.codechallenge.web.security.UserCredentialsResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfigurer(
    private val jwtTokenService: JwtTokenService
) : WebMvcConfigurer {

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(UserCredentialsResolver())
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(
            JwtAuthInterceptor(jwtTokenService)
        )
    }
}
