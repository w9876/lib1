package repository;

public class BookNotFoundException extends BookRepositoryException {
    public BookNotFoundException(int bookId) {
        super(String.format("Book with id %d not found!", bookId));
    }
}
