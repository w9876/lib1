package service;

import model.Book;
import model.BookHistoryEntry;
import model.Reader;
import org.junit.Test;
import repository.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class LibraryServiceITest {

    BookRepository bookRepo = new BookRepositoryImpl();
    ReaderRepository readerRepo = new ReaderRepositoryImpl();
    BookHistoryRepository bookHistoryRepo = new BookHistoryRepositoryImpl(bookRepo, readerRepo);
    LibraryService libraryService = new LibraryService(bookRepo, readerRepo, bookHistoryRepo);

    Book book1 = new Book("Title1", "Author1", 22);
    Reader reader1 = new Reader("John", "Smith");



    @Test
    public void shouldBorrowAndReturnBookAndRecordHistory() {
        // given
        final int bookId = bookRepo.addOrUpdateBook(book1).getId();
        final int readerId = readerRepo.add(reader1).getId();

        // when
        libraryService.borrowBook(readerId, bookId);
        // then

        assertThat(bookRepo.getBook(bookId).getBorrow().getReaderId()).isEqualTo(readerId);


        // when
        libraryService.returnBook(readerId, bookId);

        // then
        assertThat(bookRepo.getBook(bookId).getBorrow()).isNull();

        // when
        List<BookHistoryEntry> bookHistory = bookHistoryRepo.getBookHistory(bookId);

        // then
        assertThat(bookHistory).hasSize(1);
        assertThat(bookHistory.get(0).getReaderId()).isEqualTo(readerId);

        // when
        List<BookHistoryEntry> readerHistory = bookHistoryRepo.getReaderHistory(readerId);

        // then
        assertThat(readerHistory).hasSize(1);
        assertThat(readerHistory.get(0).getBookId()).isEqualTo(bookId);


    }


}
