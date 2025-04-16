package io.gamehub.gamehub.Dto;

import lombok.Getter;

@Getter
public class RegisterUserDto {
    private String username;
    private String email;
    private String password;

    public RegisterUserDto(String email, String password, String username) {
        if (email == null || !isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        if (username == null || username.length() < 3) {
            throw new IllegalArgumentException("Username must be at least 2 characters long");
        }

        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void setEmail(String email) {
        if (email == null || !isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        this.password = password;
    }

    public void setUsername(String username) {
        if (username == null || username.length() < 2) {
            throw new IllegalArgumentException("Username must be at least 8 characters long");
        }
        this.username = username;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
}
