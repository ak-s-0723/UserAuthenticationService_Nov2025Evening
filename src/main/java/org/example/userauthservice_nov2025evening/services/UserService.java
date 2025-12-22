package org.example.userauthservice_nov2025evening.services;

import org.example.userauthservice_nov2025evening.exceptions.UserAlreadyExistException;
import org.example.userauthservice_nov2025evening.exceptions.UserNotRegisteredException;
import org.example.userauthservice_nov2025evening.models.User;
import org.example.userauthservice_nov2025evening.repos.RoleRepo;
import org.example.userauthservice_nov2025evening.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    public User getUserById(Long id) {
        Optional<User> userOptional = userRepo.findById(id);
        if(userOptional.isEmpty()) {
            throw new UserNotRegisteredException("user with requested id not found");
        }

        return userOptional.get();
    }

    public User saveUser(User user) {
        Optional<User> userOptional = userRepo.findByEmail(user.getEmail());
        if(userOptional.isPresent()) {
            throw new UserAlreadyExistException("user already exists");
        }

        return userRepo.save(user);
    }
}
