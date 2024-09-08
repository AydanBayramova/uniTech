package az.edu.turing.unitech.service;

import az.edu.turing.unitech.config.JwtTokenProvider;
import az.edu.turing.unitech.domain.entity.AccountEntity;
import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.domain.repository.AccountRepository;
import az.edu.turing.unitech.domain.repository.UserRepository;
import az.edu.turing.unitech.model.dto.LoginRequest;
import az.edu.turing.unitech.model.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest loginRequest) {

        UserEntity user = userRepository.findByPin(loginRequest.getPin())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        AccountEntity account = accountRepository.findByAccountNumber(loginRequest.getAccountNumber())
                .orElseThrow(() -> new BadCredentialsException("Account not found"));


        if (!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }


        String token = jwtTokenProvider.createToken(user.getPin());


        return new LoginResponse(token);
    }
}
