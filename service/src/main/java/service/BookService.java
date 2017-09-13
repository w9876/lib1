package service;

import model.Book;
import repository.BookRepository;
import repository.BookRepositoryImpl;

public class BookService {
    private final BookRepository bookRepository = new BookRepositoryImpl();

    public Book addOrUpdateBook(Book book) {
        return bookRepository.addOrUpdateBook(book);
    }
}
