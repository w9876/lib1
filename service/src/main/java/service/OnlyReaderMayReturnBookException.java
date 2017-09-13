package service;

public class OnlyReaderMayReturnBookException extends BookServiceException {
    public OnlyReaderMayReturnBookException(String message) {
        super(message);
    }
}
