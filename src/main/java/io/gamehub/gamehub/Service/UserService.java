package io.gamehub.gamehub.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.gamehub.gamehub.Dto.GameDto;
import io.gamehub.gamehub.Dto.PurchaseDto;
import io.gamehub.gamehub.Exception.ResourceNotFoundException;
import io.gamehub.gamehub.Exception.UnauthorizedException;
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

    public List<PurchaseDto> getPurchaseLog(UserDetails userDetails) {

        if (userDetails == null) {
            throw new UnauthorizedException("User not found");
        }

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        List<Purchase> purchases = purchaseRepository.findByUserId(user.getId());

        return purchases.stream().map(purchase -> {
            return new PurchaseDto(purchase, new GameDto(purchase.getGame()));
        }).collect(Collectors.toList());
    }

}