package org.example.userauthservice_nov2025evening.services;

import org.example.userauthservice_nov2025evening.models.User;

public interface IAuthService {

    User signup(String name,String email,String password);

    User login(String email,String password);
}
