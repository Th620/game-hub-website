package io.gamehub.gamehub.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.gamehub.gamehub.Dto.PurchaseDto;
import io.gamehub.gamehub.Model.User;
import io.gamehub.gamehub.Service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteAccount(Authentication authentication) {
        String email = authentication.getName();
        userService.deleteUserByEmail(email);
        return ResponseEntity.ok("You account has been deleted");
    }

    @GetMapping("/purchases")
    public ResponseEntity<List<PurchaseDto>> getPurchaseLog(@AuthenticationPrincipal UserDetails userDetails) {
        List<PurchaseDto> purchases = userService.getPurchaseLog(userDetails);
        return ResponseEntity.ok(purchases);
    }
}
