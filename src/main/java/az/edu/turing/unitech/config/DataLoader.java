package az.edu.turing.unitech.config;

import az.edu.turing.unitech.domain.entity.Roles;
import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.domain.repository.RoleRepository;
import az.edu.turing.unitech.domain.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DataLoader {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner initRolesAndAdmin() {
        return args -> {
            // Initialize roles if they don't exist
            if (roleRepository.findByRole("USER").isEmpty()) {
                Roles userRole = new Roles();
                userRole.setRole("USER");
                roleRepository.save(userRole);
            }

            if (roleRepository.findByRole("ADMIN").isEmpty()) {
                Roles adminRole = new Roles();
                adminRole.setRole("ADMIN");
                roleRepository.save(adminRole);
            }

            // Fetch roles
            Roles userRole = roleRepository.findByRole("USER").orElseThrow(() -> new RuntimeException("USER role not found"));
            Roles adminRole = roleRepository.findByRole("ADMIN").orElseThrow(() -> new RuntimeException("ADMIN role not found"));

            // Initialize admin user if not present
            if (userRepository.findByPin("0000001").isEmpty()) {
                UserEntity adminUser = new UserEntity();
                adminUser.setFirstName("Admin");
                adminUser.setLastName("User");
                adminUser.setPin("0000001");
                adminUser.setEmail("elvinaliyevinfo@gmail.com");
                adminUser.setPassword(passwordEncoder.encode("adminPass"));

                adminUser.getRoles().add(userRole);
                adminUser.getRoles().add(adminRole);

                userRepository.save(adminUser);
            }
        };
    }

}
