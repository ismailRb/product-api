package com.services.product.service;

import com.services.product.model.User;
import com.services.product.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserService {

    @Autowired
    private UserRepository userRepository;


    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
