package io.gamehub.gamehub.Model;

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
    private String paymentMethod;
    @DocumentReference
    private User user;
    @DocumentReference
    private Game game;

    public Purchase(String paymentMethod, User user, Game game) {
        this.paymentMethod = paymentMethod;
        this.user = user;
        this.game = game;
    }

}
