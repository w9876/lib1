package service;

public class AlreadyBorrowedException extends BookServiceException {
    public AlreadyBorrowedException(String message) {
        super(message);
    }
}
