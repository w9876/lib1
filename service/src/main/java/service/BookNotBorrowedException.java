package service;

public class BookNotBorrowedException extends BookServiceException{
    public BookNotBorrowedException(String message) {
        super(message);
    }
}
