package service;


import model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import repository.BookHistoryRepository;
import repository.BookRepository;
import repository.ReaderRepository;

import java.time.LocalDate;
import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BorrowServiceTest {

    private static final int BOOK_ID = 11;
    private static final int READER_ID = 44;
    private static final int READER2_ID = 55;
    @Mock

    private BookRepository bookRepo;

    @Mock
    private ReaderRepository readerRepo;

    @Mock
    private BookHistoryRepository bookHistoryRepository;

    BorrowService borrowService;

    private Book book;
    private Book borrowedBook;
    private Reader reader;

    @Before
    public void setUp() {
        reader = new Reader("First", "Last");
        reader.setId(READER_ID);

        book = new Book("Title", "Author", 123);
        book.setId(BOOK_ID);

        borrowedBook = new Book("Title", "Author", 123);
        borrowedBook.setId(BOOK_ID);
        borrowedBook.setBorrow(new Borrow(reader.getId(), LocalDate.now(), LocalDate.now().plus(10, DAYS)));


        initMocks(this);
        borrowService = new BorrowService(bookRepo, readerRepo, bookHistoryRepository);


        when(bookRepo.getBook(BOOK_ID)).thenReturn(book);
        when(readerRepo.getReader(READER_ID)).thenReturn(reader);
    }

    @Test
    public void shouldBorrow() {


        // when
        borrowService.borrowBook(READER_ID, BOOK_ID);

        // then

        verify(bookRepo).getBook(BOOK_ID);
        verify(readerRepo).getReader(READER_ID);

        ArgumentCaptor<Book> bookArg = ArgumentCaptor.forClass(Book.class);
        verify(bookRepo).addOrUpdateBook(bookArg.capture());
        Book bookArgValue = bookArg.getValue();
        assertThat(bookArgValue.getId()).isEqualTo(book.getId());
        assertThat(bookArgValue.getBorrow()).isNotNull();
        assertThat(bookArgValue.getBorrow().getBorrowDate()).isNotNull();
        assertThat(bookArgValue.getBorrow().getReturnBy()).isNotNull();

        assertThat(bookArgValue.getBorrow().getReturnBy().isAfter(bookArgValue.getBorrow().getBorrowDate()))
                .describedAs(String.format("Return date should be after borrow date: %s", bookArgValue))
                .isTrue();

    }


    @Test
    public void shouldNotAllowToBorrowWhenAlreadyBorrowed() {
        // given
        when(bookRepo.getBook(BOOK_ID)).thenReturn(borrowedBook);

        // when
        catchException(borrowService).borrowBook(READER_ID, BOOK_ID);

        // then
        Exception e = caughtException();
        assertThat(e)
                .isExactlyInstanceOf(AlreadyBorrowedException.class)
                .hasMessage("Book with ID=11 already borrowed by reader with ID=44");

    }

    @Test
    public void shouldFailOnReturningNotBorrowed() {
        // given
        // not borrowed

        // when
        catchException(borrowService).returnBook(READER_ID, BOOK_ID);


        // then
        Exception exception = caughtException();
        assertThat(exception).isExactlyInstanceOf(BookNotBorrowedException.class).hasMessage("Book with ID=11 is not borrowed - cannot return");

    }

    @Test
    public void shouldReturnBook() {
        // given

        when(bookRepo.getBook(BOOK_ID)).thenReturn(borrowedBook);

        // when
        borrowService.returnBook(READER_ID, BOOK_ID);

        // then
        ArgumentCaptor<Book> bookArg = ArgumentCaptor.forClass(Book.class);
        verify(bookRepo).addOrUpdateBook(bookArg.capture());
        Book bookArgValue = bookArg.getValue();
        assertThat(bookArgValue.getBorrow()).isNull();
    }

    @Test
    public void shouldNotAllowToReturnByOtherReader() {
        // given

        when(bookRepo.getBook(BOOK_ID)).thenReturn(borrowedBook);

        // when
        catchException(borrowService).returnBook(READER2_ID, BOOK_ID);

        // then
        Exception exception = caughtException();
        assertThat(exception)
                .isExactlyInstanceOf(OnlyReaderMayReturnBookException.class)
                .hasMessage("Book with ID=11 is borrowed by reader with ID=44 so reader with ID=55 may not return it");

    }


    @Test
    public void shouldReturnEmptyHistory() {
        // given
        // no history

        // when
        List<BookHistoryEntry> bookHistory = borrowService.getBookHistory(BOOK_ID);

        // then
        assertThat(bookHistory).hasSize(0);
    }

    @Test
    public void shouldRecordBookHistory() {

        // when
        borrowService.borrowBook(READER_ID, BOOK_ID);


        // then
        ArgumentCaptor<BookHistoryEntry> argumentCaptor = ArgumentCaptor.forClass(BookHistoryEntry.class);
        verify(bookHistoryRepository).addBookHistoryEntry(argumentCaptor.capture());
        BookHistoryEntry bookHistoryEntry = argumentCaptor.getValue();

        assertThat(bookHistoryEntry.getBookId()).isEqualTo(BOOK_ID);
        assertThat(bookHistoryEntry.getReaderId()).isEqualTo(READER_ID);
        assertThat(bookHistoryEntry.getDate()).isNotNull();
        assertThat(bookHistoryEntry.getOperation()).isEqualTo(Operation.BORROW);
    }


}