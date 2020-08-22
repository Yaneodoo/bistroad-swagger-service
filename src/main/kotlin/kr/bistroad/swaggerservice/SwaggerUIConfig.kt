package kr.bistroad.swaggerservice

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider
import springfox.documentation.swagger.web.SwaggerResourcesProvider

@Configuration
class SwaggerUIConfig(
    private val serviceDefinitionsContext: ServiceDefinitionsContext
) {
    @Primary
    @Bean
    fun swaggerResourcesProvider(
        defaultResourcesProvider: InMemorySwaggerResourcesProvider
    ) = SwaggerResourcesProvider { serviceDefinitionsContext.swaggerDefinitions }
}