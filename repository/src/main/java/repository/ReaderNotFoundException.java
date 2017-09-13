package repository;

public class ReaderNotFoundException extends BookRepositoryException {
    public ReaderNotFoundException(int bookId) {
        super(String.format("Reader with id %d not found!", bookId));
    }
}
