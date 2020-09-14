package kr.bistroad.swaggerservice.servicedefinition.domain

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import springfox.documentation.swagger.web.SwaggerResource
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
class ServiceDefinitionsContext {
    private val serviceDescriptions: ConcurrentMap<String, String> = ConcurrentHashMap()

    val swaggerDefinitions: List<SwaggerResource>
        get() = serviceDescriptions.map { (serviceName, _) ->
            SwaggerResource().apply {
                this.location = "/service/${serviceName}"
                this.name = serviceName
                this.swaggerVersion = "2.0"
            }
        }

    fun addServiceDefinition(serviceName: String, serviceDescription: String) {
        serviceDescriptions[serviceName] = serviceDescription
    }

    fun getSwaggerDefinition(serviceId: String) =
        serviceDescriptions[serviceId]
}