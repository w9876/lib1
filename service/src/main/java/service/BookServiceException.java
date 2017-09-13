package service;

public abstract class BookServiceException extends RuntimeException{
    public BookServiceException(String message) {
        super(message);
    }
}
