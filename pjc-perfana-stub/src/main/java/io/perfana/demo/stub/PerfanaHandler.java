package io.perfana.demo.stub;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class PerfanaHandler {

    public Mono<ServerResponse> test(ServerRequest request) {
        System.out.println("test called: " + request);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"hello\": \"test\" }"));
    }

    public Mono<ServerResponse> events(ServerRequest request) {
        System.out.println("events called: " + request);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"hello\": \"events\" }"));
    }

    public Mono<ServerResponse> benchMarkResults(ServerRequest request) {
        System.out.println("benchMarkResults called: " + request);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"hello\": \"benchmark\" }"));
    }
}
