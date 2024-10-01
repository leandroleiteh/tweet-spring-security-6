package tech.leandroleitedev.springsecurity.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.leandroleitedev.springsecurity.controllers.dtos.LoginRequest;
import tech.leandroleitedev.springsecurity.controllers.dtos.LoginResponse;
import tech.leandroleitedev.springsecurity.entitys.EnumRoleName;
import tech.leandroleitedev.springsecurity.entitys.Role;
import tech.leandroleitedev.springsecurity.repositorys.UserRepository;

import java.time.Instant;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class LoginTokenController {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {

        var user = userRepository.findByUsername(loginRequest.username());
        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("User or passowrd is invalid! ");
        }
        var now = Instant.now();
        var expiresIn = 300L;

        var scopes = user.get().getRoles()
                .stream()
                .map(Role::getRoleName)
                .map(String::valueOf)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("tech.leandroleitedev.spring-security")
                .subject(user.get().getUserId().toString())
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .issuedAt(now).build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, jwtValue));
    }
}
