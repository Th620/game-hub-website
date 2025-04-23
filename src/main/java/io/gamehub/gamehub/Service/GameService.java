package io.gamehub.gamehub.Service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.gamehub.gamehub.Exception.AlreadyExistsException;
import io.gamehub.gamehub.Exception.PurchaseAlreadyExistsException;
import io.gamehub.gamehub.Exception.ResourceNotFoundException;
import io.gamehub.gamehub.Model.Game;
import io.gamehub.gamehub.Model.Purchase;
import io.gamehub.gamehub.Model.Rating;
import io.gamehub.gamehub.Model.User;
import io.gamehub.gamehub.Repository.GameRepository;
import io.gamehub.gamehub.Repository.PurchaseRepository;
import io.gamehub.gamehub.Repository.RatingRepository;
import io.gamehub.gamehub.Repository.UserRepository;

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

    public List<Game> findGames(String search, String genre) {

        if ((genre == null || genre.isEmpty()) && (search == null || search.isEmpty())) {
            return gameRepository.findAll();
        } else {

            return genre == null ? gameRepository.findByTitleContainingIgnoreCase(search)
                    : search == null ? gameRepository.findByTitleContainingIgnoreCaseAndGenre("",
                            genre.toLowerCase())
                            : gameRepository.findByTitleContainingIgnoreCaseAndGenre(search,
                                    genre.toLowerCase());
        }

    }

    public Optional<Game> findGameById(ObjectId id) {
        return gameRepository.findById(id);
    }

    public Purchase addPurchase(UserDetails userDetails, ObjectId gameId) {
        if (purchaseRepository.existsByGameId(gameId)) {
            throw new AlreadyExistsException("You Already Purchased this Game");
        }

        Game game = gameRepository.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Game Not Found"));

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Purchase purchase = new Purchase("", user, game);

        return purchaseRepository.save(purchase);
    }

    public Rating addRating(UserDetails userDetails, ObjectId gameId, int ratingInt) {
        if (ratingRepository.existsByGameId(gameId)) {
            throw new AlreadyExistsException("You Already Rated this Game");
        }

        Game game = gameRepository.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Game Not Found"));

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Rating rating = new Rating(ratingInt, user.getId(), game.getId());

        Rating newRating = ratingRepository.save(rating);

        game.setRating((game.getRating() * game.getRatingCount() + ratingInt) / (game.getRatingCount() + 1));
        game.setRatingCount(game.getRatingCount() + 1);

        return newRating;
    }

}
