package fr.lunatech.mbtiassessment.handler;

import fr.lunatech.mbtiassessment.error.domain.NotFoundException;
import fr.lunatech.mbtiassessment.security.domain.UpdateUserForm;
import fr.lunatech.mbtiassessment.security.domain.UserForm;
import fr.lunatech.mbtiassessment.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

import static fr.lunatech.mbtiassessment.model.util.CustomHttpResponse.renderCustomResponse;
import static fr.lunatech.mbtiassessment.security.domain.AuthConstants.USER_NOT_FOUND;
import static fr.lunatech.mbtiassessment.service.util.FileConstants.TEMP_IMAGE_BASE_URL;
import static fr.lunatech.mbtiassessment.service.util.FileConstants.USER_FOLDER;
import static java.nio.file.StandardOpenOption.READ;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.util.StreamUtils.BUFFER_SIZE;

@Component
@Tag(name = "User", description = "Endpoints for operations on Users")
public class UserHandler {

    private final static NotFoundException NOT_FOUND_EXCEPTION =
            new NotFoundException(USER_NOT_FOUND);
    private final UserService userService;
    private final RequestHandler requestHandler;

    public UserHandler(UserService userService, RequestHandler requestHandler) {
        this.userService = userService;
        this.requestHandler = requestHandler;
    }

    public @NonNull Mono<ServerResponse> getAllUsers(ServerRequest request) {
        return this.userService.getAllUsers()
                .collectList()
                .flatMap(users -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(users)
                );
    }

    public @NonNull Mono<ServerResponse> findUser(ServerRequest request) {
        String username = request.pathVariable("username");
        return this.userService.findUserByUsername(username)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(user)
                )
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION));
    }

    public @NonNull Mono<ServerResponse> addUser(ServerRequest request) {
        return this.requestHandler.requireValidBodyMono(body -> body
                .flatMap(userForm -> this.userService.addNewUser(userForm, request))
                .flatMap(addedUser -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(addedUser)
                ), request, UserForm.class);

    }

    public @NonNull Mono<ServerResponse> updateUser(ServerRequest request) {
        return this.requestHandler.requireValidBodyMono(body -> body
                .flatMap(this.userService::updateUser)
                .flatMap(updatedUser -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(updatedUser)
                ), request, UpdateUserForm.class);
    }

    public @NonNull Mono<ServerResponse> updateUserProfileImage(ServerRequest request) {
        String username = request.pathVariable("username");
        return request.multipartData()
                .flatMap(stringPartMultiValueMap -> Mono.just(stringPartMultiValueMap.toSingleValueMap().get("profileImage")))
                .flatMap(part -> this.userService.updateUserProfileImage(username, part, request))
                .flatMap(user -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(user)
                );
    }

    public @NonNull Mono<ServerResponse> getProfileImage(ServerRequest request) {
        String filename = request.pathVariable("filename");
        Path imagePath = Paths.get(USER_FOLDER + "/" + filename);
        Flux<DataBuffer> profileImageFlux = DataBufferUtils.read(imagePath, new DefaultDataBufferFactory(), BUFFER_SIZE, READ);
        return ServerResponse.ok()
                .header(CACHE_CONTROL, CacheControl.noCache().getHeaderValue())
                .contentType(IMAGE_JPEG)
                .body(profileImageFlux, DataBuffer.class);
    }

    public @NonNull Mono<ServerResponse> getTemporaryProfileImage(ServerRequest request) {
        String username = request.pathVariable("username");
        Callable<InputStream> inputStreamCallable = () -> {
            URL imageUrl = new URL(TEMP_IMAGE_BASE_URL + username);
            return imageUrl.openStream();
        };
        Flux<DataBuffer> profileImageFlux = DataBufferUtils.readInputStream(inputStreamCallable, new DefaultDataBufferFactory(), BUFFER_SIZE);
        return ServerResponse.ok()
                .header(CACHE_CONTROL, CacheControl.noCache().getHeaderValue())
                .contentType(IMAGE_JPEG)
                .body(profileImageFlux, DataBuffer.class);
    }

    public @NonNull Mono<ServerResponse> deleteUser(ServerRequest request) {
        String id = request.pathVariable("id");
        return this.userService.findUserById(id)
                .switchIfEmpty(Mono.error(NOT_FOUND_EXCEPTION))
                .flatMap(user -> this.userService.deleteUser(id))
                .flatMap(unused -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(renderCustomResponse(NO_CONTENT, "User deleted successfully"))
                );
    }

}
