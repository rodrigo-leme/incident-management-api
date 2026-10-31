package br.com.fiap.incidentmanagement.controller;

import br.com.fiap.incidentmanagement.dto.LoginData;
import br.com.fiap.incidentmanagement.dto.TokenData;
import br.com.fiap.incidentmanagement.entity.User;
import br.com.fiap.incidentmanagement.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenData> login(@RequestBody @Valid LoginData data) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        Authentication authentication = manager.authenticate(authenticationToken);

        User user = (User) authentication.getPrincipal();
        String tokenJWT = tokenService.generateToken(user);

        return ResponseEntity.ok(new TokenData(tokenJWT, user.getId(), user.getLogin()));
    }
}
