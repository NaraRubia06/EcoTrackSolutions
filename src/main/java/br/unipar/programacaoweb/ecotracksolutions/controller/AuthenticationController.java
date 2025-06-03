package br.unipar.programacaoweb.ecotracksolutions.controller;

import br.unipar.programacaoweb.ecotracksolutions.configuration.AuthorizationService;
import br.unipar.programacaoweb.ecotracksolutions.dto.LoginDTO;
import br.unipar.programacaoweb.ecotracksolutions.dto.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthorizationService authorizationService;

    public AuthenticationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginResponse loginResponse = authorizationService.doLogin(loginDTO);
        return ResponseEntity.ok(loginResponse);
    }
}
