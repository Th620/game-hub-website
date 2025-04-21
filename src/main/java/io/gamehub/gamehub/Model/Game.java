package io.gamehub.gamehub.Model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "games")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    @Id
    private ObjectId id;
    private String title;
    private String genre;
    private String description;
    private List<String> systemRequirements;
    private double price;
    @DocumentReference
    private User author;
    private double rating;
}
