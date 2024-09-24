package az.edu.turing.unitech.controller;

import az.edu.turing.unitech.model.dto.ChangePasswordRequest;
import az.edu.turing.unitech.model.dto.UserDto;
import az.edu.turing.unitech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{pin}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String pin, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.update(pin, userDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{pin}")
    public ResponseEntity<UserDto> deleteById(@PathVariable String pin) {
        userService.deleteById(pin);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PostMapping("/assign-role/{pin}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> assignAdminRole(@PathVariable String pin) {
        try {
            userService.assignAdminRole(pin);
            return ResponseEntity.ok("Admin role assigned to user with pin: " + pin);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Handle exceptions
        }
    }
    @PutMapping("/{userId}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long userId,
            @RequestBody ChangePasswordRequest changePasswordRequest) {

        userService.changePassword(userId, changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("all")
    public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable) {
        Page<UserDto> all = userService.getAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(all);
    }

}