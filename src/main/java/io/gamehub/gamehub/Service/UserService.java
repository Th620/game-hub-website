package io.gamehub.gamehub.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.gamehub.gamehub.Model.Purchase;
import io.gamehub.gamehub.Model.User;
import io.gamehub.gamehub.Repository.PurchaseRepository;
import io.gamehub.gamehub.Repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;

    public UserService(UserRepository userRepository, PurchaseRepository purchaseRepository) {
        this.userRepository = userRepository;
        this.purchaseRepository = purchaseRepository;
    }

    public void deleteUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        user.ifPresent(userRepository::delete);
    }

    public List<Purchase> getPurchaseLog(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        List<Purchase> purchases = purchaseRepository.findByUserId(user.getId());

        return purchases;
    }

}