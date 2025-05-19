package io.gamehub.gamehub.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/index")
    public String homePage() {
        return "forward:/index.html";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "forward:/login.html";
    }

    @GetMapping("/signup")
    public String SignupPage() {
        return "forward:/signup.html";
    }

    @GetMapping("/games")
    public String GamesPage() {
        return "forward:/games.html";
    }

    @GetMapping("/games/{id}")
    public String GamePage() {
        return "forward:/game.html";
    }

     @GetMapping("/profile")
    public String ProfilePage() {
        return "forward:/profile.html";
    }

     @GetMapping("/purchase-log")
    public String PurchaseLog() {
        return "forward:/purchase-log.html";
    }
}