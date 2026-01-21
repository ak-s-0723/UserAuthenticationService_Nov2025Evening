package org.example.userauthservice_nov2025evening.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.userauthservice_nov2025evening.models.Role;

import java.util.List;

@Setter
@Getter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    //private List<Role> roles;
}
