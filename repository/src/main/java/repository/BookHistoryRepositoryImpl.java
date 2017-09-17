package repository;

import model.BookHistoryEntry;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.util.stream.Collectors.toList;

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
        return history.stream()
                .filter(bhe -> bhe.getBookId() == bookId)
                .collect(toList());
    }

    @Override
    public List<BookHistoryEntry> getReaderHistory(int readerId) {
        return history.stream()
                .filter(bhe -> bhe.getReaderId() == readerId)
                .collect(toList());
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
