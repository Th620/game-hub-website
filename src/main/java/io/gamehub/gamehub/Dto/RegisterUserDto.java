package io.gamehub.gamehub.Dto;

import lombok.Getter;

@Getter
public class RegisterUserDto {
    private String name;
    private String email;
    private String password;

    public RegisterUserDto(String email, String password, String name) {
        if (email == null || !isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        if (name == null || name.length() < 3) {
            throw new IllegalArgumentException("name must be at least 2 characters long");
        }

        this.name = name;
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

    public void setName(String name) {
        if (name == null || name.length() < 2) {
            throw new IllegalArgumentException("name must be at least 8 characters long");
        }
        this.name = name;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
}
