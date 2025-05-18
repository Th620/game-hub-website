package io.gamehub.gamehub.Dto;

import io.gamehub.gamehub.Model.Game;
import io.gamehub.gamehub.Model.SysRequirements;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameDto {
    private String id;
    private String title;
    private String genre;
    private String description;
    private SysRequirements system_requirements;
    private double price;
    private String img;
    private double rating;
    private int rating_count;

    public GameDto(Game game, double rating, int rating_count) {
        this.id = game.getId().toString();
        this.title = game.getTitle();
        this.genre = game.getGenre();
        this.description = game.getDescription();
        this.system_requirements = game.getSystem_requirements();
        this.price = game.getPrice();
        this.img = game.getImg();
        this.rating = rating;
        this.rating_count = rating_count;
    }
}
