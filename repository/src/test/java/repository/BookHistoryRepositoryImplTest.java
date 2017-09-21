package repository;

import model.Book;
import model.BookHistoryEntry;
import model.Operation;
import model.Reader;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by SG0891718 on 6/16/2017.
 */
public class BookHistoryRepositoryImplTest {
    BookRepository bookRepository = new BookRepositoryImpl();
    ReaderRepository readerRepository = new ReaderRepositoryImpl();
    BookHistoryRepository bookHistoryRepository = new BookHistoryRepositoryImpl(bookRepository, readerRepository);
    Book book1 = new Book("Title1", "Author1", 22);
    Book book2 = new Book("Title2", "Author2", 22);
    Reader reader1 = new Reader("FirstName", "LastName");



    @Test
    public void shouldAddBookHistoryEntry() {
        // given
        int bookId = bookRepository.addOrUpdateBook(book1).getId();
        int readerId = readerRepository.addReader(reader1).getId();

        // when
        BookHistoryEntry entry = new BookHistoryEntry(bookId, readerId, LocalDate.now(), Operation.BORROW);
        bookHistoryRepository.addBookHistoryEntry(entry);

        // then
        List<BookHistoryEntry> history = bookHistoryRepository.getBookHistory(bookId);
        assertThat(history).hasSize(1);
        BookHistoryEntry bhe = history.get(0);
        assertThat(bhe).isEqualTo(entry);


    }
}
