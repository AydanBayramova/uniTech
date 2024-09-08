package az.edu.turing.unitech;

import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.domain.repository.UserRepository;
import az.edu.turing.unitech.exception.UsernameAlreadyExistsException;
import az.edu.turing.unitech.model.dto.UserDto;
import az.edu.turing.unitech.model.mapper.UserMapper;
import az.edu.turing.unitech.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

     @Test
    void createUser_ShouldCreateUserSuccessfully() {
        UserDto userDto = new UserDto();
        userDto.setFirstName("Jane");
        userDto.setLastName("Doe");
        userDto.setPin("67890");

        UserEntity userEntity = new UserEntity();
        UserEntity savedUserEntity = new UserEntity();
        UserDto savedUserDto = new UserDto();

        when(userRepository.findByLastNameAndFirstName(userDto.getFirstName(), userDto.getLastName()))
                .thenReturn(Optional.empty());

        when(userRepository.findByPin(userDto.getPin()))
                .thenReturn(Optional.empty());

        when(userMapper.userDtoToEntity(userDto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(savedUserEntity);
        when(userMapper.userEntityToDto(savedUserEntity)).thenReturn(savedUserDto);

        UserDto result = userService.createUser(userDto);

        assertNotNull(result);
        assertEquals(savedUserDto, result);
        verify(userRepository).save(userEntity);
    }

    @Test
    void update_ShouldUpdateUserSuccessfully() {
        String pin = "12345";
        UserDto userDto = new UserDto();
        userDto.setEmail("newemail@example.com");
        userDto.setPin("67890");

        UserEntity existingUserEntity = new UserEntity();
        existingUserEntity.setPin(pin);

        UserEntity updatedUserEntity = new UserEntity();
        updatedUserEntity.setEmail(userDto.getEmail());
        updatedUserEntity.setPin(userDto.getPin());

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setEmail(updatedUserEntity.getEmail());
        updatedUserDto.setPin(updatedUserEntity.getPin());

        when(userRepository.findByPin(pin)).thenReturn(Optional.of(existingUserEntity));
        //when(userRepository.save(any(UserEntity.class))).thenReturn(updatedUserEntity);
        when(userMapper.userEntityToDto(updatedUserEntity)).thenReturn(updatedUserDto);

        UserDto result = userService.update(pin, userDto);

        assertNotNull(result);
        assertEquals("newemail@example.com", result.getEmail());
        assertEquals("67890", result.getPin());
        verify(userRepository).save(existingUserEntity);
    }





}
