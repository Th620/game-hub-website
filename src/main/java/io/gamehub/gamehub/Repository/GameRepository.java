package io.gamehub.gamehub.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import io.gamehub.gamehub.Model.Game;

@Repository
public interface GameRepository extends MongoRepository<Game, ObjectId>  {
    
}
