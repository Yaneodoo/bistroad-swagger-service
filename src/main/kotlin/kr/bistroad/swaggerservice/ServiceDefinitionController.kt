package kr.bistroad.swaggerservice

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ServiceDefinitionController(
    private val serviceDefinitionsContext: ServiceDefinitionsContext
) {
    @GetMapping("/service/{serviceName}")
    fun getServiceDefinition(@PathVariable serviceName: String) =
        serviceDefinitionsContext.getSwaggerDefinition(serviceName)
}