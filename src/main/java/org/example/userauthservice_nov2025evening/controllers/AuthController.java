package org.example.userauthservice_nov2025evening.controllers;

import org.example.userauthservice_nov2025evening.dtos.LoginRequestDto;
import org.example.userauthservice_nov2025evening.dtos.SignupRequestDto;
import org.example.userauthservice_nov2025evening.dtos.UserDto;
import org.example.userauthservice_nov2025evening.models.User;
import org.example.userauthservice_nov2025evening.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    //signup
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequestDto
                                                      signupRequestDto){
        User user = authService.signup(signupRequestDto.getName(), signupRequestDto.getEmail(), signupRequestDto.getPassword());
        return new ResponseEntity<>(from(user), HttpStatus.CREATED);
    }

    //login
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) {
       User user = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
       return new ResponseEntity<>(from(user),HttpStatus.OK);
    }

    private UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setRoles(user.getRoles());
        userDto.setEmail(user.getEmail());
        return userDto;
    }


}
