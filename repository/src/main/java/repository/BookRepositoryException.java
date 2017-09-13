package repository;

/**
 * Created by SG0891718 on 6/16/2017.
 */
public abstract class BookRepositoryException extends RuntimeException {
    public BookRepositoryException(String message) {
        super(message);
    }
}
