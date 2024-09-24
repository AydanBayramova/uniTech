package az.edu.turing.unitech.controller;

import az.edu.turing.unitech.config.JwtTokenProvider;
import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.domain.repository.UserRepository;
import az.edu.turing.unitech.model.dto.LoginRequest;
import az.edu.turing.unitech.model.dto.LoginResponse;
import az.edu.turing.unitech.model.dto.RegistrationRequest;
import az.edu.turing.unitech.model.dto.UserDto;
import az.edu.turing.unitech.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("Attempting login with PIN: " + loginRequest.getPin());
            System.out.println("Attempting login with Password: " + loginRequest.getPassword());
            UserEntity userEntity = userRepository.findByPin(loginRequest.getPin())
                    .orElseThrow(() -> new UsernameNotFoundException("PIN not found: " + loginRequest.getPin()));


            System.out.println("Raw password: " + loginRequest.getPassword());
            System.out.println("Stored (hashed) password: " + userEntity.getPassword());

            if (passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())) {
                System.out.println("Password matched!");
            } else {
                System.out.println("Password did not match!");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getPin(), loginRequest.getPassword()));

            String token = jwtTokenProvider.generateToken(authentication);

            return ResponseEntity.ok(new LoginResponse(token));
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed: " + e.getMessage());
            return ResponseEntity.status(401).body(new LoginResponse("Invalid PIN or password"));
        }
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        if (userService.existsByPin(registrationRequest.getPin())) {
            return ResponseEntity.badRequest().body("PIN is already taken");
        }

        UserDto newUser = new UserDto();
        newUser.setFirstName(registrationRequest.getFirstName());
        newUser.setLastName(registrationRequest.getLastName());
        newUser.setPin(registrationRequest.getPin());

        String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        newUser.setPassword(encodedPassword);
        newUser.setEmail(registrationRequest.getEmail());

        userService.createUser(newUser);
        System.out.println("Registered user with PIN: " + newUser.getPin());

        return ResponseEntity.ok("User registered successfully");
    }

}
