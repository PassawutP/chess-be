package com.project.chess_be.service;

import com.project.chess_be.entities.Game;
import com.project.chess_be.entities.User;
import com.project.chess_be.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final AuthenticationManager authManager;

    private final JwtService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.authManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public Optional<User> getUser(Integer userId){
        return userRepository.findById(userId);
    }

    public List<Integer> getUserId(String username){
        return userRepository.getUserId(username);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public List<User> getUserIdByGameId(Integer gameId){
        return userRepository.getUserIdByGameId(gameId);
    }

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public String verify(User user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        } else {
            return "fail";
        }
    }
}
