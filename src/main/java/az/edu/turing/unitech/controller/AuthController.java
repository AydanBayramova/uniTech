package az.edu.turing.unitech.controller;

import az.edu.turing.unitech.config.JwtTokenProvider;
import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.model.dto.LoginRequest;
import az.edu.turing.unitech.model.dto.LoginResponse;
import az.edu.turing.unitech.model.dto.RegistrationRequest;
import az.edu.turing.unitech.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getPin(), loginRequest.getPassword()));


            String token = jwtTokenProvider.generateToken(authentication);


            return ResponseEntity.ok(new LoginResponse(token));
        } catch (AuthenticationException e) {

            return ResponseEntity.status(401).body(new LoginResponse("Invalid PIN or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest registrationRequest) {

        if (userService.existsByPin(registrationRequest.getPin())) {
            return ResponseEntity.badRequest().body("PIN is already taken");
        }

        UserEntity newUser = new UserEntity();
        newUser.setFirstName(registrationRequest.getFirstName());
        newUser.setLastName(registrationRequest.getLastName());
        newUser.setPin(registrationRequest.getPin());
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));  // Encrypt password
        newUser.setEmail(registrationRequest.getEmail());

        userService.saveUser(newUser);

        return ResponseEntity.ok("User registered successfully");
    }


}
