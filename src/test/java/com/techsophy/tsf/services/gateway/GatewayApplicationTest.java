package com.techsophy.tsf.services.gateway;

import com.techsophy.tsf.services.gateway.exception.InvalidInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GatewayApplicationTest {

    @Mock
    RouteDefinitionLocator routeDefinitionLocator;

    @Mock
    SwaggerUiConfigParameters swaggerUiConfigParameters;

    @InjectMocks
    GatewayApplication gatewayApplication;

    @Test
    void apisLocatorNullTest() {
        Assertions.assertThrows(InvalidInputException.class, () -> gatewayApplication.apis(swaggerUiConfigParameters, null));
    }

    @Test
    void apisTest() {
        // Mock a RouteDefinition
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId("test-service");

        // Mock the RouteDefinitionLocator to return the mocked RouteDefinition
        when(routeDefinitionLocator.getRouteDefinitions()).thenReturn(Flux.just(routeDefinition));

        // Mock the SwaggerUiConfigParameters behavior
        // Note: swaggerUiConfigParameters.addGroup() does not return anything
        // You can verify if addGroup was called with the correct argument
        // In this case, no need to mock return values
        // When testing methods without return values, use verify() methods

        // Call the method to test
        List<GroupedOpenApi> result = gatewayApplication.apis(swaggerUiConfigParameters, routeDefinitionLocator);

        // Verify the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("test", result.get(0).getGroup());
    }
}
