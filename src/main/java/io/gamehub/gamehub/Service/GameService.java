package io.gamehub.gamehub.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.gamehub.gamehub.Dto.GameDto;
import io.gamehub.gamehub.Exception.AlreadyExistsException;
import io.gamehub.gamehub.Exception.ResourceNotFoundException;
import io.gamehub.gamehub.Exception.UnauthorizedException;
import io.gamehub.gamehub.Model.Game;
import io.gamehub.gamehub.Model.Purchase;
import io.gamehub.gamehub.Model.Rating;
import io.gamehub.gamehub.Model.User;
import io.gamehub.gamehub.Repository.GameRepository;
import io.gamehub.gamehub.Repository.PurchaseRepository;
import io.gamehub.gamehub.Repository.RatingRepository;
import io.gamehub.gamehub.Repository.UserRepository;
import io.gamehub.gamehub.Repository.projection.GenreOnly;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    public GameService(GameRepository gameRepository, PurchaseRepository purchaseRepository,
            UserRepository userRepository, RatingRepository ratingRepository) {
        this.gameRepository = gameRepository;
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
    }

    public List<GameDto> findGames(String search, String genre) {

        if ((genre == null || genre.isEmpty()) && (search == null || search.isEmpty())) {
            List<Game> games = gameRepository.findAll();

            return games.stream().map(game -> {
                List<Rating> ratings = ratingRepository.findAllByGameId(game.getId());
                double rating = ratings.stream().mapToInt(Rating::getRating).average().orElse(0.0);
                return new GameDto(game, rating, ratings.size());
            }).collect(Collectors.toList());
        } else {

            List<Game> games = (genre == null || genre.isEmpty())
                    ? gameRepository.findByTitleContainingIgnoreCase(search.trim())
                    : (search == null || search.isEmpty()) ? gameRepository.findByTitleContainingIgnoreCaseAndGenre("",
                            genre.toLowerCase().trim())
                            : gameRepository.findByTitleContainingIgnoreCaseAndGenre(search.trim(),
                                    genre.toLowerCase().trim());

            return games.stream().map(game -> {
                List<Rating> ratings = ratingRepository.findAllByGameId(game.getId());
                double rating = ratings.stream().mapToInt(Rating::getRating).average().orElse(0.0);
                return new GameDto(game, rating, ratings.size());
            }).collect(Collectors.toList());
        }

    }

    public List<String> findGenres() {
        return gameRepository.findAllBy().stream().map(GenreOnly::getGenre).toList();
    }

    public GameDto findGameById(ObjectId id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game Not Found"));

        List<Rating> ratings = ratingRepository.findAllByGameId(id);

        double rating = ratings.stream().mapToInt(Rating::getRating).average().orElse(0.0);

        return new GameDto(game, rating, ratings.size());

    }

    public Purchase addPurchase(UserDetails userDetails, ObjectId gameId) {

        if (userDetails == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        if (purchaseRepository.existsByGameId(gameId)) {
            throw new AlreadyExistsException("You Already Purchased this Game");
        }

        Game game = gameRepository.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Game Not Found"));

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Purchase purchase = new Purchase(game.getPrice(), user, game);
        purchase.setCreatedAt(Instant.now());

        return purchaseRepository.save(purchase);
    }

    public Rating addRating(UserDetails userDetails, ObjectId gameId, int ratingInt) {

        if (userDetails == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        if (ratingRepository.existsByGameId(gameId)) {
            throw new AlreadyExistsException("You Already Rated this Game");
        }

        Game game = gameRepository.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Game Not Found"));

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Rating rating = new Rating(ratingInt, user.getId(), game.getId());

        Rating newRating = ratingRepository.save(rating);

        return newRating;
    }

}
