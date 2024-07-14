package com.project.chess_be.service;

import com.project.chess_be.entities.User;
import com.project.chess_be.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

}
