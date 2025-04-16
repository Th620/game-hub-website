package io.gamehub.gamehub.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.gamehub.gamehub.Dto.LoginUserDto;
import io.gamehub.gamehub.Dto.RegisterUserDto;
import io.gamehub.gamehub.Model.User;
import io.gamehub.gamehub.Service.AuthenticationService;
import io.gamehub.gamehub.Service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto, HttpServletResponse response) {
        User registeredUser = authenticationService.signup(registerUserDto);

        String jwt = jwtService.generateToken(registeredUser);

        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(2 * 24 * 60 * 60);

        response.addCookie(cookie);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> authenticate(@RequestBody LoginUserDto loginUserDto, HttpServletResponse response) {

        User loggedInUser = authenticationService.authenticate(loginUserDto);

        String jwt = jwtService.generateToken(loggedInUser);

        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(2 * 24 * 60 * 60);

        response.addCookie(cookie);

        return ResponseEntity.ok(loggedInUser);
    }
}
