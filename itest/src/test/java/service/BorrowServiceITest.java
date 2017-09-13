package service;

import model.Book;
import model.Reader;
import org.junit.Test;
import repository.*;

import static org.assertj.core.api.Assertions.assertThat;


public class BorrowServiceITest {

    BookRepository bookRepo = new BookRepositoryImpl();
    ReaderRepository readerRepo = new ReaderRepositoryImpl();
    BookHistoryRepository bookHistoryRepository = new BookHistoryRepositoryImpl(bookRepo, readerRepo);
    BorrowService borrowService = new BorrowService(bookRepo, readerRepo, bookHistoryRepository);

    Book book1 = new Book("Title1", "Author1", 22);
    Reader reader1 = new Reader("FirstName", "LastName");


    @Test
    public void shouldBorrowAndReturnBookAndRecordHistory() {
        // given
        int bookId = bookRepo.addOrUpdateBook(book1).getId();
        int readerId = readerRepo.add(reader1).getId();

        // when
        borrowService.borrowBook(readerId, bookId);
        // then

        assertThat(bookRepo.getBook(bookId).getBorrow().getReaderId()).isEqualTo(readerId);


        // when
        borrowService.returnBook(readerId, bookId);

        // then

        assertThat(bookRepo.getBook(bookId).getBorrow()).isNull();
    }

}
