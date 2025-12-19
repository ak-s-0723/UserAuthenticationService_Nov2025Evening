package org.example.userauthservice_nov2025evening.services;

import org.example.userauthservice_nov2025evening.exceptions.IncorrectPasswordException;
import org.example.userauthservice_nov2025evening.exceptions.UserAlreadyExistException;
import org.example.userauthservice_nov2025evening.exceptions.UserNotRegisteredException;
import org.example.userauthservice_nov2025evening.models.Role;
import org.example.userauthservice_nov2025evening.models.State;
import org.example.userauthservice_nov2025evening.models.User;
import org.example.userauthservice_nov2025evening.repos.RoleRepo;
import org.example.userauthservice_nov2025evening.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public User signup(String name, String email, String password) {
       Optional<User> userOptional = userRepo.findByEmail(email);
       if (userOptional.isPresent()) {
          throw new UserAlreadyExistException("Please try different email Id");
       }

       User user = new User();
       user.setEmail(email);
       user.setName(name);
       user.setPassword(password); //ToDo on Anurag to encyrpt/encode it.
       user.setCreatedAt(new Date());
       user.setLastUpdatedAt(new Date());
       user.setState(State.ACTIVE);
       Optional<Role> roleOptional = roleRepo.findByValue("NON_ADMIN");
       if(roleOptional.isEmpty()) {
           Role role = new Role();
           role.setValue("NON_ADMIN");
           role.setCreatedAt(new Date());
           role.setLastUpdatedAt(new Date());
           role.setState(State.ACTIVE);
           roleRepo.save(role);
       }

        Role role = roleRepo.findByValue("NON_ADMIN").get();
        List<Role> existingRoles = user.getRoles();
        existingRoles.add(role);
        user.setRoles(existingRoles);
        return userRepo.save(user);
    }

    @Override
    public User login(String email, String password) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserNotRegisteredException("Please register first");
        }

        User user = userOptional.get();
        if (!password.equals(user.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password passed");
        }

        return user;

    }
}
