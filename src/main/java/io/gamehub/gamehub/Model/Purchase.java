package io.gamehub.gamehub.Model;

import java.time.Instant;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "purchases")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {
    @Id
    private ObjectId id;
    private double amount;
    @DocumentReference
    private User user;
    @DocumentReference
    private Game game;
    private Instant createdAt;

    public Purchase(double amount, User user, Game game) {
        this.amount = amount;
        this.user = user;
        this.game = game;
    }

}
