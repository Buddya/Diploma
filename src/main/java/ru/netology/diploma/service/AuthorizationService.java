package ru.netology.diploma.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.netology.diploma.dto.RequestLogin;
import ru.netology.diploma.dto.ResponseLogin;
import ru.netology.diploma.entities.Token;
import ru.netology.diploma.entities.User;
import ru.netology.diploma.repository.TokenRepository;
import ru.netology.diploma.repository.UserRepository;

import java.util.Random;

@Service
public class AuthorizationService {
    private final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    private final Random random = new Random();

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public AuthorizationService(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    //log in
    public ResponseLogin login(RequestLogin requestLogin) {
        final String login = requestLogin.getLogin();
        final User user = userRepository.findById(login).orElseThrow(() ->
                new RuntimeException("User with login " + login + " not found"));

        if (!user.getPassword().equals(requestLogin.getPassword())) {
            throw new RuntimeException("Incorrect password for user " + login);
        }
        final String authToken = String.valueOf(random.nextLong());
        tokenRepository.save(new Token(authToken));
        logger.info("User " + login + " entered with token " + authToken);
        return new ResponseLogin(authToken);
    }

    //log out
    public void logout(String authToken) {
        tokenRepository.deleteById(authToken);
    }

    //авторизация
    public void tokenValidation(String authToken){
        if (!tokenRepository.existsById(authToken)) {
            throw new RuntimeException("User is not authorized");
        }
    }
}
