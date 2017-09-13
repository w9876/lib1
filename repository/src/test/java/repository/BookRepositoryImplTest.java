package repository;

import model.Book;
import model.BookHistoryEntry;
import model.Operation;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Created by SG0891718 on 6/16/2017.
 */
public class BookRepositoryImplTest {
    BookRepository repository = new BookRepositoryImpl();
    Book book1 = new Book("Title1", "Author1", 22);
    Book book2 = new Book("Title2", "Author2", 22);

    @Test
    public void shouldAddAndRetrieveBook()  {
        // when
        Book persistedBook1 = repository.addOrUpdateBook(book1);
        Book persistedBook2 = repository.addOrUpdateBook(book2);

        // then

        assertThat(book1).isNotSameAs(persistedBook1);
        assertThat(book1.getId()).isNull();
        assertThat(persistedBook1.getId()).isNotNull();
        assertThat(book1).isEqualToIgnoringGivenFields(persistedBook1,"id");

        assertThat(persistedBook1.getId()).isNotEqualTo(persistedBook2.getId());

    }

    @Test
    public void shouldFailOnGetNonExistingBook() {
        // when
        repository.addOrUpdateBook(book1);

        // then
        catchException(repository).getBook(3);

        Exception actual = caughtException();
        assertThat(actual).isInstanceOf(BookNotFoundException.class);
    }

}
