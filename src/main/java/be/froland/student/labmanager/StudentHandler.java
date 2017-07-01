package be.froland.student.labmanager;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
class StudentHandler {
    private final StudentRepository studentRepository;
    private static final Mono<ServerResponse> NOT_FOUND = ServerResponse.notFound().build();

    StudentHandler(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    Mono<ServerResponse> createStudent(ServerRequest req) {
        return req.bodyToMono(Student.class)
            .flatMap(studentRepository::save)
            .flatMap(student -> {
                URI studentUri = UriComponentsBuilder.fromUriString("/students/{id}").buildAndExpand(student.getId()).toUri();
                return ServerResponse.created(studentUri).build();
            }).switchIfEmpty(NOT_FOUND);
    }

    Mono<ServerResponse> getStudent(ServerRequest request) {
        Mono<Student> studentMono = studentRepository.findById(request.pathVariable("id"));
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(studentMono, Student.class)
            .switchIfEmpty(NOT_FOUND);
    }

    Mono<ServerResponse> getAllStudents(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(studentRepository.findAll(), Student.class);
    }
}
