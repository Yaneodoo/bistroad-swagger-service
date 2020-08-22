package kr.bistroad.swaggerservice

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Component
class ServiceDescriptionUpdater(
    private val discoveryClient: DiscoveryClient,
    private val serviceDefinitionsContext: ServiceDefinitionsContext,

    @Value("\${spring.application.name}")
    private val swaggerServiceId: String
) {
    private val restTemplate: RestTemplate = RestTemplate()
    private val objectMapper: ObjectMapper = ObjectMapper()

    @Scheduled(fixedDelayString = "\${swagger.config.refresh-rate}")
    fun refreshSwaggerConfigurations() =
        discoveryClient.services
            .filter { it != swaggerServiceId }
            .map(discoveryClient::getInstances)
            .filter { it != null && it.isNotEmpty() }
            .forEach { instances ->
                val instance = instances.first()
                val swaggerUrl = getSwaggerUrl(instance)

                val jsonData = getSwaggerDefinitionForApi(instance.serviceId, swaggerUrl)

                if (jsonData != null) {
                    val content = getJson(instance.serviceId, jsonData)
                    serviceDefinitionsContext.addServiceDefinition(instance.serviceId, content)
                }
            }

    fun getSwaggerUrl(serviceInstance: ServiceInstance): String {
        val swaggerUrl = serviceInstance.metadata[KEY_SWAGGER_URL] ?: DEFAULT_SWAGGER_URL
        return serviceInstance.uri.toString() + swaggerUrl
    }

    fun getSwaggerDefinitionForApi(serviceName: String, url: String): Any? {
        return try {
            restTemplate.getForObject<Any>(url)
        } catch (ex: RestClientException) {
            null
        }
    }

    fun getJson(serviceId: String, jsonData: Any): String {
        return try {
            objectMapper.writeValueAsString(jsonData)
        } catch (ex: JsonProcessingException) {
            ""
        }
    }

    companion object {
        private const val DEFAULT_SWAGGER_URL = "/v2/api-docs"
        private const val KEY_SWAGGER_URL = "swagger_url"
    }
}