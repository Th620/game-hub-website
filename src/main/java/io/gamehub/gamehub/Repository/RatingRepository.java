package io.gamehub.gamehub.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import io.gamehub.gamehub.Model.Rating;

public interface RatingRepository extends MongoRepository<Rating, ObjectId> {
    boolean existsByGameId(ObjectId game);
}
