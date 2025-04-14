package io.gamehub.gamehub.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.gamehub.gamehub.Repository.UserRepository;

public class UserService {

    @Autowired
    private UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

}