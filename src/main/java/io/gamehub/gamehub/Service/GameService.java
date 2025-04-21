package io.gamehub.gamehub.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.gamehub.gamehub.Model.Game;
import io.gamehub.gamehub.Repository.GameRepository;

@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> findAllGames(){
        return gameRepository.findAll();
    }
}
