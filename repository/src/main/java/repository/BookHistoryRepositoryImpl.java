package repository;

import model.BookHistoryEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BookHistoryRepositoryImpl implements BookHistoryRepository {

    private final Collection<BookHistoryEntry> history = new ConcurrentLinkedQueue<>();

    public BookHistoryRepositoryImpl(BookRepository bookRepository, ReaderRepository readerRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
    }

    private BookRepository bookRepository;
    private ReaderRepository readerRepository;


    @Override
    public List<BookHistoryEntry> getBookHistory(int bookId) {
        return new ArrayList<>(history);
    }


    @Override
    public void addBookHistoryEntry(BookHistoryEntry entry) {
        if (!bookRepository.bookExists(entry)) {
            throw new BookNotFoundException(entry.getBookId());
        }
        if (!readerRepository.readerExists(entry.getReaderId())) {
            throw new ReaderNotFoundException(entry.getReaderId());
        }
        history.add(entry);

    }

}
