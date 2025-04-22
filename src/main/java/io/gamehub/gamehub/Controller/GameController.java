package io.gamehub.gamehub.Controller;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.gamehub.gamehub.Model.Game;
import io.gamehub.gamehub.Service.GameService;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("")
    public ResponseEntity<List<Game>> getGames(){
        return ResponseEntity.ok(gameService.findAllGames());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGame(@PathVariable String id){

        if (!ObjectId.isValid(id)) {
            ResponseEntity.badRequest().body("Game not found");
        }

        ObjectId objectId = new ObjectId(id);

        Optional<Game> game = gameService.findGameById(objectId);
        
        return game.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
}
