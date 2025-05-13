package io.gamehub.gamehub.Controller;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.gamehub.gamehub.Dto.RatingDto;
import io.gamehub.gamehub.Model.Game;
import io.gamehub.gamehub.Model.Purchase;
import io.gamehub.gamehub.Model.Rating;
import io.gamehub.gamehub.Service.GameService;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("")
    public ResponseEntity<List<Game>> getGames(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String search) {

        return ResponseEntity.ok(gameService.findGames(search, genre));

    }

    @GetMapping("/genres")
    public ResponseEntity<List<String>> getGenres() {

        return ResponseEntity.ok(gameService.findGenres());

    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGame(@PathVariable String id) {

        if (!ObjectId.isValid(id)) {
            ResponseEntity.badRequest().body("Invalid Id");
        }

        ObjectId objectId = new ObjectId(id);

        Optional<Game> game = gameService.findGameById(objectId);

        return game.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/purchase")
    public ResponseEntity<Purchase> purchaseGame(@PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (!ObjectId.isValid(id)) {
            ResponseEntity.badRequest().body("Invalid Id");
        }

        ObjectId gameId = new ObjectId(id);

        Purchase purchase = gameService.addPurchase(userDetails, gameId);

        return ResponseEntity.ok(purchase);
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<Rating> rateGame(
            @RequestBody RatingDto ratingDto,
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (!ObjectId.isValid(id)) {
            ResponseEntity.badRequest().body("Invalid Id");
        }

        ObjectId gameId = new ObjectId(id);

        Rating rating = gameService.addRating(userDetails, gameId, ratingDto.getRating());

        return ResponseEntity.ok(rating);
    }
}
