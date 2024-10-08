package az.edu.turing.unitech.service.impl;

import az.edu.turing.unitech.domain.entity.MyUserDetails;
import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String pin) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByPin(pin)
                .orElseThrow(() -> new UsernameNotFoundException("PIN not found: " + pin));


        System.out.println("Loaded PIN: " + userEntity.getPin());
        System.out.println("Stored password (hashed): " + userEntity.getPassword());

        return new MyUserDetails(userEntity);
    }

}
