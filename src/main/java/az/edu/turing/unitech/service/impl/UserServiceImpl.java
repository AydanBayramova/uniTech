package az.edu.turing.unitech.service.impl;

import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.domain.repository.AccountRepository;
import az.edu.turing.unitech.domain.repository.UserRepository;
import az.edu.turing.unitech.exception.UserNotFoundException;
import az.edu.turing.unitech.exception.UsernameAlreadyExistsException;
import az.edu.turing.unitech.model.dto.UserDto;
import az.edu.turing.unitech.model.mapper.AccountMapper;
import az.edu.turing.unitech.model.mapper.UserMapper;
import az.edu.turing.unitech.service.Notification;
import az.edu.turing.unitech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public Optional<UserDto> getUserById(UserDto userDto) {

    }

    @Override
    public Page<UserDto> getAll(Pageable pageable) {

    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
             UserEntity userEntity=userRepository.findById(id).orElseThrow(()->new
                     UserNotFoundException("User not found with id: "+id));
             userEntity.setUsername(userDto.getUsername());
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            userEntity.setPassword(userDto.getPassword());
        }
        if (userDto.getPin() != null && !userDto.getPin().isEmpty()) {
            userEntity.setPin(userDto.getPin());
        }
        userEntity.setUpdateDate(LocalDateTime.now());
        userEntity.setCreateDate(userDto.getCreateDate());
        userEntity=userRepository.save(userEntity);

        return userMapper.userEntityToDto(userEntity);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto createUser(UserDto userDto) {
            if(userRepository.findByLastNameAndFirstName(userDto.getFirstName(), userDto.getLastName()).isPresent()){
                throw new UsernameAlreadyExistsException("Username already exists.");
            }

        UserEntity user=userMapper.userDtoToEntity(userDto);
        String encryptedPassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        UserEntity savedUser=userRepository.save(user);
        return userMapper.userEntityToDto(savedUser);
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void verifyUser(Long id) {

    }
}
