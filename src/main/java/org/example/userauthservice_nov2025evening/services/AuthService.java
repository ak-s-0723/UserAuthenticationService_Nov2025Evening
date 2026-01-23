package org.example.userauthservice_nov2025evening.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice_nov2025evening.clients.KafkaProducerClient;
import org.example.userauthservice_nov2025evening.dtos.EmailDto;
import org.example.userauthservice_nov2025evening.exceptions.IncorrectPasswordException;
import org.example.userauthservice_nov2025evening.exceptions.UserAlreadyExistException;
import org.example.userauthservice_nov2025evening.exceptions.UserNotRegisteredException;
import org.example.userauthservice_nov2025evening.models.Role;
import org.example.userauthservice_nov2025evening.models.Session;
import org.example.userauthservice_nov2025evening.models.State;
import org.example.userauthservice_nov2025evening.models.User;
import org.example.userauthservice_nov2025evening.repos.RoleRepo;
import org.example.userauthservice_nov2025evening.repos.SessionRepo;
import org.example.userauthservice_nov2025evening.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SessionRepo sessionRepo;

    @Autowired
    private SecretKey secretKey;

    @Autowired
    private KafkaProducerClient kafkaProducerClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public User signup(String name, String email, String password) {
       Optional<User> userOptional = userRepo.findByEmail(email);
       if (userOptional.isPresent()) {
          throw new UserAlreadyExistException("Please try different email Id");
       }

       User user = new User();
       user.setEmail(email);
       user.setName(name);
       user.setPassword(bCryptPasswordEncoder.encode(password));
       user.setCreatedAt(new Date());
       user.setLastUpdatedAt(new Date());
       user.setState(State.ACTIVE);

       Role role = null;
       Optional<Role> roleOptional = roleRepo.findByValue("NON_ADMIN");
       if(roleOptional.isEmpty()) {
           role = new Role();
           role.setValue("NON_ADMIN");
           role.setCreatedAt(new Date());
           role.setLastUpdatedAt(new Date());
           role.setState(State.ACTIVE);
           roleRepo.save(role);
       } else {
           role = roleOptional.get();
       }

        List<Role> existingRoles = user.getRoles();
        existingRoles.add(role);
        user.setRoles(existingRoles);

        //Putting a message into kafka
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(email);
        emailDto.setSubject("Welcome to Scaler");
        emailDto.setBody("Have a good learning experience");
        emailDto.setFrom("anuragonhiring@gmail.com");
        try {
            kafkaProducerClient.sendMessage("signup",
                    objectMapper.writeValueAsString(emailDto));
        }catch (JsonProcessingException exception) {
            throw new RuntimeException(exception);
        }

        return userRepo.save(user);
    }

    @Override
    public Pair<User,String> login(String email, String password) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserNotRegisteredException("Please register first");
        }

        User user = userOptional.get();
        //if (!password.equals(user.getPassword())) {
        if(!bCryptPasswordEncoder.matches(password,user.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password passed");
        }

        // Generating JWT

//        String message = "{\n" +
//                "   \"email\": \"anurag@gmail.com\",\n" +
//                "   \"roles\": [\n" +
//                "      \"instructor\",\n" +
//                "      \"buddy\"\n" +
//                "   ],\n" +
//                "   \"expirationDate\": \"2ndApril2026\"\n" +
//                "}";
//
//        byte[] content = message.getBytes(StandardCharsets.UTF_8);

        Map<String,Object> claims = new HashMap<>();
        claims.put("user_id" ,user.getId());
        List<String> roles = new ArrayList<>();

        for(Role role : user.getRoles()) {
            roles.add(role.getValue());
        }

        claims.put("access",roles);

        Long currentTime = System.currentTimeMillis();
        claims.put("iat",currentTime);
        claims.put("exp",currentTime+10);
        claims.put("issuer","scaler");


        String token = Jwts.builder().claims(claims)
                .signWith(secretKey).compact();

        Session session = new Session();
        session.setUser(user);
        session.setToken(token);
        session.setState(State.ACTIVE);
        sessionRepo.save(session);

        return new Pair<>(user,token);
    }

    public Boolean validateToken(String token) {

        Optional<Session> sessionOptional = sessionRepo.findByToken(token);

        if(sessionOptional.isEmpty()) {
            return false;
        }

        Session session = sessionOptional.get();

        //MacAlgorithm algorithm = Jwts.SIG.HS256;
        //SecretKey secretKey = algorithm.key().build();

        JwtParser jwtParser  = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();

        Long expiry = (Long)claims.get("exp");

        Long currentTime = System.currentTimeMillis();

        if(currentTime > expiry) {
            System.out.println("Token has expired");
            session.setState(State.INACTIVE);
            sessionRepo.save(session);
            return false;
        }

        return true;
    }
}
