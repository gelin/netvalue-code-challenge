package nz.netvalue.codechallenge.web.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 * Configures some basic properties of the OpenAPI spec exposed.
 */
@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(@Value("\${version.application}") appVersion: String): OpenAPI {
        return OpenAPI()
            .info(Info().title("Code Challenge API").version(appVersion))
            .components(Components()
                .addSecuritySchemes("bearer", SecurityScheme()
                    .name("bearer")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")))
            .security(listOf(
                SecurityRequirement().addList("bearer")
            ))
    }

}
