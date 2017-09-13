package repository;

import model.Book;
import model.BookHistoryEntry;
import org.assertj.core.util.Preconditions;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.util.Preconditions.checkNotNull;

/**
 * Created by SG0891718 on 6/16/2017.
 */
public class BookRepositoryImpl implements BookRepository {
    private final Map<Integer, Book> bookMap = new ConcurrentHashMap<>();
    private final AtomicInteger bookIdCtr = new AtomicInteger(0);
    private final Serializer serializer = new Serializer();
    private final ReaderRepository readerRepository = new ReaderRepositoryImpl();


    @Override
    public Book getBook(int id) {
        Book book = bookMap.get(id);
        if (book == null) {
            throw new BookNotFoundException(id);
        }

        Book clone = serializer.fromJson(serializer.toJson(book), Book.class);
        return clone;
    }

    @Override
    public Book addOrUpdateBook(Book book)
    {
        checkNotNull(book, "book parameter must not be null");
        Book clonedBook = serializer.fromJson(serializer.toJson(book), Book.class);
        if (clonedBook.getId() == null) {
            clonedBook.setId(bookIdCtr.incrementAndGet());
        }
        bookMap.put(clonedBook.getId(), clonedBook);
        return getBook(clonedBook.getId());
    }


    @Override
    public boolean bookExists(BookHistoryEntry entry) {
        return bookMap.get(entry.getBookId()) != null;
    }



}
