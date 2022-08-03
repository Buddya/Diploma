package ru.netology.diploma.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.diploma.dto.RequestLogin;
import ru.netology.diploma.entities.User;
import ru.netology.diploma.repository.TokenRepository;
import ru.netology.diploma.repository.UserRepository;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class AuthorizationServiceTest {
    public static final String USER = "admin";
    public static final String PASSWORD = "password";
    public static final String AUTH_TOKEN = "test123";
    public static final String FAKE_USER = "blank";
    public static final String FAKE_TOKEN = "fake123";

    private final UserRepository userRepository = createUserRepositoryMock();
    private final TokenRepository tokenRepository = createTokenRepositoryMock();

    private UserRepository createUserRepositoryMock() {
        final UserRepository userRepository = Mockito.mock(UserRepository.class);
        when(userRepository.findById(USER)).thenReturn(Optional.of(new User(USER, PASSWORD)));
        when(userRepository.findById(FAKE_USER)).thenReturn(Optional.empty());
        return userRepository;
    }

    private TokenRepository createTokenRepositoryMock() {
        final TokenRepository tokenRepository = Mockito.mock(TokenRepository.class);
        when(tokenRepository.existsById(AUTH_TOKEN)).thenReturn(true);
        when(tokenRepository.existsById(FAKE_TOKEN)).thenReturn(false);
        return tokenRepository;
    }

    @Test
    void login() {
        final AuthorizationService authorizationService = new AuthorizationService(userRepository, tokenRepository);
        Assertions.assertDoesNotThrow(() -> authorizationService.login(new RequestLogin(USER, PASSWORD)));
    }

    @Test
    void loginFailure() {
        final AuthorizationService authorizationService = new AuthorizationService(userRepository, tokenRepository);
        Assertions.assertThrows(RuntimeException.class, () -> authorizationService.login(new RequestLogin(FAKE_USER, PASSWORD)));
    }

    @Test
    void loginPasswordFailure() {
        final AuthorizationService authorizationService = new AuthorizationService(userRepository, tokenRepository);
        Assertions.assertThrows(RuntimeException.class, () -> authorizationService.login(new RequestLogin(USER, "qwerty")));
    }

    @Test
    void logout() {
        final AuthorizationService authorizationService = new AuthorizationService(userRepository, tokenRepository);
        Assertions.assertDoesNotThrow(() -> authorizationService.logout(AUTH_TOKEN));
    }

    @Test
    void checkToken() {
        final AuthorizationService authorizationService = new AuthorizationService(userRepository, tokenRepository);
        Assertions.assertDoesNotThrow(() -> authorizationService.tokenValidation(AUTH_TOKEN));
    }

    @Test
    void checkTokenFailure() {
        final AuthorizationService authorizationService = new AuthorizationService(userRepository, tokenRepository);
        Assertions.assertThrows(RuntimeException.class, () -> authorizationService.tokenValidation(FAKE_TOKEN));
    }
}
