package org.example.userauthservice_nov2025evening.controllers;


import org.example.userauthservice_nov2025evening.dtos.UserDto;
import org.example.userauthservice_nov2025evening.models.User;
import org.example.userauthservice_nov2025evening.services.UserService;
import org.example.userauthservice_nov2025evening.utils.mappers.UserDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
      User user = userService.getUserById(id);
      return UserDtoMapper.from(user);
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
       User input = UserDtoMapper.from(userDto);
       User response  = userService.saveUser(input);
       return UserDtoMapper.from(response);
    }
}
