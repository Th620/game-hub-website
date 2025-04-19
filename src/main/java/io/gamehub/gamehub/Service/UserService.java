package io.gamehub.gamehub.Service;

import org.springframework.stereotype.Service;

import io.gamehub.gamehub.Repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}