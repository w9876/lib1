package repository;

import model.BookHistoryEntry;

import java.util.List;

public interface BookHistoryRepository {
    void addBookHistoryEntry(BookHistoryEntry entry);

    List<BookHistoryEntry> getBookHistory(int bookId);

    List<BookHistoryEntry> getReaderHistory(int readerId);
}
