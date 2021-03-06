package service;

import model.Book;
import model.BookHistoryEntry;
import model.Operation;
import model.Reader;
import org.junit.Before;
import org.junit.Test;
import repository.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;


public class LibraryServiceITest {

    LibraryService libraryService;


    Book book1 = new Book("Title1", "Author1", 11);
    Book book2 = new Book("Title2", "Author2", 33);
    Reader reader1 = new Reader("John", "Smith");
    Reader reader2 = new Reader("Jan", "Kowalski");
    private int bookId1;
    private int bookId2;
    private int readerId1;
    private int readerId2;

    @Before
    public void setUp() {
        BookRepository bookRepo = new BookRepositoryImpl();
        ReaderRepository readerRepo = new ReaderRepositoryImpl();
        BookHistoryRepository bookHistoryRepo = new BookHistoryRepositoryImpl(bookRepo, readerRepo);
        libraryService = new LibraryService(bookRepo, readerRepo, bookHistoryRepo);

        bookId1 = bookRepo.addOrUpdateBook(book1).getId();
        bookId2 = bookRepo.addOrUpdateBook(book2).getId();
        readerId1 = readerRepo.addReader(reader1).getId();
        readerId2 = readerRepo.addReader(reader2).getId();

    }


    @Test
    public void shouldBorrowAndReturnBookAndRecordHistory() {

        // when
        libraryService.borrowBook(readerId1, bookId1);
        // then

        assertThat(libraryService.getBook(bookId1).getBorrow().getReaderId()).isEqualTo(readerId1);


        // when
        libraryService.returnBook(readerId1, bookId1);

        // then
        assertThat(libraryService.getBook(bookId1).getBorrow()).isNull();

        // when
        List<BookHistoryEntry> bookHistory = libraryService.getBookHistory(bookId1);

        // then
        assertThat(bookHistory).hasSize(2);
        assertThat(bookHistory.get(0).getReaderId()).isEqualTo(readerId1);

        // when
        List<BookHistoryEntry> readerHistory = libraryService.getReaderHistory(readerId1);

        // then
        assertThat(readerHistory).hasSize(2);
        assertThat(readerHistory.get(0).getBookId()).isEqualTo(bookId1);
    }

    @Test
    public void shouldReturnBookHistory() {

        // when
        libraryService.borrowBook(readerId1, bookId1);
        libraryService.borrowBook(readerId2, bookId2);
        libraryService.returnBook(readerId1, bookId1);
        libraryService.borrowBook(readerId2, bookId1);


        // when
        List<BookHistoryEntry> bookHistory = libraryService.getBookHistory(bookId1);

        // then
        assertThat(bookHistory).hasSize(3);
        assertThat(bookHistory.get(0).getReaderId()).isEqualTo(readerId1);

        assertThat(bookHistory.stream().map(bhe1 -> bhe1.getBookId()).distinct().collect(toList()))
                .containsExactly(bookId1);

        List<Integer> borrowers = bookHistory.stream()
                .filter(bhe -> bhe.getOperation() == Operation.BORROW)
                .map(bhe -> bhe.getReaderId())
                .flatMap( p-> Arrays.asList(p).stream())
                .collect(toList());
        assertThat(borrowers).containsOnly(readerId1, readerId2);


        ToIntFunction<? super Object> a;
        bookHistory.stream()
                .mapToInt(BookHistoryEntry::getReaderId)
                .collect(Collectors.averagingInt( i -> 1));


        System.out.println("aaa:" + collect);

Collectors.averagingInt()

    }




    @Test
    public void shouldReturnReaderHistory() {

        // when
        libraryService.borrowBook(readerId1, bookId1);
        libraryService.borrowBook(readerId2, bookId2);
        libraryService.returnBook(readerId1, bookId1);
        libraryService.borrowBook(readerId2, bookId1);


        // when
        List<BookHistoryEntry> readerHistory = libraryService.getReaderHistory(readerId1);

        // then
        assertThat(readerHistory).hasSize(2);
        assertThat(readerHistory.get(0).getReaderId()).isEqualTo(readerId1);
    }


}
