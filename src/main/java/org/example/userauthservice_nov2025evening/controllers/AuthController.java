package org.example.userauthservice_nov2025evening.controllers;

import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice_nov2025evening.dtos.LoginRequestDto;
import org.example.userauthservice_nov2025evening.dtos.SignupRequestDto;
import org.example.userauthservice_nov2025evening.dtos.UserDto;
import org.example.userauthservice_nov2025evening.dtos.ValidateTokenDto;
import org.example.userauthservice_nov2025evening.models.User;
import org.example.userauthservice_nov2025evening.services.IAuthService;
import org.example.userauthservice_nov2025evening.utils.mappers.UserDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
        return new ResponseEntity<>(UserDtoMapper.from(user), HttpStatus.CREATED);
    }

    //login
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        Pair<User,String> pair = authService.login(
                loginRequestDto.getEmail(), loginRequestDto.getPassword());

        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add("Set-Cookie",pair.b);
        User user = pair.a;
       return new ResponseEntity<>(UserDtoMapper.from(user), headers, HttpStatus.OK);
    }

    @PostMapping("/validateToken")
    public ResponseEntity<Void> validateToken(@RequestBody ValidateTokenDto validateTokenDto) {
        Boolean result = authService.validateToken(validateTokenDto.getToken());

        if(result) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
