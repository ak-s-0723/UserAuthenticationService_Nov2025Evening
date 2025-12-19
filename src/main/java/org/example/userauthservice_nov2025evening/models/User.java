package org.example.userauthservice_nov2025evening.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class User extends BaseModel {
    private String email;
    private String password;
    private String name;
    @ManyToMany
    private List<Role> roles = new ArrayList<>();
}


//1           M
//user       role
//M            1
//
//
//M     :     M