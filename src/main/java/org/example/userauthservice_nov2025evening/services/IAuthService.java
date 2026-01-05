package org.example.userauthservice_nov2025evening.services;

import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice_nov2025evening.models.User;

public interface IAuthService {

    User signup(String name,String email,String password);

    Pair<User,String> login(String email, String password);

    Boolean validateToken(String token);
}
