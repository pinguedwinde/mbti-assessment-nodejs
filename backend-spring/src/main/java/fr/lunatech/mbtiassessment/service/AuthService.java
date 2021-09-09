package fr.lunatech.mbtiassessment.service;

import fr.lunatech.mbtiassessment.model.User;
import fr.lunatech.mbtiassessment.security.domain.UserCredentials;
import fr.lunatech.mbtiassessment.security.domain.UserForm;
import fr.lunatech.mbtiassessment.security.domain.UserPrincipal;
import fr.lunatech.mbtiassessment.security.jwt.JwtTokenProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import static fr.lunatech.mbtiassessment.security.domain.Role.ROLE_USER;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final ReactiveAuthenticationManager reactiveAuthenticationManager;
    private final UserService userService;


    public AuthService(JwtTokenProvider jwtTokenProvider, ReactiveAuthenticationManager reactiveAuthenticationManager, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
        this.userService = userService;
    }

    public Mono<User> loginUser(UserCredentials userCredentials) {
        return authenticate(userCredentials.getUsername(), userCredentials.getPassword())
                .then(userService.findUserByUsername(userCredentials.getUsername()));
    }

    public Mono<User> register(UserForm registerUserForm, ServerRequest request) {
        return this.userService.validateNewUsernameAndEmail(StringUtils.EMPTY, registerUserForm.getUsername(), registerUserForm.getEmail())
                .then(Mono.just(registerUserForm))
                .flatMap(registerUser -> {
                    registerUser.setIsEnabled(true);
                    registerUser.setIsNonLocked(true);
                    registerUser.setRole(ROLE_USER.name());
                    User user = this.userService.buildUserFromUserForm(registerUser);
                    this.userService.setTemporaryProfileImage(user, request);
                    return this.userService.saveUser(user);
                });
    }

    public String getToken(UserPrincipal userPrincipal) {
        return jwtTokenProvider.generateJwtToken(userPrincipal);
    }

    private Mono<Authentication> authenticate(String username, String password) {
        return this.reactiveAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
