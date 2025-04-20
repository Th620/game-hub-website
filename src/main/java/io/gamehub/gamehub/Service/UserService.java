package io.gamehub.gamehub.Service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import io.gamehub.gamehub.Model.User;
import io.gamehub.gamehub.Repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void deleteUserByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        user.ifPresent(userRepository::delete);
    }

}