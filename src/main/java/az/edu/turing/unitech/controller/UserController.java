//package az.edu.turing.unitech.controller;
//
//import az.edu.turing.unitech.model.dto.UserDto;
//import az.edu.turing.unitech.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/users")
//public class UserController {
//    private final UserService userService;
//
//    @PostMapping("/create")
//    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
//           UserDto createdUser=userService.createUser(userDto);
//           return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
//    }
//
//    @PutMapping("/{pin}")
//    public ResponseEntity<UserDto> updateUser(@PathVariable String pin, @RequestBody UserDto userDto)
//    {
//        UserDto updatedUser=userService.update(pin,userDto);
//        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
//    }
//
//}