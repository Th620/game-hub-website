package io.gamehub.gamehub.Exception;

public class PurchaseAlreadyExistsException extends RuntimeException {
    public PurchaseAlreadyExistsException(String message){
        super(message);
    }
}
