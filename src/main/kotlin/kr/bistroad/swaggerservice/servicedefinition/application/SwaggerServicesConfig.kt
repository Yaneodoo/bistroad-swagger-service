package kr.bistroad.swaggerservice.servicedefinition.application

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("swagger.config")
class SwaggerServicesConfig {
    var services: List<String> = emptyList()
}