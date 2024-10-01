package tech.leandroleitedev.springsecurity.configs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import tech.leandroleitedev.springsecurity.entitys.EnumRoleName;
import tech.leandroleitedev.springsecurity.entitys.User;
import tech.leandroleitedev.springsecurity.repositorys.RoleRepository;
import tech.leandroleitedev.springsecurity.repositorys.UserRepository;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void run(String... args) {

        var roleAdmin = roleRepository.findByRoleName(EnumRoleName.ADMIN);
        var userAdmin = userRepository.findByUsername("admin");

        userAdmin.ifPresentOrElse(
                (user) -> log.info("Admin user exists!"),
                () -> {
                    var user = new User();
                    user.setUsername("admin");
                    user.setPassword(passwordEncoder.encode("123456"));
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );

    }
}
