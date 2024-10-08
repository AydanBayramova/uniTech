package az.edu.turing.unitech.service.impl;

import az.edu.turing.unitech.domain.entity.AccountEntity;
import az.edu.turing.unitech.domain.entity.Roles;
import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.domain.repository.AccountRepository;
import az.edu.turing.unitech.domain.repository.RoleRepository;
import az.edu.turing.unitech.domain.repository.UserRepository;
import az.edu.turing.unitech.exception.IllegalArgumentException;
import az.edu.turing.unitech.exception.UsernameAlreadyExistsException;
import az.edu.turing.unitech.model.dto.UserDto;
import az.edu.turing.unitech.model.mapper.AccountMapper;
import az.edu.turing.unitech.model.mapper.UserMapper;
import az.edu.turing.unitech.service.Notification;
import az.edu.turing.unitech.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final Notification notification;
    private final RoleRepository roleRepository;

    @Override
    public Page<UserDto> getAll(Pageable pageable) {

        Page<UserEntity> all = userRepository.findAll(pageable);
        return all.map(userMapper::userEntityToDto);
    }


    @Override
    public UserDto update(String pin, UserDto userDto) {
        UserEntity userEntity = userRepository.findByPin(pin).orElseThrow(() -> new IllegalArgumentException("User not found"));
        userEntity.setEmail(userDto.getEmail());
        if (userDto.getPin() != null && !userDto.getPin().isEmpty()) {
            userEntity.setPin(userDto.getPin());
        }
        userEntity = userRepository.save(userEntity);
        return userMapper.userEntityToDto(userEntity);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new java.lang.IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new java.lang.IllegalArgumentException("Old password is incorrect");
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new java.lang.IllegalArgumentException("New password cannot be the same as the old password");
        }

        validateNewPassword(newPassword);

        String encode = passwordEncoder.encode(newPassword);

        user.setPassword(encode);
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);

        notification.sendPasswordChangeNotification(user);

    }

    private void validateNewPassword(String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            throw new java.lang.IllegalArgumentException("New password must be at least 6 characters");
        }
    }
    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByLastNameAndFirstName(userDto.getFirstName(), userDto.getLastName()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists.");
        }
        if (userRepository.findByPin(userDto.getPin()).isPresent()) {
            throw new UsernameAlreadyExistsException("User with this pin already exists.");
        }

        UserEntity user = userMapper.userDtoToEntity(userDto);


        System.out.println("Password (already encoded): " + user.getPassword());


        Roles defaultRole = roleRepository.findByRole("USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(defaultRole);

        UserEntity savedUser = userRepository.save(user);

        return userMapper.userEntityToDto(savedUser);
    }



    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(String pin) {
        UserEntity user = userRepository.findByPin(pin)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
    }





    public boolean existsByPin(String pin) {
        return userRepository.existsByPin(pin);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void assignAdminRole(String pin) {
        UserEntity user = userRepository.findByPin(pin)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Roles adminRole = roleRepository.findByRole("ADMIN")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if (!user.getRoles().contains(adminRole)) {
            user.getRoles().add(adminRole);
            userRepository.save(user);
        }
    }

}
