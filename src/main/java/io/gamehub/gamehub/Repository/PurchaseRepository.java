package io.gamehub.gamehub.Repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import io.gamehub.gamehub.Model.Game;
import io.gamehub.gamehub.Model.Purchase;

public interface PurchaseRepository extends MongoRepository<Purchase, ObjectId> {

    Optional<Game> findByGameId(ObjectId gameId);
    boolean existsByGameId(ObjectId gameId);
    List<Purchase> findByUserId(ObjectId userId);
    
}