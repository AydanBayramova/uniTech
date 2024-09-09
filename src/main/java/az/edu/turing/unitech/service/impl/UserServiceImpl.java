package az.edu.turing.unitech.service.impl;

import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.domain.repository.AccountRepository;
import az.edu.turing.unitech.domain.repository.UserRepository;
import az.edu.turing.unitech.exception.IllegalArgumentException;
import az.edu.turing.unitech.exception.UsernameAlreadyExistsException;
import az.edu.turing.unitech.model.dto.UserDto;
import az.edu.turing.unitech.model.mapper.AccountMapper;
import az.edu.turing.unitech.model.mapper.UserMapper;
import az.edu.turing.unitech.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public Page<UserDto> getAll(Pageable pageable) {

        Page<UserEntity> all = userRepository.findAll(pageable);
        return all.map(userMapper::userEntityToDto);
    }



    @Override
    public UserDto update(String pin, UserDto userDto) {
        UserEntity userEntity= userRepository.findByPin(pin).orElseThrow(()->new IllegalArgumentException("User not found"));
        userEntity.setEmail(userDto.getEmail());
        if (userDto.getPin() != null && !userDto.getPin().isEmpty()) {
            userEntity.setPin(userDto.getPin());
        }
        userEntity=userRepository.save(userEntity);
        return userMapper.userEntityToDto(userEntity);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if(userRepository.findByLastNameAndFirstName(userDto.getFirstName(), userDto.getLastName()).isPresent()){
            throw new UsernameAlreadyExistsException("Username already exists.");
        }
        if (userRepository.findByPin(userDto.getPin()).isPresent()) {
            throw new UsernameAlreadyExistsException("User with this pin already exists.");
        }

        UserEntity user=userMapper.userDtoToEntity(userDto);
        UserEntity savedUser=userRepository.save(user);
        return userMapper.userEntityToDto(savedUser);

    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
    }



    @Override
    public void verifyUser(Long id) {

                UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

    }


}
