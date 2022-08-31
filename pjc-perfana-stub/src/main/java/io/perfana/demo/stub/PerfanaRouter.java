package io.perfana.demo.stub;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class PerfanaRouter {

    @Bean
    public RouterFunction<ServerResponse> route(PerfanaHandler perfanaHandler) {

        return RouterFunctions.route()
                .GET("/api/test", RequestPredicates.accept(MediaType.APPLICATION_JSON), perfanaHandler::test)
                .GET("/api/benchmark-results/{systemUnderTest}/{testRunId}", RequestPredicates.accept(MediaType.APPLICATION_JSON), perfanaHandler::benchMarkResults)
                .POST("/api/events", RequestPredicates.accept(MediaType.APPLICATION_JSON), perfanaHandler::events)
                .build();
    }

}
