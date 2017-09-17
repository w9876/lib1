package service;

import model.*;
import repository.BookHistoryRepository;
import repository.BookRepository;
import repository.ReaderRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class LibraryService {
    private static final Period BORROW_PERIOD = Period.of(0, 0, 14);
    private final BookRepository bookRepo;
    private final ReaderRepository readerRepo;
    private final BookHistoryRepository bookHistoryRepo;


    public LibraryService(BookRepository bookRepo, ReaderRepository readerRepo, BookHistoryRepository bookHistoryRepo) {
        this.bookRepo = bookRepo;
        this.readerRepo = readerRepo;
        this.bookHistoryRepo = bookHistoryRepo;
    }

    public void borrowBook(int readerId, int bookId) {

        Book book = bookRepo.getBook(bookId);
        if (book.getBorrow() != null) {
            throw new AlreadyBorrowedException(format("Book with ID=%s already borrowed by reader with ID=%s", bookId, readerId));
        }
        Reader reader = readerRepo.getReader(readerId);

        LocalDate borrowDate = LocalDate.now();

        Borrow borrow = new Borrow(reader.getId(), borrowDate, computeReturnBy(borrowDate));
        book.setBorrow(borrow);

        bookRepo.addOrUpdateBook(book);
        bookHistoryRepo.addBookHistoryEntry(new BookHistoryEntry(bookId, readerId, borrowDate, Operation.BORROW));

    }

    private LocalDate computeReturnBy(LocalDate borrowDate) {
        return borrowDate.plus(BORROW_PERIOD);
    }

    public void returnBook(int readerId, int bookId) {
        Book book = bookRepo.getBook(bookId);
        if (book.getBorrow() == null) {
            throw new BookNotBorrowedException(format("Book with ID=%s is not borrowed - cannot return", bookId));
        }
        Integer borrowerId = book.getBorrow().getReaderId();
        if (readerId != borrowerId) {
            throw new OnlyReaderMayReturnBookException(format("Book with ID=%d is borrowed by reader with ID=%d so reader with ID=%d may not return it", book.getId(), borrowerId, readerId));
        }
        book.setBorrow(null);
        bookRepo.addOrUpdateBook(book);
    }

    public List<BookHistoryEntry> getBookHistory(int bookId) {
        return new ArrayList<>();
    }
}
