package repository;

import model.Book;
import model.BookHistoryEntry;

import java.util.List;

/**
 * Created by SG0891718 on 7/9/2017.
 */
public interface BookRepository {
    Book getBook(int id);


    /**
     *  Populates {@link Book#id} field of the passed object
      * @param book
     */
    Book addOrUpdateBook(Book book);


    boolean bookExists(BookHistoryEntry entry);
}
