package az.edu.turing.unitech.service.impl;

import az.edu.turing.unitech.domain.entity.AccountEntity;
import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.domain.repository.AccountRepository;
import az.edu.turing.unitech.domain.repository.UserRepository;
import az.edu.turing.unitech.exception.IllegalArgumentException;
import az.edu.turing.unitech.model.dto.UserDto;
import az.edu.turing.unitech.model.enums.Status;
import az.edu.turing.unitech.model.mapper.AccountMapper;
import az.edu.turing.unitech.model.mapper.UserMapper;
import az.edu.turing.unitech.service.Notification;
import az.edu.turing.unitech.service.SecurityService;
import az.edu.turing.unitech.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AccountMapper accountMapper;
    private final SecurityService securityService;
    private final AccountRepository accountRepository;

    @Override
    public Optional<UserDto> getUserById(UserDto userDto) {
        return Optional.empty();
    }

    @Override
    public Page<UserDto> getAll(Pageable pageable) {

        Page<UserEntity> all = userRepository.findAll(pageable);
        return all.map(userMapper::userEntityToDto);
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        return null;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        return null;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!securityService.currentUserHasAdminRole()) {
            throw new SecurityException("You do not have permission to delete this User");
        }

        userRepository.delete(user);
    }

    @Override
    public void deleteAll() {

    }

//    @Override
//    public void verifyUser(Long id) {
//
//        UserEntity user = userRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
//
//        if (user.getStatus() == Status.DEACTIVATE || user.getStatus() == Status.DELETED) {
//            throw new IllegalArgumentException("User is not active and cannot be verified");
//        }
//    }

    @Override
    public Page<UserDto> getAllByStatus(Status status, Pageable pageable) {

        Page<UserEntity> allByStatus = userRepository.findAllByStatus(status, pageable);

        return allByStatus.map(userMapper::userEntityToDto);
    }


}
