package az.edu.turing.unitech.service;

import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.domain.repository.UserRepository;
import az.edu.turing.unitech.model.dto.UserDto;
import az.edu.turing.unitech.model.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    Page<UserDto> getAll(Pageable pageable);

    UserDto update(String pin, UserDto userDto);

    UserDto createUser(UserDto userDto);

    void deleteById(Long id);

    void verifyUser(Long id);

    void saveUser(UserEntity user);

    boolean existsByPin(String pin);

}
