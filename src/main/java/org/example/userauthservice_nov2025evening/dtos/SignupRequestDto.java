package org.example.userauthservice_nov2025evening.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {
    private String email;
    private String name;
    private String password;
}
