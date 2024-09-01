package az.edu.turing.unitech.service;

import az.edu.turing.unitech.domain.repository.UserRepository;
import az.edu.turing.unitech.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public interface UserService {

    Optional<UserDto> getUserById(UserDto userDto);

    Page<UserDto> getAll(Pageable pageable);

    UserDto update(Long id, UserDto userDto);

    UserDto createUser(UserDto userDto);

    void deleteById(Long id);

    void deleteAll();
}
