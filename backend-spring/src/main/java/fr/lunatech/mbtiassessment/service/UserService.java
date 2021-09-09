package fr.lunatech.mbtiassessment.service;

import fr.lunatech.mbtiassessment.error.domain.AlreadyExistException;
import fr.lunatech.mbtiassessment.error.domain.NotAnImageFileException;
import fr.lunatech.mbtiassessment.error.domain.NotFoundException;
import fr.lunatech.mbtiassessment.model.User;
import fr.lunatech.mbtiassessment.repository.UserRepository;
import fr.lunatech.mbtiassessment.security.domain.Role;
import fr.lunatech.mbtiassessment.security.domain.UpdateUserForm;
import fr.lunatech.mbtiassessment.security.domain.UserForm;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import static fr.lunatech.mbtiassessment.security.domain.AuthConstants.*;
import static fr.lunatech.mbtiassessment.service.util.FileConstants.*;

@Service
public class UserService implements ReactiveUserDetailsService {

    private static final Log LOGGER = LogFactory.getLog(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return this.userRepository.findUserByUsername(username)
                .flatMap(
                        user -> {
                            LOGGER.info(FOUND_USER_BY_USERNAME + username);
                            user.setLastLoginDateDisplay(user.getLastLoginDate());
                            user.setLastLoginDate(new Date());
                            return this.userRepository.save(user);
                        }
                )
                .switchIfEmpty(Mono.defer(
                        () -> {
                            LOGGER.error(NO_USER_FOUND_BY_USERNAME + username);
                            return Mono.error(new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username));
                        }
                ))
                .map(User::toDetails);
    }

    public Flux<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public Mono<User> findUserByUsername(String username) {
        return this.userRepository.findUserByUsername(username);
    }

    public Mono<User> findUserById(String id) {
        return this.userRepository.findById(id);
    }

    public Mono<User> saveUser(User user) {
        return this.userRepository.save(user);
    }

    public Mono<User> findUserByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }

    public Mono<Void> deleteUser(String id) {
        return this.userRepository.deleteById(id);
    }

    public Mono<User> addNewUser(UserForm newUserForm, ServerRequest request) {
        return validateNewUsernameAndEmail(StringUtils.EMPTY, newUserForm.getUsername(), newUserForm.getEmail())
                .flatMap(unused -> {
                    if (Objects.isNull(newUserForm.getIsNonLocked())) {
                        newUserForm.setIsNonLocked(true);
                    }
                    if (Objects.isNull(newUserForm.getIsEnabled())) {
                        newUserForm.setIsEnabled(true);
                    }
                    User user = buildUserFromUserForm(newUserForm);
                    setTemporaryProfileImage(user, request);
                    return this.userRepository.save(user);
                });
    }

    public Mono<User> updateUser(UpdateUserForm updateUserForm) {
        return validateNewUsernameAndEmail(updateUserForm.getCurrentUsername(), updateUserForm.getUsername(), updateUserForm.getEmail())
                .flatMap(currentUser -> {
                    currentUser.setFirstName(updateUserForm.getFirstName());
                    currentUser.setLastName(updateUserForm.getLastName());
                    currentUser.setUsername(updateUserForm.getUsername());
                    currentUser.setEmail(updateUserForm.getEmail());
                    if (Objects.nonNull(updateUserForm.getIsNonLocked()))
                        currentUser.setNonLocked(updateUserForm.getIsNonLocked());
                    if (Objects.nonNull(updateUserForm.getIsEnabled()))
                        currentUser.setEnabled(updateUserForm.getIsEnabled());
                    if (Objects.nonNull(updateUserForm.getRole())) {
                        currentUser.setRole(Role.getRoleByString(updateUserForm.getRole()).getLabel());
                        currentUser.setAuthorities(new ArrayList<>(Arrays.asList(Role.getRoleByString(updateUserForm.getRole()).getAuthorities())));
                    }
                    return this.userRepository.save(currentUser);
                });
    }

    public Mono<User> updateUserProfileImage(String username, Part profileImage, ServerRequest request) {
        return validateNewUsernameAndEmail(username, null, null)
                .flatMap(user -> saveProfileImageUrl(user, (FilePart) profileImage, request));
    }

    public Mono<User> validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) {
        Mono<User> userByNewUsername = findUserByUsername(newUsername);
        Mono<User> userByNewEmail = findUserByEmail(newEmail);

        if (StringUtils.isNotBlank(currentUsername)) {
            Mono<User> currentUser = findUserByUsername(currentUsername);
            return currentUser.switchIfEmpty(Mono.error(new NotFoundException(USER_NOT_FOUND)))
                    .zipWith(userByNewEmail)
                    .flatMap(objects -> {
                        if (!Objects.equals(objects.getT1().getId(), objects.getT2().getId())) {
                            return Mono.error(new AlreadyExistException(USERNAME_ALREADY_EXISTS));
                        } else {
                            return currentUser;
                        }
                    })
                    .zipWith(userByNewEmail)
                    .flatMap(objects -> {
                        if (!Objects.equals(objects.getT1().getId(), objects.getT2().getId())) {
                            return Mono.error(new AlreadyExistException(EMAIL_ALREADY_EXISTS));
                        } else {
                            return currentUser;
                        }
                    })
                    .then(currentUser);
        } else {
            return userByNewUsername.flatMap(user -> Mono.error(new AlreadyExistException(USERNAME_ALREADY_EXISTS)))
                    .then(userByNewEmail)
                    .flatMap(user -> Mono.error(new AlreadyExistException(EMAIL_ALREADY_EXISTS)))
                    .then(Mono.just(new User()));
        }
    }

    public void setTemporaryProfileImage(User user, ServerRequest request) {
        user.setProfileImageUrl(getTemporaryProfileImageUrl(user.getUsername(), request));
    }

    public User buildUserFromUserForm(UserForm userForm) {
        return User.builder()
                .firstName(userForm.getFirstName())
                .lastName(userForm.getLastName())
                .username(userForm.getUsername())
                .password(encodePassword(userForm.getPassword()))
                .email(userForm.getEmail())
                .joinDate(new Date())
                .isEnabled(userForm.getIsEnabled())
                .isNonLocked(userForm.getIsNonLocked())
                .role(Role.getRoleByString(userForm.getRole()).getLabel())
                .authorities(new ArrayList<>(Arrays.asList(Role.getRoleByString(userForm.getRole()).getAuthorities())))
                .build();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private Mono<User> saveProfileImageUrl(User user, FilePart profileImage, ServerRequest request) {

        if (!IMAGE_TYPE.equals(Objects.requireNonNull(profileImage.headers().getContentType()).getType())) {
            return Mono.error(new NotAnImageFileException(profileImage.filename() + NOT_AN_IMAGE_FILE));
        }
        Path userFolder = Paths.get(USER_FOLDER).toAbsolutePath().normalize();
        Path profileImagePath = userFolder.resolve(user.getUsername() + "." + JPEG_EXTENSION);
        return DataBufferUtils.write(profileImage.content(), profileImagePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
                .doOnSuccess(unused -> {
                    user.setProfileImageUrl(setProfileImageUrl(user.getUsername(), request));
                    LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.filename());
                })
                .then(userRepository.save(user));
    }

    public String setProfileImageUrl(String username, ServerRequest request) {
        return UriComponentsBuilder.fromHttpRequest(request.exchange().getRequest()).replacePath(USER_IMAGE_PATH + username + "." + JPEG_EXTENSION).toUriString();
    }

    private String getTemporaryProfileImageUrl(String username, ServerRequest request) {
        return UriComponentsBuilder.fromHttpRequest(request.exchange().getRequest()).replacePath(DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }
}
