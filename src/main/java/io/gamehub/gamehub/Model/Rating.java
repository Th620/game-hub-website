package io.gamehub.gamehub.Model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "ratings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    @Id
    private ObjectId id;
    private int rating;
    private ObjectId userId;
    private ObjectId gameId;

    public Rating(int rating, ObjectId userId, ObjectId gameId) {
        this.rating = rating;
        this.userId = userId;
        this.gameId = gameId;
    }
}
