package com.example.smartlock.service;

import com.example.smartlock.model.entity.User;
import com.example.smartlock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    protected UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> saveUser(User user) {

        return Optional.of(userRepository.save(user));

    }

    public Optional<User> getUserByEmail(String email) {
        return (userRepository.findByEmail(email));
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
    }
}
