package be.froland.student.labmanager;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
@RequiredArgsConstructor
public class RouteConfiguration {

    private final StudentHandler studentHandler;

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions
            .route(GET("/students").and(accept(MediaType.APPLICATION_JSON)), studentHandler::getAllStudents)
            .andRoute(POST("/students").and(contentType(MediaType.APPLICATION_JSON)), studentHandler::createStudent)
            .andRoute(GET("/students/{id}").and(accept(MediaType.APPLICATION_JSON)), studentHandler::getStudent)
            .andRoute(all(), request -> ServerResponse.notFound().build());
    }
}
