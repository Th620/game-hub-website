package io.gamehub.gamehub.Repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import io.gamehub.gamehub.Model.Game;
import io.gamehub.gamehub.Repository.projection.GenreOnly;

@Repository
public interface GameRepository extends MongoRepository<Game, ObjectId>  {
    List<Game> findByGenre(String genre);
    List<Game> findByTitleContainingIgnoreCaseAndGenre(String search, String genre);
    List<Game> findByTitleContainingIgnoreCase(String search);
    List<GenreOnly> findAllBy();
}
