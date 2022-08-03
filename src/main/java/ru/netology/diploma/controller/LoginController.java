package ru.netology.diploma.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.diploma.dto.RequestLogin;
import ru.netology.diploma.dto.ResponseLogin;
import ru.netology.diploma.service.AuthorizationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final AuthorizationService authorizationService;

    public LoginController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping
    public ResponseLogin login(@Valid @RequestBody RequestLogin requestLogin) {
        return authorizationService.login(requestLogin);
    }
}
