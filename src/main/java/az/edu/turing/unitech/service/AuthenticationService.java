package az.edu.turing.unitech.service;

import az.edu.turing.unitech.service.impl.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final MyUserDetailsService myUserDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean authenticateUser(String pin, String rawPassword) {
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(pin);
        String storedHashedPassword = userDetails.getPassword();

        System.out.println("Raw password: " + rawPassword);
        System.out.println("Stored hashed password: " + storedHashedPassword);

        if (passwordEncoder.matches(rawPassword, storedHashedPassword)) {
            System.out.println("Password matches!");
            return true;
        } else {
            System.out.println("Password does not match.");
            return false;
        }
    }

}
