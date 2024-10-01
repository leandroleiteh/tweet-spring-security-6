package tech.leandroleitedev.springsecurity.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tech.leandroleitedev.springsecurity.controllers.dtos.CreateUserRequest;
import tech.leandroleitedev.springsecurity.entitys.EnumRoleName;
import tech.leandroleitedev.springsecurity.entitys.User;
import tech.leandroleitedev.springsecurity.repositorys.RoleRepository;
import tech.leandroleitedev.springsecurity.repositorys.UserRepository;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/createUser")
    @Transactional
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest userRequest) {

        var basicRole = roleRepository.findByRoleName(EnumRoleName.BASIC);
        var userFromDb = userRepository.findByUsername(userRequest.username());

        if (userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        var user = new User();
        user.setUsername(userRequest.username());
        user.setPassword(bCryptPasswordEncoder.encode(userRequest.password()));
        user.setRoles(Set.of(basicRole));

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
