package model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookHistoryEntry {
    private final int bookId;
    private final int readerId;
    private final LocalDate date;
    private final Operation operation;
}
