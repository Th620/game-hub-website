package io.gamehub.gamehub.Dto;

import java.time.Instant;

import org.bson.types.ObjectId;

import io.gamehub.gamehub.Model.Purchase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseDto {
    private ObjectId id;
    private GameDto game;
    private Instant createdAt;

    public PurchaseDto(Purchase purchase, GameDto gameDto){
        this.id = purchase.getId();
        this.game = gameDto;
        this.createdAt = purchase.getCreatedAt();
    }
}
